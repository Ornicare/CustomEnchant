package fr.enchantments.custom.runnables;

import org.bukkit.entity.Projectile;
import org.bukkit.scheduler.BukkitRunnable;

import fr.enchantments.custom.storage.Storage;

public class RunnableDeadProjectileRemover extends BukkitRunnable {

	private Projectile projectile;
	
	public RunnableDeadProjectileRemover(Projectile projectile) {
		this.projectile = projectile;
	}
	
	@Override
	public void run() {
		if(Storage.ARROWOWNER.containsKey(projectile.getUniqueId())) {
			Storage.ARROWOWNER.remove(projectile.getUniqueId());
		}
		
	}

}
