package pandemist.adrundaal.auctions;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import pandemist.adrundaal.auctions.commands.*;
import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.config.LogConfig;
import pandemist.adrundaal.auctions.handler.InventoryEventHandler;
import pandemist.adrundaal.auctions.model.BidItem;
import pandemist.adrundaal.auctions.model.CollectableItem;
import pandemist.adrundaal.auctions.model.SellItem;
import pandemist.adrundaal.auctions.utils.EcoUtils;
import pandemist.adrundaal.auctions.utils.Utils;

public class AdrundaalAuctions extends JavaPlugin {
	public static boolean itemMoneyUse;
	private static AdrundaalAuctions instance;
	public static ArrayList<SellItem> sellItemList = new ArrayList<SellItem>();
	public static ArrayList<BidItem> bidItemList = new ArrayList<BidItem>();
	public static ArrayList<CollectableItem> collectItemList = new ArrayList<CollectableItem>();

	public static AdrundaalAuctions getInstance() {
		return instance;
	}

	public void onEnable() {
		super.onEnable();
		instance = this;
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryEventHandler(), this);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		Config.init(this.getConfig());
		ItemConfig.init(this.getDataFolder() + File.separator + "itmes.yml");
		LogConfig.init(this.getDataFolder() + File.separator + "log.yml");
		ItemConfig.loadItemList();
		itemMoneyUse = Config.getOptionValue("config.item-money-use").equals("true");
		if(!itemMoneyUse&&EcoUtils.hasVault()) {
			onDisable();
			this.getLogger().info("Your Server has no Vault Plugin. Please change your Settings, or install Vault.");
		}
		if(!itemMoneyUse&&EcoUtils.setupEconomy()) {
			onDisable();
			this.getLogger().info("There was an error, while initialize Economy.");
		}
	}
	public void onDisable() {
		if(sellItemList!=null) {
			Utils.updateListActuallity();
			ItemConfig.refreshLists();
			sellItemList.clear();
			bidItemList.clear();
			collectItemList.clear();
		}
		super.onDisable();
		Config.destroy();
		ItemConfig.destroy();
		LogConfig.destroy();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==0) {
			new CommandOpenGUI(this, sender, args);
		}else if(args.length>0) {
			if(args[0].equalsIgnoreCase("bid")||args[0].equalsIgnoreCase("sell")) {
				new CommandBidSell(this, sender, args);
			}else if(args[0].equalsIgnoreCase("reload")) {
				new CommandBidSell(this, sender, args);
				new CommandReload(this, sender, args);
			}else{
				new CommandHelp(this, sender, args);
			}
			return true;
		}
		return false;
	}
}