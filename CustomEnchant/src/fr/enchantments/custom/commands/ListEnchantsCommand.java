package fr.enchantments.custom.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.IEnchantment;

public class ListEnchantsCommand implements CommandExecutor {

	private PluginLoader plugin;

	public ListEnchantsCommand(PluginLoader plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command,
			String label, String[] args) {

			List<IEnchantment> enchantments = plugin.getFactory().getEnchantmentList();
			
			for(IEnchantment ench : enchantments) {
				commandSender.sendMessage(ench.getName()+" -- Id:"+ench.getId()+" -- LvlMax:"+ench.getMaxLevel()+" -- Weight:"+ench.getWeight());
			}


		return true;
	}
}
