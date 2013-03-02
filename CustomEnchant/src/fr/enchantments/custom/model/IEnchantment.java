package fr.enchantments.custom.model;
import java.util.List;

import org.bukkit.Material;


public interface IEnchantment {

	public List<Material> getAuthorizedItems();

	public short getId();

	public String getName();

	public short getLevel(int cost);
	
	public List<IEnchantment> getIncompatibleCombination();
	
}
