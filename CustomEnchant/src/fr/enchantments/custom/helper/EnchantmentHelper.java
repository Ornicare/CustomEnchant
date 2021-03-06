package fr.enchantments.custom.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTQuery;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;
import me.dpohvar.powernbt.nbt.NBTTagString;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.model.EnchantablesItems;
import fr.enchantments.custom.model.IEnchantment;

//import fr.enchantments.custom.loader.PluginLoader;

/**
 * This class is a bridge between raw enchantments class and
 * <code>ItemStack</code>(s). It stores the enchantmentID(s) &
 * enchantmentLevel(s) in the <code>ItemStack</code>'s NBT storage.
 */
public abstract class EnchantmentHelper extends EnchantablesItems {

	// private static int[] bow = { Item.BOW.id };
	// private static int[] swords = { Item.WOOD_SWORD.id, Item.STONE_SWORD.id,
	// Item.IRON_SWORD.id, Item.DIAMOND_SWORD.id, Item.GOLD_SWORD.id };
	// private static int[] axes = { Item.WOOD_AXE.id, Item.STONE_AXE.id,
	// Item.IRON_AXE.id, Item.DIAMOND_AXE.id, Item.GOLD_AXE.id };
	// private static int[] pickaxes = { Item.WOOD_PICKAXE.id,
	// Item.STONE_PICKAXE.id, Item.IRON_PICKAXE.id,
	// Item.DIAMOND_PICKAXE.id, Item.GOLD_PICKAXE.id };
	// private static int[] special = { Item.SNOW_BALL.id };
	// private static int[] armors = { Item.LEATHER_BOOTS.id,
	// Item.LEATHER_CHESTPLATE.id, Item.LEATHER_HELMET.id,
	// Item.LEATHER_LEGGINGS.id, Item.IRON_BOOTS.id, Item.IRON_BOOTS.id,
	// Item.IRON_HELMET.id, Item.IRON_LEGGINGS.id, Item.GOLD_BOOTS.id,
	// Item.GOLD_CHESTPLATE.id, Item.GOLD_HELMET.id,
	// Item.GOLD_LEGGINGS.id, Item.CHAINMAIL_BOOTS.id,
	// Item.CHAINMAIL_CHESTPLATE.id, Item.CHAINMAIL_HELMET.id,
	// Item.CHAINMAIL_LEGGINGS.id, Item.DIAMOND_BOOTS.id,
	// Item.DIAMOND_CHESTPLATE.id, Item.DIAMOND_HELMET.id,
	// Item.DIAMOND_LEGGINGS.id
	//
	// };

	private static int[][] enchantableItems = { bow, swords, axes, pickaxes,
			special, armors, interact };

	/**
	 * Lore recognition prefix.
	 */
	private static String CEPREFIX = "§z§c§e§p";

	private static ListenerRegistrationFactory factory;

	/**
	 * Check if <code>ItemStack</code> have at least one new enchant.
	 * 
	 * @param itemStack
	 *            The <code>ItemStack</code> to check
	 * @return true if <code>ItemStack</code> have at least one new enchant.
	 */
	public static boolean haveCustomEnchant(ItemStack itemStack) {
		return !getCustomEnchantmentList(itemStack).isEmpty();
	}

	/**
	 * Get all the enchantments of the given <code>ItemStack</code>
	 * 
	 * @param item
	 *            The <code>ItemStack</code> to check
	 * @return Map<enchantmentID, enchantmentLevel>
	 */
	public static Map<Short, Short> getCustomEnchantmentList(ItemStack item) {
		return getCustomEnchantmentList(item, false);
	}

	public static void setFactoryInstance(ListenerRegistrationFactory factoryI) {
		factory = factoryI;
	}

	public static Map<Short, Short> getCustomEnchantmentList(ItemStack item,
			boolean rewrite) {

		// Map<enchantmentID, enchantmentLevel>
		Map<Short, Short> customEnchant = new HashMap<Short, Short>();

		if (item == null)
			return customEnchant;

		// //If it doesn't have a tag, return
		// error in case of mob damage player && workexplosion doesn't
		// NBTContainerItem container = new NBTContainerItem(item);

		if (!isEnchantable(item))
			return customEnchant;
		//
		// try {
		// if(container.getTag()==null) return customEnchant;
		// }
		// catch(Throwable e) {
		// return customEnchant;
		// }
		//
		//
		//
		// //test if it have already "customenchant" tag
		// NBTTagList existingCustomEnchant = (NBTTagList)
		// getCompoundFromString(container,"customenchant");
		//
		// if (existingCustomEnchant == null) return customEnchant;
		//
		// for (int i=0;i<existingCustomEnchant.size();i++) {
		// NBTTagCompound enchant = (NBTTagCompound)
		// existingCustomEnchant.get(i);
		// customEnchant.put(enchant.getShort("id"),enchant.getShort("lvl"));
		// }

		NBTTagList Lore = getLore(item);

		for (int i = 0; i < Lore.size(); i++) {

			NBTTagString loreCompound = (NBTTagString) Lore.get(i);

			if (loreCompound.get().startsWith(CEPREFIX)) {
				customEnchant = deserialize(loreCompound.get());

				if (rewrite)
					rewriteLore(item, customEnchant);
				return customEnchant;
			}
		}

		return customEnchant;
	}

	/**
	 * Test if the item is legally enchantable (by legally, we mean, for our
	 * plugin).
	 * 
	 * @param item
	 * @return
	 */
	public static boolean isEnchantable(ItemStack item) {

		@SuppressWarnings("deprecation")
		int itemId = item.getTypeId();

		for (int[] list : enchantableItems) {
			for (int id : list) {
				if (id == itemId)
					return true;
			}
		}
		return false;
	}

	/**
	 * Transform a Map of customs enchantments into a string like
	 * <code>§1§-§2§;§7§-§9§;</code>
	 * 
	 * @param customEnchants
	 * @return
	 */
	private static String serialize(Map<Short, Short> customEnchants) {
		String tempResult = "";
		for (short id : customEnchants.keySet()) {
			tempResult += id + "-" + customEnchants.get(id) + ";";
		}

		String result = "";
		for (short i = 0; i < tempResult.length(); ++i) {
			result += "§" + tempResult.substring(i, i + 1);

		}

		return CEPREFIX + result;
	}

	/**
	 * Transform a string encode in a certain pattern into a Map.
	 * 
	 * @param s
	 * @return
	 */
	private static Map<Short, Short> deserialize(String s) {

		Map<Short, Short> result = new HashMap<Short, Short>();
		try {
			String tempResult = s.replaceAll(CEPREFIX, "");
			tempResult = tempResult.replaceAll("§", "");

			// PluginLoader.pluginLoader.getLogger().log(Level.INFO,tempResult);

			for (String subS : tempResult.split(";")) {
				String[] temp = subS.split("-");
				short id = Short.parseShort(temp[0]);
				short value = Short.parseShort(temp[1]);
				result.put(id, value);
			}
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * Add the enchantment's ID & Level to the given <code>ItemStack</code>'s
	 * NBT Storage. Then add the awesome lore.
	 * 
	 * @param item
	 *            The <code>ItemStack</code> to enchant
	 * @param enchantment
	 *            The enchantment that will be added to the
	 * @param cost
	 *            The enchantment cost
	 */
	public static void addCustomEnchant(ItemStack item,
			IEnchantment enchantment, int cost) {
		// Get an enchantment level
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
	public static void addCustomEnchantWithLevel(ItemStack item,
			IEnchantment enchantment, short level) {

		Map<Short, Short> enchants = getCustomEnchantmentList(item, false);

		// erase previous enchant if it already exists
		if (enchants.containsKey(enchantment.getId())) {
			modifyCustomEnchantByUsingLevel(item, enchantment, level);
		} else {
			// Add the corresponding lore
			String romanLevel = level > 3999 ? Integer.toString(level)
					: new RomanNumeral(level).toString();
			addLoreToItem(item, ChatColor.GRAY, enchantment, romanLevel
					+ CEPREFIX);
		}

		String oldEnchants = serialize(enchants);

		// We use the non-redondancy property of map to erase all old keys.
		enchants.put(enchantment.getId(), level);

		writeEnchants(item, enchants, oldEnchants);

	}

	/**
	 * Rewrite the lore to remove unused enchants.
	 * 
	 * @param itemStack
	 * @param customEnchant
	 */
	public static void rewriteLore(ItemStack itemStack,
			Map<Short, Short> enchants) {

		// try {
		// throw new Exception();
		// }catch(Exception e) {
		// PluginLoader.pluginLoader.getLogger().info("@@"+e.getStackTrace()[0]+"@@"+e.getStackTrace()[1]+"@@"+e.getStackTrace()[2]+"@@"+e.getStackTrace()[3]);
		// }

		// PluginLoader.pluginLoader.getLogger().info("fdugfdugyfds"+enchants.size());
		// Get the NBT version of the item

		NBTTagList Lore = getLore(itemStack);


		// Do not remove customs lore.
		List<Integer> loreToRemoveIndex = new ArrayList<Integer>();
		String savedEnchants = null;
		for (int i = 0; i < Lore.size(); i++) {

			NBTTagString loreCompound = (NBTTagString) Lore.get(i);
			// Remove tagged lore
			if (loreCompound.get().endsWith(CEPREFIX)) {
				loreToRemoveIndex.add(i);
			}
			if (loreCompound.get().startsWith(CEPREFIX)) {
				savedEnchants = loreCompound.get();
			}
		}

		for (int i = loreToRemoveIndex.size() - 1; i > -1; i--) {
			Lore.remove(loreToRemoveIndex.get(i));
		}

		setLore(itemStack, Lore);

		for (IEnchantment enchant : factory.getEnchantmentList()) {
			if (enchants.keySet().contains(enchant.getId())) {
				addCustomEnchantWithLevel(itemStack, enchant,
						enchants.get(enchant.getId()));
			}
		}

		// rewrite old enchants
		Lore = getLore(itemStack);
		Lore.set(Lore.size() - 1, new NBTTagString(savedEnchants));
		setLore(itemStack, Lore);
	}

	/**
	 * Write a new lore to save custom enchants.
	 * 
	 * @param item
	 * @param enchants
	 * @param oldEnchants
	 */
	private static void writeEnchants(ItemStack item,
			Map<Short, Short> enchants, String oldEnchants) {
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
	public static void modifyCustomEnchantByUsingLevel(ItemStack item,
			IEnchantment enchantment, short level) {

		Map<Short, Short> enchants = getCustomEnchantmentList(item);

		String oldRomanLevel = null;
		for (short id : enchants.keySet()) {
			if (id == enchantment.getId()) {
				// get the old lore
				oldRomanLevel = new RomanNumeral(enchants.get(id)).toString();
			}
		}

		// Modify the corresponding lore
		String romanLevel = level > 3999 ? Integer.toString(level)
				: new RomanNumeral(level).toString();
		delItemLore(item, ChatColor.GRAY + enchantment.getName() + " "
				+ oldRomanLevel + CEPREFIX);
		addLoreToItem(item, ChatColor.GRAY, enchantment, romanLevel + CEPREFIX);
	}

	// /**
	// * Return the customenchant NBTTag
	// *
	// * @param container
	// * @return
	// */
	// private static NBTBase getCompoundFromString(NBTContainerItem container,
	// String query) {
	// return container.getCustomTag(NBTQuery.fromString(query));
	// }

	/**
	 * Try to remove <code>loreToRemove</code> from <code>itemStack</code> Lore.
	 * 
	 * @param itemStack
	 * @param loreToRemove
	 */
	public static void delItemLore(ItemStack itemStack, String loreToRemove) {

		NBTTagList Lore = getLore(itemStack);

		int loreToRemoveIndex = -1;
		for (int i = 0; i < Lore.size(); i++) {

			NBTTagString loreCompound = (NBTTagString) Lore.get(i);
			// PluginLoader.pluginLoader.getLogger().log(Level.INFO,loreCompound.get()+"---"+loreToRemove);
			if (loreCompound.get().equals(loreToRemove)) {
				loreToRemoveIndex = i;
				break;
			}
		}

		if (loreToRemoveIndex != -1)
			Lore.remove(loreToRemoveIndex);

		setLore(itemStack, Lore);
	}

	private static void setLore(ItemStack itemStack, NBTTagList lore) {
		// Get the NBT version of the item
		NBTContainerItem container = new NBTContainerItem(itemStack);

		// If it doesn't have any tag, create it.
		if (container.getTag() == null)
			container.setTag(new NBTTagCompound("tag"));

		NBTTagCompound display = container.getTag().getCompound("display");
		if (display == null)
			display = new NBTTagCompound("display");
		
		display.set("Lore", lore);
		container.setCustomTag(NBTQuery.fromString("display"), display);
	}

	private static NBTTagList getLore(ItemStack itemStack) {

		// Get the NBT version of the item
		NBTContainerItem container = new NBTContainerItem(itemStack);

		// If it doesn't have any tag, create it.
		if (container.getTag() == null)
			container.setTag(new NBTTagCompound("tag"));

		NBTTagCompound display = container.getTag().getCompound("display");
		if (display == null)
			display = new NBTTagCompound("display");

		NBTTagList Lore = display.getList("Lore");
		if (Lore == null)
			Lore = new NBTTagList("Lore");

		return Lore;
	}

	/**
	 * Add a new lore line to the given <code>ItemStack</code>, based on an
	 * enchantment
	 * 
	 * @param itemStack
	 *            The <code>ItemStack</code> that will receive a new lore line
	 * @param loreColor
	 *            The color of the new lore line
	 * @param enchantment
	 *            The enchantment's name that will be added to the
	 *            <code>ItemStack</code>
	 * @param enchantmentLevel
	 *            The enchantment level that will be displayed next to the lore
	 *            text
	 */
	public static void addLoreToItem(ItemStack itemStack, ChatColor loreColor,
			IEnchantment enchantment, String enchantmentLevel) {
		addLoreToItem(itemStack, loreColor + enchantment.getName() + " "
				+ enchantmentLevel);
	}

	/**
	 * Add a new lore line to the given <code>ItemStack</code>, based on raw
	 * <code>String</code>
	 * 
	 * @param itemStack
	 *            The <code>ItemStack</code> that will receive a new lore line
	 * @param rawTextToAdd
	 */
	public static void addLoreToItem(ItemStack itemStack, String rawTextToAdd) {
		NBTTagList Lore = getLore(itemStack);


		NBTTagString loreToadd = new NBTTagString("", rawTextToAdd);
		Lore.add(loreToadd);
		setLore(itemStack, Lore);
	}

	public static Map<IEnchantment, Short> getTotalArmorEnchantmentsLevels(
			ItemStack[] playerArmor, List<IEnchantment> enchantmentList) {
		Map<IEnchantment, Short> finalLevels = new HashMap<IEnchantment, Short>();

		for (IEnchantment actualEnchantment : enchantmentList) {
			short totalEnchantmentLevels = 0;

			for (ItemStack actualArmorPart : playerArmor) {
				Map<Short, Short> actualPartEnchantments = EnchantmentHelper
						.getCustomEnchantmentList(actualArmorPart);

				if (!actualPartEnchantments.containsKey(actualEnchantment
						.getId())) {
					continue;
				}
				totalEnchantmentLevels += actualPartEnchantments
						.get(actualEnchantment.getId());
			}

			if (totalEnchantmentLevels != 0) {
				finalLevels.put(actualEnchantment, totalEnchantmentLevels);
			}
		}

		return finalLevels;
	}

}
