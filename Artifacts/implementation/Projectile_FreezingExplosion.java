package fr.enchantments.custom.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import fr.enchantments.custom.runnables.RunnableFreezingExplosion;

public class Projectile_FreezingExplosion extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_FreezingExplosion(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

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
                    fallingBlock.setVelocity(new Vector((random.nextInt(200) - 100D) / 100D, 1000000D/(1000000D+random.nextInt(10000)), (random.nextInt(200) - 100D) / 100D));
                    fallingBlock.setDropItem(false);
                    new RunnableFreezingExplosion(fallingBlock).runTaskLater(PluginLoader.pluginLoader, (long) (level*4*Math.random()));
                    
                    lolBlock.setType(Material.AIR);
                }
            }
        }

        
        
        ExplosionHelper.doFakeExplosion(blockHit.getLocation(), 200);
    }
}
