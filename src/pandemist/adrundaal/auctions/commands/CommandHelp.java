package pandemist.adrundaal.auctions.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import pandemist.adrundaal.auctions.AdrundaalAuctions;

public class CommandHelp {
	/*
	*   Help Command:
	*   This Command only prints all Commands, that can be used
	*   This Command can be performed by any Type of sender
	 */
	public CommandHelp(AdrundaalAuctions plugin, CommandSender sender, String[] args) {
		sender.sendMessage("=== "+ChatColor.GOLD+"["+ChatColor.RED+"Command List"+ChatColor.GOLD+"]"+ChatColor.RESET+"===");
		sender.sendMessage("- "+ChatColor.WHITE+"/ah sell <Price> [<Amount>]");
		sender.sendMessage("- "+ChatColor.WHITE+"/ah bid <Price> [<Amount>]");
		sender.sendMessage("- "+ChatColor.WHITE+"/ah");
		sender.sendMessage("- "+ChatColor.WHITE+"/ah reload");
	}
}
