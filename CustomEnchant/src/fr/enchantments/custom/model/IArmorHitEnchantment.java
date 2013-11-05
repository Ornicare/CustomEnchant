package fr.enchantments.custom.model;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IArmorHitEnchantment extends IEnchantment
{

    public void onArmorHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short totalEnchantmentsLevels, short damage);

}