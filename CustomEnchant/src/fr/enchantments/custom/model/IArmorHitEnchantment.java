package fr.enchantments.custom.model;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public interface IArmorHitEnchantment
{

    public void onArmorHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack armorHit, ItemStack weaponUsed, short level, int damage);

}
