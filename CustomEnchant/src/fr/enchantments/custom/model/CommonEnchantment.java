package fr.enchantments.custom.model;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;


public abstract class CommonEnchantment implements IEnchantment{
	
	private List<Material> authorizedItems = new ArrayList<Material>();
	private int maxLevel;

	public List<Material> getAuthorizedItems() {
		return authorizedItems;
	}

	public void addAuthorizedItems(Material... authorizedItems) {
		for(Material m : authorizedItems) this.authorizedItems.add(m);
	}

	
	public int getLevel() {
		return maxLevel;
	}

}
