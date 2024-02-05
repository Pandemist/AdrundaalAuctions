package pandemist.adrundaal.auctions.commands;

import java.sql.Time;
import java.util.ArrayList;

import com.mysql.jdbc.TimeUtil;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.AdrundaalAuctions;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.config.LogConfig;
import pandemist.adrundaal.auctions.model.BidItem;
import pandemist.adrundaal.auctions.model.SellItem;
import pandemist.adrundaal.auctions.utils.ChatUtils;
import pandemist.adrundaal.auctions.utils.ItemUtils;
import pandemist.adrundaal.auctions.utils.TimeUtils;
import pandemist.adrundaal.auctions.utils.Utils;

public class CommandBidSell {
	/*
	*   Command for Bid and Sell
	*   This Command is the whole way to insert a bid- or sellable Item to the Plugin
	*   Command raw: /ah bid/sell <Price> [Amount]
	*
	 */
	public CommandBidSell(AdrundaalAuctions plugin, CommandSender sender, String[] args) {
//Test if Player has the Permissions
		if(args[0].equalsIgnoreCase("Sell")) {
			if(!Utils.hasPermission(sender, "sell")) {
				return;
			}
		}
		if(args[0].equalsIgnoreCase("Bid")) {
			if(!Utils.hasPermission(sender, "bid")) {
				return;
			}
		}
//Test if Player is Instance of Player
		if(!(sender instanceof Player)) {
			sender.sendMessage(Config.getLang("sender-is-no-player"));
			return;
		}
		Player player=(Player) sender;
//Test if the Argumentamount is correnct
		if((args.length>3)&&(args.length<2)) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "parameters-not-matching");
			return;
		}
		ItemStack is=Utils.getItemInHand(player);
//Test if Hand is Empty
		if(is.getType()==Material.AIR) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "no-item-in-hand");
			return;
		}
		int amountOfItemInHand=is.getAmount();
		int amountOfItemToTake=amountOfItemInHand;
//Test if Price and Amount are Integer
		if(!Utils.isInt(args[1])) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "argument-is-not-a-number");
			return;
		}
		if(args.length==3) {
			if(!Utils.isInt(args[2])) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "argument-is-not-a-number");
				return;
			}
			amountOfItemToTake=Integer.parseInt(args[2]);
		}
//Test if Player has enough of this Item
		if((amountOfItemToTake>64)||(amountOfItemToTake<1)
				||(amountOfItemInHand<amountOfItemToTake)) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "not-enough-item-in-hand");
			return;
		}
		int price=Integer.parseInt(args[1]);
//Test if bid and sell limit is not reached
		if(args[0].equalsIgnoreCase("Bid")) {
			if(price<=Config.getInt("config.minimum-bid-price")) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "bid-price-to-low");
				return;
			}
			if(price>=Config.getInt("config.max-beginning-bid-price")) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "bid-price-to-high");
				return;
			}
		}else{
			if(price<=Config.getInt("config.minimum-sell-price")) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "sell-price-to-low");
				return;
			}
			if(price>=Config.getInt("config.max-beginning-sell-price")) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "sell-price-to-high");
				return;
			}
		}
//Test if Player has reached the Maximum
		if(args[0].equalsIgnoreCase("Bid")) {
			if(AdrundaalAuctions.bidItemList.size()>=Config.getInt("config.max-biddableItems")
					&&Config.getInt("config.max-biddableItems")>0) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "bid-max-item-reached");
				return;
			}
		}else{
			if(AdrundaalAuctions.sellItemList.size()>=Config.getInt("config.max-sellableItems")
					&&Config.getInt("config.max-sellableItems")>0) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "sell-max-item-reached");
				return;
			}
		}
//Test if item is on BlackList
		for(String id : Config.getStringList("config.blacklist")) {
			if(id.equals("")) {
				continue;
			}
			if(is.getType()==Material.matchMaterial(id)) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "ítem-blacklisted");
				return;
			}
		}
//Test if item is on Name BlackList
		for(String id : Config.getStringList("config.name_blacklist")) {
			System.out.println("id "+ItemUtils.color(id));
			if(id.equals("")) {
				continue;
			}
			System.out.println("Display "+ItemUtils.color(is.getItemMeta().getDisplayName()));
			if(is.getItemMeta().getDisplayName()==null) {
				break;
			}
			System.out.println(ItemUtils.color(is.getItemMeta().getDisplayName()).equals(ItemUtils.color(id)));
			if(ItemUtils.color(is.getItemMeta().getDisplayName()).equals(ItemUtils.color(id))) {
				System.out.println("MUH!");
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "ítem-blacklisted");
				return;
			}
		}
//Test if item is on Lore BlackList
		for(String id : Config.getStringList("config.lore_blacklist")) {
			if(id.equals("")) {
				continue;
			}
			if(is.getItemMeta().getLore()==null) {
				break;
			}
			for(String loreLine : is.getItemMeta().getLore()) {
				if(loreLine.replaceAll("§","&").equals(id)) {
					ChatUtils.sendMessageToPlayer(player.getUniqueId(), "ítem-blacklisted");
					return;
				}
			}
		}
//Test if damaged Items are allowed, and if this item is one.
		if(!Config.getOptionValue("config.allow-damaged-items").equals("true")) {
			for(Material i : getDamageableItems()) {
				if(is.getType()==i) {
					if(is.getDurability()>0) {
						ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-damaged");
						return;
					}
				}
			}
		}
//Create AuctionItem (means Biditem oder SellItem, depends on args[0]) an fill it with values
		ItemStack iss=is.clone();
		iss.setAmount(amountOfItemToTake);
		String indivID=player.getUniqueId().toString()+"-"+TimeUtils.getNowTime();
		if(args[0].equalsIgnoreCase("Bid")) {
			indivID="b-"+indivID;
			BidItem bi=new BidItem(TimeUtils.convertToMill(Config.getOptionValue("config.bid-time")), Integer.parseInt(args[1]), iss, indivID, player.getName(), ((Player) sender).getUniqueId());
			AdrundaalAuctions.bidItemList.add(bi);
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "successfully-inserted", bi.getItem());
			LogConfig.addToLogFile("addItem", player.getDisplayName(), bi.getItem());
		}else{
			indivID="s-"+indivID;
			SellItem si=new SellItem(TimeUtils.convertToMill(Config.getOptionValue("config.bid-time")), Integer.parseInt(args[1]), iss, indivID, player.getName(), ((Player) sender).getUniqueId());
			AdrundaalAuctions.sellItemList.add(si);
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "successfully-inserted", si.getItem());
			LogConfig.addToLogFile("addItem", player.getDisplayName(), si.getItem());
		}
		System.out.println(indivID);
		Utils.updateListActuallity();
		ItemConfig.refreshLists();

//Take the Items, that are now insertet in the Auctionhouse
		if(amountOfItemInHand-amountOfItemToTake<=0) {
			Utils.setItemInHand(player, new ItemStack(Material.AIR));
		}else{
			is.setAmount(amountOfItemInHand-amountOfItemToTake);
		}
	}
	//List of damageable Items
	ArrayList<Material> getDamageableItems() {
		ArrayList<Material> ma=new ArrayList<Material>();
		ma.add(Material.DIAMOND_HELMET);
		ma.add(Material.DIAMOND_CHESTPLATE);
		ma.add(Material.DIAMOND_LEGGINGS);
		ma.add(Material.DIAMOND_BOOTS);
		ma.add(Material.CHAINMAIL_HELMET);
		ma.add(Material.CHAINMAIL_CHESTPLATE);
		ma.add(Material.CHAINMAIL_LEGGINGS);
		ma.add(Material.CHAINMAIL_BOOTS);
		ma.add(Material.GOLD_HELMET);
		ma.add(Material.GOLD_CHESTPLATE);
		ma.add(Material.GOLD_LEGGINGS);
		ma.add(Material.GOLD_BOOTS);
		ma.add(Material.IRON_HELMET);
		ma.add(Material.IRON_CHESTPLATE);
		ma.add(Material.IRON_LEGGINGS);
		ma.add(Material.IRON_BOOTS);
		ma.add(Material.DIAMOND_HELMET);
		ma.add(Material.DIAMOND_CHESTPLATE);
		ma.add(Material.DIAMOND_LEGGINGS);
		ma.add(Material.DIAMOND_BOOTS);
		ma.add(Material.BOW);
		ma.add(Material.WOOD_SWORD);
		ma.add(Material.STONE_SWORD);
		ma.add(Material.IRON_SWORD);
		ma.add(Material.GOLD_SWORD);
		ma.add(Material.DIAMOND_SWORD);
		ma.add(Material.WOOD_PICKAXE);
		ma.add(Material.STONE_PICKAXE);
		ma.add(Material.IRON_PICKAXE);
		ma.add(Material.GOLD_PICKAXE);
		ma.add(Material.DIAMOND_PICKAXE);
		ma.add(Material.WOOD_AXE);
		ma.add(Material.STONE_AXE);
		ma.add(Material.IRON_AXE);
		ma.add(Material.GOLD_AXE);
		ma.add(Material.DIAMOND_AXE);
		ma.add(Material.WOOD_SPADE);
		ma.add(Material.STONE_SPADE);
		ma.add(Material.IRON_SPADE);
		ma.add(Material.GOLD_SPADE);
		ma.add(Material.DIAMOND_SPADE);
		ma.add(Material.WOOD_HOE);
		ma.add(Material.STONE_HOE);
		ma.add(Material.IRON_HOE);
		ma.add(Material.GOLD_HOE);
		ma.add(Material.DIAMOND_HOE);
		ma.add(Material.FLINT_AND_STEEL);
		ma.add(Material.ANVIL);
		ma.add(Material.FISHING_ROD);
		ma.add(Material.SHIELD);
		return ma;
	}
}
