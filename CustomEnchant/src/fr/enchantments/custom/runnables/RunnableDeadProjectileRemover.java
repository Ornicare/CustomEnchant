package fr.enchantments.custom.runnables;

import java.util.UUID;

import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import fr.enchantments.custom.storage.Storage;

public class RunnableDeadProjectileRemover extends BukkitRunnable
{

	private UUID projectileUUID;
	// private Projectile projectile;
	
	public RunnableDeadProjectileRemover(Projectile projectile)
    {
        // this.projectile = projectile;
        this.projectileUUID = projectile.getUniqueId();
    }
	
	@Override
	public void run()
    {
        if ( Storage.ARROWOWNER.containsKey(projectileUUID) )
        {
            Storage.ARROWOWNER.remove(projectileUUID);
            //PluginLoader.pluginLoader.getServer().broadcastMessage(ChatColor.GREEN + "REMOVE UUID : " + projectileUUID);
        }
    }

}
