package fr.enchantments.custom.helper;

import java.util.HashMap;
import java.util.Map;

import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.powernbt.nbt.NBTTagShort;
import me.dpohvar.powernbt.nbt.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

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
		
		// Map<enchantmentID, enchantmentLevel>
		Map<Short, Short> customEnchant = new HashMap<Short, Short>();
		
		if(item==null) return customEnchant;
		
		//If it doesn't have a tag, return
		//TODO error in case of mob damage player &&  workexplosion doesn't
		NBTContainerItem container = new NBTContainerItem(item);
		try {
			if(container.getTag()==null) return customEnchant;
		}
		catch(Throwable e) {
			return customEnchant;
		}
		
		//test if it have already "customenchant" tag
		NBTTagList existingCustomEnchant = container.getTag().getList("customenchant");
		if (existingCustomEnchant == null) return customEnchant;

		for (int i=0;i<existingCustomEnchant.size();i++) {
			NBTTagCompound enchant = (NBTTagCompound) existingCustomEnchant.get(i);
			customEnchant.put(enchant.getShort("id"),enchant.getShort("lvl"));
		}
		
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
		//Get an enchantment level
		short level = enchantment.getLevel(cost);
		
		addCustomEnchantWithLevel(item, enchantment, level);
	}

	/**
	 * Set an enchantment by using a level
	 * 
	 * @param item
	 * @param enchantment
	 * @param level
	 */
	public static void addCustomEnchantWithLevel(ItemStack item, IEnchantment enchantment, short level) {
		//erase previous enchant if it already exists
		if(getCustomEnchantmentList(item).containsKey(enchantment.getId())) {
			modifyCustomEnchantByUsingLevel(item,enchantment,level);
			return;
		}
		
		//Get the NBT version of the item
		NBTContainerItem container = new NBTContainerItem(item);
		
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
     * Set <code>enchantment</code> to <code>level</code> for <code>item</code>
     * 
     * @param item
     * @param enchantment
     * @param level
     */
    public static void modifyCustomEnchantByUsingLevel(ItemStack item, IEnchantment enchantment, short level) {

    	//Get the NBT version of the item
		NBTContainerItem container = new NBTContainerItem(item);

		NBTTagList existingCustomEnchant = container.getTag().getList("customenchant");
		String oldRomanLevel = null;
		for (int i=0;i<existingCustomEnchant.size();i++) {
			NBTTagCompound enchant = (NBTTagCompound) existingCustomEnchant.get(i);
			if(enchant.getShort("id")==enchantment.getId()) {
				//get the old lore
				oldRomanLevel = new RomanNumeral(enchant.getShort("lvl")).toString();
				
				enchant.set("lvl", new NBTTagShort(level));
				existingCustomEnchant.set(i, enchant);
			}
		}
        
        //add and save it
		container.getTag().set("customenchant",existingCustomEnchant);
		
		// Modify the corresponding lore
        String romanLevel = new RomanNumeral(level).toString();
        delItemLore(item, ChatColor.GRAY + enchantment.getName() + " " + oldRomanLevel);
        addLoreToItem(item, ChatColor.GRAY, enchantment, romanLevel);
	}

	/**
     * Try to remove <code>loreToRemove</code> from <code>itemStack</code> Lore.
     * 
     * @param itemStack
     * @param loreToRemove
     */
	public static void delItemLore(ItemStack itemStack, String loreToRemove) {
		//Get the NBT version of the item
    	NBTContainerItem container = new NBTContainerItem(itemStack);
    	
    	//If it doesn't have any tag, create it.
    	if(container.getTag()==null) container.setTag(new NBTTagCompound("tag"));
    	
    	NBTTagCompound display = container.getTag().getCompound("display");
    	if (display == null) display = new NBTTagCompound("display");
    	
    	NBTTagList Lore = display.getList("Lore");
    	if (Lore == null) Lore = new NBTTagList("Lore");

    	int loreToRemoveIndex = -1;
		for (int i=0;i<Lore.size();i++) {

			NBTTagString loreCompound = (NBTTagString)Lore.get(i);

			if(loreCompound.get().equals(loreToRemove)) {
				loreToRemoveIndex = i;
				break;
			}
		}
    	
    	if(loreToRemoveIndex!=-1) Lore.remove(loreToRemoveIndex);
    	
    	display.set("Lore", Lore);
    	
		container.getTag().set("display",display);
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
