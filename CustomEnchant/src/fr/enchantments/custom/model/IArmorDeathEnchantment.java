package fr.enchantments.custom.model;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public interface IArmorDeathEnchantment
{

    public void onArmorOwnerDeath(EntityDeathEvent event, short totalEnchantmentsLevels);

}
