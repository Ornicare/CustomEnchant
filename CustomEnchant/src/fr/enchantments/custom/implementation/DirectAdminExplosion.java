package fr.enchantments.custom.implementation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IDirectEnchantment;
import fr.enchantments.custom.storage.Storage;


/**
 * Explosion enchantment for direct weapons
 * 
 * @author Antoine
 *
 */
public class DirectAdminExplosion extends CommonEnchantment implements IDirectEnchantment{


	public DirectAdminExplosion(String name, int id, int maxLevel) {
		super(name, (short) id, (short) maxLevel);
	}

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, short level, int damage) {
		
		ExplosionHelper.doFakeExplosion(entityVictim.getLocation(), level);
		
		for(Entity ent : entityVictim.getNearbyEntities(level,level,level)) {
			if(!ent.equals(entityInflicter) && ent instanceof LivingEntity) {
				LivingEntity entLV = (LivingEntity) ent;
				//Following damages to entLV by entityInflicter do not check enchantment
				Storage.IGNOREEVENTS.add(entLV.getUniqueId().toString()+entityInflicter.getUniqueId().toString());
				entLV.damage(damage,entityInflicter);
			}
		}
		
	}


}
