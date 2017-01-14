package pandemist.adrundaal.auctions.model;

import org.bukkit.entity.Player;

public class Page {
	private Player player;
	private int actualPage;
	private int maxPage;
	/*
	*   Constructor
	 */
	public Page() {
	}

	public Page(Player player, int actualPage, int maxPage) {
		this.player=player;
		this.actualPage=actualPage;
		this.maxPage=maxPage;
	}
	/*
	*   Getter and Setter Methodes
	 */
	public Player getPlayer() {
		return this.player;
	}

	public int getActualPage() {
		return this.actualPage;
	}

	public int getMaxPage() {
		return this.maxPage;
	}

	public void updatePage(int page) {
		this.actualPage=page;
	}
}
