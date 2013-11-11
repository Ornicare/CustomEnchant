package fr.enchantments.custom.runnables;

import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public class RunnableSetAfter extends BukkitRunnable
{

	private Block block;
	private int id;
	
	public RunnableSetAfter(Block block, int id)
    {
        this.block = block;
        this.id = id;
    }
	
	@Override
	public void run()
    {
		block.setTypeId(id);
    }

}
