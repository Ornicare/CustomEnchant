package fr.enchantments.custom.implementation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IDirectEnchantment;


/**
 * Explosion enchantment for direct weapons
 * 
 * @author Ornicare
 *
 */
public class DirectSpinUp extends CommonEnchantment implements IDirectEnchantment{


	public DirectSpinUp(String name, int id, int maxLevel) {
		super(name, (short) id, (short) maxLevel);
	}

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, short level, int damage) {
		entityVictim.setVelocity(entityVictim.getVelocity().add(new Vector(0,level,0)));
	}


}
