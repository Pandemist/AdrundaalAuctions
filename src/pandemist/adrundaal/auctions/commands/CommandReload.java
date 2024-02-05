package pandemist.adrundaal.auctions.commands;

import java.io.File;

import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import pandemist.adrundaal.auctions.AdrundaalAuctions;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.utils.ChatUtils;
import pandemist.adrundaal.auctions.utils.Utils;

public class CommandReload {
	/*
	*   ReloadCommand:
	*   Destroys all Lists of Items and the Settings and Item Config.
	*   Create new Item and Settings Config and imports new lists.
	 */
	public CommandReload(AdrundaalAuctions plugin, CommandSender sender, String[] args) {
		System.out.println("reload");
//Destroy all Settings
		Config.destroy();
		Utils.updateListActuallity();
		ItemConfig.refreshLists();
		AdrundaalAuctions.sellItemList.clear();
		AdrundaalAuctions.bidItemList.clear();
		AdrundaalAuctions.collectItemList.clear();
		ItemConfig.destroy();
//Fetch Settings new.
		Config.init(plugin.getConfig());
		ItemConfig.init(plugin.getDataFolder()+File.separator+"itmes.yml");
		ItemConfig.loadItemList();
		AdrundaalAuctions.itemMoneyUse=Config.getOptionValue("config.item-money-use").equals("true");
		if(sender instanceof Player) {
			ChatUtils.sendMessageToPlayer(((Player) sender).getUniqueId(), "reload-completed");
		}else{
			sender.sendMessage(Config.getLang("reload-completed"));
		}
	}
}
