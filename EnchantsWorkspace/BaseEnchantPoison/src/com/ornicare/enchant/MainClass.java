package com.ornicare.enchant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IDirectHitEnchantment;


public class MainClass extends BaseEnchantment implements IDirectHitEnchantment {

	public MainClass(String name, short maxLevel, int weight, boolean isLegit) {
		super(name, maxLevel, weight, isLegit);
		
		addAuthorizedItems(bow);
		addAuthorizedItems(swords);
	}

	@Override
	public void onEntityHit(LivingEntity entityInflicter,LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage) {
		entityVictim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,level*100,level/3));
	}


}
