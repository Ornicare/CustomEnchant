package fr.enchantments.custom.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import fr.enchantments.custom.factory.EnchantmentFactory;

public class EnchantmentListener implements Listener {
	
	private EnchantmentFactory enchantmentFactory;

	public EnchantmentListener(EnchantmentFactory enchantmentFactory) {
		this.enchantmentFactory = enchantmentFactory;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnchantItemEvent(EnchantItemEvent event) {
		enchantmentFactory.addNewEnchantments(event.getItem(),event.getEnchantsToAdd(),event.getExpLevelCost());
	}

}
