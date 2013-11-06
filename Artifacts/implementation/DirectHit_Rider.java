package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.entity.LivingEntity;

import fr.enchantments.custom.model.IDirectHitEnchantment;
import org.bukkit.inventory.ItemStack;

public class DirectHit_Rider extends BaseEnchantment implements IDirectHitEnchantment
{

    public DirectHit_Rider(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage) {
		if(entityInflicter.isInsideVehicle()) {
			entityInflicter.leaveVehicle();
		}
		else {
			entityVictim.setPassenger(entityInflicter);
		}
		
	}

   
}
