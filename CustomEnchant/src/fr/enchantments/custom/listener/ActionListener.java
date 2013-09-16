package fr.enchantments.custom.listener;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.runnables.RunnableDeadProjectileRemover;
import fr.enchantments.custom.runnables.RunnableDeadSnowBallRemover;
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
		// if ( !EnchantmentHelper.haveCustomEnchant(projectileShooter) ) { return; }

        // 4] And then do the cool things !
	    Storage.ARROWOWNER.put(event.getProjectile().getUniqueId(), projectileShooter);

        // PluginLoader.pluginLoader.getServer().broadcastMessage(ChatColor.GREEN + "HOOK UUID : " + event.getProjectile().getUniqueId());
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
        ItemStack projectileShooter;
        
        
        //Snowball case
        if(event.getEntity().getType()==EntityType.SNOWBALL) {
        	if(Storage.SNOWBALLOWNER.containsKey(event.getEntity().getShooter())) {
        		projectileShooter = Storage.SNOWBALLOWNER.get(event.getEntity().getShooter());
        		plugin.getFactory().projectileHitSomething(projectileShooter, event.getEntity());
        		
        		//Delay the suppression of the registration
                new RunnableDeadSnowBallRemover((Player) event.getEntity().getShooter()).runTaskLater(plugin, 20);
        		return;
        	}
        }
        
        
        // 2] Verification : does the fundamentals of physics are falling down ?
        if ( !Storage.ARROWOWNER.containsKey(projectile.getUniqueId()) ) { return; }
        projectileShooter = Storage.ARROWOWNER.get(projectile.getUniqueId());
        
        //Delay the suppression of the registration
        new RunnableDeadProjectileRemover(projectile).runTaskLater(plugin, 20);
        
        // 3] Hell yeah ! Now we can do cool things !
        plugin.getFactory().projectileHitSomething(projectileShooter, event.getEntity());
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		if(event.getItem().getType()==Material.SNOW_BALL && (event.getAction()==Action.RIGHT_CLICK_AIR || event.getAction()==Action.RIGHT_CLICK_BLOCK)) {
			Storage.SNOWBALLOWNER.put(event.getPlayer(), event.getPlayer().getItemInHand());
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDie(EntityDeathEvent event)
    {
		
        ItemStack[] armorContents = event.getEntity().getEquipment().getArmorContents();
        for ( ItemStack actualArmorPart : armorContents )
        {
            if ( EnchantmentHelper.haveCustomEnchant(actualArmorPart))
            {
                plugin.getFactory().entityDie(event);
                return;
            }
        }
	}
	
	/**
	 * Call when an entity take damages
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		
		//TODO CraftArrow is not Living !
        // 1] Cool fields declarations
        if ( !(event.getEntity() instanceof LivingEntity) ) { return; }
        LivingEntity entityVictim = (LivingEntity)event.getEntity();
        
        LivingEntity entityInflicter = null;
        Projectile projectile = null;
        
        if(event instanceof EntityDamageByEntityEvent) {
        	Entity tempEntityInflicter = ((EntityDamageByEntityEvent)event).getDamager();
        	if(tempEntityInflicter instanceof Projectile) {
        		entityInflicter = ((Projectile) tempEntityInflicter).getShooter();
        		projectile = (Projectile) tempEntityInflicter;
            }
            else if(tempEntityInflicter instanceof LivingEntity){
            	entityInflicter = (LivingEntity) tempEntityInflicter;
            }
        }
        
        
        
        
		//Test if it's an ignore event.
        String eventId;
        if(entityInflicter!=null) {
        	eventId = entityVictim.getUniqueId().toString()+entityInflicter.getUniqueId().toString();
        	if(Storage.IGNOREEVENTS.contains(eventId)) {
    			Storage.IGNOREEVENTS.remove(eventId);
    			return;
    		}
        }
		

        // 2] Verification : does the fundamentals of physics are falling down ?
        if ( entityVictim == null || entityInflicter == null ) { return; }

        // 3] Get the tool used to spread death
        ItemStack weaponUsed = null;
        if ( event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE )
        {
        	if ( projectile != null && projectile.getType()==EntityType.SNOWBALL) {
            	if(Storage.SNOWBALLOWNER.containsKey(projectile.getShooter())) {
            		weaponUsed = Storage.SNOWBALLOWNER.get(projectile.getShooter());
            	}
            }
        	else {
        		if ( !Storage.ARROWOWNER.containsKey(projectile.getUniqueId()) ) { return; }
                
                weaponUsed = Storage.ARROWOWNER.get(projectile.getUniqueId());
        	}
            
        }
        else { weaponUsed = entityInflicter.getEquipment().getItemInHand(); }

        // 4] Verify Enchantment
        if ( weaponUsed != null && EnchantmentHelper.haveCustomEnchant(weaponUsed) )
        {
            plugin.getFactory().entityHit(entityInflicter, entityVictim, (int)event.getDamage());
        }
        else
        {
            ItemStack[] armorContents = entityVictim.getEquipment().getArmorContents();
            for ( ItemStack actualArmorPart : armorContents )
            {
                if ( EnchantmentHelper.haveCustomEnchant(actualArmorPart))
                {
                    plugin.getFactory().entityHit(entityInflicter, entityVictim, (int)event.getDamage());
                    return;
                }
            }
        }
	}

}
