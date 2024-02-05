package pandemist.adrundaal.auctions.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogConfig {
	private static String file;
	public static FileConfiguration log;

	//Initialize the Config
	public static void init(String file) {
		LogConfig.file=file;
		LogConfig.createIfNotExists(file);
		log= YamlConfiguration.loadConfiguration((File) new File(file));
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
		LogConfig.saveConfig();
		log=null;
	}
	public static void addToLogFile(String key, String player, ItemStack item) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd / HH:mm:ss");
		String time = "["+sdf.format(cal.getTime())+"]";
		String text = Config.getLang("log."+key);
		text = text.replaceAll("%NAME%", player);
		text = text.replaceAll("%ITEM%", item.getType().toString());
		text = text.replaceAll("%AMOUNT%", item.getAmount()+"");
		log.set(time, text);
		saveConfig();
	}
	public static void saveConfig() {
		try {
			log.save(file);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
