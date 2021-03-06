package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.enchantments.custom.model.IDirectHitEnchantment;

public class DirectHit_Starvation extends BaseEnchantment implements IDirectHitEnchantment
{

    public DirectHit_Starvation(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

	@Override
	public void onEntityHit(LivingEntity entityInflicter,LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage) {
		entityVictim.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,level*100,level/3));
	}
}
