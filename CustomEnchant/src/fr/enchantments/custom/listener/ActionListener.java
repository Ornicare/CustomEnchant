package fr.enchantments.custom.listener;

import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.EnchantementHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.storage.Storage;

public class ActionListener implements Listener{
	
	private PluginLoader plugin;
	
	public ActionListener(PluginLoader plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * When an arrow is shot, register the weapon of the shooter.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
        // 1] Only a living entity can use enchantments. ( cf : dispensers )
		if ( !(event.getEntity() instanceof LivingEntity) ) { return; }

        // 2] Verification : does the fundamentals of physics are falling down ?
        ItemStack projectileShooter = event.getBow();
        if ( projectileShooter == null ) { return; }

        // 3] Skip non-enchanted items... obviously...
		if ( !EnchantementHelper.haveSpecificEnchant(projectileShooter) ) { return; }

        // 4] And then do the cool things !
	    Storage.ARROWOWNER.put(event.getProjectile().getUniqueId(), projectileShooter);
	}
	
	/**
	 * When a projectile it a thing, transmit the event to the factory.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onProjectileHitEvent(ProjectileHitEvent event) {
        // 1]  Cool Fields Declaration
        Projectile projectile = event.getEntity();

        // 2] Verification : does the fundamentals of physics are falling down ?
        if ( !Storage.ARROWOWNER.containsKey(projectile.getUniqueId()) ) { return; }
        ItemStack projectileShooter = Storage.ARROWOWNER.get(projectile.getUniqueId());

        // 3] Hell yeah ! Now we can do cool things !
        ListenerRegistrationFactory.listenerFactory.projectileHitSomething(projectileShooter, event.getEntity());
	}
	
	
	/**
	 * Call when an entity take damages
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
        // 1] Cool fields declarations
        if ( !(event.getEntity() instanceof LivingEntity) ) { return; }
        LivingEntity entityVictim = (LivingEntity)event.getEntity();
        LivingEntity entityInflicter = ( event instanceof EntityDamageByEntityEvent ) ? (LivingEntity)((EntityDamageByEntityEvent)event).getDamager() : null;

        // 2] Verification : does the fundamentals of physics are falling down ?
        if ( entityVictim == null || entityInflicter == null ) { return; }

        // 3] Get the tool used to spread death
        net.minecraft.server.v1_4_R1.ItemStack weaponUsed;
        if ( event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE )
        {
            if ( !Storage.ARROWOWNER.containsKey(entityInflicter.getUniqueId()) ) { return; }

            weaponUsed = CraftItemStack.asNMSCopy(Storage.ARROWOWNER.get(entityInflicter.getUniqueId()));
            Storage.ARROWOWNER.remove(entityInflicter.getUniqueId());
        }
        else { weaponUsed = CraftItemStack.asNMSCopy(entityInflicter.getEquipment().getItemInHand()); }

        // 4] Verify Enchantment
        if ( !EnchantementHelper.haveSpecificEnchant(weaponUsed) ) { return; }

        // 3] Send all that shit to the factory of hell
        ListenerRegistrationFactory.listenerFactory.entityHit(entityInflicter, entityVictim);
	}


}
