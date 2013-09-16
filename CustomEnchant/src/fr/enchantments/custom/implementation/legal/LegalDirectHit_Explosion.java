package fr.enchantments.custom.implementation.legal;
import fr.enchantments.custom.model.BaseEnchantment;
import net.minecraft.server.v1_6_R2.Item;

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


	public LegalDirectHit_Explosion(String name, int id, int maxLevel,int weight, boolean isLegit) {
		super(name, (short)id, (short)maxLevel, weight, isLegit);
		
		//Add authorized items
		addAuthorizedItems(swords);
		addAuthorizedItems(pickaxes);
		addAuthorizedItems(axes);
		}

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
