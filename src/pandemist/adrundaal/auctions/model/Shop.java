package pandemist.adrundaal.auctions.model;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.utils.ItemUtils;
import pandemist.adrundaal.auctions.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static pandemist.adrundaal.auctions.AdrundaalAuctions.bidItemList;
import static pandemist.adrundaal.auctions.AdrundaalAuctions.collectItemList;
import static pandemist.adrundaal.auctions.AdrundaalAuctions.sellItemList;

public class Shop {
    private static HashMap<Player, ShopType> TypeMap = new HashMap<Player, ShopType>();
    private static HashMap<Player, Filter> FilterMap = new HashMap<Player, Filter>();
    private static HashMap<Player, ArrayList<String>> InvMap = new HashMap<Player, ArrayList<String>>();
    private static ArrayList<Page> pageList = new ArrayList<Page>();
    private static HashMap<Player, String> SelectedItemMap = new HashMap<Player, String>();
    private static HashMap<Player, Integer> BidValueMap = new HashMap<>();

    /*
    *   Add, Remove, Get, isIn and update Methodes
     */
    public static void addToTypeMap(Player player, ShopType type) {
        if (isPlayerInTypeMap(player)) {
            removeFromTypeMap(player);
        }
        TypeMap.put(player, type);
    }

    public static void addToFilterMap(Player player, Filter filter) {
        if (isPlayerInFilterMap(player)) {
            removeFromFilterMap(player);
        }
        FilterMap.put(player, filter);
    }

    public static void addToInvMap(Player player, ArrayList<String> idList) {
        if (isPlayerInInvMap(player)) {
            removeFromInvMap(player);
        }
        InvMap.put(player, idList);
    }

    public static void addToPageList(Player player, int actualPage, int maxPage) {
        if (isPlayerInPageList(player)) {
            removeFromPageList(player);
        }
        pageList.add(new Page(player, actualPage, maxPage));
    }

    public static void addToSelectedItemMap(Player player, String indivID) {
    //    System.out.println("Add to selected Item Map");
        if (isPlayerInSelectedItemMap(player)) {
            removeFromSelectedItemMap(player);
        }
        SelectedItemMap.put(player, indivID);
    }

    public static void addToBidValueMap(Player player, int value) {
        System.out.println("Player add to Bid Map");
        if (isPlayerInBidValueMap(player)) {
            removeFromBidValueMap(player);
        }
        BidValueMap.put(player, value);
    }

    public static void removeFromTypeMap(Player player) {
        TypeMap.remove(player);
    }

    public static void removeFromFilterMap(Player player) {
        FilterMap.remove(player);
    }

    public static void removeFromInvMap(Player player) {
        InvMap.remove(player);
    }

    public static void removeFromPageList(Player player) {
        if (isPlayerInPageList(player)) {
            for (int i = 0; i < pageList.size(); i++) {
                if (pageList.get(i).getPlayer().equals(player)) {
                    pageList.remove(pageList.get(i));
                }
            }
        }
    }

    public static void removeFromBidValueMap(Player player) {
        System.out.println("Player remove from Bid Map");
        BidValueMap.remove(player);
    }

    public static void removeFromSelectedItemMap(Player player) {
    //    System.out.println("Remove Player from Sel I Map");
        SelectedItemMap.remove(player);
    }

    public static boolean isPlayerInTypeMap(Player player) {
        return TypeMap.containsKey(player);
    }

    public static boolean isPlayerInFilterMap(Player player) {
        return FilterMap.containsKey(player);
    }

    public static boolean isPlayerInInvMap(Player player) {
        return InvMap.containsKey(player);
    }

    public static boolean isPlayerInBidValueMap(Player player) {
        System.out.println(player.getDisplayName());
        return BidValueMap.containsKey(player);
    }

    public static boolean isPlayerInSelectedItemMap(Player player) {
        return SelectedItemMap.containsKey(player);
    }

    public static boolean isTwoTimesIdInSelecteditemMap(String string) {
        int i =0;
        ArrayList<String> list = new ArrayList<String>(SelectedItemMap.values());
        for (String s : list) {
            if (s.equals(string)) {
                i++;
            }
        }
        if(i>1) {
            return true;
        }else {
            return false;
        }
    }

    public static boolean isPlayerInPageList(Player player) {
        for (Page p : pageList) {
            if (p.getPlayer().equals(player)) {
                return true;
            }
        }
        return false;
    }

    public static ShopType getTypeByName(Player player) {
        return TypeMap.get(player);
        //	return TypeMap.get(TypeMap.containsKey(player));
    }

    public static Filter getFilterByName(Player player) {
        return FilterMap.get(player);
    }

    public static ArrayList<String> getInvByName(Player player) {
        return InvMap.get(player);
    }

    public static int getBidValueByName(Player player) {
        return BidValueMap.get(player);
    }

    public static int getPageByName(Player player) {
        for (Page p : pageList) {
            if (p.getPlayer().equals(player)) {
                return p.getActualPage();
            }
        }
        return -1;
    }

    public static int getMaxPageByName(Player player) {
        for (Page p : pageList) {
            if (p.getPlayer().equals(player)) {
                return p.getMaxPage();
            }
        }
        return -1;
    }

    public static void updatePageByName(Player player, int newPage) {
        for (Page p : pageList) {
            if (p.getPlayer().equals(player)) {
                p.updatePage(newPage);
            }
        }
    }

    public static void updateBidValueByName(Player player, int value) {
        BidValueMap.replace(player, BidValueMap.get(player), value);
    }

    public static String getSelectedItemByName(Player player) {
        return SelectedItemMap.get(player);
    }

    /*
    *   Close the Last view
     */
    public static void closeLastView(Player player) {
        //player.closeInventory();
    }

    /*
    *   Create an Empty shop with Size of 54
     */
    public static Inventory getEmptyShop() {
        Inventory inv;
        inv = Bukkit.createInventory(null, (54), Config.getLang("shop-gui-name"));
        return inv;
    }

    /*
    *   Setup different Shops
     */
    public static Inventory setupCollectAble(Inventory inv) {
        //Add Menu Items to the lower Bar 45-53
        inv.setItem(45, Config.getOptionItem("menu.myOverview"));
        inv.setItem(47, Config.getOptionItem("menu.sellList"));
        inv.setItem(48, Config.getOptionItem("menu.pageBack"));
        inv.setItem(49, Config.getOptionItem("menu.collectAll"));
   //     inv.setItem(49, Config.getOptionItem("menu.refresh"));
        inv.setItem(50, Config.getOptionItem("menu.pageNext"));
        inv.setItem(51, Config.getOptionItem("menu.bidList"));

        return inv;
    }

    public static Inventory setupMyOverview(Inventory inv) {
        inv.setItem(45, Config.getOptionItem("menu.collect"));
        inv.setItem(47, Config.getOptionItem("menu.sellList"));
        inv.setItem(48, Config.getOptionItem("menu.pageBack"));
        inv.setItem(49, Config.getOptionItem("menu.refresh"));
        inv.setItem(50, Config.getOptionItem("menu.pageNext"));
        inv.setItem(51, Config.getOptionItem("menu.bidList"));
        return inv;
    }

    public static Inventory setupSellView(Inventory inv) {
        inv.setItem(45, Config.getOptionItem("menu.myOverview"));
        inv.setItem(46, Config.getOptionItem("menu.collect"));
        inv.setItem(47, Config.getOptionItem("menu.filter"));
        inv.setItem(48, Config.getOptionItem("menu.pageBack"));
        inv.setItem(49, Config.getOptionItem("menu.refresh"));
        inv.setItem(50, Config.getOptionItem("menu.pageNext"));
        inv.setItem(51, Config.getOptionItem("menu.filter"));
        inv.setItem(52, Config.getOptionItem("menu.bidList"));
        //	inv.setItem(53, Config.getOptionItem("menu.back"));
        return inv;
    }

    public static Inventory setupConfirmView(Inventory inv) {
        inv.setItem(0, Config.getOptionItem("dialog.confirm"));
        inv.setItem(1, Config.getOptionItem("dialog.confirm"));
        inv.setItem(2, Config.getOptionItem("dialog.confirm"));
        inv.setItem(3, Config.getOptionItem("dialog.confirm"));
        inv.setItem(5, Config.getOptionItem("dialog.abort"));
        inv.setItem(6, Config.getOptionItem("dialog.abort"));
        inv.setItem(7, Config.getOptionItem("dialog.abort"));
        inv.setItem(8, Config.getOptionItem("dialog.abort"));
        return inv;
    }

    public static Inventory setupBidView(Inventory inv) {
        inv.setItem(45, Config.getOptionItem("menu.myOverview"));
        inv.setItem(46, Config.getOptionItem("menu.collect"));
        inv.setItem(47, Config.getOptionItem("menu.filter"));
        inv.setItem(48, Config.getOptionItem("menu.pageBack"));
        inv.setItem(49, Config.getOptionItem("menu.refresh"));
        inv.setItem(50, Config.getOptionItem("menu.pageNext"));
        inv.setItem(51, Config.getOptionItem("menu.filter"));
        inv.setItem(52, Config.getOptionItem("menu.sellList"));
        return inv;
    }

    public static Inventory setupBidConfirmView(Inventory inv) {
        inv.setItem(0, Config.getOptionItem("dialog.bid+64"));
        inv.setItem(1, Config.getOptionItem("dialog.bid+32"));
        inv.setItem(2, Config.getOptionItem("dialog.bid+10"));
        inv.setItem(3, Config.getOptionItem("dialog.bid+1"));
        inv.setItem(5, Config.getOptionItem("dialog.bid-1"));
        inv.setItem(6, Config.getOptionItem("dialog.bid-10"));
        inv.setItem(7, Config.getOptionItem("dialog.bid-32"));
        inv.setItem(8, Config.getOptionItem("dialog.bid-64"));
        inv.setItem(12, Config.getOptionItem("dialog.confirm"));
        inv.setItem(14, Config.getOptionItem("dialog.abort"));
        return inv;
    }

    public static Inventory setupFilterView(Inventory inv) {
        inv.setItem(10, Config.getOptionItem("filter.armor"));
        inv.setItem(11, Config.getOptionItem("filter.food"));
        inv.setItem(12, Config.getOptionItem("filter.weapons"));
        inv.setItem(13, Config.getOptionItem("filter.potions"));
        inv.setItem(14, Config.getOptionItem("filter.tools"));
        inv.setItem(15, Config.getOptionItem("filter.blocks"));
        inv.setItem(16, Config.getOptionItem("filter.others"));
        inv.setItem(22, Config.getOptionItem("filter.none"));

        inv.setItem(45, Config.getOptionItem("menu.myOverview"));
        inv.setItem(46, Config.getOptionItem("menu.collect"));
        //	inv.setItem(49, Config.getOptionItem("menu.refresh"));
        return inv;
    }

    /*
    *   Remove from all Lists
     */
    public static void clearListsFromPlayer(Player player) {
        removeFromFilterMap(player);
        removeFromPageList(player);
        removeFromTypeMap(player);
        removeFromInvMap(player);
        removeFromBidValueMap(player);
        removeFromSelectedItemMap(player);
    }

    /*
    *   Clears the Lore from a item
     */
    public static ItemStack clearLore(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        im.setLore(null);
        is.setItemMeta(im);
        return is;
    }

    /*
    *   Gets a Itemlist of Collectable Items that schould the Player see
     */
    public static ArrayList<CollectableItem> getMatchingCollectableByPlayerName(Player player) {
        ArrayList<CollectableItem> matchingItems = new ArrayList<CollectableItem>();
        for (CollectableItem cItem : collectItemList) {
            if (cItem.getOwnerUUID().equals(player.getUniqueId())) {
                matchingItems.add(cItem);
            }
        }
        return matchingItems;
    }

    /*
    *   Gets a Itemlist of Bidable Items that schould the Player see, by Filter
     */
    public static ArrayList<BidItem> getMatchingBidByFilter(Player player) {
        ArrayList<BidItem> matchingItems = new ArrayList<BidItem>();
        if (!isPlayerInFilterMap(player)) {
            addToFilterMap(player, Filter.NONE);
        }
        for (BidItem bItem : bidItemList) {
            if (getFilterByName(player).equals(Filter.NONE) || getFilterByName(player).getItem().contains(bItem.getItem().getType())) {
                matchingItems.add(bItem);
            }
        }
        return matchingItems;
    }

    /*
    *   Gets a Itemlist of Bidable Items that schould the Player see
     */
    public static ArrayList<BidItem> getMatchingBidByPlayerName(Player player) {
        ArrayList<BidItem> matchingItems = new ArrayList<BidItem>();
        for (BidItem bItem : bidItemList) {
            if (bItem.getSellerUUID().equals(player.getUniqueId())) {
                matchingItems.add(bItem);
            }
        }
        return matchingItems;
    }

    /*
    *   Gets a Itemlist of Sellable Items that schould the Player see, by Filter
     */
    public static ArrayList<SellItem> getMatchingSellByFilter(Player player) {
        ArrayList<SellItem> matchingItems = new ArrayList<SellItem>();
        if (!isPlayerInFilterMap(player)) {
            addToFilterMap(player, Filter.NONE);
        }
        Filter f = getFilterByName(player);
        for (SellItem sItem : sellItemList) {
            System.out.println("Filter" + f);
            System.out.println("ItemName" + sItem.getItem().getType());
            if (f.equals(Filter.NONE)
                    || f.getItem().contains(sItem.getItem().getType())) {
                matchingItems.add(sItem);
            }
        }
        return matchingItems;
    }

    /*
    *   Gets a Itemlist of Sellable Items that schould the Player see
     */
    public static ArrayList<SellItem> getMatchingSellByPlayerName(Player player) {
        ArrayList<SellItem> matchingItems = new ArrayList<SellItem>();
        for (SellItem sItem : sellItemList) {
            if (sItem.getSellerUUID().equals(player.getUniqueId())) {
                matchingItems.add(sItem);
            }
        }
        return matchingItems;
    }

    /*
    *   Adds the Bidlore to an item on a special Slot
     */
    public static Inventory prepareItem(BidItem bItem, int pos, Inventory inv) {
        if(bItem.getItem()==null) {
            return inv;
        }
        ItemStack itemForInv = bItem.getItem().clone();
        if (Config.getOptionValue("has-normal-lore-in-Views").equals("false")) {
            itemForInv = clearLore(itemForInv);
        }
        ItemMeta im = itemForInv.getItemMeta();
        List<String> lore = Config.getStringList("items.bid-item-lore");
        ArrayList<String> l = new ArrayList<String>();
        for (String loreLine : lore) {
            loreLine=loreLine.replaceAll("%SELLER%", bItem.getSellerName());
            if(!bItem.getTopBidderName().equals("")) {
                loreLine=loreLine.replaceAll("%TOP_BIDDER%", bItem.getTopBidderName());
            }else{
                loreLine=loreLine.replaceAll("%TOP_BIDDER%", "-");
            }
            loreLine=loreLine.replaceAll("%BID%", bItem.getOffer() + "");
            loreLine=loreLine.replaceAll("%LEFT%", TimeUtils.convertToTime(bItem.getTimeExpire()));
            l.add(ItemUtils.color(loreLine));
        }
        im.setLore(l);
        itemForInv.setItemMeta(im);
        inv.setItem(pos, itemForInv);
        return inv;
    }

    /*
    *   Adds the Selllore to an item on a special Slot
     */
    public static Inventory prepareItem(SellItem sItem, int pos, Inventory inv) {
        ItemStack itemForInv = sItem.getItem().clone();
        itemForInv = clearLore(itemForInv);
        ItemMeta im = itemForInv.getItemMeta();
        List<String> lore = Config.getStringList("items.sell-item-lore");
        ArrayList<String> l = new ArrayList<String>();
        for (String loreLine : lore) {
            loreLine=loreLine.replaceAll("%SELLER%", sItem.getSellerName());
            loreLine=loreLine.replaceAll("%PRICE%", sItem.getPrice() + "");
            loreLine=loreLine.replaceAll("%LEFT%", TimeUtils.convertToTime(sItem.getTimeExpire()));
            l.add(ItemUtils.color(loreLine));
        }
        im.setLore(l);
        itemForInv.setItemMeta(im);
        inv.setItem(pos, itemForInv);
        return inv;
    }

    /*
    *   Adds the Collectlore to an item on a special Slot
     */
    public static Inventory prepareItem(CollectableItem cItem, int pos, Inventory inv) {
        ItemStack itemForInv = cItem.getItem().clone();
        itemForInv = clearLore(itemForInv);
        ItemMeta im = itemForInv.getItemMeta();
        List<String> lore = Config.getStringList("items.collectable-item-lore");
        ArrayList<String> l = new ArrayList<String>();
        for (String loreLine : lore) {
            loreLine=loreLine.replaceAll("%LEFT%", TimeUtils.convertToTime(cItem.getTimeExpire()));
            l.add(ItemUtils.color(loreLine));
        }
        im.setLore(l);
        itemForInv.setItemMeta(im);
        inv.setItem(pos, itemForInv);
        return inv;
    }
}
