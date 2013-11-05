package fr.enchantments.custom.model;

import org.bukkit.event.entity.EntityDeathEvent;

public interface IArmorDeathEnchantment extends IEnchantment
{

    public void onArmorOwnerDeath(EntityDeathEvent event, short totalEnchantmentsLevels);

}
