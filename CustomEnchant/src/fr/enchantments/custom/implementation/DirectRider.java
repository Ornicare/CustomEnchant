package fr.enchantments.custom.implementation;

import org.bukkit.entity.LivingEntity;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IDirectEnchantment;

public class DirectRider extends CommonEnchantment implements IDirectEnchantment
{

    public DirectRider(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, short level, int damage) {
		if(entityInflicter.isInsideVehicle()) {
			entityInflicter.leaveVehicle();
		}
		else {
			entityVictim.setPassenger(entityInflicter);
		}
		
	}

   
}
