package fr.enchantments.custom.implementation;

import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IArmorDeathEnchantment;
import fr.enchantments.custom.model.IArmorHitEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorDeath_DeathDetector extends BaseEnchantment implements IArmorDeathEnchantment
{


    public ArmorDeath_DeathDetector(String name, int id, int maxLevel) { super(name, (short)id, (short)maxLevel); }

    @Override
    public void onArmorOwnerDeath(EntityDeathEvent event, short totalEnchantmentsLevels)
    {
        if ( !(event.getEntity() instanceof Player) ) { return; }
        ((Player)event.getEntity()).sendMessage(ChatColor.RED + "Tu t'es fait niquer avec du niveau " + totalEnchantmentsLevels);
    }
}
