package pandemist.adrundaal.auctions.model;

import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.utils.ItemUtils;

public class AuctionItem extends ItemStack {
	protected long timeExpire=0;
	protected ItemStack is=null;
	protected String attributes="";
	protected String individualID="";
	/*
	*   Constructor
	 */
	public AuctionItem(long ttime, ItemStack tis, String indivID) {
		this.timeExpire=ttime;
		this.is=tis;
		this.individualID=indivID;
		getAttrFromItem();
	}

	public AuctionItem() {

	}

	/*
	*   Getter and Setter Methodes
	 */
	public long getTimeExpire() {
		return this.timeExpire;
	}

	public void setTimeExpire(long ttime) {
		this.timeExpire=ttime;
	}

	public ItemStack getItem() {
		return this.is;
	}

	public String getAttributes() {
		return this.attributes;
	}

	public void setAttributes(String tattr) {
		this.attributes=tattr;
		makeItem();
	}

	public String getIndividualID() {
		return this.individualID;
	}
	/*
	*   Attribute Methodes
	 */
	public void makeItem() {
		ItemUtils.loadItemAttributesFromString(this.is, this.attributes);
	}

	public void getAttrFromItem() {
		this.attributes=ItemUtils.getItemAttributes(this.is);
	}
}
