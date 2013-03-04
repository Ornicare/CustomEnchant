package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class Projectile_Compressor extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_Compressor(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        for(Entity ent : projectileEntity.getNearbyEntities(level, level, level)) {
        	if(ent instanceof LivingEntity) {
        		ent.teleport(projectileEntity);
        	}
        }
        
        projectileEntity.remove();
    }
}
