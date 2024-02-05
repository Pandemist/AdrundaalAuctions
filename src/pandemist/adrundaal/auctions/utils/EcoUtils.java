package pandemist.adrundaal.auctions.utils;

import java.util.UUID;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;

import pandemist.adrundaal.auctions.AdrundaalAuctions;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.model.CollectableItem;

public class EcoUtils {
	public static Economy econ=null;
	public static EconomyResponse r;
	/*
	*   Test if Vault is installed on this Server
	 */
	public static boolean hasVault() {
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault")!=null) {
			return true;
		}
		return false;
	}
	/*
	*   Setup the Economy
	 */
	public static boolean setupEconomy() {
		if(Bukkit.getServer().getPluginManager().getPlugin("Vault")==null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp=Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp==null) {
			return false;
		}
		econ=rsp.getProvider();
		return econ!=null;
	}
	/*
	*   Tests witch method for Money is in use an Call Submethode
	 */
	public static boolean ecoHasMoney(Player player, int amount) {
		if(AdrundaalAuctions.itemMoneyUse) {
			return itemHasMoney(player, amount);
		}else{
			return vaultHasMoney(player, amount);
		}
	}
	/*
	*   Tests witch method for Money is in use an Call Submethode
	*/
	public static boolean ecoTakeMoney(Player player, int amount) {
		if(AdrundaalAuctions.itemMoneyUse) {
			return itemTakeMoney(player, amount);
		}else{
			return vaultTakeMoney(player, amount);
		}
	}
	/*
	*   Tests witch method for Money is in use an Call Submethode
	*/
	public static boolean ecoGiveMoney(String name, UUID uuid, long amount) {
		if(AdrundaalAuctions.itemMoneyUse) {
			return itemGiveMoney(name, uuid, amount);
		}else{
			return vaultGiveMoney(name, uuid, amount);
		}
	}
	/*
	*   Test with Vault, if the Player has Money
	 */
	private static boolean vaultHasMoney(Player player, int amount) {
		return econ.getBalance(player)>=amount ? true : false;
	}
	/*
	*   Take Money with Vault
	 */
	private static boolean vaultTakeMoney(Player player, int amount) {
		econ.withdrawPlayer(player, amount);
		return true;
	}
	/*
	*   Give Money with Vault
	 */
	private static boolean vaultGiveMoney(String name, UUID uuid, long amount) {
		OfflinePlayer player=Utils.getOfflinePlayer(name);
		econ.depositPlayer(player, amount);
		return true;
	}
	/*
	*   Test if Player has Item Money
	 */
	private static boolean itemHasMoney(Player player, int amount) {
		return EcoUtils.itemGetMoney(player)>=amount;
	}
	/*
	*   Gets the amount of Money from the Player
	 */
	private static int itemGetMoney(Player player) {
		int money=0;
		for(ItemStack stack : player.getInventory()) {
			if(!isValid(stack)) {
				continue;
			}
			money+=stack.getAmount();
		}
		System.out.println(player.getDisplayName()+" hat Geld: "+money);
		return money;
	}
	/*
	*   Take Item Money from Player
	 */
	private static boolean itemTakeMoney(Player player, int amount) {
		if(!EcoUtils.itemHasMoney(player, amount)) {
			return false;
		}
		ItemStack[] contents=player.getInventory().getContents();
		int taken=0;
		for(int i=0; taken<=amount&&i<contents.length; ++i) {
			if(contents[i]==null||!EcoUtils.isValid(contents[i])) continue;
			if(contents[i].getAmount()>amount-taken) {
				contents[i].setAmount(contents[i].getAmount()-(amount-taken));
				taken=amount;
				continue;
			}
			if(contents[i].getAmount()>amount-taken) continue;
			taken+=contents[i].getAmount();
			contents[i]=null;
		}
		player.getInventory().setContents(contents);
		player.updateInventory();
		return true;
	}
	/*
	*   Takes Item Money from the Player
	 */
	private static boolean itemGiveMoney(String name, UUID uuid, long amount) {
		while(amount>64) {
			addItemMoneyToAuctionItem(name, uuid, 64);
			amount=amount-64;
		}
		addItemMoneyToAuctionItem(name, uuid, (int) amount);
		return true;
	}
	/*
	*   Adds Item Money to the AH- Chest of a Player
	 */
	private static void addItemMoneyToAuctionItem(String name, UUID uuid, int amount) {
		String indivID="c-"+uuid.toString()+"-"+TimeUtils.getNowTime();
	//	ItemStack is=new ItemStack(ItemUtils.getMaterialbyId(Config.getOptionValue("items.money-item.type")), amount);
		ItemStack is=Config.getOptionItem("money-item");
		is.setAmount(amount);
		CollectableItem ai=new CollectableItem(TimeUtils.convertToMill(Config.getOptionValue("config.full-expire-time")), is, indivID, name, uuid);
//add Item to auctionList and update items.data.
		AdrundaalAuctions.collectItemList.add(ai);
		//Utils.updateListActuallity();
		ItemConfig.refreshLists();
	}
	/*
	*   Private help Methode. Tests if found itemStack is the Money Item
	 */
	private static boolean isValid(ItemStack stack) {
		if (stack==null) {
			return false;
		}
	//	System.out.println(stack.getType());
	//	System.out.println(ItemUtils.getMaterialbyId(Config.getOptionValue("items.money-item.type")));
		if (!stack.getType().equals(ItemUtils.getMaterialbyId(Config.getOptionValue("items.money-item.type")))) {
	//		System.out.println("Stack type passt nicht");
			return false;
		}
	//	System.out.println(Config.getOptionValue("items.money-item.name"));
		if(!Config.getOptionValue("items.money-item.name").equals("")) {
			if(!ItemUtils.getDisplayNameIfNotNull(stack).equals(Config.getOptionValue("items.money-item.name"))) {
	//			System.out.println("Stack name passt nicht");
				return false;
			}
		}
	//	System.out.println(Config.getStringList("items.money-item.lore"));
		if (!Utils.containsListOnlyEmpty(Config.getStringList("items.money-item.lore"))) {
			if (!Utils.isEqual(stack.getItemMeta().getLore(),Config.getStringList("items.money.lore"))) {
	//			System.out.println("Stack lore passt nicht");
				return false;
			}

		}
		return true;


	/*	return !(stack!=null
				&&(stack.getType()==ItemUtils.getMaterialbyId(Config.getOptionValue("money-item.type")))
				&&(stack.getItemMeta().getDisplayName().equals(Config.getOptionValue("money-item.name"))
				||Config.getOptionValue("money-item.name").isEmpty())
				&&(stack.getItemMeta().getLore().equals(Config.getStringList("money-item.lore"))
				||Config.getStringList("money-item.lore").isEmpty()));*/
	}
}
