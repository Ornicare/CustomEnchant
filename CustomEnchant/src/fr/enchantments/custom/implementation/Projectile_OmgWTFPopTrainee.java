package fr.enchantments.custom.implementation;

import java.util.Random;

import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import fr.enchantments.custom.runnables.RunnableTrainee;

public class Projectile_OmgWTFPopTrainee extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_OmgWTFPopTrainee(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        Block blockHit = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
        if ( blockHit==null ) { return; }
        projectileEntity.remove();

        Random random = new Random();
        int marge = level * 2;
        for ( int X=-marge; X<marge; X++)
        {
            for ( int Y=-marge; Y<marge; Y++)
            {
                for ( int Z=-marge; Z<marge; Z++)
                {
                    double finalX = blockHit.getX() + X;
                    double finalY = blockHit.getY() + Y;
                    double finalZ = blockHit.getZ() + Z;

                    //Location lolLocation = new Location(blockHit.getWorld(), finalX, finalY, finalZ);

                    if ( Math.sqrt(X*X + Y*Y + Z*Z) > level ) { continue; }
                    Block lolBlock = blockHit.getWorld().getBlockAt((int)finalX, (int)finalY, (int)finalZ);

                    if ( lolBlock.getTypeId() == 0 || /*lolBlock.isLiquid() ||*/ lolBlock.getTypeId() == 7 ) { continue; }
                    FallingBlock fallingBlock = lolBlock.getWorld().spawnFallingBlock(lolBlock.getLocation(), lolBlock.getType(), lolBlock.getData());
                    double distance = lolBlock.getLocation().distance(blockHit.getLocation());
                    Vector dir = lolBlock.getLocation().clone().subtract(blockHit.getLocation().clone()).toVector().normalize().multiply(level/10*4*(distance+level)/(2*level));
                    dir.setY(level/10*Math.random());
                    dir.setX(dir.getX()*Math.random());
                    dir.setZ(dir.getZ()*Math.random());
                    fallingBlock.setVelocity(dir);
                    fallingBlock.setDropItem(false);
                    new RunnableTrainee(fallingBlock,100).runTaskTimer(PluginLoader.pluginLoader, 1, 1);
                    lolBlock.setType(Material.AIR);
                }
            }
        }

        ExplosionHelper.doFakeExplosion(blockHit.getLocation(), 200);
    }
}
