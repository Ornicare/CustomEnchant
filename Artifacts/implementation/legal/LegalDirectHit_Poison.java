package fr.enchantments.custom.implementation.legal;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IDirectHitEnchantment;


/**
 * Poison enchantment for direct weapons
 * 
 * @author Antoine
 *
 */
public class LegalDirectHit_Poison extends BaseEnchantment implements IDirectHitEnchantment {


	public LegalDirectHit_Poison(String name, int id, int maxLevel,int weight, boolean isLegit) {
		super(name, (short)id, (short)maxLevel, weight, isLegit);
		addAuthorizedItems(bow);
		addAuthorizedItems(swords);
		addAuthorizedItems(pickaxes);
		addAuthorizedItems(axes);
		}

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage) {
		entityVictim.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100*level, 1));
	}


}
