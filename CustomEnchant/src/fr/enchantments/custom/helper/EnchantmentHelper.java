package fr.enchantments.custom.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.powernbt.nbt.NBTTagShort;
import net.minecraft.server.v1_4_R1.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import fr.enchantments.custom.loader.PluginLoader;
import fr.enchantments.custom.model.IEnchantment;

/**
 * This class is a bridge between raw enchantments class and <code>ItemStack</code>(s).
 * It stores the enchantmentID(s) & enchantmentLevel(s) in the <code>ItemStack</code>'s NBT storage.
 */
public abstract class EnchantmentHelper {
	
	/**
	 * Check if <code>ItemStack</code> have at least one new enchant.
	 * 
	 * @param itemStack The <code>ItemStack</code> to check
	 * @return true if <code>ItemStack</code> have at least one new enchant.
	 */
	public static boolean haveSpecificEnchant(ItemStack itemStack) {
		return !getCustomEnchantmentList(itemStack).isEmpty();
	}

    /**
     * Get all the enchantments of the given <code>ItemStack</code>
     *
     * @param item The <code>ItemStack</code> to check
     * @return Map<enchantmentID, enchantmentLevel>
     */
	public static Map<Short, Short> getCustomEnchantmentList(ItemStack item) {
        // TODO : rewrite using NBT Tags

		// Get The Original Minecraft Instantiated ItemStack
		net.minecraft.server.v1_4_R1.ItemStack minecraftItemStack = CraftItemStack.asNMSCopy(item);

        // Map<enchantmentID, enchantmentLevel>
		Map<Short, Short> customEnchant = new HashMap<Short, Short>();
		try {
			if (minecraftItemStack.hasTag()) {
				net.minecraft.server.v1_4_R1.NBTTagCompound tag = minecraftItemStack.tag;
				if (tag.hasKey("customenchant")) {
					net.minecraft.server.v1_4_R1.NBTTagList enchantList = tag.getList("customenchant");
					for (int i = 0; i < enchantList.size(); i++) {
						net.minecraft.server.v1_4_R1.NBTTagCompound ench = (net.minecraft.server.v1_4_R1.NBTTagCompound) enchantList.get(i);
						customEnchant.put(ench.getShort("id"),ench.getShort("lvl"));
					}
				}

			}
		} catch (NullPointerException e) { }

		return customEnchant;
	}

    /**
     * Add the enchantment's ID & Level to the given <code>ItemStack</code>'s NBT Storage.
     * Then add the awesome lore.
     *
     * @param item The <code>ItemStack</code> to enchant
     * @param enchantment The enchantment that will be added to the
     * @param cost The enchantment cost
     */
	public static void addCustomEnchant(ItemStack item, IEnchantment enchantment, int cost) {
		
		//TODO : erase previous enchant if it already exists
		
		//Get the NBT version of the item
		NBTContainerItem container = new NBTContainerItem(item);
		
		//Get an enchantment level
		short level = enchantment.getLevel(cost);
		
		//Create the NBT version of the enchantment
		NBTTagCompound enchantmentNBT  = new NBTTagCompound();
			enchantmentNBT.set("id", new NBTTagShort(enchantment.getId()));
			enchantmentNBT.set("lvl", new NBTTagShort(level));
			
		//If it doesn't have any tag, create it.
		if(container.getTag()==null) container.setTag(new NBTTagCompound("tag"));
		
		//test if it have already "customenchant" tag
		NBTTagList existingCustomEnchant = container.getTag().getList("customenchant");
		if (existingCustomEnchant == null) existingCustomEnchant = new NBTTagList("customenchant");
	
		//add and save it
		existingCustomEnchant.add(enchantmentNBT);
		container.getTag().set("customenchant",existingCustomEnchant);
		
		// Add the corresponding lore
        String romanLevel = new RomanNumeral(level).toString();
        addLoreToItem(item, ChatColor.GRAY, enchantment, romanLevel);
	}

    /**
     * Add a new lore line to the given <code>ItemStack</code>, based on an enchantment
     *
     * @param itemStack The <code>ItemStack</code> that will receive a new lore line
     * @param loreColor The color of the new lore line
     * @param enchantment The enchantment's name that will be added to the <code>ItemStack</code>
     * @param enchantmentLevel The enchantment level that will be displayed next to the lore text
     */
    public static void addLoreToItem(ItemStack itemStack, ChatColor loreColor, IEnchantment enchantment, String enchantmentLevel)
    {
        addLoreToItem(itemStack, loreColor + enchantment.getName() + " " + enchantmentLevel);
    }

    /**
     * Add a new lore line to the given <code>ItemStack</code>, based on raw <code>String</code>
     *
     * @param itemStack The <code>ItemStack</code> that will receive a new lore line
     * @param rawTextToAdd
     */
    public static void addLoreToItem(ItemStack itemStack, String rawTextToAdd)
    {
    	//Get the NBT version of the item
    	NBTContainerItem container = new NBTContainerItem(itemStack);
    	
    	//If it doesn't have any tag, create it.
    	if(container.getTag()==null) container.setTag(new NBTTagCompound("tag"));
    	
    	
    	NBTTagCompound display = container.getTag().getCompound("display");
    	if (display == null) display = new NBTTagCompound("display");
    	
    	NBTTagList Lore = display.getList("Lore");
    	if (Lore == null) Lore = new NBTTagList("Lore");
    	
    	NBTTagString loreToadd = new NBTTagString("",rawTextToAdd);
    	
    	Lore.add(loreToadd);
    	
    	display.set("Lore", Lore);
    	
		container.getTag().set("display",display);
    }
	
	
}
