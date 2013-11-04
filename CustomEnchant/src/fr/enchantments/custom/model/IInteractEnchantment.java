package fr.enchantments.custom.model;

import org.bukkit.entity.Player;

public interface IInteractEnchantment
{
    public void onInteract(Player entityInflicter, short totalEnchantmentsLevels);

}