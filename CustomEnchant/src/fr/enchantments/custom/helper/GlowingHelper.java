package fr.enchantments.custom.helper;

import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import org.bukkit.inventory.ItemStack;

public class GlowingHelper
{

    public static void addGlowingEffect(ItemStack... stacks)
    {
        for ( ItemStack actualItemStack : stacks )
        {
            addGlowingEffect(actualItemStack);
        }
    }

    public static void addGlowingEffect(ItemStack actualItemStack)
    {
        if ( actualItemStack == null ) { return; }
        //if ( actualItemStack.getEnchantmentLevel(Enchantment.SILK_TOUCH) != 32 ) { return; }

        NbtCompound compound = (NbtCompound) NbtFactory.fromItemTag(actualItemStack);
        compound.put(NbtFactory.ofList("ench"));
    }

}
