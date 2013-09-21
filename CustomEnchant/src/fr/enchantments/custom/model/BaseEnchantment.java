package fr.enchantments.custom.model;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_6_R2.Item;

import org.bukkit.Material;

import fr.enchantments.custom.helper.MathHelper;


/**
 * Class for common enchantments methods.
 * 
 * @author Ornicare
 *
 */
public abstract class BaseEnchantment extends EnchantablesItems  implements IEnchantment{
	
	/**
	 * List of materials who can accept this enchant
	 */
	private List<Integer> authorizedItems = new ArrayList<Integer>();
	
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
	
	/**
	 * Enchantment weight
	 */
	private int weight;
	
	/**
	 * naturally obtainable enchant ?
	 */
	private boolean isLegit;
	
	public List<Integer> getAuthorizedItems() {
		return authorizedItems;
	}
	
	/**
	 * Add new material to materials who can accept this enchant
	 * 
	 * @param id2 material to add
	 */
	public void addAuthorizedItems(int... id2) {
		for(int m : id2) this.authorizedItems.add(m);
	}
	
	public short getMaxLevel() {
		return maxLevel;
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
	
	public int getWeight() {
		return weight;
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
		this.weight = 10;
		this.isLegit = false;
	}
	
	public BaseEnchantment(String name, short id, short maxLevel, int weight, boolean isLegit) {
		this.name = name;
		this.id = id;
		this.maxLevel = maxLevel;
		this.weight = weight;
		this.isLegit = isLegit;
	}

	public boolean isLegit() {
		return isLegit;
	}


}
