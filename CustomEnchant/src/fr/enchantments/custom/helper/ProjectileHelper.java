package fr.enchantments.custom.helper;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.BlockIterator;

public class ProjectileHelper
{

    /**
     * Return the block shot by a common projectile
     *
     * @param projectileEntity The projectile that will be scanned
     * @return The block shot by a projectile
     */
    public static Block getBlockShotByProjectile(Entity projectileEntity)
    {
        World projectileWorld = projectileEntity.getWorld();
        BlockIterator blockIterator = new BlockIterator(projectileWorld, projectileEntity.getLocation().toVector(), projectileEntity.getVelocity().normalize(), 0, 4);
        Block hitBlock = null;

        while(blockIterator.hasNext())
        {
            hitBlock = blockIterator.next();
            if ( hitBlock.getTypeId() != 0 ) { break; }
        }

        if ( hitBlock == null ) { return null; }
        if ( hitBlock.isEmpty() ) { return null; }
        if ( hitBlock.isLiquid() ) { return null; }
        if ( hitBlock.getTypeId() == 0 ) { return null; }

        return hitBlock;
    }

}
