package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.Effect;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class Projectile_Starvation extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_Starvation(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        for(Entity ent : projectileEntity.getNearbyEntities(level, level, level)) {
        	if(ent instanceof LivingEntity) {
        		((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,level*100,level/3));
        	}
        }
        projectileEntity.getWorld().playEffect(projectileEntity.getLocation(), Effect.POTION_BREAK, 4, 100); 
        projectileEntity.remove();
    }
}
