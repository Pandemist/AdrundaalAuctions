package pandemist.adrundaal.auctions.config;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import pandemist.adrundaal.auctions.utils.ItemUtils;

public class Config {
	private static FileConfiguration config;

	//Initialize the Config
	public static void init(FileConfiguration config) {
		Config.config=config;
	}

	//Destroy the Config
	public static void destroy() {
		config=null;
	}

	//Get a String from the message Section in the config File
	public static String getLang(String key) {
		return ChatColor.translateAlternateColorCodes((char) '&', (String) Config.notNull(config.getString("messages."+key)));
	}

	//Get a String from no special Section in the config File
	public static String getOptionValue(String key) {
		return (String) Config.notNull(config.getString(key));
	}

	//Get a List of Values for the inserted key
	public static List<String> getStringList(String key) {
		return config.getStringList(key);
	}

	//Returns a Item, created from an Item in the item Section
	public static ItemStack getOptionItem(String key) {
		//	System.out.println(key);
		if(!config.isConfigurationSection("items."+key)) {
			return null;
		}
		String type=config.getString("items."+key+".type");
		int ty=0;
		ItemStack i;
		//	System.out.println(type);
		if(type.contains(":")) {
			String[] b=type.split(":");
			type=b[0];
			ty=Integer.parseInt(b[1]);
			//		System.out.println(type);
			//		System.out.println(Material.matchMaterial(type));
			if(Material.matchMaterial(type) == null) {
				return null;
			}
			i = new ItemStack(Material.matchMaterial(type), 1, (short) ty);
		}else{
			//		System.out.println(type);
			//		System.out.println(Material.matchMaterial(type));
			if(Material.matchMaterial(type) == null) {
				return null;
			}
			i = new ItemStack(Material.matchMaterial(type),1);
		}


		i=ItemUtils.addMetaToItem(i, config.getString("items."+key+".name"),
				config.getStringList("items."+key+".lore"),
				null, null);
		//	System.out.println(type);
	//	System.out.println(config.getString("items."+key+".name"));
		//	System.out.println(config.getStringList("items."+key+".lore"));
		return i;
	}

	//Private help methode, that checks if string is not Null
	private static String notNull(String string) {
		return string!=null ? string : "";
	}

	//Get a Integer value from no special Section in the config File
	public static int getInt(String key) {
		return Integer.parseInt(Config.notNull(config.getString(key)));
	}
	public static long getLong(String key) {
		return Long.parseLong(Config.notNull(config.getString(key)));
	}
}
