package fr.enchantments.custom.implementation;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class ProjectileKick extends CommonEnchantment implements IZoneEffectEnchantment{

	public ProjectileKick(String enchantmentName, int enchantmentID, int maxLevel) { super(enchantmentName, (short) enchantmentID, (short) maxLevel); }

	@Override
	    public void onProjectileHit(ItemStack projectileShooter, Entity projectileEntity, short level)
	    {
	        for(Entity ent : projectileEntity.getNearbyEntities(level, level, level)) {
	        	if(ent instanceof Player) ((Player) ent).kickPlayer("Kick by peer !");
	        }
	        projectileEntity.remove();
	    }

}
