package fr.enchantments.custom.model;

import net.minecraft.server.v1_6_R3.Item;

public abstract class EnchantablesItems {
	public static int[] bow = { Item.BOW.id };
	public static int[] swords = { Item.WOOD_SWORD.id, Item.STONE_SWORD.id,
			Item.IRON_SWORD.id, Item.DIAMOND_SWORD.id, Item.GOLD_SWORD.id };
	public static int[] axes = { Item.WOOD_AXE.id, Item.STONE_AXE.id,
			Item.IRON_AXE.id, Item.DIAMOND_AXE.id, Item.GOLD_AXE.id };
	public static int[] pickaxes = { Item.WOOD_PICKAXE.id, Item.STONE_PICKAXE.id,
			Item.IRON_PICKAXE.id, Item.DIAMOND_PICKAXE.id, Item.GOLD_PICKAXE.id };
	public static int[] special = { Item.SNOW_BALL.id };
	public static int[] armors = { Item.LEATHER_BOOTS.id,
			Item.LEATHER_CHESTPLATE.id, Item.LEATHER_HELMET.id,
			Item.LEATHER_LEGGINGS.id, Item.IRON_BOOTS.id, Item.IRON_BOOTS.id,
			Item.IRON_HELMET.id, Item.IRON_LEGGINGS.id, Item.GOLD_BOOTS.id,
			Item.GOLD_CHESTPLATE.id, Item.GOLD_HELMET.id,
			Item.GOLD_LEGGINGS.id, Item.CHAINMAIL_BOOTS.id,
			Item.CHAINMAIL_CHESTPLATE.id, Item.CHAINMAIL_HELMET.id,
			Item.CHAINMAIL_LEGGINGS.id, Item.DIAMOND_BOOTS.id,
			Item.DIAMOND_CHESTPLATE.id, Item.DIAMOND_HELMET.id,
			Item.DIAMOND_LEGGINGS.id };
	public static int[] interact = { Item.BOOK.id };
}
