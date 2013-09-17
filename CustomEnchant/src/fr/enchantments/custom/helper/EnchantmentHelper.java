package fr.enchantments.custom.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dpohvar.powernbt.nbt.NBTBase;
import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTQuery;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.powernbt.nbt.NBTTagString;
import net.minecraft.server.v1_6_R2.Item;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.model.IEnchantment;
//import fr.enchantments.custom.loader.PluginLoader;

/**
 * This class is a bridge between raw enchantments class and <code>ItemStack</code>(s).
 * It stores the enchantmentID(s) & enchantmentLevel(s) in the <code>ItemStack</code>'s NBT storage.
 */
public abstract class EnchantmentHelper
{
	
	private static int[] bow = {Item.BOW.id};
	private static int[] swords = {Item.WOOD_SWORD.id,Item.STONE_SWORD.id,Item.IRON_SWORD.id,Item.DIAMOND_SWORD.id,Item.GOLD_SWORD.id};
	private static int[] axes = {Item.WOOD_AXE.id,Item.STONE_AXE.id,Item.IRON_AXE.id,Item.DIAMOND_AXE.id,Item.GOLD_AXE.id};
	private static int[] pickaxes = {Item.WOOD_PICKAXE.id,Item.STONE_PICKAXE.id,Item.IRON_PICKAXE.id,Item.DIAMOND_PICKAXE.id,Item.GOLD_PICKAXE.id};
	private static int[] special = {Item.SNOW_BALL.id};
	
	//TODO snowballs ! arrows !
	private static int[][] enchantableItems = {bow,swords,axes,pickaxes,special};
	
	/**
	 * Lore recognition prefix.
	 */
	private static String CEPREFIX = "§z§c§e§p";
	
	/**
	 * Check if <code>ItemStack</code> have at least one new enchant.
	 * 
	 * @param itemStack The <code>ItemStack</code> to check
	 * @return true if <code>ItemStack</code> have at least one new enchant.
	 */
	public static boolean haveCustomEnchant(ItemStack itemStack) {
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
		
//		//If it doesn't have a tag, return
//		//TODO error in case of mob damage player &&  workexplosion doesn't
//		NBTContainerItem container = new NBTContainerItem(item);
			
		//TODO euh... on verra
		int itemId = item.getTypeId();
		
		boolean isEnch = false;
		for(int[] list : enchantableItems) {
			for(int id : list) {
				if(id==itemId) isEnch = true;
			}
		}
		
		if(!isEnch) return customEnchant;
//		
//		try {
//			if(container.getTag()==null) return customEnchant;
//		}
//		catch(Throwable e) {
//			return customEnchant;
//		}
//		
//		
//		
//		//test if it have already "customenchant" tag
//		NBTTagList existingCustomEnchant = (NBTTagList) getCompoundFromString(container,"customenchant");
//
//		if (existingCustomEnchant == null) return customEnchant;
//
//		for (int i=0;i<existingCustomEnchant.size();i++) {
//			NBTTagCompound enchant = (NBTTagCompound) existingCustomEnchant.get(i);
//			customEnchant.put(enchant.getShort("id"),enchant.getShort("lvl"));
//		}
		
		
		//Get the NBT version of the item
    	NBTContainerItem container = new NBTContainerItem(item);
    	
    	//If it doesn't have any tag, create it.
    	if(container.getTag()==null) container.setTag(new NBTTagCompound("tag"));
    	
    	NBTTagCompound display = container.getTag().getCompound("display");
    	if (display == null) display = new NBTTagCompound("display");
    	
    	NBTTagList Lore = display.getList("Lore");
    	if (Lore == null) Lore = new NBTTagList("Lore");

		for (int i=0;i<Lore.size();i++) {

			NBTTagString loreCompound = (NBTTagString)Lore.get(i);
					
			if(loreCompound.get().startsWith(CEPREFIX)) {
				return deserialize(loreCompound.get());
			}
		}
		
		return customEnchant;
	}
	
	/**
	 * Transform a Map of customs enchantments into a string like <code>§1§-§2§;§7§-§9§;</code>
	 * 
	 * @param customEnchants
	 * @return
	 */
	private static String serialize(Map<Short,Short> customEnchants) {
		String tempResult = "";
		for(short id : customEnchants.keySet()) {
			tempResult+=id+"-"+customEnchants.get(id)+";";
		}
		
		String result = "";
		for(short i = 0; i < tempResult.length() ; ++i) {
			result+="§"+tempResult.substring(i, i+1);
			
		}
		
		return CEPREFIX+result;
	}
	
	/**
	 * Transform a string encode in a certain pattern into a Map.
	 * 
	 * @param s
	 * @return
	 */
	private static Map<Short,Short> deserialize(String s) {
		
		
		
		Map<Short,Short> result = new HashMap<Short,Short>();
		try {
			String tempResult = s.replaceAll(CEPREFIX, "");
			tempResult = tempResult.replaceAll("§", "");
			
//			PluginLoader.pluginLoader.getLogger().log(Level.INFO,tempResult);
			
			for(String subS : tempResult.split(";")) {
				String[] temp = subS.split("-");
				short id = Short.parseShort(temp[0]);
				short value = Short.parseShort(temp[1]);
				result.put(id,value);
			}
		}
		catch(Exception e) {}
		
		
		return result;
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
		Map<Short, Short> enchants = getCustomEnchantmentList(item);
		
		//erase previous enchant if it already exists
		if(enchants.containsKey(enchantment.getId())) {
			modifyCustomEnchantByUsingLevel(item,enchantment,level);
		}
		else {
			// Add the corresponding lore
	        String romanLevel = level>3999?Integer.toString(level):new RomanNumeral(level).toString();
	        addLoreToItem(item, ChatColor.GRAY, enchantment, romanLevel);
		}
		
		String oldEnchants = serialize(enchants);
		
		//We use the non-redondancy property of map to erase all old keys.
		enchants.put(enchantment.getId(), level);
		
	
		writeEchants(item,enchants,oldEnchants);

		
	}

	/**
	 * Write a new lore to save custom enchants.
	 * 
	 * @param item
	 * @param enchants
	 * @param oldEnchants 
	 */
    private static void writeEchants(ItemStack item, Map<Short, Short> enchants, String oldEnchants) {
		// TODO Auto-generated method stub
    	delItemLore(item, oldEnchants);
        addLoreToItem(item, serialize(enchants));
	}

	/**
     * Set <code>enchantment</code> to <code>level</code> for <code>item</code>
     * 
     * @param item
     * @param enchantment
     * @param level
     */
    public static void modifyCustomEnchantByUsingLevel(ItemStack item, IEnchantment enchantment, short level) {
    	
    	Map<Short, Short> enchants = getCustomEnchantmentList(item);
    	
		String oldRomanLevel = null;
		for (short id : enchants.keySet()) {
			if(id==enchantment.getId()) {
				//get the old lore
				oldRomanLevel = new RomanNumeral(enchants.get(id)).toString();
			}
		}
		
		// Modify the corresponding lore
        String romanLevel = level>3999?Integer.toString(level):new RomanNumeral(level).toString();
        delItemLore(item, ChatColor.GRAY + enchantment.getName() + " " + oldRomanLevel);
        addLoreToItem(item, ChatColor.GRAY, enchantment, romanLevel);
	}

    /**
     * Return the customenchant NBTTag
     * 
     * @param container
     * @return
     */
	private static NBTBase getCompoundFromString(NBTContainerItem container, String query) {
		return container.getCustomTag(NBTQuery.fromString(query));
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
//			PluginLoader.pluginLoader.getLogger().log(Level.INFO,loreCompound.get()+"---"+loreToRemove);
			if(loreCompound.get().equals(loreToRemove)) {
				loreToRemoveIndex = i;
				break;
			}
		}
    	
    	if(loreToRemoveIndex!=-1) Lore.remove(loreToRemoveIndex);
    	
    	display.set("Lore", Lore);
    	
    	container.setCustomTag(NBTQuery.fromString("display"),display); //TODO : duplicat bordel !
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
    	
    	
    	NBTTagCompound display = (NBTTagCompound) container.getCustomTag(NBTQuery.fromString("display"));
    	if (display == null) display = new NBTTagCompound("display");
    	
    	NBTTagList Lore = display.getList("Lore");
    	if (Lore == null) Lore = new NBTTagList("Lore");
    	
    	NBTTagString loreToadd = new NBTTagString("",rawTextToAdd);
    	
    	Lore.add(loreToadd);
    	
    	display.set("Lore", Lore);
    	
		container.setCustomTag(NBTQuery.fromString("display"),display);
    }

    public static Map<IEnchantment, Short> getTotalArmorEnchantmentsLevels(ItemStack[] playerArmor, List<IEnchantment> enchantmentList)
    {
        Map<IEnchantment, Short> finalLevels = new HashMap<IEnchantment, Short>();

        for ( IEnchantment actualEnchantment : enchantmentList )
        {
            short totalEnchantmentLevels = 0;

            for ( ItemStack actualArmorPart : playerArmor )
            {
                Map<Short, Short> actualPartEnchantments = EnchantmentHelper.getCustomEnchantmentList(actualArmorPart);

                if ( !actualPartEnchantments.containsKey(actualEnchantment.getId()) ) { continue; }
                totalEnchantmentLevels += actualPartEnchantments.get(actualEnchantment.getId());
            }

            if ( totalEnchantmentLevels != 0 ) { finalLevels.put(actualEnchantment, totalEnchantmentLevels); }
        }

        return finalLevels;
    }
	
}
