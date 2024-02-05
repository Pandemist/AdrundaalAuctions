package pandemist.adrundaal.auctions.model;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import pandemist.adrundaal.auctions.config.Config;
import pandemist.adrundaal.auctions.config.ItemConfig;
import pandemist.adrundaal.auctions.utils.TimeUtils;

public class SellItem extends AuctionItem {
	private int price=0;
	private String sellerName="";
	private UUID sellerUUID=null;
	/*
	*   Constructor
	 */
	public SellItem(long ttime, int tprice, ItemStack tis, String indivID, String tsellerName, UUID tsellerUUID) {
		super.timeExpire=ttime;
		super.is=tis;
		getAttrFromItem();
		super.individualID=indivID;
		this.price=tprice;
		this.sellerName=tsellerName;
		this.sellerUUID=tsellerUUID;
	}

	public SellItem(String key) {
		super.timeExpire=ItemConfig.config.getLong("sell."+key+".timeExpire");
		super.is=ItemConfig.config.getItemStack("sell."+key+".item");
		super.attributes=ItemConfig.notNull(ItemConfig.config.getString("sell."+key+".attributes"));
		super.individualID=ItemConfig.notNull(ItemConfig.config.getString("sell."+key+".indivID"));
		makeItem();
		this.price=ItemConfig.config.getInt("sell."+key+".price");
		this.sellerName=ItemConfig.notNull(ItemConfig.config.getString("sell."+key+".sellerName"));
		this.sellerUUID=UUID.fromString(ItemConfig.config.getString("sell."+key+".sellerUUID"));
	}

	public SellItem() {

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

	public int getPrice() {
		return this.price;
	}

	public void setPrice(int price) {
		this.price=price;
	}

	public String getSellerName() {
		return this.sellerName;
	}

	public void setSellerName(String pSellerName) {
		this.sellerName = pSellerName;
	}

	public UUID getSellerUUID() {
		return this.sellerUUID;
	}

	public void setSellerUUID(UUID puuid) {
		this.sellerUUID = puuid;
	}
	/*
	*   Converts Item to Config
	 */
	public void toConfig(String key) {
		getAttrFromItem();
		ItemConfig.config.set("sell."+key+".timeExpire", this.timeExpire);
		ItemConfig.config.set("sell."+key+".item", this.is);
		ItemConfig.config.set("sell."+key+".attributes", this.attributes);
		ItemConfig.config.set("sell."+key+".indivID", this.individualID);
		ItemConfig.config.set("sell."+key+".price", this.price);
		ItemConfig.config.set("sell."+key+".sellerName", this.sellerName);
		ItemConfig.config.set("sell."+key+".sellerUUID", this.sellerUUID.toString());
		ItemConfig.saveConfig();
	}
	/*
	*   Converts item to Collect Item
	 */
	public CollectableItem toCollectble() {
		long ttime=TimeUtils.convertToMill(Config.getOptionValue("config.full-expire-time"));
		String tname=this.getSellerName();
		UUID tuuid=this.getSellerUUID();
		CollectableItem c=new CollectableItem(ttime, this.getItem(), this.getIndividualID(), tname, tuuid);
		return c;
	}
}
