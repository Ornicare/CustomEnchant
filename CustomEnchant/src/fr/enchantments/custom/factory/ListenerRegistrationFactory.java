package fr.enchantments.custom.factory;

import fr.enchantments.custom.model.CommonEnchantment;
import fr.enchantments.custom.model.IDirectEnchantment;
import fr.enchantments.custom.model.IEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * That awesome class collects :
 *     - All the enchantments, and register them
 *     - All the required game events, and send them to the previously registered enchantments
 *
 * I love cats !
 */
public class ListenerRegistrationFactory
{

    public static ListenerRegistrationFactory listenerFactory;
    private ListenerRegistrationFactory() { }
    public static void initializeListenerFactory() { listenerFactory = new ListenerRegistrationFactory(); }

    private List<IEnchantment> enchantmentList = new ArrayList<IEnchantment>();

    /**
     * Register The Enchantment In The Storage DataBase... and then... NOTHING ! MWAHAHAHAHA !
     *
     * @param enchantmentToRegister : The Enchantment To Register In The DataBase
     */
    public void registerEnchantment(CommonEnchantment enchantmentToRegister) { enchantmentList.add(enchantmentToRegister); }

    /**
     * Enchantment Factory : Send the zone event to all required & registered enchantments ! =D
     *
     * @param projectileShooter : The projectile that was shot
     * @param projectileEntity : The projectile entity
     */
    public void projectileHitSomething(ItemStack projectileShooter, Entity projectileEntity)
    {
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

            ((IZoneEffectEnchantment)actualEnchantment).onProjectileHit(projectileShooter, projectileEntity);
        }
    }

    /**
     * That method does the same thing that the one above, but with an EntityDamage event.
     * Awesome, huh ?
     *
     * @param entityShooter : The entity that shoot the second one, he's the bad guy D:
     * @param entityVictim : The entity that was shot by the first one, he's the victim :D
     */
    public void entityHit(LivingEntity entityShooter, LivingEntity entityVictim)
    {
        for ( IEnchantment actualEnchantment : enchantmentList )
        {
            // 1] Skip if the enchantment does not implements the correct interface
            if ( !(actualEnchantment instanceof IDirectEnchantment) ) { continue; }

            // 2] AND THEN...I.. no no, this time i'm not gonna draw a super-lazor of the death.
            ((IDirectEnchantment) actualEnchantment).onEntityHit(entityShooter, entityVictim);
        }
    }

}
