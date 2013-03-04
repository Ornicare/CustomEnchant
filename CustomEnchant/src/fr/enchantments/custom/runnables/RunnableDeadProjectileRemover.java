package fr.enchantments.custom.runnables;

import fr.enchantments.custom.loader.PluginLoader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import fr.enchantments.custom.storage.Storage;

import java.util.UUID;

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
