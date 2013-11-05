package fr.enchantments.custom.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IEnchantment;


/**
 * Wrapper for a  Map<weigh,IEnchantment>
 * Probability to get choose is proportionnal to the weight
 * 
 * @author Ornicare
 *
 */
public class RandomizerMap{

	private int totalWeigh = 0;
	private Map<Integer[],IEnchantment> map = new HashMap<Integer[],IEnchantment>();
	
	public int getTotalWeigh() {
		return totalWeigh;
	}

	public void push(Integer key, IEnchantment enchantmentToRegister) {
		Integer[] definitionValue = new Integer[2];
		definitionValue[0] = new Integer(this.totalWeigh);
		this.totalWeigh += key.intValue();
		definitionValue[1] = new Integer(this.totalWeigh);
		
		map.put(definitionValue, enchantmentToRegister);
	}
	
	public void push(int key, BaseEnchantment value) {
		Integer[] definitionValue = new Integer[2];
		definitionValue[0] = new Integer(this.totalWeigh);
		this.totalWeigh += key;
		definitionValue[1] = new Integer(this.totalWeigh);
		
		map.put(definitionValue, value);
	}
	
	public void remove(Integer[] key) {
		map.remove(key);
		this.totalWeigh -= key[0].intValue();
	}
	
	/**
	 * Choose a random MobModel according to his weigh
	 * 
	 * @return MobModel
	 */
	public IEnchantment getRandomEnchantment() {
		int mobInt = MathHelper.randomize((short)totalWeigh);
		for(Integer[] definitionDomain : map.keySet()) {
			if(mobInt>definitionDomain[0] && mobInt<=definitionDomain[1]) return map.get(definitionDomain);
		}
		return null;
	}
	
	public List<IEnchantment> keySet() {
		List<IEnchantment> keyset = new ArrayList<IEnchantment>();
		for(Integer[] i : map.keySet()) {
			keyset.add((IEnchantment)map.get(i));
		}
		return keyset;
	}

}
