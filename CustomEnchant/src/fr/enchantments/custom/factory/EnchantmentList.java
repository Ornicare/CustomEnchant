package fr.enchantments.custom.factory;

import fr.enchantments.custom.model.CommonEnchantment;
import net.minecraft.server.v1_4_R1.EntityLiving;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentList
{

    private List<CommonEnchantment> enchantmentList = new ArrayList<CommonEnchantment>();

    /**
     * Register The Enchantement In The Storage DataBase
     *
     * @param enchantmentToRegister : The Enchantement To Register In The DataBase
     */
    public void registerEnchantment(CommonEnchantment enchantmentToRegister)
    {
        enchantmentList.add(enchantmentToRegister);
    }

    public void arrowHitEntity(EntityLiving shooter, Entity receiver)
    {
        //((ProjectileHitEvent)(Object)shooter).get

        for ( CommonEnchantment actualEnchantment : enchantmentList )
        {

        }
    }

}
