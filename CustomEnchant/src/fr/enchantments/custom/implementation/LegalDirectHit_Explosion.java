package fr.enchantments.custom.implementation;
import fr.enchantments.custom.model.BaseEnchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import fr.enchantments.custom.helper.ExplosionHelper;
import fr.enchantments.custom.model.IDirectHitEnchantment;
import fr.enchantments.custom.storage.Storage;
import org.bukkit.inventory.ItemStack;


/**
 * Explosion enchantment for direct weapons
 * 
 * @author Antoine
 *
 */
public class LegalDirectHit_Explosion extends BaseEnchantment implements IDirectHitEnchantment {


	public LegalDirectHit_Explosion(String name, int id, int maxLevel) { super(name, (short)id, (short)maxLevel); }

	@Override
	public void onEntityHit(LivingEntity entityInflicter, LivingEntity entityVictim, ItemStack weaponUsed, short level, short damage) {
		
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
