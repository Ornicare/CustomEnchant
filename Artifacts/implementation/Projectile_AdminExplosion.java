package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.model.IZoneEffectEnchantment;

/**
 * Explosive enchant for bow.
 * 
 * @author Ornicare
 *
 */
public class Projectile_AdminExplosion extends BaseEnchantment implements IZoneEffectEnchantment{

    /**
     *
     * @param enchantmentName The name of the enchantment
     * @param enchantmentID The ID of the enchantment
     * @param maxLevel The maximum level of the enchantment
     */
	public Projectile_AdminExplosion(String enchantmentName, int enchantmentID, int maxLevel) {
		super(enchantmentName, (short) enchantmentID, (short) maxLevel);
	}

    /**
     * DO AWESOME EXPLOSION !
     *
     * @param projectileShooter : The projectile that was shot
     * @param projectileEntity : The projectile entity ! Hell Yeah !
     */
	@Override
	public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level) {
		// TODO Auto-generated method stub
	
		projectileEntity.getWorld().createExplosion(projectileEntity.getLocation(), level, false);
		projectileEntity.remove();
	}

}
