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
public abstract class BaseEnchantment implements IEnchantment{
	
	/**
	 * List of materials who can accept this enchant
	 */
	private List<Material> authorizedItems = new ArrayList<Material>();
	
	/**
	 * The list of enchantments which cannot be found with this
	 */
	private List<IEnchantment> incompatibleCombination;
	
	/**
	 * Enchantment id
	 */
	private short id;
	
	/**
	 * Enchantment's name
	 */
	private String name;
	
	/**
	 * Max level of this enchant
	 */
	private short maxLevel;
	
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
	 * Get a level to this enchantment
	 * 
	 * @return An integer between 1 and <code>maxLevel</code>
	 */
	public short getLevel(int cost) {
		//TODO return a level using the cost
		return MathHelper.randomize(maxLevel);
	}

	public short getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public List<IEnchantment> getIncompatibleCombination() {
		return incompatibleCombination;
	}
	
	public BaseEnchantment(String name, short id, short maxLevel) {
		this.name = name;
		this.id = id;
		this.maxLevel = maxLevel;
	}


}
