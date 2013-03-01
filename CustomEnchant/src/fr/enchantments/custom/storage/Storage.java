package fr.enchantments.custom.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public abstract class Storage {
	public static Map<UUID,ItemStack> ARROWOWNER = new HashMap<UUID,ItemStack>();
}
