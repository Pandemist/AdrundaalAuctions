package pandemist.adrundaal.auctions.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pandemist.adrundaal.auctions.model.BidItem;
import pandemist.adrundaal.auctions.model.CollectableItem;
import pandemist.adrundaal.auctions.model.SellItem;
import pandemist.adrundaal.auctions.model.Shop;

import static pandemist.adrundaal.auctions.AdrundaalAuctions.bidItemList;
import static pandemist.adrundaal.auctions.AdrundaalAuctions.collectItemList;
import static pandemist.adrundaal.auctions.AdrundaalAuctions.sellItemList;

public class ItemUtils {
	/*
	*   Adds Meta like Name, lore, Enchantment and Attribute to an item.
	 */
	public static ItemStack addMetaToItem(ItemStack i, String name, List<String> lore, Map<Enchantment, Integer> enchantments, String attr) {
		ArrayList<String> l=new ArrayList<String>();
		ItemMeta im=i.getItemMeta();
		if(name!=null) {
			if(notNull(name)&&name!="") {
				im.setDisplayName(color(name));
			}
		}
		if(!lore.isEmpty()) {
			for(String L : lore){
				if(L.equals("")) {
					continue;
				}
				l.add(color(L));
			}
			im.setLore(l);
		}
		if(enchantments!=null) {
			i.addUnsafeEnchantments(enchantments);
		}
		if(notNull(attr)) {
			loadItemAttributesFromString(i, attr);
		}
		i.setItemMeta(im);
		return i;
	}
	/*
	*   Gets Materialname by Material ID
	 */
	public static Material getMaterialbyId(String id) {
		return Material.matchMaterial(id);
	}
	/*
	*   Get Attributes from an ItemStack
	 */
	public static String getItemAttributes(ItemStack item) {
		net.minecraft.server.v1_10_R1.ItemStack nmsItem=CraftItemStack.asNMSCopy(item);
		if(nmsItem==null) return null;
		NBTTagCompound tag=getItemTag(nmsItem);
		if(tag==null||!tag.hasKey("AttributeModifiers")) {
			return null;
		}
		String data="";
		NBTTagList list=tag.getList("AttributeModifiers", 10);
		for(int i=0; i<list.size(); i++) {
			NBTTagCompound attr=list.get(i);
			data+=attr.getString("Name")+","+attr.getString("AttributeName")+","+attr.getDouble("Amount")+","+attr.getInt("Operation")+","+attr.getLong("UUIDLeast")+","+attr.getLong("UUIDMost")+";";
		}
		return data;
	}
	/*
	*   Gets ItemTag from an Actual ItemStack
	 */
	private static NBTTagCompound getItemTag(net.minecraft.server.v1_10_R1.ItemStack itemStack) {
		if(itemStack==null) return null;
		try {
			Field tag=itemStack.getClass().getDeclaredField("tag");
			tag.setAccessible(true);
			return (NBTTagCompound) tag.get(itemStack);
		}catch(NoSuchFieldException e){
			return null;
		}catch(IllegalAccessException e2){
			return null;
		}
	}

	/*
	*   Load Attributes from an inserted String
	 */
	public static org.bukkit.inventory.ItemStack loadItemAttributesFromString(org.bukkit.inventory.ItemStack item, String data) {
		NBTTagList list=new NBTTagList();
		String[] attrs=data.split(";");
		for(String s : attrs) {
			if(!s.isEmpty()) {
				String[] attrData=s.split(",");
				NBTTagCompound attr=new NBTTagCompound();
				attr.setString("Name", attrData[0]);
				attr.setString("AttributeName", attrData[1]);
				attr.setDouble("Amount", Double.parseDouble(attrData[2]));
				attr.setInt("Operation", Integer.parseInt(attrData[3]));
				attr.setLong("UUIDLeast", Long.parseLong(attrData[4]));
				attr.setLong("UUIDMost", Long.parseLong(attrData[5]));
				list.add(attr);
			}
		}
		net.minecraft.server.v1_10_R1.ItemStack i=CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag=getItemTag(i);
		if(tag==null) {
			tag=new NBTTagCompound();
			setItemTag(i, tag);
		}
		tag.set("AttributeModifiers", list);
		return CraftItemStack.asBukkitCopy(i);
	}
	/*
	*   Sets Tag to an ItemStack
	 */
	private static void setItemTag(net.minecraft.server.v1_10_R1.ItemStack itemStack, NBTTagCompound newTag) {
		if(itemStack==null) return;
		try {
			Field tag=itemStack.getClass().getDeclaredField("tag");
			tag.setAccessible(true);
			tag.set(itemStack, newTag);
		}catch(NoSuchFieldException e){
		}catch(IllegalAccessException e2){
		}
	}
	/*
	*   Checks if String is not Null
	 */
	private static boolean notNull(String string) {
		return string!=null ? true : false;
	}
	/*
	*   Colourize an String Message
	 */
	public static String color(String msg) {
		try {
			msg=ChatColor.translateAlternateColorCodes('&', msg);
		}catch(Exception e){
			System.err.println(e);
		}
		return msg;
	}
	/*
	*   Iterates over the BidItemList and Selects the Item where the IndivID is the same for the Player in the SelectedItemMap
	 */
	public static BidItem getActualBidItemByPlayer(Player player) {
		if(!Shop.isPlayerInSelectedItemMap(player)) {
			return null;
		}
		String uniqueBidItemID=Shop.getSelectedItemByName(player);
		for(BidItem bitem : bidItemList) {
			if(bitem.getIndividualID().equals(uniqueBidItemID)) {
				return bitem;
			}
		}
		return null;
	}
/*
*   Iterates over the SellItemList and Selects the Item where the IndivID is the same for the Player in the SelectedItemMap
 */

	public static SellItem getActualSellItemByPlayer(Player player) {
		if(!Shop.isPlayerInSelectedItemMap(player)) {
			return null;
		}
		String uniqueBidItemID=Shop.getSelectedItemByName(player);
		for(SellItem sitem : sellItemList) {
			if(sitem.getIndividualID().equals(uniqueBidItemID)) {
				return sitem;
			}
		}
		return null;
	}
	/*
	*   Gets a SellItem by inserted String ID
	 */
	public static SellItem getSellItemByUID(String uniqID) {
		for(SellItem sItem : sellItemList) {
			if(sItem.getIndividualID().equals(uniqID)) {
				return sItem;
			}
		}
		return null;
	}
	/*
	*   Gets a BidItem by inserted String ID
	 */
	public static BidItem getBidItemByUID(String uniqID) {
		for(BidItem bItem : bidItemList) {
			if(bItem.getIndividualID().equals(uniqID)) {
				return bItem;
			}
		}
		return null;
	}
	/*
	*   Gets a CollectItem by inserted String ID
	 */
	public static CollectableItem getCollectItemByUID(String uniqID) {
		for(CollectableItem cItem : collectItemList) {
			if(cItem.getIndividualID().equals(uniqID)) {
				return cItem;
			}
		}
		return null;
	}
	/*
	*   Replaces a item in the BidItemList
	 */
	public static void replaceBidItemInList(BidItem bidItem) {
		for(int i=0; i<bidItemList.size(); i++) {
			if(bidItemList.get(i).getIndividualID().equals(bidItem.getIndividualID())) {
				bidItemList.set(i, bidItem);
			}
		}
	}
	public static void removeFromBidList(BidItem bItem) {
		for(int i=0;i<bidItemList.size();i++) {
			if(bidItemList.get(i).getIndividualID().equals(bItem.getIndividualID())) {
				bidItemList.remove(i);
			}
		}
	}
	public static void removeFromSellList(SellItem sItem) {
		for(int i=0;i<sellItemList.size();i++) {
			if(sellItemList.get(i).getIndividualID().equals(sItem.getIndividualID())) {
				sellItemList.remove(i);
			}
		}
	}
	public static void removeFromCollectList(CollectableItem cItem) {
		for(int i=0;i<collectItemList.size();i++) {
			if(collectItemList.get(i).getIndividualID().equals(cItem.getIndividualID())) {
				collectItemList.remove(i);
			}
		}
	}
	public static String getDisplayNameIfNotNull(ItemStack iStack) {
		String s = iStack.getItemMeta().getDisplayName();
		return (s!=null)?s:"";
	}
}
