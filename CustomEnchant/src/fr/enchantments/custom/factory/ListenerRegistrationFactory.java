package fr.enchantments.custom.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.helper.EnchantmentHelper;
import fr.enchantments.custom.helper.RandomizerMap;
import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.IArmorDeathEnchantment;
import fr.enchantments.custom.model.IArmorHitEnchantment;
import fr.enchantments.custom.model.IDirectHitEnchantment;
import fr.enchantments.custom.model.IEnchantment;
import fr.enchantments.custom.model.IInteractEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

/**
 * That awesome class collects :
 *     - All the enchantments, and register them
 *     - All the required game events, and send them to the previously registered enchantments
 *
 * I love cats !
 */
public class ListenerRegistrationFactory
{
	
	private short currentId = 0;

    private EnchantmentFactory enchantmentFactory;

	private PluginLoader pluginLoader;

	//public static ListenerRegistrationFactory listenerFactory;
    public ListenerRegistrationFactory(PluginLoader pluginLoader) {
    	this.enchantmentFactory = new EnchantmentFactory(this);
    	this.pluginLoader = pluginLoader;
    	EnchantmentHelper.setFactoryInstance(this);
    }
    //public static void initializeListenerFactory() { listenerFactory = new ListenerRegistrationFactory(); }

    private List<IEnchantment> enchantmentList = new ArrayList<IEnchantment>();
    private RandomizerMap enchantmentMap = new RandomizerMap();
    

    private boolean acceptRegistration = true;

	private Map<String, String> idTable;
    
    /**
     * Register The Enchantment In The Storage DataBase... and then... NOTHING ! MWAHAHAHAHA !
     * @param enchant 
     *
     * @param enchantmentToRegister : The Enchantment To Register In The DataBase
     */
    public void registerEnchantment(String enchant, IEnchantment enchantmentToRegister) {
    	if(acceptRegistration) {
    		while(idTable.values().contains(String.valueOf(currentId))) {
    			++currentId;
    		}
    		
    		if(idTable.containsKey(enchant)) {
    			enchantmentToRegister.setId(Short.parseShort(idTable.get(enchant)));
    		}
    		else {
    			enchantmentToRegister.setId(currentId);
    			idTable.put(enchant, String.valueOf(currentId));
    		}
    		
    		
    		
    		if(enchantmentToRegister.isLegit()) enchantmentMap.push(enchantmentToRegister.getWeight(), enchantmentToRegister);
    		enchantmentList.add(enchantmentToRegister);
    	}
    }

    /**
     * Enchantment Factory : Send the zone event to all required & registered enchantments ! =D
     *
     * @param projectileShooter : The projectile that was shot
     * @param projectileEntity : The projectile entity
     */
    public void projectileHitSomething(ItemStack projectileShooter, Entity projectileEntity)
    {
    	Map<Short,Short> shooterEnchantments = EnchantmentHelper.getCustomEnchantmentList(projectileShooter);
    	
        for ( IEnchantment actualEnchantment : enchantmentList )
        {
            // 1] Skip if the enchantment does not implements the correct interface
            if ( !(actualEnchantment instanceof IZoneEffectEnchantment) ) { continue; }

            // 2] AND THEN FIRE THE FUCKING LAZOR FROM MA MOOOOUUUUUTTTHHHH !
            //  _____
            // | o  o|  <--[this is me]           __
            // |   i |                           /  \
            // |   O==========================> |    |   KABOOM !
            // |_____|                           \__/       ( Yes, that is the Earth !... )
            //                                                ( I'm an awesome artist, huh ? ... no ? :( )
            
            if(shooterEnchantments.containsKey(actualEnchantment.getId())) {
            	((IZoneEffectEnchantment)actualEnchantment).onProjectileHit(projectileShooter, projectileEntity, shooterEnchantments.get(actualEnchantment.getId()));
            }
        }
    }
    
    
    public void playerInteract(Player player, ItemStack item) {
    		Map<Short,Short> shooterEnchantments = EnchantmentHelper.getCustomEnchantmentList(item);
    	
        for ( IEnchantment actualEnchantment : enchantmentList )
        {

            // 1] Skip if the enchantment does not implements the correct interface
            if ( !(actualEnchantment instanceof IInteractEnchantment) ) { continue; }
            pluginLoader.getLogger().log(Level.INFO,"sddsdsdsss");
            if(shooterEnchantments.containsKey(actualEnchantment.getId())) {
    			pluginLoader.getLogger().log(Level.INFO,"sdss");
            	((IInteractEnchantment)actualEnchantment).onInteract(player,  shooterEnchantments.get(actualEnchantment.getId()));
            	}
        }
	}

    /**
     * That method does the same thing that the one above, but with an EntityDamage event.
     * Awesome, huh ?
     *
     * @param entityShooter : The entity that shoot the second one, he's the bad guy D:
     * @param entityVictim : The entity that was shot by the first one, he's the victim :D
     */
    @SuppressWarnings("deprecation")
	public void entityHit(LivingEntity entityShooter, LivingEntity entityVictim, int damage)
    {
    	
        ItemStack inflicterWeapon = entityShooter.getEquipment().getItemInHand();
        

    	pluginLoader.getLogger().log(Level.SEVERE,"sword !"+inflicterWeapon.getTypeId());
    	
        
        if ( EnchantmentHelper.haveCustomEnchant(inflicterWeapon) )
        {
            Map<Short,Short> inflicterEnchantments = EnchantmentHelper.getCustomEnchantmentList(inflicterWeapon);

            pluginLoader.getLogger().log(Level.SEVERE,"sword !"+inflicterEnchantments.size());
            
            for ( IEnchantment actualEnchantment : enchantmentList )
            {
                // 1] Skip if the enchantment does not implements the correct interface
                if ( !(actualEnchantment instanceof IDirectHitEnchantment) ) { continue; }

                // 2] If bla bla
                if ( !inflicterEnchantments.containsKey(actualEnchantment.getId()) ) { continue; }

                // 3] AND THEN...I.. no no, this time i'm not gonna draw a super-lazor of the death.
                ((IDirectHitEnchantment)actualEnchantment).onEntityHit(entityShooter, entityVictim, inflicterWeapon, inflicterEnchantments.get(actualEnchantment.getId()), (short)damage);

            }
        }

        ItemStack[] victimEquipment = entityVictim.getEquipment().getArmorContents();

        Map<IEnchantment, Short> processedEnchantments = EnchantmentHelper.getTotalArmorEnchantmentsLevels(victimEquipment, enchantmentList);
        for ( IEnchantment actualEnchantment : enchantmentList )
        {
            if ( !processedEnchantments.containsKey(actualEnchantment) ) { continue; }
            if ( !(actualEnchantment instanceof IArmorHitEnchantment) ) { continue; }

            ((IArmorHitEnchantment)actualEnchantment).onArmorHit(entityShooter, entityVictim, inflicterWeapon, processedEnchantments.get(actualEnchantment), (short)damage);
        }
    }

    public void entityDie(EntityDeathEvent event)
    {
        ItemStack[] victimEquipment = event.getEntity().getEquipment().getArmorContents();

        Map<IEnchantment, Short> processedEnchantments = EnchantmentHelper.getTotalArmorEnchantmentsLevels(victimEquipment, enchantmentList);
        for ( IEnchantment actualEnchantment : enchantmentList )
        {
            if ( !processedEnchantments.containsKey(actualEnchantment) ) { continue; }
            if ( !(actualEnchantment instanceof IArmorDeathEnchantment) ) { continue; }

            ((IArmorDeathEnchantment)actualEnchantment).onArmorOwnerDeath(event, processedEnchantments.get(actualEnchantment));
        }
    }
    
    public IEnchantment getEnchantementById(short id) {
    	for ( IEnchantment actualEnchantment : enchantmentList )
        {
    		if(actualEnchantment.getId()==id) return actualEnchantment;
        }
    	return null;
    }

	public void stopRegistration() {
		acceptRegistration = false;
		//enchantmentList = enchantmentMap.keySet();
	}

	public RandomizerMap getEnchantmentMap() {
		return enchantmentMap;
	}

	public EnchantmentFactory getEnchantmentFactory() {
		return enchantmentFactory;
	}

	public List<IEnchantment> getEnchantmentList() {
		return enchantmentList;
	}

	public void setIdTable(Map<String, String> hashIdTable) {
		this.idTable = hashIdTable;
	}
	
	public Map<String, String> getIdTable() {
		return this.idTable;
	}



	

}
