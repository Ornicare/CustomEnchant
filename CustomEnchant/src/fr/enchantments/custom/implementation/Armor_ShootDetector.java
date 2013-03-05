package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IArmorHitEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Armor_ShootDetector extends BaseEnchantment implements IArmorHitEnchantment
{


    public Armor_ShootDetector(String name, int id, int maxLevel) { super(name, (short)id, (short)maxLevel); }

    @Override
    public void onArmorHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short totalEnchantmentsLevels, short damage)
    {
        if ( !(entityVictim instanceof Player) ) { return; }
        ((Player) entityVictim).sendMessage(ChatColor.RED + "Tu t'es fait shooter par " + entityInflicter.toString() + " / Niveau : " + totalEnchantmentsLevels);
    }
}
