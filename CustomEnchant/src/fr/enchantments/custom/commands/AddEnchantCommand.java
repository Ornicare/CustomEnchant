package fr.enchantments.custom.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.IEnchantment;

public class AddEnchantCommand implements CommandExecutor
{

    private PluginLoader plugin;
    public AddEnchantCommand(PluginLoader plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        if ( !(commandSender instanceof Player) ) { return true; }

        if(args.length>0) {
        	Player playerSender = (Player)commandSender;
            ItemStack handledItemStack = playerSender.getItemInHand();

                try
                {
                	IEnchantment enchantment = plugin.getFactory().getEnchantementById(Short.parseShort(args[0]));
                	if(enchantment!=null) {
                		if(args.length>1) {
                    		EnchantmentHelper.addCustomEnchantWithLevel(handledItemStack, enchantment, Short.parseShort(args[1]));
                    	}
                    	else {
                    		EnchantmentHelper.addCustomEnchant(handledItemStack, enchantment, 30);
                    	}
                    	commandSender.sendMessage(ChatColor.RED + "Enchantement ajouté !");
                	}
                	else {commandSender.sendMessage(ChatColor.RED + "Enchantement non trouvé !");}
                }
                catch ( Throwable t ) { commandSender.sendMessage(ChatColor.RED + "Erreur lors de la tentative d'enchantement !"); }

            return true;
        }
        commandSender.sendMessage(ChatColor.RED + "Erreur lors de la tentative d'enchantement !");
        return false;
    }
}
