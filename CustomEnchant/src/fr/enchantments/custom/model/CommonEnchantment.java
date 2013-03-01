package fr.enchantments.custom.model;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import fr.enchantments.custom.helper.MathHelper;


/**
 * Class for common enchantments methods.
 * 
 * @author Ornicare
 *
 */
public abstract class CommonEnchantment implements IEnchantment{
	
	/**
	 * List of materials who can accept this enchant
	 */
	private List<Material> authorizedItems = new ArrayList<Material>();
	
	/**
	 * Max level of this enchant
	 */
	private int maxLevel;
	
	public List<Material> getAuthorizedItems() {
		return authorizedItems;
	}
	
	/**
	 * Add new material to materials who can accept this enchant
	 * 
	 * @param authorizedItems material to add
	 */
	public void addAuthorizedItems(Material... authorizedItems) {
		for(Material m : authorizedItems) this.authorizedItems.add(m);
	}

	/**
	 * Get a level to this enchantement
	 * 
	 * @return An integer between 1 and <code>maxLevel</code>
	 */
	public int getLevel() {
		return MathHelper.randomize(maxLevel);
	}

}
