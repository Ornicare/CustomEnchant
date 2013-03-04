package fr.enchantments.custom.implementation;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IDirectEnchantment;

public class DirectStarvation extends CommonEnchantment implements IDirectEnchantment
{

    public DirectStarvation(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

	@Override
	public void onEntityHit(LivingEntity entityInflicter,LivingEntity entityVictim, short level, int damage) {
		entityVictim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,level*100,level/3));
	}
}
