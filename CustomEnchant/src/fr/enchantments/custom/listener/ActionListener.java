package fr.enchantments.custom.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.enchantments.custom.helper.EnchantementHelper;
import fr.enchantments.custom.storage.Storage;

public class ActionListener {
	
	@SuppressWarnings("unused")
	private JavaPlugin plugin;
	
	public ActionListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * When an arrow is shot, register the weapon of the shooter.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		//Dispenser is not living
		if(event.getEntity() instanceof LivingEntity) {
			ItemStack bow = event.getBow();
			
			//Verify if the bow add specific enchantment
			if(EnchantementHelper.haveSpecificEnchant(bow)) {
				Storage.ARROWOWNER.put(event.getProjectile().getUniqueId(), bow);
			}
		}
	}
	
	/**
	 * When a projectile it a thing, transmit the event to the factory.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		Projectile shooter = event.getEntity();
		if(Storage.ARROWOWNER.containsKey(shooter.getUniqueId())) {
			//TODO transmit to the factory.
		}
	}
	
	
	/**
	 * Call when an entity take damages
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		Entity damagee = event.getEntity();
		if(damagee instanceof LivingEntity) {
			//TODO transmit to the factory.
		}
	}


}