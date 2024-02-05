package pandemist.adrundaal.auctions.model;

import org.bukkit.Material;

import java.util.ArrayList;

public enum Filter {
	NONE("None", new ArrayList<Material>()), ARMOR("Armor", getArmor()), FOOD("Food", getFood()), WEAPONS("Weapons", getWeapons()), POTIONS("Potions", getPotions()), OTHER("Other", getOthers()), TOOLS("Tools", getTools()), BLOCKS("Blocks", getBlocks());

	String name;
	ArrayList<Material> items;
	/*
	*   Constructor
	 */
	private Filter(String pname, ArrayList<Material> pitems) {
		this.name=pname;
		this.items=pitems;
	}
	/*
	*   Get Name
	 */
	public String getName() {
		return this.name;
	}
	/*
	*   Gets Itemlist
	 */
	public ArrayList<Material> getItem() {
		return items;
	}
	/*
	*   ItemLists
	 */
	private static ArrayList<Material> getArmor() {
		ArrayList<Material> ma=new ArrayList<Material>();
		ma.add(Material.DIAMOND_HELMET);
		ma.add(Material.DIAMOND_CHESTPLATE);
		ma.add(Material.DIAMOND_LEGGINGS);
		ma.add(Material.DIAMOND_BOOTS);
		ma.add(Material.CHAINMAIL_HELMET);
		ma.add(Material.CHAINMAIL_CHESTPLATE);
		ma.add(Material.CHAINMAIL_LEGGINGS);
		ma.add(Material.CHAINMAIL_BOOTS);
		ma.add(Material.GOLD_HELMET);
		ma.add(Material.GOLD_CHESTPLATE);
		ma.add(Material.GOLD_LEGGINGS);
		ma.add(Material.GOLD_BOOTS);
		ma.add(Material.IRON_HELMET);
		ma.add(Material.IRON_CHESTPLATE);
		ma.add(Material.IRON_LEGGINGS);
		ma.add(Material.IRON_BOOTS);
		ma.add(Material.LEATHER_HELMET);
		ma.add(Material.LEATHER_CHESTPLATE);
		ma.add(Material.LEATHER_LEGGINGS);
		ma.add(Material.LEATHER_BOOTS);
		return ma;
	}

	private static ArrayList<Material> getTools() {
		ArrayList<Material> ma=new ArrayList<Material>();
		ma.add(Material.WOOD_PICKAXE);
		ma.add(Material.STONE_PICKAXE);
		ma.add(Material.IRON_PICKAXE);
		ma.add(Material.GOLD_PICKAXE);
		ma.add(Material.DIAMOND_PICKAXE);
		ma.add(Material.WOOD_AXE);
		ma.add(Material.STONE_AXE);
		ma.add(Material.IRON_AXE);
		ma.add(Material.GOLD_AXE);
		ma.add(Material.DIAMOND_AXE);
		ma.add(Material.WOOD_SPADE);
		ma.add(Material.STONE_SPADE);
		ma.add(Material.IRON_SPADE);
		ma.add(Material.GOLD_SPADE);
		ma.add(Material.DIAMOND_SPADE);
		ma.add(Material.WOOD_HOE);
		ma.add(Material.STONE_HOE);
		ma.add(Material.IRON_HOE);
		ma.add(Material.GOLD_HOE);
		ma.add(Material.DIAMOND_HOE);
		ma.add(Material.FISHING_ROD);
		ma.add(Material.FLINT_AND_STEEL);
		return ma;
	}

	private static ArrayList<Material> getWeapons() {
		ArrayList<Material> ma=new ArrayList<Material>();
		ma.add(Material.WOOD_SWORD);
		ma.add(Material.STONE_SWORD);
		ma.add(Material.IRON_SWORD);
		ma.add(Material.DIAMOND_SWORD);
		ma.add(Material.WOOD_AXE);
		ma.add(Material.STONE_AXE);
		ma.add(Material.IRON_AXE);
		ma.add(Material.DIAMOND_AXE);
		ma.add(Material.SHIELD);
		ma.add(Material.BOW);
		ma.add(Material.ARROW);
		ma.add(Material.TIPPED_ARROW);
		ma.add(Material.SPECTRAL_ARROW);
		return ma;
	}

	private static ArrayList<Material> getFood() {
		ArrayList<Material> ma=new ArrayList<Material>();
		for(Material m : Material.values()) {
			if(m.isEdible()) {
				if(m!=Material.POTION) ma.add(m);
			}
		}
		return ma;
	}

	private static ArrayList<Material> getPotions() {
		ArrayList<Material> ma=new ArrayList<Material>();
		ma.add(Material.POTION);
		ma.add(Material.SPLASH_POTION);
		ma.add(Material.LINGERING_POTION);
		return ma;
	}

	private static ArrayList<Material> getBlocks() {
		ArrayList<Material> ma=new ArrayList<Material>();
		for(Material m : Material.values()) {
			if(m.isBlock()) {
				ma.add(m);
			}
		}
		return ma;
	}

	private static ArrayList<Material> getOthers() {
		ArrayList<Material> ma=new ArrayList<Material>();
		for(Material m : Material.values()) {
			if(!(getArmor().contains(m)||getTools().contains(m)||getWeapons().contains(m)||getFood().contains(m)||getPotions().contains(m)||getBlocks().contains(m))) {
				ma.add(m);
			}
		}
		return ma;
	}
}