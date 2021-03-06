package fr.enchantments.custom.implementation.legal;

import java.util.Map;
import java.util.Set;

import net.minecraft.server.v1_6_R3.Item;
import net.minecraft.server.v1_6_R3.Material;
import net.minecraft.server.v1_6_R3.MathHelper;
import net.minecraft.server.v1_6_R3.NBTTagCompound;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import fr.enchantments.custom.storage.Storage;

/**
 * Explosive enchant for bow.
 * 
 * @author Ornicare
 *
 */
//public class LegalProjectile_Explosion extends LegalDirectHit_Explosion implements IZoneEffectEnchantment{
public class LegalProjectile_Explosion extends BaseEnchantment implements IZoneEffectEnchantment{
	
    /**
     *
     * @param enchantmentName The name of the enchantment
     * @param enchantmentID The ID of the enchantment
     * @param maxLevel The maximum level of the enchantment
     */
	public LegalProjectile_Explosion(String enchantmentName, int enchantmentID, int maxLevel, int weight, boolean isLegit) {
		super(enchantmentName, (short) enchantmentID, (short) maxLevel, weight, isLegit);
		
		//Add authorized items
		addAuthorizedItems(bow);
	}

    /**
     * DO AWESOME EXPLOSION !
     *
     * @param projectileShooter : The projectile that was shot
     * @param projectileEntity : The projectile entity ! Hell Yeah !
     */
	@Override
	public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level) {
		
		if(!(projectileEntity instanceof Projectile)) return;
		
		Projectile projectile = (Projectile) projectileEntity;
		
		//determine arrow damage
		double damage = 2.0D;
        double velocityDamage = projectileEntity.getVelocity().length();
        int damageint = (int) ((double)velocityDamage * damage);
        boolean fireTick = false;
        Map<Enchantment, Integer> enchantments = projectileShooter.getEnchantments();
		if(projectileShooter.getEnchantments()!=null) {
			for(Enchantment ench : enchantments.keySet()) {
				if(ench.getId()==48) {
					damageint=(int) (damageint*1.5+0.25*(enchantments.get(ench)-1));
				}
				if(ench.getId()==(short) 50) {
					fireTick = true;
				}
			}
		}
		
		ExplosionHelper.doFakeExplosion(projectile.getLocation(), level);
		
		LivingEntity shooter = projectile.getShooter();
		
		for(Entity ent : projectileEntity.getNearbyEntities(level,level,level)) {
			if(ent instanceof LivingEntity) {
				LivingEntity entLV = (LivingEntity) ent;
				//Following damages to entLV by entityInflicter do not check enchantment
				Storage.IGNOREEVENTS.add(entLV.getUniqueId().toString()+shooter.getUniqueId().toString());
				if(fireTick) entLV.setFireTicks(5);
				int distanceModifier = (int) (entLV.getLocation().distance(projectile.getLocation())/5);
				entLV.damage(distanceModifier==0?damageint:damageint/distanceModifier,shooter);
			}
		}
		
		
		projectileEntity.remove();
	}

}
