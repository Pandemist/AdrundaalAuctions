package pandemist.adrundaal.auctions.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.model.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static pandemist.adrundaal.auctions.AdrundaalAuctions.*;

public class Utils {
	/*
	*   Return the Item a Player has in his Hand
	 */
	public static ItemStack getItemInHand(Player player) {
		return player.getInventory().getItemInMainHand();
	}
	/*
	*   Sets the Item in Players hand
	 */
	public static void setItemInHand(Player player, ItemStack item) {
		player.getInventory().setItemInMainHand(item);
	}
	/*
	*   Test if inserted String is an Integer
	 */
	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		}catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}
	/*
	*   Get a OfflinePlayer by his Name
	 */
	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(String name) {
		return Bukkit.getServer().getOfflinePlayer(name);
	}
	/*
	*   Test if the inserted Player has the inserted Permissions
	 */
	public static boolean hasPermission(Player player, String perm) {
		if(!player.hasPermission("AdrundaalAuctions."+perm)) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "no-permissions");
			return false;
		}
		return true;
	}
	/*
	*   test if the inserted CommandSender has the inserted Permissions
	 */
	public static boolean hasPermission(CommandSender sender, String perm) {
		if(sender instanceof Player) {
			Player player=(Player) sender;
			if(!player.hasPermission("AdrundaalAuctions."+perm)) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "no-permission");
				return false;
			}else{
				return true;
			}
		}else{
			return true;
		}
	}
	/*
	+   Test if a Player is Online
	 */
	public static boolean isOnline(UUID uuid) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(player.getUniqueId().equals(uuid)) {
				return true;
			}
		}
		return false;
	}
	/*
	*   Test if the Invertory on the inserted Player is Full
	 */
	public static boolean isInvFull(Player player) {
		if(player.getInventory().firstEmpty()==-1) {
			return true;
		}
		return false;
	}
	/*
	*   Tests the Items in the 3 Lists (Bid, Sell, Collect) if Time is expired and evaluate them.
	 */
	public static void updateListActuallity() {
		Calendar cal=Calendar.getInstance();
		for(BidItem b : bidItemList) {
			if(cal.after(b.getTimeExpire())) {
				collectItemList.add(b.toCollectble());
				if(b.getTopBidderName()!="") {
					EcoUtils.ecoGiveMoney(b.getSellerName(), b.getSellerUUID(), b.getOffer());
					ChatUtils.sendMessageToPlayer(b.getTopBidderUUID(), "auction-end-bidder", b.getItem());
					ChatUtils.sendMessageToPlayer(b.getSellerUUID(), "auction-end-seller", b.getItem());
				}else{
					ChatUtils.sendMessageToPlayer(b.getSellerUUID(), "item-time-expired", b.getItem());
				}
				bidItemList.remove(b);
			}
		}
		for(SellItem s : sellItemList) {
			if(cal.after(s.getTimeExpire())) {
				collectItemList.add(s.toCollectble());
				ChatUtils.sendMessageToPlayer(s.getSellerUUID(), "item-time-expired", s.getItem());
				sellItemList.remove(s);
			}
		}
		for(CollectableItem c : collectItemList) {
			if(cal.after(c.getTimeExpire())) {
				ChatUtils.sendMessageToPlayer(c.getOwnerUUID(), "item-time-expired-clear", c.getItem());
				sellItemList.remove(c);
			}
		}
	}
	public static boolean isEqual(List<String> list1, List<String> list2) {
		if(list1==null||list2==null) {
			return false;
		}
		if (list1.isEmpty()&&list2.isEmpty()) {
			return true;

		}
		if(list1.size()!=list2.size()) {
			return false;
		}
		for(int i=0;i<list1.size();i++) {
			if(list1.get(i).equals(list2.get(i))) {
				return false;
			}
		}
		return true;
	}
}
