package fr.enchantments.custom.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import fr.enchantments.custom.factory.EnchantmentFactory;
import fr.enchantments.custom.loader.PluginLoader;

public class EnchantmentListener implements Listener {
	
	private EnchantmentFactory enchantmentFactory;

	public EnchantmentListener(PluginLoader plugin) {
		this.enchantmentFactory = plugin.getFactory().getEnchantmentFactory();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEnchantItemEvent(EnchantItemEvent event) {
		enchantmentFactory.addNewEnchantments(event.getItem(), event.getExpLevelCost());
	}

}
