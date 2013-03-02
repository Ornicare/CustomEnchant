package fr.enchantments.custom.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dpohvar.powernbt.nbt.NBTContainerItem;
import me.dpohvar.powernbt.nbt.NBTTagCompound;
import me.dpohvar.powernbt.nbt.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.enchantments.custom.model.IEnchantment;




public abstract class EnchantementHelper {
	
	/**
	 * Check if <code>itemStack</code>have at least one new enchant.
	 * 
	 * @param itemStack
	 * @return true if <code>itemStack</code>have at least one new enchant.
	 */
	public static boolean haveSpecificEnchant(ItemStack itemStack) {
		// TODO Auto-generated method stub
		return !getCustomEnchantmentList(itemStack).isEmpty();
	}
	
	public static Map<Short, Short> getCustomEnchantmentList(ItemStack item) {
		//get the minecraft itemStack version
		net.minecraft.server.v1_4_R1.ItemStack minecraftItemStack = CraftItemStack.asNMSCopy(item);
		
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
		} catch (NullPointerException e) {

		}
		return customEnchant;
	}
	
	public static void addCustomEnchant(ItemStack item, IEnchantment enchantment, short level) {
		//get the nbt version of the item
		NBTContainerItem con = new NBTContainerItem(item);
		
		//create the nbt version of the enchantment
		NBTTagCompound enchantmentNBT  = new me.dpohvar.powernbt.nbt.NBTTagCompound();
			enchantmentNBT.set("id", new me.dpohvar.powernbt.nbt.NBTTagShort(enchantment.getId()));
			enchantmentNBT.set("lvl", new me.dpohvar.powernbt.nbt.NBTTagShort(level));
			
		//If it doesn't have any tag, create it.
		if(con.getTag()==null) con.setTag(new me.dpohvar.powernbt.nbt.NBTTagCompound("tag"));
		
		//test if it have already customenchant tag
		NBTTagList existingCustomEnchant = con.getTag().getList("customenchant");
		if (existingCustomEnchant == null) {
			existingCustomEnchant = new NBTTagList("customenchant");
		}
		
		//add and save it
		existingCustomEnchant.add(enchantmentNBT);
		con.getTag().set("customenchant",existingCustomEnchant);
		
		//And now add the corresponding lore
		ItemMeta im1 = item.getItemMeta();
        List<String> lore1 = im1.getLore();
        if(lore1==null) lore1 = new ArrayList<String>();
        lore1.add(ChatColor.GRAY + enchantment.getName()+" "+(new RomanNumeral(level)).toString());
        im1.setLore(lore1);
        item.setItemMeta(im1);
	}
	
	
}
