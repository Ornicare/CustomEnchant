package fr.enchantments.custom.model;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

/**
 * That interface is the one which implements the required methods to perform a zone-recognition enchantments.
 * ( Such as explosions, thrown potions, more explosions, M-O-R-E explosions. And eventually, explosions. )
 */
public interface IZoneEffectEnchantment
{

    /**
     * Enchantment's really A-W-E-S-O-M-E things to do when the projectile hit something A-W-F-U-L
     * OH MY GOD, JAVADOCS ARE AMAZING ! Trololo.
     *
     * @param projectileShooter : The projectile that was shot
     * @param projectileEntity : The projectile entity ! Hell Yeah !
     */
	public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity);
}
