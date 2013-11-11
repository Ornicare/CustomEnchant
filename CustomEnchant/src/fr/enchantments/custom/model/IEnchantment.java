package fr.enchantments.custom.model;
import java.util.List;


public interface IEnchantment {

	public List<Integer> getAuthorizedItems();

	public short getId();

	public String getName();

	public short getLevel(int cost);
	
	public List<IEnchantment> getIncompatibleCombination();
	
	public boolean isLegit();
	
	public int getWeight();

	public short getMaxLevel();

	public void setId(short currentId);
	
}
