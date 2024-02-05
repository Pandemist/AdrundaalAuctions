package pandemist.adrundaal.auctions.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pandemist.adrundaal.auctions.AdrundaalAuctions;
import pandemist.adrundaal.auctions.model.BidItem;
import pandemist.adrundaal.auctions.model.CollectableItem;
import pandemist.adrundaal.auctions.model.SellItem;
import pandemist.adrundaal.auctions.utils.Utils;

public class ItemConfig {
	private static String file;
	public static FileConfiguration config;

	//Initialize the ItemConfig
	public static void init(String file) {
		ItemConfig.file=file;
		ItemConfig.createIfNotExists(file);
		config=YamlConfiguration.loadConfiguration((File) new File(file));
	}

	//Create the ItemConfig (Only use on first Plugin start)
	private static boolean createIfNotExists(String file) {
		try {
			return new File(file).getParentFile().mkdirs()&&new File(file).createNewFile();
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

	//Destroys the Item Config
	public static void destroy() {
		ItemConfig.saveConfig();
		config=null;
	}

	//Test if Item Section exists
	public static boolean existsItem(String key) {
		return config.isConfigurationSection(key);
	}

	//Test if String is null
	public static String notNull(String string) {
		return string!=null ? string : "";
	}

	//Removes an Item
	public static void clearSection(String key) {
		config.set(key, null);
		saveConfig();
	}

	//Saves the Changes to the File
	public static void saveConfig() {
		try {
			config.save(file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	//Iterates over the File and fills the sell-, bid- and collect list
	public static void loadItemList() {
		int i=0;
		while(ItemConfig.existsItem("sell."+i)) {
			AdrundaalAuctions.sellItemList.add(new SellItem(i+""));
			i++;
		}
		i=0;
		while(ItemConfig.existsItem("bid."+i)) {
			AdrundaalAuctions.bidItemList.add(new BidItem(i+""));
			i++;
		}
		i=0;
		while(ItemConfig.existsItem("collect."+i)) {
			AdrundaalAuctions.collectItemList.add(new CollectableItem(i+""));
			i++;
		}
	}

	//Clears the File and fill it with all the actual stats. Works like a backup File
	public static void refreshLists() {
		//Utils.updateListActuallity();
		clearSection("sell");
		for(int i=0; i<AdrundaalAuctions.sellItemList.size(); i++) {
			AdrundaalAuctions.sellItemList.get(i).toConfig(i+"");
		}
		clearSection("bid");
		for(int i=0; i<AdrundaalAuctions.bidItemList.size(); i++) {
			AdrundaalAuctions.bidItemList.get(i).toConfig(i+"");
		}
		clearSection("collect");
		for(int i=0; i<AdrundaalAuctions.collectItemList.size(); i++) {
			AdrundaalAuctions.collectItemList.get(i).toConfig(i+"");
		}
	}
}
