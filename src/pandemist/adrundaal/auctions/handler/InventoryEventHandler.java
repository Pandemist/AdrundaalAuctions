package pandemist.adrundaal.auctions.handler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.model.Filter;
import pandemist.adrundaal.auctions.model.Shop;
import pandemist.adrundaal.auctions.model.ShopType;
import pandemist.adrundaal.auctions.utils.Utils;

public class InventoryEventHandler implements Listener {
	//Listener for Inventory Closing. Removes from all Shop maps.
	/*@EventHandler(priority=EventPriority.HIGH)
	public void onCloseListener(InventoryCloseEvent event) {
			Player eventTriggerer=(Player) event.getPlayer();
			Shop.removeFromSelectedItemMap(eventTriggerer);
			Shop.removeFromBidValueMap(eventTriggerer);
	}*/
	/*
	*   The Item onclick Handler
	*   Handels all clicks in a GUI Window
	*   Difference all cases and calls the correct methods from the ExtendItemHandler
	 */
	@EventHandler(priority=EventPriority.HIGH)
	public void onClickListener(InventoryClickEvent event) {
//		System.out.println(event.getInventory().getTitle());
		if(!event.getInventory().getTitle().contains(Config.getLang("shop-gui-name"))) {
			return;
		}
		event.setCancelled(true);
		if(event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
			return;
		}
//		System.out.println(event.getClickedInventory().getTitle());
		if(!event.getClickedInventory().getTitle().contains(Config.getLang("shop-gui-name"))) {
			return;
		}
	/*	if(!event.getClickedInventory().getTitle().contains(Config.getLang("shop-gui-name"))) {
			return;
		}*/
		if(event.getClick().equals(ClickType.SHIFT_LEFT)||event.getClick().equals(ClickType.SHIFT_RIGHT)) {
			event.setCancelled(true);
			return;
		}
		ItemStack clickedItem=event.getCurrentItem();
		if(clickedItem==null) {
			return;
		}
		Utils.updateListActuallity();
		Player eventTriggerer=(Player) event.getWhoClicked();
		int slot=event.getRawSlot();
		if(clickedItem.getType().equals(Material.AIR)
				||(clickedItem.equals(Config.getOptionItem("blocked-item")))) {
			return;
		}
		System.out.println("~~~~"+clickedItem.getType());
	//	System.out.println("This happend");
		if(clickedItem.equals(Config.getOptionItem("menu.collect"))) {
			Shop.removeFromPageList(eventTriggerer);
			Shop.removeFromTypeMap(eventTriggerer);
			ExtendetItemHandler.openItemCollect(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.myOverview"))) {
			Shop.removeFromPageList(eventTriggerer);
			Shop.removeFromTypeMap(eventTriggerer);
			ExtendetItemHandler.openMyOverview(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.pageBack"))) {
			ExtendetItemHandler.goPageBack(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.pageNext"))) {
			ExtendetItemHandler.goPageNext(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.refresh"))) {
			Utils.updateListActuallity();
			ItemConfig.refreshLists();
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.bidList"))) {
			Shop.removeFromPageList(eventTriggerer);
			Shop.removeFromTypeMap(eventTriggerer);
			ExtendetItemHandler.openBidView(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.sellList"))) {
			Shop.removeFromPageList(eventTriggerer);
			Shop.removeFromTypeMap(eventTriggerer);
			ExtendetItemHandler.openSellView(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.collectAll"))) {
			ExtendetItemHandler.collectAllItems(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.back"))) {
			ExtendetItemHandler.theGoBackMethod(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("menu.filter"))) {
			Shop.removeFromPageList(eventTriggerer);
			Shop.removeFromTypeMap(eventTriggerer);
			ExtendetItemHandler.openFilterPage(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.armor"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.ARMOR);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.food"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.FOOD);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.weapons"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.WEAPONS);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.potions"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.POTIONS);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.tools"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.TOOLS);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.blocks"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.BLOCKS);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.others"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.OTHER);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("filter.none"))) {
			Shop.removeFromFilterMap(eventTriggerer);
			Shop.addToFilterMap(eventTriggerer, Filter.NONE);
			Shop.removeFromTypeMap(eventTriggerer);
			Shop.addToTypeMap(eventTriggerer, ShopType.SELL);
			ExtendetItemHandler.openShopByType(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.confirm"))) {
			ExtendetItemHandler.mainConfirmMethod(eventTriggerer);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid+1"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '+', 1);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid+10"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '+', 10);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid+32"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '+', 32);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid+64"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '+', 64);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid-1"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '-', 1);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid-10"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '-', 10);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid-32"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '-', 32);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.bid-64"))) {
			ExtendetItemHandler.changeOffer(eventTriggerer, '-', 64);
			ExtendetItemHandler.updateBidView(eventTriggerer, event);
		}else if(clickedItem.equals(Config.getOptionItem("dialog.abort"))) {
			ExtendetItemHandler.theGoBackMethod(eventTriggerer);
		}else/* if(clickedItem.equals(Config.getOptionItem(""))) {
			//TOsDO: SOMETHING
		}else*/{
			System.out.println("No Special Item Selcted");
			ExtendetItemHandler.mainItemClickHandling(eventTriggerer, event);
		}
	}
}
