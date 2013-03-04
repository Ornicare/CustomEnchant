package fr.enchantments.custom.implementation;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ProjectileDestructFoundations extends CommonEnchantment implements IZoneEffectEnchantment
{

    public ProjectileDestructFoundations(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        Block blockHit = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
        if ( blockHit == null ) { return; }
        projectileEntity.remove();

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

                    if ( Math.sqrt(X*X + Y*Y + Z*Z) > level ) { continue; }
                    Block lolBlock = blockHit.getWorld().getBlockAt((int)finalX, (int)finalY, (int)finalZ);

                    if ( lolBlock.getTypeId() == 0 || /*lolBlock.isLiquid() ||*/ lolBlock.getTypeId() == 7 ) { continue; }

                    FallingBlock fallingBlock = lolBlock.getWorld().spawnFallingBlock(lolBlock.getLocation(), lolBlock.getType(), lolBlock.getData());
                    fallingBlock.setDropItem(false);

                    lolBlock.setType(Material.AIR);
                }
            }
        }

        ExplosionHelper.doFakeExplosion(blockHit.getLocation(), 200);
    }

}
