package pandemist.adrundaal.auctions.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import pandemist.adrundaal.auctions.AdrundaalAuctions;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.config.LogConfig;
import pandemist.adrundaal.auctions.model.*;
import pandemist.adrundaal.auctions.utils.ChatUtils;
import pandemist.adrundaal.auctions.utils.EcoUtils;
import pandemist.adrundaal.auctions.utils.ItemUtils;
import pandemist.adrundaal.auctions.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static pandemist.adrundaal.auctions.AdrundaalAuctions.bidItemList;
import static pandemist.adrundaal.auctions.AdrundaalAuctions.collectItemList;
import static pandemist.adrundaal.auctions.AdrundaalAuctions.sellItemList;

public class ExtendetItemHandler {
	/*
	*   Creates a Inventory, for the Item Collect.
	*   Then fills it with up to 45 items depends on the Pagenumber
	 */
	public static Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("AdrundaalAuctions");
	@SuppressWarnings("static-access")
	public ExtendetItemHandler(Plugin plugin){
		this.plugin = plugin;
	}
	public static void openItemCollect(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Shop.getEmptyShop();

		inv=Shop.setupCollectAble(inv);

		//Prepare ArrayList with Matiching Items
		ArrayList<CollectableItem> matchingItems=Shop.getMatchingCollectableByPlayerName(player);

		int onPage=1;
		if(Shop.isPlayerInPageList(player)) {
			onPage=Shop.getPageByName(player);
		}
		Shop.addToTypeMap(player, ShopType.COLLECT);
		Shop.addToPageList(player, 1, (int) (matchingItems.size()/45.0));
		int minItemPerPage=0+((onPage-1)*45);
		int maxItemPerPage=44+((onPage-1)*45);
		ArrayList<String> keyList=new ArrayList<String>();
		for(int i=minItemPerPage, j=1; i<maxItemPerPage&&i<matchingItems.size()&&j<=45; i++, j++) {
			inv=Shop.prepareItem(matchingItems.get(i), i, inv);
			keyList.add(matchingItems.get(i).getIndividualID());
		}
		Shop.removeFromInvMap(player);
		Shop.addToInvMap(player, keyList);
		player.openInventory(inv);
	}
	/*
	*   Creates a Inventory, for the Overview of all inserts from the Player.
	*   Then fills it with up to 45 items depends on the Pagenumber.
	*   Basicly the same as openItemColellct
	*/
	public static void openMyOverview(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Shop.getEmptyShop();

		inv=Shop.setupMyOverview(inv);

		ArrayList<BidItem> matchingBidItems=Shop.getMatchingBidByPlayerName(player);
		ArrayList<SellItem> matchingSellItems=Shop.getMatchingSellByPlayerName(player);

		int onPage=1;
		if(Shop.isPlayerInPageList(player)) {
			onPage=Shop.getPageByName(player);
		}
		Shop.addToTypeMap(player, ShopType.MY);
		Shop.addToPageList(player, 1, (int) ((matchingBidItems.size()+matchingSellItems.size())/45.0));
		int minItemValPerPage=0+((onPage-1)*45);
		int maxItemValPerPage=44+((onPage-1)*45);
		int i=minItemValPerPage;
		int j=1;
		ArrayList<String> keyList=new ArrayList<String>();
		for(; i<maxItemValPerPage&&i<matchingBidItems.size()&&j<=45; i++, j++) {
			inv=Shop.prepareItem(matchingBidItems.get(i), i, inv);
			keyList.add(matchingBidItems.get(i).getIndividualID());
		}
		for(; i<maxItemValPerPage&&i<matchingSellItems.size()&&j<=45; i++, j++) {
			inv=Shop.prepareItem(matchingSellItems.get(i), i, inv);
			keyList.add(matchingSellItems.get(i).getIndividualID());
		}
		Shop.removeFromInvMap(player);
		Shop.addToInvMap(player, keyList);
		player.openInventory(inv);
	}
	/*
	*   Creates a Inventory, for the Sellview.
	*   Then fills it with up to 45 items depends on the Pagenumber.
	*   Basicly the same as openItemColellct
	*/
	public static void openSellView(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Shop.getEmptyShop();

		inv=Shop.setupSellView(inv);

		ArrayList<SellItem> matchingItems=Shop.getMatchingSellByFilter(player);

		int onPage=1;
		if(Shop.isPlayerInPageList(player)) {
			onPage=Shop.getPageByName(player);
		}
		Shop.addToTypeMap(player, ShopType.SELL);
		Shop.addToPageList(player, 1, (int) (matchingItems.size()/45.0));
		int minItemValPerPage=0+((onPage-1)*45);
		int maxItemValPerPage=44+((onPage-1)*45);
		int j=1;
		ArrayList<String> keyList=new ArrayList<String>();
		for(int i=minItemValPerPage; i<maxItemValPerPage&&i<matchingItems.size()&&j<=45; i++, j++) {
			inv=Shop.prepareItem(matchingItems.get(i), i, inv);
			keyList.add(matchingItems.get(i).getIndividualID());
		}
		Shop.removeFromInvMap(player);
		Shop.addToInvMap(player, keyList);
		player.openInventory(inv);
	}
	/*
	*   Prepares a Confirm Dialog View for buying.
	*   Contains 4 Yes, 4 No and the buyable item Buttons
	 */
	public static void openSellConfirmView(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Bukkit.createInventory(null, (9), Config.getLang("shop-gui-name"));
		inv=Shop.setupConfirmView(inv);
		SellItem sItem=ItemUtils.getActualSellItemByPlayer(player);
		ItemStack is=sItem.getItem().clone();
		ItemMeta im=is.getItemMeta();
		im.setLore(null);
		List<String> lore=Config.getStringList("items.selling-item-lore");
		ArrayList<String> l=new ArrayList<String>();
		for(String loreLine : lore) {
			loreLine.replaceAll("%PRICE%", sItem.getPrice()+"");
			l.add(ItemUtils.color(loreLine));
		}
		im.setLore(l);
		is.setItemMeta(im);
		inv.setItem(4, is);
		player.openInventory(inv);
	}
	/*
	*   Creates a Inventory, for the Bidview.
	*   Then fills it with up to 45 items depends on the Pagenumber.
	*   Basicly the same as openItemColellct
	*/
	public static void openBidView(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Shop.getEmptyShop();

		inv=Shop.setupBidView(inv);

		ArrayList<BidItem> matchingItems=Shop.getMatchingBidByFilter(player);

		int onPage=1;
		if(Shop.isPlayerInPageList(player)) {
			onPage=Shop.getPageByName(player);
		}
		Shop.addToTypeMap(player, ShopType.BID);
		Shop.addToPageList(player, 1, (int) (matchingItems.size()/45.0));
		int minItemValPerPage=0+((onPage-1)*45);
		int maxItemValPerPage=44+((onPage-1)*45);
		int j=1;
		ArrayList<String> keyList=new ArrayList<String>();
		for(int i=minItemValPerPage; i<maxItemValPerPage&&i<matchingItems.size()&&j<=45; i++, j++) {
			inv=Shop.prepareItem(matchingItems.get(i), i, inv);
			keyList.add(matchingItems.get(i).getIndividualID());
		}
		Shop.removeFromInvMap(player);
		Shop.addToInvMap(player, keyList);
		player.openInventory(inv);
	}
	/*
	*   Prepares a Confirm Dialog View for bidding.
	*   Contains 4 add to Bid, 4 Remove from Bid, 1 Yes, 1 NO and the bidable item Buttons
	*/
	public static void openBidConfirmView(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Bukkit.createInventory(null, 18, Config.getLang("shop-gui-name"));
		inv=Shop.setupBidConfirmView(inv);
		BidItem bItem=ItemUtils.getActualBidItemByPlayer(player);
		ItemStack is=bItem.getItem().clone();
		ItemMeta im=is.getItemMeta();
		im.setLore(null);
		List<String> lore=Config.getStringList("items.bidding-item-lore");
		ArrayList<String> l=new ArrayList<String>();
		for(String loreLine : lore) {
			loreLine=loreLine.replaceAll("%OLD_BID%", ItemUtils.getActualBidItemByPlayer(player).getOffer()+"");
			loreLine=loreLine.replaceAll("%NEW_BID%", "&c"+ItemUtils.getActualBidItemByPlayer(player).getOffer()+"");
			l.add(ItemUtils.color(loreLine));
		}
		Shop.addToBidValueMap(player, ItemUtils.getActualBidItemByPlayer(player).getOffer()+1);
		im.setLore(l);
		is.setItemMeta(im);
		inv.setItem(4, is);
		player.openInventory(inv);
	}
	/*
	*   Prepares a Confirm Dialog View for abbording a Item from Bidding/Selling.
	*   Contains 4 Yes, 4 No and the Selling/buyitem item Buttons
	*/
	public static void openMyOverviewConfirmView(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Bukkit.createInventory(null, 9, Config.getLang("shop-gui-name"));
		inv=Shop.setupConfirmView(inv);
		BidItem bItem=ItemUtils.getActualBidItemByPlayer(player);
		System.out.println(bItem);
		ItemStack is;
		if (bItem != null) {
			is = bItem.getItem();
		}else{
			SellItem sItem = ItemUtils.getActualSellItemByPlayer(player);
			System.out.println(sItem);
			if(sItem == null) {
				return;
			}else{
				is = sItem.getItem();
			}
		}
		ItemStack iss = is.clone();
		ItemMeta im=iss.getItemMeta();
		im.setLore(null);
		List<String> lore=Config.getStringList("items.myOverviewConfirm-item-lore");
		ArrayList<String> l=new ArrayList<String>();
		for(String loreLine : lore) {
			l.add(ItemUtils.color(loreLine));
		}
		im.setLore(l);
		iss.setItemMeta(im);
		inv.setItem(4, iss);
		player.openInventory(inv);
	}
	/*
	*   Opens the Filter page. Only Contains the Different Filter Buttons
	 */
	public static void openFilterPage(Player player) {
		Shop.closeLastView(player);
		Inventory inv=Shop.getEmptyShop();

		inv=Shop.setupFilterView(inv);

		player.openInventory(inv);
	}
	/*
	*   The Handling with the going back. Counts -1 for the acutal page and reopens the shop with the new page
	 */
	public static void goPageBack(Player player) {
		if(Shop.isPlayerInPageList(player)) {
			int page=Shop.getPageByName(player);
			if(page>1) {
				Shop.updatePageByName(player, page--);
				openShopByType(player);
			}
		}else{
			return;
		}
	}
	/*
	*   The Handling with the going forward. Counts +1 for the acutal page and reopens the shop with the new page
	*/
	public static void goPageNext(Player player) {
		if(Shop.isPlayerInPageList(player)) {
			int page=Shop.getPageByName(player);
			if(page<Shop.getMaxPageByName(player)) {
				Shop.updatePageByName(player, page++);
				openShopByType(player);
			}
		}else{
			return;
		}
	}

	/*
	*   This Button iterates over all Collectable Items and gif them back to the Player, if Inventory has space
	 */
	public static void collectAllItems(Player player) {
		ArrayList<CollectableItem> matchingItems=Shop.getMatchingCollectableByPlayerName(player);
		System.out.println("KLICKED");
		System.out.println(matchingItems.size());
		for(CollectableItem cItem : matchingItems) {
			LogConfig.addToLogFile("givenBack", cItem.getOwnerName(), cItem.getItem());
			if(Utils.isInvFull(player)) {
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "inventory-full");
				break;
			}else{
				System.out.println("ITEM");
				player.getInventory().addItem(cItem.getItem());
			//	collectItemList.remove(cItem);
				ItemUtils.removeFromCollectList(cItem);
			}
		}
		ItemConfig.refreshLists();
		openShopByType(player);
	}
	/*
	*   Open the Shop for the inserted Player, by the value in the ShopType Map
	 */
	public static void openShopByType(Player player) {
		System.out.println(Shop.isPlayerInTypeMap(player));
		if(Shop.isPlayerInTypeMap(player)) {
			ShopType s=Shop.getTypeByName(player);
			System.out.println(s);
			if(ShopType.BID.equals(s)) {
				openBidView(player);
			}else if(ShopType.SELL.equals(s)) {
				openSellView(player);
			}else if(ShopType.COLLECT.equals(s)) {
				openItemCollect(player);
			}else if(ShopType.MY.equals(s)) {
				openMyOverview(player);
			}else if(ShopType.FILTER.equals(s)) {
				openFilterPage(player);
			}
		}
	}
	/*
	*   Handels the Back buttons. This button is only appers in the Dialogs, so it will return to a Shop Page
	 */
	public static void theGoBackMethod(Player player) {
		if(!Shop.isPlayerInTypeMap(player)) {
			return;
		}
		if(!Shop.isPlayerInPageList(player)) {
			return;
		}
		Shop.removeFromInvMap(player);
		Shop.removeFromSelectedItemMap(player);
		openShopByType(player);
	}
	/*
	*   Updates the offer, a Player give on a Item
	 */
	public static void changeOffer(Player player, char operation, int operand) {
		if(!Shop.isPlayerInBidValueMap(player)) {
			return;
		}
		int offer=Shop.getBidValueByName(player);
		if(operation=='+') {
			offer=offer+operand;
		}else{
			offer=offer-operand;
		}
		if(offer<1) {
			offer=0;
		}
		Shop.updateBidValueByName(player, offer);
	}
	/*
	*   Updates the Itemlore of the Bid Item Dialog
	 */
	public static void updateBidView(Player player, InventoryClickEvent event) {
		//BidItem isBiddingItem = ItemUtils.getActualBidItemByPlayer(player);
		ItemStack is=event.getInventory().getItem(4);
		if(is==null) {
			return;
		}
		String topBidding;
		if(ItemUtils.getActualBidItemByPlayer(player).getOffer()>=Shop.getBidValueByName(player)) {
			topBidding="&c"+Shop.getBidValueByName(player);
		}else{
			topBidding="&a"+Shop.getBidValueByName(player);
		}
		ItemMeta im=is.getItemMeta();
		im.setLore(null);
		List<String> lore=Config.getStringList("items.bidding-item-lore");
		ArrayList<String> l=new ArrayList<String>();
		for(String loreLine : lore) {
			loreLine=loreLine.replaceAll("%OLD_BID%", (ItemUtils.getActualBidItemByPlayer(player).getOffer()+1)+"");
			loreLine=loreLine.replaceAll("%NEW_BID%", topBidding);
			l.add(ItemUtils.color(loreLine));
		}
		im.setLore(l);
		is.setItemMeta(im);
		event.getInventory().setItem(4, is);
	}
	/*
	*   Tests witch Submethod handels the witch cases, and calls these
	 */
	public static void mainConfirmMethod(Player player) {
		if(!Shop.isPlayerInTypeMap(player)) {
			return;
		}
		ShopType st=Shop.getTypeByName(player);
		if(st.equals(ShopType.SELL)) {
			sellConfirm(player);
		}else if(st.equals(ShopType.BID)) {
			bidConfirm(player);
		}else if(st.equals(ShopType.MY)) {
			myConfirm(player);
		}
		return;
	}
	/*
	*   Handels the Confrim in the Buy Dialog. If the Player has enough money, he will recieve the item.
	 */
	public static void sellConfirm(Player player) {
		SellItem sellingItem=ItemUtils.getActualSellItemByPlayer(player);
		if(sellingItem==null) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-not-found");
			return;
		}
		if(!EcoUtils.ecoHasMoney(player, sellingItem.getPrice())) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "not-enought-money");
			return;
		}
		EcoUtils.ecoTakeMoney(player, sellingItem.getPrice());
		EcoUtils.ecoGiveMoney(sellingItem.getSellerName(), sellingItem.getSellerUUID(), sellingItem.getPrice());
		sellingItem.setSellerName(player.getDisplayName());
		sellingItem.setSellerUUID(player.getUniqueId());

		if(!Utils.isInvFull(player)) {
			player.getInventory().addItem(sellingItem.getItem());
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-has-been-given");
		}else{
			CollectableItem c=sellingItem.toCollectble();
			collectItemList.add(c);
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "inventory-full");
		}
		ItemUtils.removeFromSellList(sellingItem);
	//	sellItemList.remove(sellingItem);
		ItemConfig.refreshLists();
		Shop.removeFromSelectedItemMap(player);
		Shop.removeFromInvMap(player);
		Shop.removeFromBidValueMap(player);
		openShopByType(player);
	}
	/*
	*   Handels the Confirm in the Bid Dialog. if the Player gives a Bid that is high enough, it will be inserted.
	 */
	public static void bidConfirm(Player player) {
		BidItem biddingItem=ItemUtils.getActualBidItemByPlayer(player);
		if(biddingItem==null) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-not-found");
			return;
		}
		if(Shop.getBidValueByName(player)>=Config.getInt("config.max-beginning-bid-price")) {
			Shop.updateBidValueByName(player, Config.getInt("config.max-beginning-bid-price"));
		}
		System.out.println(Shop.getBidValueByName(player));
		System.out.println(biddingItem.getOffer());
		if(Shop.getBidValueByName(player)<=biddingItem.getOffer()) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "offer-to-low");
			return;
		}
		if(!EcoUtils.ecoHasMoney(player, Shop.getBidValueByName(player))) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "not-enought-money");
			return;
		}
		if(biddingItem.getTopBidderUUID()!=null) {
			EcoUtils.ecoGiveMoney(biddingItem.getTopBidderName(), biddingItem.getTopBidderUUID(), biddingItem.getOffer());
		}
		LogConfig.addToLogFile("givenBack", biddingItem.getSellerName(), biddingItem.getItem());
		biddingItem.setOffer(Shop.getBidValueByName(player));
		biddingItem.setTopBidderName(player.getDisplayName());
		biddingItem.setTopBidderUUID(player.getUniqueId());
		ItemUtils.replaceBidItemInList(biddingItem);
		EcoUtils.ecoTakeMoney(player, biddingItem.getOffer());
		if(biddingItem.getOffer()==Config.getInt("config.max-beginning-bid-price")) {
			if(!Utils.isInvFull(player)) {
				player.getInventory().addItem(biddingItem.getItem());
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-has-been-given");
			}else{
				CollectableItem c=biddingItem.toCollectble();
				collectItemList.add(c);
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "inventory-full");
			}
			bidItemList.remove(biddingItem);
		}
		ItemConfig.refreshLists();
		Shop.removeFromSelectedItemMap(player);
		Shop.removeFromInvMap(player);
		Shop.removeFromBidValueMap(player);
		openShopByType(player);
	}
	/*
	* Handels the Confirm Dialog in the actual inserted Overview. The Item will be returned in the AH-Chest
	 */
	public static void myConfirm(Player player) {
		if(Utils.isInvFull(player)) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "inventory-full");
			return;
		}
		String itemID=Shop.getSelectedItemByName(player);
		System.out.println(itemID);
		ItemStack i;
		BidItem bItem = ItemUtils.getBidItemByUID(itemID);
		SellItem sItem = ItemUtils.getSellItemByUID(itemID);
		if(bItem!=null) {
			System.out.println("bItem: "+bItem.getItem().getType());
			LogConfig.addToLogFile("aborted", bItem.getSellerName(), bItem.getItem());
			i = bItem.getItem();
		}else{
			if(sItem!=null) {
				System.out.println("sItem: "+sItem.getItem().getType());
				LogConfig.addToLogFile("aborted", sItem.getSellerName(), sItem.getItem());
				i = sItem.getItem();
			}else{
				ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-not-found");
				return;
			}
		}
		player.getInventory().addItem(i);
		if (sItem!=null) {
			System.out.println("This will be removed: "+sItem.getItem().getType());
			ItemUtils.removeFromSellList(sItem);
		//	sellItemList.remove(sItem);
		}else{
			System.out.println("This will be removed: "+bItem.getItem().getType());
			ItemUtils.removeFromBidList(bItem);
		//	bidItemList.remove(bItem);
		}
		ItemConfig.refreshLists();
		Shop.removeFromSelectedItemMap(player);
		Shop.removeFromInvMap(player);
		openShopByType(player);
	}
	/*
	*   Handels the click on no special menu item. The methode calls the Submethodes debend on the Players ShopType.
	 */
	public static void mainItemClickHandling(Player player, InventoryClickEvent e) {
		int slot = e.getRawSlot();
		if(!Shop.isPlayerInTypeMap(player)) {
			return;
		}
		if(Shop.isPlayerInSelectedItemMap(player)) {
			System.out.println("TETST");
			return;
		}
		Shop.addToSelectedItemMap(player, Shop.getInvByName(player).get(slot));
		System.out.println("ID of Clicked Slot"+Shop.getInvByName(player).get(slot));
		ShopType st=Shop.getTypeByName(player);
		if(st.equals(ShopType.SELL)) {
			itemClickHandlingSell(player, e);
		}else if(st.equals(ShopType.BID)) {
			itemClickHandlingBid(player, e);
		}else if(st.equals(ShopType.COLLECT)) {
			itemClickHandlingCollect(player, slot);
		}else if(st.equals(ShopType.MY)) {
			itemClickHandlingMy(player);
		}
	}
	/*
	*   Handels the Click in the Sellview. If the player not owns the clicked Item, the confirm dialog will be opened
	 */
	public static void itemClickHandlingSell(Player player, InventoryClickEvent event) {
		int slot = event.getRawSlot();
		String itemID=Shop.getInvByName(player).get(slot);
		SellItem sItem=ItemUtils.getSellItemByUID(itemID);
		if(sItem==null) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-not-found");
			return;
		}
		if(sItem.getSellerUUID().equals(player.getUniqueId())) {
			itemIsOwnItem(player, event);
			return;
		}
		Shop.addToSelectedItemMap(player, sItem.getIndividualID());
		openSellConfirmView(player);
	}
	/*
	*   handels the Click in the Bidview. If the Player, is not owner, or Topbidder, the confirm Dialog will be opened
	 */
	public static void itemClickHandlingBid(Player player, InventoryClickEvent event) {
		int slot = event.getRawSlot();
		String itemID=Shop.getInvByName(player).get(slot);
		BidItem bItem=ItemUtils.getBidItemByUID(itemID);
		if(bItem==null) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "item-not-found");
			Shop.removeFromSelectedItemMap(player);
			return;
		}
		if(bItem.getTopBidderUUID()==null) {
			if(bItem.getSellerUUID().equals(player.getUniqueId())) {
				Shop.removeFromSelectedItemMap(player);
				itemIsOwnItem(player, event);
				return;
			}
		}else{
			if(bItem.getSellerUUID().equals(player.getUniqueId())||bItem.getTopBidderUUID().equals(player.getUniqueId())) {
				Shop.removeFromSelectedItemMap(player);
				itemIsOwnItem(player, event);
				return;
			}
		}
		if(Shop.isTwoTimesIdInSelecteditemMap(itemID)) {
			Shop.removeFromSelectedItemMap(player);
			itemIsOwnItem(player, event);
			return;
		}
		Shop.addToBidValueMap(player, bItem.getOffer()+1);
		Shop.addToSelectedItemMap(player, bItem.getIndividualID());
		openBidConfirmView(player);
	}
	/*
	*   Opens the Confirm Dialog form the Items in the Inserted Overview
	 */
	public static void itemClickHandlingMy(Player player) {
		openMyOverviewConfirmView(player);

	}
	/*
	*   Opens the Confirm Dialog for My inserted Overview
	 */
	public static void itemClickHandlingCollect(Player player, int slot) {
		if(Utils.isInvFull(player)) {
			ChatUtils.sendMessageToPlayer(player.getUniqueId(), "inventory-full");
			return;
		}
		String itemID=Shop.getInvByName(player).get(slot);
		CollectableItem cItem =ItemUtils.getCollectItemByUID(itemID);
		ItemStack itemToReturn= cItem.getItem();
		LogConfig.addToLogFile("givenBack", cItem.getOwnerName(), cItem.getItem());
		player.getInventory().addItem(itemToReturn);
		ItemUtils.removeFromCollectList(cItem);
		Shop.removeFromSelectedItemMap(player);
		Utils.updateListActuallity();
		ItemConfig.refreshLists();
		openShopByType(player);
	}

	/*
	*   Replace a Item for 3 Seconds with a Blocksignal, that you can see that you are not allowed to click
	 */
	public static void itemIsOwnItem(Player player, InventoryClickEvent event) {
		int slot = event.getRawSlot();
		Inventory inv = event.getInventory();
		System.out.println(slot);
		ItemStack item=inv.getItem(slot);
		ItemStack is=Config.getOptionItem("blocked-item");
		inv.setItem(slot, is);
		System.out.println(is.getType());
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				inv.setItem(slot, item);
			}
		}, 3*20);
	}
}
