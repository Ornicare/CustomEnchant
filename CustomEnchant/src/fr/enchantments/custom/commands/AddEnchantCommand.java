package fr.enchantments.custom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.implementation.ProjectileExplosion;
import fr.enchantments.custom.loader.PluginLoader;

public class AddEnchantCommand implements CommandExecutor
{

    private PluginLoader plugin;
    public AddEnchantCommand(PluginLoader plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        if ( !(commandSender instanceof Player) ) { return true; }


        Player playerSender = (Player)commandSender;
        ItemStack handledItemStack = playerSender.getItemInHand();

            try
            {
            	EnchantmentHelper.addCustomEnchant(handledItemStack, new ProjectileExplosion("Explosion", 0, 10), 30);
            	commandSender.sendMessage(ChatColor.RED + "Enchantement ajouté !");
            }
            catch ( Throwable t ) { commandSender.sendMessage(ChatColor.RED + "Erreur lors de la tentative d'enchantement !"); }

        return true;
    }
}
