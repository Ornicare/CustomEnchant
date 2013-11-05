package fr.enchantments.custom.model;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

/**
 * That interface is the one which implements the required methods to perform some weapons based enchantments.
 * ( Such as swords, shovels and pumpkins of doom. )
 */
public interface IDirectHitEnchantment extends IEnchantment{
	
	/**
	 * What ! A poor entity was shot ! POLICE, POLICE !
     * ... wait, no : LISTENER, LISTENER !
     *
     * @param entityInflicter : The cool bad guy.
     * @param entityVictim : The sad victim.
	 */
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage);

}
