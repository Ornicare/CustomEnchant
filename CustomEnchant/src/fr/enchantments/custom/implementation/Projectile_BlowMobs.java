package fr.enchantments.custom.implementation;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class Projectile_BlowMobs extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_BlowMobs(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
    	 Block blockHit = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
         if ( blockHit==null ) { return; }
         projectileEntity.remove();


         for(Entity ent : projectileEntity.getNearbyEntities(level, level, level)) {
        	 /*if(ent instanceof LivingEntity)*/ ent.setVelocity(ent.getVelocity().add(ent.getLocation().clone().subtract(blockHit.getLocation().clone()).toVector().normalize().multiply(1+level-ent.getLocation().distance(blockHit.getLocation()))));
         }
         ExplosionHelper.doFakeExplosion(blockHit.getLocation(), 200);
    }
}
