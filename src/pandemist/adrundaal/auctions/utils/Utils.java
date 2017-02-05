package pandemist.adrundaal.auctions.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.config.LogConfig;
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
		Calendar cal = Calendar.getInstance();
		Calendar exp = Calendar.getInstance();
	//	for(BidItem b : bidItemList) {
		for(int i=0;i<bidItemList.size();i++) {
			long h = bidItemList.get(i).getTimeExpire();
			exp.setTimeInMillis(h);
			if(cal.after(exp)) {
				collectItemList.add(bidItemList.get(i).toCollectble());
				if(bidItemList.get(i).getTopBidderName()!="") {
					EcoUtils.ecoGiveMoney(bidItemList.get(i).getSellerName(), bidItemList.get(i).getSellerUUID(), bidItemList.get(i).getOffer());
					ChatUtils.sendMessageToPlayer(bidItemList.get(i).getTopBidderUUID(), "auction-end-bidder", bidItemList.get(i).getItem());
					ChatUtils.sendMessageToPlayer(bidItemList.get(i).getSellerUUID(), "auction-end-seller", bidItemList.get(i).getItem());
				}else{
					ChatUtils.sendMessageToPlayer(bidItemList.get(i).getSellerUUID(), "item-time-expired", bidItemList.get(i).getItem());
				}
				LogConfig.addToLogFile("expired", bidItemList.get(i).getSellerName(), bidItemList.get(i).getItem());
				ItemUtils.removeFromBidList(bidItemList.get(i));
			}
		}
	//	for(SellItem s : sellItemList) {
		System.out.println(sellItemList.size());
		for(int i=0;i<sellItemList.size();i++) {
			System.out.println(i);
			long h = sellItemList.get(i).getTimeExpire();
			exp.setTimeInMillis(h);
	//		System.out.println(cal);
	//		System.out.println(exp);
	//		System.out.println(cal.after(exp));
			System.out.println("---"+sellItemList.get(i).getItem().getType());
			if(cal.after(exp)) {
				System.out.println("Das geht");
				collectItemList.add(sellItemList.get(i).toCollectble());
				ChatUtils.sendMessageToPlayer(sellItemList.get(i).getSellerUUID(), "item-time-expired", sellItemList.get(i).getItem());
				ItemUtils.removeFromSellList(sellItemList.get(i));
			}
		}
	//	for(CollectableItem c : collectItemList) {
		for(int i=0;i<collectItemList.size();i++) {
			long h = collectItemList.get(i).getTimeExpire();
			exp.setTimeInMillis(h);
			if(cal.after(exp)) {
				ChatUtils.sendMessageToPlayer(collectItemList.get(i).getOwnerUUID(), "item-time-expired-clear", collectItemList.get(i).getItem());
				LogConfig.addToLogFile("removed", collectItemList.get(i).getOwnerName(), collectItemList.get(i).getItem());
				ItemUtils.removeFromCollectList(collectItemList.get(i));
			}
		}
	}
	public static boolean containsListOnlyEmpty(List<String> list1) {
		if (list1==null) {
			return true;
		}
		boolean returnValue = true;
		for(String s : list1) {
			if(!s.equals("")) {
				returnValue = false;
			}
		}
		return returnValue;
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
	public static UUID stringToUUID(String string) {
		if(string==null) {
			return null;
		}
		if(string.equals("")) {
			return null;
		}else{
			return UUID.fromString(string);
		}
	}
	public static String UUIDToString(UUID uuid) {
		if(uuid == null) {
			return "";
		}else{
			return uuid.toString();
		}
	}
}
