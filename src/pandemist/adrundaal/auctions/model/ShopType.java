package pandemist.adrundaal.auctions.model;

public enum ShopType {
	SELL("Sell"), BID("Bid"), COLLECT("collect"), MY("actualInserted"), FILTER("filter");

	String name;
	/*
	*   Gets ShopType by Name
	 */
	private ShopType(String pname) {
		this.name=pname;
	}
	/*
	*   Gets Shop Name
	 */
	public String getName() {
		return name;
	}
}