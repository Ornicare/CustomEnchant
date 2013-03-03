package fr.enchantments.custom.implementation;

import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.Random;

public class Projectile_BaseBowL extends CommonEnchantment implements IZoneEffectEnchantment
{

    private static final String[] listText = new String[]
    {
        "HOME RUN ! HELL YEEEEEEEAH !",
        "OMG WTF LOL !",
        "BASE-BALL POWER !",
        "OH YEAH, OH YEEEEEAAAAAAH !",
        "HOLY SHIT !",
        "FUCKING HELL YEAH !",
        "AMAZING OMG WTF POP !",
        "YEAH YEAH YEAH YEAH !",
        "LOL XD MDR PTDR !"
    };
    private static final String getRandomText() { return listText[(new Random()).nextInt(listText.length)]; }

    public Projectile_BaseBowL(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        try
        {
            if ( !(projectileEntity instanceof Arrow) ) { return; }
            LivingEntity shooter = ((Arrow)projectileEntity).getShooter();
            if ( !(shooter instanceof Player) ) { return; }

            Block blockShot = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
            if ( blockShot == null ) { return; }

            //PluginLoader.pluginLoader.getServer().broadcastMessage("HOME RUN ! [2]");

            Vector directionVector = blockShot.getLocation().toVector().subtract(((Arrow)projectileEntity).getShooter().getLocation().toVector());
            directionVector = directionVector.normalize().multiply(0.9999D);

            FallingBlock fallingBlock = blockShot.getWorld().spawnFallingBlock(blockShot.getLocation(), blockShot.getType(), (byte)0);
            fallingBlock.setVelocity(directionVector);

            blockShot.setType(Material.AIR);
            projectileEntity.remove();

            ((Player)shooter).sendMessage(ChatColor.RED + getRandomText());
        }
        catch ( Throwable t ) { ((Player)(((Arrow)projectileEntity).getShooter())).sendMessage(ChatColor.RED + getRandomText()); }
    }
}