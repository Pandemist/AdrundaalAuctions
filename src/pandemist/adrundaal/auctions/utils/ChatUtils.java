package pandemist.adrundaal.auctions.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pandemist.adrundaal.auctions.config.Config;

import java.util.UUID;

public class ChatUtils {
	/*
	*   Reads a Message from Settings and prints it to the Player with chat praefix
	*/
	public static void sendMessageToPlayer(UUID uuid, String key) {
		if(Utils.isOnline(uuid)) {
			Player player=Bukkit.getPlayer(uuid);
			String message=Config.getLang(key);
			String praefix=Config.getLang("chat-praefix");
			player.sendMessage(ItemUtils.color(praefix+" "+message));
		}
	}
	/*
	*   Replaces Variables in a Message from the Config and print it with chat Praefix tot the Player
	 */
	public static void sendMessageToPlayer(UUID uuid, String key, ItemStack is) {
		if(Utils.isOnline(uuid)) {
			Player player=Bukkit.getPlayer(uuid);
			String message=Config.getLang(key);
			if(is!=null) {
				message=message.replaceAll("%AMOUNT%", is.getAmount()+"");
				message=message.replaceAll("%ITEM%", is.getType()+"");
			}
			String praefix=Config.getLang("chat-praefix");
			player.sendMessage(ItemUtils.color(praefix+" "+message));
		}
	}
}
