package fr.enchantments.custom.implementation;

import java.util.ArrayList;
import java.util.List;

import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.helper.ProjectileHelper;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class Projectile_BlockExchanger extends BaseEnchantment implements IZoneEffectEnchantment
{

    public Projectile_BlockExchanger(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

    @Override
    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
    {
        Block blockHit = ProjectileHelper.getBlockShotByProjectile(projectileEntity);
        if(blockHit==null) return;
        
        List<Block> L1 = new ArrayList<Block>();
        List<Block> L2 = new ArrayList<Block>();
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

                    if ( X*X + Y*Y + Z*Z > level*level ) { continue; }
                    Block lolBlock = blockHit.getWorld().getBlockAt((int)finalX, (int)finalY, (int)finalZ);
                    if(lolBlock.isEmpty()) continue;
                    if(Math.random()<0.5) {
                    	L1.add(lolBlock);
                    }
                    else {
                    	L2.add(lolBlock);
                    }
                }
            }
        }
        for(int i = 0; i<L1.size();i++) {
        	int j = (int) (Math.random()*L1.size());
        	Block temp = L1.get(j);
        	L1.set(j, L1.get(i));
        	L1.set(i, temp);  	
        }
        
        for(int i = 0; i<L2.size();i++) {
        	int j = (int) (Math.random()*L2.size());
        	Block temp = L2.get(j);
        	L2.set(j, L2.get(i));
        	L2.set(i, temp);  	
        }
        
        while(!L1.isEmpty() && !L2.isEmpty()) {
        	Material temp = L1.get(0).getType();
        	L1.get(0).setType(L2.get(0).getType());
        	L2.get(0).setType(temp);
        	L1.remove(0);
        	L2.remove(0);
        }
        
        projectileEntity.remove();

        ExplosionHelper.doFakeExplosion(blockHit.getLocation(), 200);
    }
}
