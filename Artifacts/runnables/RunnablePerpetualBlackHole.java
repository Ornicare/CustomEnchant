package fr.enchantments.custom.runnables;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.storage.Storage;

public class RunnablePerpetualBlackHole extends BukkitRunnable {

	private Entity projectileEntity;
	private int iterations;

	public RunnablePerpetualBlackHole(Entity projectileEntity, short iterations) {
		this.projectileEntity = projectileEntity;
		this.iterations = iterations;

	}

	@Override
	public void run() {
//		iterations--;
		ExplosionHelper.playBlackHoleEffect(projectileEntity.getLocation());
			//Black hole power decrease with level
			ExplosionHelper.blowEntities((Entity)projectileEntity,-iterations/50.0,iterations*2);
		
		if(iterations==0) {
			projectileEntity.remove();
			this.cancel();
		}
	}

}
