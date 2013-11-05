package fr.enchantments.custom.model;

import org.bukkit.entity.Player;

public interface IInteractEnchantment extends IEnchantment
{
    public void onInteract(Player entityInflicter, short totalEnchantmentsLevels);

}