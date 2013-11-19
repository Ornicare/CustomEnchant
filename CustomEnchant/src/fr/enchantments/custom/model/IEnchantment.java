package fr.enchantments.custom.model;
import java.util.List;


public interface IEnchantment {

	public List<Integer> getAuthorizedItems();

	public short getId();

	public String getName();

	public short getLevel(int cost);
	
	public String getIncompatiblePlugins();
	
	public boolean isLegit();
	
	public int getWeight();

	public short getMaxLevel();

	public void setId(short currentId);
	
	public void setIncompatibilityList(String pluginName, String iList);
	
	public boolean isCompatibleWith(String pluginName);

	public String getPluginName();
	
}
