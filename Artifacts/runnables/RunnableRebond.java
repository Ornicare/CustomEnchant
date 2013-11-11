package fr.enchantments.custom.runnables;

import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RunnableRebond extends BukkitRunnable
{

	private FallingBlock fallingBlock;
	
	public RunnableRebond(FallingBlock fallingBlock)
    {
        this.fallingBlock = fallingBlock;
    }
	
	@Override
	public void run()
    {
		Block underB = fallingBlock.getWorld().getBlockAt(fallingBlock.getLocation().subtract(new Vector(0,1.1,0)));
		if(underB.getTypeId()!=0) fallingBlock.setVelocity(fallingBlock.getVelocity().add(new Vector(0,-2*fallingBlock.getVelocity().getY(),0)));
		if(fallingBlock.isDead() || underB.isLiquid()) this.cancel();
    }

}
