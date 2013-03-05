package fr.enchantments.custom.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import fr.enchantments.custom.factory.EnchantmentFactory;

public class EnchantmentListener implements Listener {
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnchantItemEvent(EnchantItemEvent event) {
		//TODO add conditions rahhhh !
		EnchantmentFactory.addNewEnchantments(event.getItem(),event.getEnchantsToAdd(),event.getExpLevelCost());
	}

}
