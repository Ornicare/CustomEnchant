package fr.enchantments.custom.implementation;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

/**
 * Explosive enchant for bow.
 * 
 * @author Ornicare
 *
 */
public class ProjectileExplosion extends CommonEnchantment implements IZoneEffectEnchantment{

	public ProjectileExplosion(String name, short id) {
		super(name, id);
	}

	@Override
	public void onProjectileHit(ItemStack projectileShooter,
			Entity projectileEntity) {
		// TODO Auto-generated method stub
		
	}

}
