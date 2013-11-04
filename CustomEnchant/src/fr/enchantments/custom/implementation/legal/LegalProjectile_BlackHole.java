package fr.enchantments.custom.implementation.legal;

import java.util.Map;
import java.util.Set;

import net.minecraft.server.v1_6_R2.Item;
import net.minecraft.server.v1_6_R2.Material;
import net.minecraft.server.v1_6_R2.MathHelper;
import net.minecraft.server.v1_6_R2.NBTTagCompound;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import fr.enchantments.custom.runnables.RunnableBlackHole;
import fr.enchantments.custom.runnables.RunnableRebond;
import fr.enchantments.custom.storage.Storage;

/**
 * Explosive enchant for bow.
 * 
 * @author Ornicare
 *
 */
//public class LegalProjectile_Explosion extends LegalDirectHit_Explosion implements IZoneEffectEnchantment{
public class LegalProjectile_BlackHole extends BaseEnchantment implements IZoneEffectEnchantment{
	
    /**
     *
     * @param enchantmentName The name of the enchantment
     * @param enchantmentID The ID of the enchantment
     * @param maxLevel The maximum level of the enchantment
     */
	public LegalProjectile_BlackHole(String enchantmentName, int enchantmentID, int maxLevel, int weight, boolean isLegit) {
		super(enchantmentName, (short) enchantmentID, (short) maxLevel, weight, isLegit);
		
		//Add authorized items
		addAuthorizedItems(bow);
	}

    /**
     * Do some damage and make a tiny black hole !
     *
     * @param projectileShooter : The projectile that was shot
     * @param projectileEntity : The projectile entity ! Hell Yeah !
     */
	@Override
	public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level) {
		
		if(!(projectileEntity instanceof Projectile)) return;
		
		Projectile projectile = (Projectile) projectileEntity;
		
		//determine arrow damage
		double damage = 2.0D/level*2; //damage over time
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
		

		LivingEntity shooter = projectile.getShooter();
		new RunnableBlackHole(projectileEntity,level).runTaskTimer(PluginLoader.pluginLoader, 0, 10);
	}

}
