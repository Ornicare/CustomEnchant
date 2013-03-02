package fr.enchantments.custom.implementation;
import org.bukkit.entity.LivingEntity;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IDirectEnchantment;


/**
 * Explosion enchantment for direct weapons
 * 
 * @author Antoine
 *
 */
public class DirectExplosion extends CommonEnchantment implements IDirectEnchantment{


	public DirectExplosion(String name, int id, int maxLevel) {
		super(name, (short) id, (short) maxLevel);
	}

	@Override
	public void onEntityHit(LivingEntity entityInflicter,
			LivingEntity entityVictim) {
		// TODO Auto-generated method stub
		
	}


}
