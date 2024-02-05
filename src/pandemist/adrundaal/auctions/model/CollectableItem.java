package pandemist.adrundaal.auctions.model;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.server.v1_10_R1.Item;
import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.config.ItemConfig;

public class CollectableItem extends AuctionItem {
	private String ownerName="";
	private UUID ownerUUID=null;
	/*
	*   Constructor
	 */
	public CollectableItem(long ttime, ItemStack tis, String indivID, String tsellerName, UUID tsellerUUID) {
		super.timeExpire=ttime;
		super.is=tis;
		getAttrFromItem();
		super.individualID=indivID;
		this.ownerName=tsellerName;
		this.ownerUUID=tsellerUUID;
	}

	public CollectableItem(String key) {
		super.timeExpire=ItemConfig.config.getLong("collect."+key+".timeExpire");
		super.is=ItemConfig.config.getItemStack("collect."+key+".item");
		super.attributes=ItemConfig.notNull(ItemConfig.config.getString("collect."+key+".attributes"));
		makeItem();
		super.individualID=ItemConfig.notNull(ItemConfig.config.getString("collect."+key+".indivID"));
		this.ownerName=ItemConfig.notNull(ItemConfig.config.getString("collect."+key+".ownerName"));
		this.ownerUUID=UUID.fromString(ItemConfig.config.getString("collect."+key+".ownerUUID"));
	}

	public CollectableItem() {

	}
	/*
	*   Getter and Setter Methodes
	 */
	@Override
	public long getTimeExpire() {
		return super.timeExpire;
	}

	@Override
	public void setTimeExpire(long time) {
		super.timeExpire=time;
	}

	@Override
	public ItemStack getItem() {
		return super.is;
	}

	public String getOwnerName() {
		return this.ownerName;
	}

	public String getIndividualID() {
		return super.getIndividualID();
	}

	public UUID getOwnerUUID() {
		return this.ownerUUID;
	}
	/*
	*   Converts Item to Config
	 */
	public void toConfig(String key) {
		getAttrFromItem();
		ItemConfig.config.set("collect."+key+".timeExpire", this.timeExpire);
		ItemConfig.config.set("collect."+key+".item", this.is);
		ItemConfig.config.set("collect."+key+".attributes", this.attributes);
		ItemConfig.config.set("collect."+key+".indivID", this.individualID);
		ItemConfig.config.set("collect."+key+".ownerName", this.ownerName);
		ItemConfig.config.set("collect."+key+".ownerUUID", this.ownerUUID.toString());
		ItemConfig.saveConfig();
	}
}
