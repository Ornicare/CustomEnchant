package fr.enchantments.custom.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

public class Projectile_FreezingShard extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_FreezingShard(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short)enchantmentID, (short)maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        Block blockHit = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
        if ( blockHit==null ) { return; }
        
        World w = projectileEntity.getWorld();
        List<Block> dir = new ArrayList<Block>();
        Location iniLoc = projectileEntity.getLocation();
        int jmax=1;
        while(level/jmax>5) jmax++;
        for(int k =0;k<10;k++) {
        	for(int j = 1;j<jmax;j++) {
            	for(int i =0;i<(jmax-j+1)*level/2+(jmax-j+1)*Math.random()*level/2;i++) {
                	double rho = Math.random()*level/j;
                	double phi = Math.random()*Math.PI;
                	double theta = Math.random()*Math.PI*2;
                	
                	dir.add(w.getBlockAt((int)(iniLoc.getX()+rho*Math.cos(theta)*Math.sin(phi)),(int)(iniLoc.getY()+rho*Math.sin(theta)*Math.sin(phi)),(int)(iniLoc.getZ()+rho*Math.cos(phi))));
                }
            }
        }
        
        
        

        
        
        for(Block b : dir) {
        	Location finalLoc =  b.getLocation();
        	double distance =finalLoc.distance(iniLoc);
        	
        	for(int i = 0;i<distance;i++) {
        		Location bLoc = iniLoc.clone().add(finalLoc.clone().subtract(iniLoc.clone()).multiply(i/distance));
        		w.getBlockAt(bLoc).setType(Material.ICE);
        	}
        }
        
        
        
        
        projectileEntity.remove();
    }
}
