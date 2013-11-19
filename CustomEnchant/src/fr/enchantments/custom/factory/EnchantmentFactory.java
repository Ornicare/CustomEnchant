package fr.enchantments.custom.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.helper.MathHelper;
import fr.enchantments.custom.helper.RandomizerMap;
import fr.enchantments.custom.model.IEnchantment;

public class EnchantmentFactory {

	private ListenerRegistrationFactory listenerFactory;
	private Map<ArrayList<Material>,Integer> enchantabliltyWeapon = new HashMap<ArrayList<Material>,Integer>();
	private Map<ArrayList<Material>,Integer> enchantabliltyArmor = new HashMap<ArrayList<Material>,Integer>();

	public EnchantmentFactory(ListenerRegistrationFactory listenerRegistrationFactory) {
		this.listenerFactory = listenerRegistrationFactory;
		enchantabliltyWeapon.put(new ArrayList<Material>(Arrays.asList(Material.WOOD_AXE,Material.WOOD_SPADE,Material.WOOD_PICKAXE,Material.WOOD_SWORD)), 15);
		enchantabliltyWeapon.put(new ArrayList<Material>(Arrays.asList(Material.STONE_AXE,Material.STONE_SPADE,Material.STONE_PICKAXE,Material.STONE_SWORD)), 5);
		enchantabliltyWeapon.put(new ArrayList<Material>(Arrays.asList(Material.IRON_AXE,Material.IRON_SPADE,Material.IRON_PICKAXE,Material.IRON_SWORD)), 14);
		enchantabliltyWeapon.put(new ArrayList<Material>(Arrays.asList(Material.DIAMOND_AXE,Material.DIAMOND_SPADE,Material.DIAMOND_PICKAXE,Material.DIAMOND_SWORD)), 10);
		enchantabliltyWeapon.put(new ArrayList<Material>(Arrays.asList(Material.GOLD_AXE,Material.GOLD_SPADE,Material.GOLD_PICKAXE,Material.GOLD_SWORD)), 22);
		
		enchantabliltyArmor.put(new ArrayList<Material>(Arrays.asList(Material.LEATHER_BOOTS,Material.LEATHER_CHESTPLATE,Material.LEATHER_HELMET,Material.LEATHER_LEGGINGS)), 15);
		enchantabliltyArmor.put(new ArrayList<Material>(Arrays.asList(Material.IRON_BOOTS,Material.IRON_CHESTPLATE,Material.IRON_HELMET,Material.IRON_LEGGINGS)), 9);
		enchantabliltyArmor.put(new ArrayList<Material>(Arrays.asList(Material.CHAINMAIL_BOOTS,Material.CHAINMAIL_CHESTPLATE,Material.CHAINMAIL_HELMET,Material.CHAINMAIL_LEGGINGS)), 12);
		enchantabliltyArmor.put(new ArrayList<Material>(Arrays.asList(Material.DIAMOND_BOOTS,Material.DIAMOND_CHESTPLATE,Material.DIAMOND_HELMET,Material.DIAMOND_LEGGINGS)), 10);
		enchantabliltyArmor.put(new ArrayList<Material>(Arrays.asList(Material.GOLD_BOOTS,Material.GOLD_CHESTPLATE,Material.GOLD_HELMET,Material.GOLD_LEGGINGS)), 25);
	}
	
	/**
	 * Try to tell if the item is an armor
	 * @param item
	 * @return
	 */
	public boolean isArmor(ItemStack item) {
		Material itemMat = item.getData().getItemType();
		boolean found = false;
		for(ArrayList<Material> mat : enchantabliltyArmor.keySet()) {
			if(mat.contains(itemMat)) found = true;
		}
		return found;
	}
	
	/**
	 * Try to tell if the item is a weapon
	 * @param item
	 * @return
	 */
	public boolean isWeapon(ItemStack item) {
		Material itemMat = item.getData().getItemType();
		boolean found = false;
		for(ArrayList<Material> mat : enchantabliltyWeapon.keySet()) {
			if(mat.contains(itemMat)) found = true;
		}
		return found;
	}

	public void addNewEnchantments(ItemStack item,Map<Enchantment, Integer> enchantsToAdd, int expLevelCost) {
		// TODO Auto-generated method stub
		//TODO enlever une partie des enchants existants
		//TODO tester si l'objet est compatible
		//TODO d�terminer nb d'enchant � ajouter.

//		int nbEnchant = MathHelper.randomize((short) (expLevelCost/5));
//		nbEnchant = nbEnchant > 3 ? 3 : nbEnchant;
		
		//minecraft enchantment steps.
		int enchantability = getEnchantability(item);
		int modifiedEnchantmentLevel = expLevelCost + MathHelper.randomize((short) (enchantability / 4 * 2));
		int enchantability_2 = enchantability / 2;

		int rand_enchantability = 1 + MathHelper.randomize((short) (enchantability_2 / 2) ) + MathHelper.randomize((short) (enchantability_2 / 2));
		int k = expLevelCost + rand_enchantability;
		float rand_bonus_percent = (float) ((Math.random() + Math.random() - 1) * 0.15);
		int final_level = (int)(k * (1 + rand_bonus_percent) + 0.5);
		
		//Already add enchantments
		List<IEnchantment> addEnchantments = new ArrayList<IEnchantment>();
		
		do {
			RandomizerMap possibleEnch = getPossiblesEnchantemnts(item,addEnchantments);
			IEnchantment enchToAdd = possibleEnch.getRandomEnchantment();
			addEnchantments.add(enchToAdd);

			//TODO en test
			double level = (enchToAdd.getMaxLevel()+1)*Math.pow(Math.E, -Math.pow(Math.random(), 2)/(2*Math.pow(expLevelCost/(100*enchantability/25),1/2)));
			level = level < 1 ? 1 : level;
			level = level > enchToAdd.getMaxLevel() ? enchToAdd.getMaxLevel() : level;
			
//			if(enchToAdd !=null) EnchantmentHelper.addCustomEnchantWithLevel(item, enchToAdd, (short) (final_level > enchToAdd.getMaxLevel() ? enchToAdd.getMaxLevel() : final_level));
			if(enchToAdd !=null) EnchantmentHelper.addCustomEnchantWithLevel(item, enchToAdd, (short) level);
			
			modifiedEnchantmentLevel/=2;
		} while(Math.random()<((modifiedEnchantmentLevel+1/50)));
		/*int legalEnchantmentsSize = enchantsToAdd.keySet().size();
		if((legalEnchantmentsSize + nbEnchant)>5) nbEnchant = 5-legalEnchantmentsSize;
		
		List<IEnchantment> addEnchantments = new ArrayList<IEnchantment>();
		
		for(int i = 0 ;i<nbEnchant;i++) {
			IEnchantment enchantment = listenerFactory.getEnchantmentMap().getRandomEnchantment();
			// level r�aliste
			short level = enchantment.getLevel(expLevelCost);
			// si enchant incompatible, ne pas juste skip ?
			//If an enchant is already present, skip it.
			// : � voir, en cor�lation avec le choix du nb d'enchant � ajouter.
			if(enchantment !=null && !addEnchantments.contains(enchantment)) {
				if(enchantment.getAuthorizedItems().contains(item.getTypeId())) EnchantmentHelper.addCustomEnchantWithLevel(item, enchantment, level);
			}
		}*/
		
		//Trop compliqu�
		/*//Eventually delete some existing enchantment ? Max enchant = 4.
		int nbToDelete = 4-(enchantsToAdd.keySet().size() + nbEnchant);
		for(int i = 0;i<nbToDelete;i++) {
			int randomId = MathHelper.randomize((short) enchantsToAdd.keySet().size());
			
		}*/
	}
	
	@SuppressWarnings("deprecation")
	public RandomizerMap getPossiblesEnchantemnts(ItemStack item, List<IEnchantment> addEnchantments) {
		//Create a list of all possible enchantments
		RandomizerMap possibleEnchantments = new RandomizerMap();
		for(IEnchantment ench : listenerFactory.getEnchantmentList()) {
			if(ench.isLegit() && ench.getAuthorizedItems().contains(item.getTypeId()) && !addEnchantments.contains(ench)) {
				possibleEnchantments.push(ench.getWeight(), (IEnchantment) ench);
			}
		}
		return possibleEnchantments;
	}


	/**
	 * Return item enchantability
	 * @param item
	 * @return
	 */
	private int getEnchantability(ItemStack item) {
		Material itemMat = item.getData().getItemType();
		for(ArrayList<Material> mat : enchantabliltyWeapon.keySet()) {
			if(mat.contains(itemMat))  return enchantabliltyWeapon.get(mat);
		}
		
		for(ArrayList<Material> mat : enchantabliltyArmor.keySet()) {
			if(mat.contains(itemMat))  return enchantabliltyArmor.get(mat);
		}
		return isArmor(item)? 9 : 14;
	}

}
