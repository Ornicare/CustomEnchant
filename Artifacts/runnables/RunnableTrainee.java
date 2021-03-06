package fr.enchantments.custom.runnables;

import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.enchantments.custom.loader.PluginLoader;

public class RunnableTrainee extends BukkitRunnable
{

	private FallingBlock fallingBlock;
	private int delay;
	
	public RunnableTrainee(FallingBlock fallingBlock, int delay)
    {
        this.fallingBlock = fallingBlock;
        this.delay = delay;
    }
	
	@Override
	public void run()
    {
		//if(fallingBlock.getWorld().getBlockAt(fallingBlock.getLocation().subtract(new Vector(0,1,0))).getTypeId()!=0) fallingBlock.setVelocity(fallingBlock.getVelocity().add(new Vector(0,1,0)));
		new RunnableSetAfter(fallingBlock.getWorld().getBlockAt(fallingBlock.getLocation()),fallingBlock.getBlockId()).runTaskLater(PluginLoader.pluginLoader, delay );
		if(fallingBlock.isDead()) this.cancel();
    }

}
