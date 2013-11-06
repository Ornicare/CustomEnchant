package fr.enchantments.custom.implementation;
import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.enchantments.custom.model.IDirectHitEnchantment;


/**
 * Explosion enchantment for direct weapons
 * 
 * @author Ornicare
 *
 */
public class DirectHit_SpinUp extends BaseEnchantment implements IDirectHitEnchantment {


	public DirectHit_SpinUp(String name, int id, int maxLevel) { super(name, (short)id, (short)maxLevel); }

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage) {
		entityVictim.setVelocity(entityVictim.getVelocity().add(new Vector(0,level,0)));
	}


}
