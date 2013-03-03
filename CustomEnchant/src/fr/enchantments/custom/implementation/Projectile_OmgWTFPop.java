package fr.enchantments.custom.implementation;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Random;

public class Projectile_OmgWTFPop extends CommonEnchantment implements IZoneEffectEnchantment
{

    public Projectile_OmgWTFPop(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        Block blockHit = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
        projectileEntity.remove();

        Random random = new Random();
        int marge = 20;
        for ( int X=-marge; X<marge; X++)
        {
            for ( int Y=-marge; Y<marge; Y++)
            {
                for ( int Z=-marge; Z<marge; Z++)
                {
                    double finalX = blockHit.getX() + X;
                    double finalY = blockHit.getY() + Y;
                    double finalZ = blockHit.getZ() + Z;

                    Location lolLocation = new Location(blockHit.getWorld(), finalX, finalY, finalZ);

                    if ( Math.sqrt(X*X + Y*Y + Z*Z) > 8 ) { continue; }
                    Block lolBlock = blockHit.getWorld().getBlockAt((int)finalX, (int)finalY, (int)finalZ);
                    //PluginLoader.pluginLoader.getServer().broadcastMessage("MAtrix : " + lolBlock.getTypeId() + " => " + X + " " + Y + " " + Z);
                    if ( lolBlock.getTypeId() == 0 ) { continue; }
                    FallingBlock fallingBlock = lolBlock.getWorld().spawnFallingBlock(lolBlock.getLocation(), lolBlock.getType(), (byte) 0);
                    fallingBlock.setVelocity(new Vector((random.nextInt(200) - 100D) / 100D, 1000000D/(1000000D+random.nextInt(10000)), (random.nextInt(200) - 100D) / 100D));
                    lolBlock.setType(Material.AIR);
                }
            }
        }

        ExplosionHelper.doFakeExplosion(blockHit.getLocation(), 200);
    }
}
