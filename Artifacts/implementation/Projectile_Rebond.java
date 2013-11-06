package fr.enchantments.custom.implementation;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import fr.enchantments.custom.runnables.RunnableRebond;

public class Projectile_Rebond extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_Rebond(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        Block blockShot = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
        if ( blockShot == null  || blockShot.getType() == Material.BEDROCK || blockShot.getType() == Material.AIR) { return; }

        //PluginLoader.pluginLoader.getServer().broadcastMessage("HOME RUN ! [2]");

        Vector directionVector = blockShot.getLocation().toVector().subtract(((Projectile)projectileEntity).getShooter().getLocation().toVector().add(new Vector(0, 1, 0)));
        directionVector.setY(Math.abs(directionVector.getBlockY()));
        directionVector = directionVector.normalize().multiply(0.9999D);

        FallingBlock fallingBlock = blockShot.getWorld().spawnFallingBlock(blockShot.getLocation(), blockShot.getType(), blockShot.getData());
        fallingBlock.setVelocity(directionVector);
        fallingBlock.setDropItem(false);

        // fallingBlock.setPassenger(shooter);
        // shooter.setPassenger(shooter);
        // fallingBlock.setPassenger(fallingBlock);
        
        new RunnableRebond(fallingBlock).runTaskTimer(PluginLoader.pluginLoader, 1, 1);
        blockShot.setType(Material.AIR);
        projectileEntity.remove();

    }
}
