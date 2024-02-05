package pandemist.adrundaal.auctions.model;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.config.LogConfig;
import pandemist.adrundaal.auctions.utils.TimeUtils;
import pandemist.adrundaal.auctions.utils.Utils;

public class BidItem extends AuctionItem {
	private int offer=0;
	private String sellerName="";
	private UUID sellerUUID=null;
	private String topBidderName="";
	private UUID topBidderUUID=null;
	/*
	*   Constructor
	 */
	public BidItem(long ttime, int toffer, ItemStack tis, String indivID, String tsellerName, UUID tsellerUUID) {
		super.timeExpire=ttime;
		super.is=tis;
		getAttrFromItem();
		super.individualID=indivID;
		this.offer=toffer;
		this.sellerName=tsellerName;
		this.sellerUUID=tsellerUUID;
		this.topBidderName="";
		this.topBidderUUID=null;
	}

	public BidItem(String key) {
		super.timeExpire=ItemConfig.config.getLong("bid."+key+".timeExpire");
		super.is=ItemConfig.config.getItemStack("bid."+key+".item");
	//	System.out.println("Biditem load"+this.getItem().getType());
		super.attributes=ItemConfig.notNull(ItemConfig.config.getString("bid."+key+".attributes"));
		makeItem();
		super.individualID=ItemConfig.notNull(ItemConfig.config.getString("bid."+key+".indivID"));
		this.offer=ItemConfig.config.getInt("bid."+key+".offer");
		this.sellerName=ItemConfig.notNull(ItemConfig.config.getString("bid."+key+".sellerName"));
	//	System.out.println(ItemConfig.notNull(ItemConfig.config.getString("bid."+key+".sellerName")));
		this.sellerUUID=Utils.stringToUUID(ItemConfig.config.getString("bid."+key+".sellerUUID"));
	//	System.out.println(ItemConfig.notNull(ItemConfig.config.getString("bid."+key+".sellerUUID")));
		this.topBidderName=ItemConfig.notNull(ItemConfig.config.getString("bid."+key+".topBidderName"));
		this.topBidderUUID=Utils.stringToUUID(ItemConfig.config.getString("bid."+key+".topBidderUUID"));
	//	this.topBidderUUID=UUID.fromString(ItemConfig.notNull(ItemConfig.config.getString("sell."+key+".topBidderUUID")));
	}

	public BidItem() {

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

	@Override
	public String getIndividualID() {
		return super.getIndividualID();
	}

	public int getOffer() {
		return this.offer;
	}

	public void setOffer(int offer) {
		this.offer=offer;
	}

	public String getSellerName() {
		return this.sellerName;
	}

	public UUID getSellerUUID() {
		return this.sellerUUID;
	}

	public String getTopBidderName() {
		return this.topBidderName;
	}

	public void setTopBidderName(String name) {
		this.topBidderName=name;
	}

	public UUID getTopBidderUUID() {
		return this.topBidderUUID;
	}

	public void setTopBidderUUID(UUID uuid) {
		this.topBidderUUID=uuid;
	}
	/*
	*   Converts the Item to the Config File
	 */
	public void toConfig(String key) {
		getAttrFromItem();
		ItemConfig.config.set("bid."+key+".timeExpire", this.timeExpire);
		ItemConfig.config.set("bid."+key+".item", this.is);
	//	System.out.println("Biditem load"+this.getItem().getType());
		ItemConfig.config.set("bid."+key+".attributes", this.attributes);
		ItemConfig.config.set("bid."+key+".offer", this.offer);
		ItemConfig.config.set("bid."+key+".indivID", this.individualID);
		ItemConfig.config.set("bid."+key+".sellerName", this.sellerName);
		ItemConfig.config.set("bid."+key+".sellerUUID", this.sellerUUID.toString());
		ItemConfig.config.set("bid."+key+".topBidderName", this.topBidderName);
		ItemConfig.config.set("bid."+key+".topBidderUUID", Utils.UUIDToString(this.topBidderUUID));
		ItemConfig.saveConfig();
	}
	/*
	*   Converts a Item to a Collect Item
	 */
	public CollectableItem toCollectble() {
	//	System.out.println(Config.getOptionValue("config.full-expire-time"));
		long ttime=TimeUtils.convertToMill(Config.getOptionValue("config.full-expire-time"));
		String tname=this.getSellerName();
		UUID tuuid=this.getSellerUUID();
		if(!this.getTopBidderName().equals("")) {
			tname=this.getTopBidderName();
			tuuid=this.getTopBidderUUID();
		}
		CollectableItem c=new CollectableItem(ttime, this.getItem(), this.getIndividualID(), tname, tuuid);
		return c;
	}
}
