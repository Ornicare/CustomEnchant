package fr.enchantments.custom.model;

import org.bukkit.entity.LivingEntity;

/**
 * That interface is the one which implements the required methods to perform some weapons based enchantments.
 * ( Such as swords, shovels and pumpkins of doom. )
 */
public interface IDirectEnchantment {
	
	/**
	 * What ! A poor entity was shot ! POLICE, POLICE !
     * ... wait, no : LISTENER, LISTENER !
     *
     * @param entityInflicter : The cool bad guy.
     * @param entityVictim : The sad victim.
	 */
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim);
}
