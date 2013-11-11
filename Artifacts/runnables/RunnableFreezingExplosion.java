package fr.enchantments.custom.runnables;

import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableFreezingExplosion extends BukkitRunnable
{

	private FallingBlock fallingBlock;
	
	public RunnableFreezingExplosion(FallingBlock fallingBlock)
    {
        this.fallingBlock = fallingBlock;
    }
	
	@Override
	public void run()
    {
		fallingBlock.remove();
		fallingBlock.getWorld().getBlockAt(fallingBlock.getLocation()).setTypeId(fallingBlock.getBlockId());
    }

}
