package fr.enchantments.custom.commands;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.helper.GlowingHelper;
import fr.enchantments.custom.helper.RomanNumeral;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.IEnchantment;
import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTQuery;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.powernbt.nbt.NBTTagShort;
import me.dpohvar.powernbt.nbt.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

                        GlowingHelper.addGlowingEffect(handledItemStack);

                    	commandSender.sendMessage(ChatColor.RED + "Enchantement ajouté !");
                	}
                	else {commandSender.sendMessage(ChatColor.RED + "Enchantement non trouvé !");}
                }
                catch ( Throwable t ) { 
                	commandSender.sendMessage(ChatColor.RED + "Erreur lors de la tentative d'enchantement !"); 
                	PrintWriter w = null;
					try {
						w = new PrintWriter("test");
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	t.printStackTrace(w);
                	w.flush();
                	w.close();
                	}

            return true;
        }
        commandSender.sendMessage(ChatColor.RED + "Erreur lors de la tentative d'enchantement !");
        return false;
    }
}
