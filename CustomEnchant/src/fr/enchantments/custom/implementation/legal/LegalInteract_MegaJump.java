package fr.enchantments.custom.implementation.legal;

import org.bukkit.entity.Player;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IInteractEnchantment;

public class LegalInteract_MegaJump extends BaseEnchantment implements IInteractEnchantment {

	public LegalInteract_MegaJump(String enchantmentName, int enchantmentID, int maxLevel, int weight, boolean isLegit) {
		super(enchantmentName, (short) enchantmentID, (short) maxLevel, weight, isLegit);
		//Add authorized items
		addAuthorizedItems(interact);
	}
	
	@Override
	public void onInteract(Player entityInflicter, short totalEnchantmentsLevels) {
		if(!entityInflicter.isFlying() && (entityInflicter.isOnGround()))
		ExplosionHelper.megaJump(entityInflicter.getLocation(),
				totalEnchantmentsLevels, entityInflicter);
	}

}
