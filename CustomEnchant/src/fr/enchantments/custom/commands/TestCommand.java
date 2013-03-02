package fr.enchantments.custom.commands;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.loader.PluginLoader;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;

public class TestCommand implements CommandExecutor
{

    private PluginLoader plugin;
    public TestCommand(PluginLoader plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        //if ( !(commandSender instanceof Player) ) { return true; }

        plugin.getServer().broadcastMessage("caca");

        BigInteger destination = new BigInteger(args[1]);
        BigInteger oneoneoneone = new BigInteger("1");

        commandSender.sendMessage("Adding \"" + args[0] + "\" to the current item lore !");

        Player playerSender = (Player)commandSender;
        ItemStack handledItemStack = playerSender.getItemInHand();

        for ( BigInteger bigInteger= new BigInteger("0"); bigInteger.compareTo(destination) == -1; bigInteger = bigInteger.add(oneoneoneone) )
        {
            try
            {
                EnchantmentHelper.addLoreToItem(handledItemStack, ChatColor.AQUA + args[0] + " => " + bigInteger);
            }
            catch ( Throwable t ) { commandSender.sendMessage(ChatColor.RED + "Prout !"); }
        }

        return true;
    }
}
