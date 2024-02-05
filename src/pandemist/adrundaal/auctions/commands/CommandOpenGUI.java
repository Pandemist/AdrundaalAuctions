package pandemist.adrundaal.auctions.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pandemist.adrundaal.auctions.AdrundaalAuctions;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.handler.ExtendetItemHandler;
import pandemist.adrundaal.auctions.model.Shop;
import pandemist.adrundaal.auctions.model.ShopType;

public class CommandOpenGUI {
	/*
	*   Command that opens the GUI:
	*   Depends on the setting 'default-shop-view' the Command opens a spezial Shop Type.
	*   This Command is only allowed for instances of Player.
	 */
	public CommandOpenGUI(AdrundaalAuctions plugin, CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Config.getLang("sender-is-no-player"));
			return;
		}
		Player player=(Player) sender;
		Shop.clearListsFromPlayer(player);
		String startPage=Config.getOptionValue("config.default-shop-view");
		if(startPage.toLowerCase().equals("bid")) {
			Shop.addToTypeMap(player, ShopType.BID);
			ExtendetItemHandler.openBidView(player);
		}else if(startPage.toLowerCase().equals("collect")) {
			Shop.addToTypeMap(player, ShopType.COLLECT);
			ExtendetItemHandler.openItemCollect(player);
		}else if(startPage.toLowerCase().equals("my")) {
			Shop.addToTypeMap(player, ShopType.MY);
			ExtendetItemHandler.openMyOverview(player);
		}else if(startPage.toLowerCase().equals("filter")) {
			Shop.addToTypeMap(player, ShopType.FILTER);
			ExtendetItemHandler.openFilterPage(player);
		}else{
			Shop.addToTypeMap(player, ShopType.SELL);
			ExtendetItemHandler.openSellView(player);
		}
	}
}
