package fr.ornicare.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dpohvar.powernbt.nbt.NBTContainerItem;
import net.minecraft.server.v1_4_R1.ItemStack;
import net.minecraft.server.v1_4_R1.NBTTagCompound;
import net.minecraft.server.v1_4_R1.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_4_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.PacketContainer;

import fr.ornicare.loader.CustomEnchantLoader;
import fr.ornicare.storage.Storage;
import fr.ornicare.util.RomanNumeral;

public class DamageHandler implements Listener {

	JavaPlugin plugin;

	public DamageHandler(JavaPlugin plugin) {
		this.plugin = plugin;
	}
	
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		Entity shooter = event.getEntity();
		if(shooter instanceof LivingEntity) {
			LivingEntity shooterLV = (LivingEntity) shooter;
			Storage.arrowAndBowCorrelation.put(event.getProjectile().getUniqueId(),  ((LivingEntity) shooterLV).getEquipment().getItemInHand());
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onProjectileHitEvent(ProjectileHitEvent event) {
		Projectile shooter = event.getEntity();
		ItemStack item;
		
		if(Storage.arrowAndBowCorrelation.containsKey(shooter.getUniqueId())) {
			item = CraftItemStack.asNMSCopy(Storage.arrowAndBowCorrelation.get(shooter.getUniqueId()));
			Map<Short, Short> enchList = getCustomEnchantmentList(item);
			if (enchList.containsKey((short) 1)) { 
				doFakeExplosion(shooter.getLocation(), enchList.get((short) 1));
				
				//for(int x = -1; x < 1; x++) {
					//for(int y = -1; y < 1; y++) {
							//Location tempLoc = shooter.getLocation().add(new Vector(x,y,0));
							//doFakeExplosion(tempLoc, enchList.get((short) 1));
					//}	
				//}
				
				int radius = 2 + enchList.get((short) 1);
				
				//determine arrow damage
				double damage = Math.max(1,(shooter.getVelocity().length()-1))*2 + Math.random();
				if(item.getEnchantments()!=null) {
					for(int i = 0; i<item.getEnchantments().size();i++) {
						if(((NBTTagCompound)item.getEnchantments().get(i)).getShort("id")==(short) 48) {
							damage=damage*1.5+0.25*(((NBTTagCompound)item.getEnchantments().get(i)).getShort("id")-1);
						}
					}
				}
				//plugin.getServer().broadcastMessage(""+damage);
				
				/*//in order to create the explosion, report it to the neraby in range entity.
				Entity nearbyEnt = null;
				double distanceNEnt = Double.MAX_VALUE;
				for (Entity ent : shooter.getNearbyEntities(radius, radius,
						radius)) {
					if (ent instanceof LivingEntity) {
						double distance = Math.max(shooter.getLocation().distance(ent.getLocation()), 1);
						if(nearbyEnt==null || distance<distanceNEnt) {
							nearbyEnt = ent;
							distanceNEnt = distance;
						}
						
					}
				}*/
				
				//The arrow explode.
				onEntityDamage(new EntityDamageByEntityEvent(shooter, shooter, DamageCause.PROJECTILE, (int) damage));
				
				shooter.remove();
				
				
				
			}
		}
		
		
		
	}
	
	
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.isCancelled()) {
			return;
		}

		Entity damager = processDamageEvent(event);
		
		// String name;
		// String dname;
		if (damager != null && (event.getEntity() instanceof LivingEntity)
				&& ((damager instanceof LivingEntity)) || (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)) {
			
			ItemStack item;
			
			if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
				if(Storage.arrowAndBowCorrelation.containsKey(damager.getUniqueId())) {
					item = CraftItemStack.asNMSCopy(Storage.arrowAndBowCorrelation.get(damager.getUniqueId()));
					//plugin.getServer().broadcastMessage("2+"+event.getDamage());
				} 
				else {
					return;
				}	
			}
			else {
				item = CraftItemStack.asNMSCopy(((LivingEntity) damager).getEquipment().getItemInHand());
			}
				
			// name = event.getEntity().getClass().getSimpleName();
			// dname = damager.getClass().getSimpleName();
			
			Map<Short, Short> enchList = getCustomEnchantmentList(item);
			Entity baseEnt = event.getEntity();
			
	
			// for(Short i : enchList.keySet())
			// CustomEnchantLoader.LOGGER.severe(i+"  "+enchList.get(i));

			try {
				LivingEntity damaged = (LivingEntity) baseEnt;
				if (enchList.containsKey((short) 0)) {
					damaged.addPotionEffect(new PotionEffect(
							PotionEffectType.POISON, 100 * enchList.get((short) 0),
							enchList.get((short) 0)));
				}
			}
			catch(Exception e) {}
			if (enchList.containsKey((short) 1)) {
				int radius = 2 + enchList.get((short) 1);
				for (Entity ent : baseEnt.getNearbyEntities(radius, radius,
						radius)) {
					if (ent instanceof LivingEntity) {
						LivingEntity entLV = (LivingEntity) ent;
						if (!entLV.equals(baseEnt) && !entLV.equals(damager)) {
							double distance = Math.max(baseEnt.getLocation()
									.distance(entLV.getLocation()), 1);
							entLV.damage((int) (event.getDamage() / distance));
							/*// trouver solution
							if (enchList.containsKey((short) 0)) {
								entLV.addPotionEffect(new PotionEffect(
										PotionEffectType.POISON,
										(int) (100 * enchList.get((short) 0) / distance),
										enchList.get((short) 0) - 1));
							}*/
						}
					}
				}

				

				// create a fake explosion paquet
				doFakeExplosion(baseEnt.getLocation(), enchList.get((short) 1));
				
			}
			// plugin.getServer().broadcastMessage(dname + " a tapé " + name);
		}
	}

	/*private void doFakeExplosion(Entity damaged, int radius) {
		PacketContainer fakeExplosion = CustomEnchantLoader.protocolManager.createPacket(Packets.Server.EXPLOSION);
		fakeExplosion.getDoubles()
		.write(0, damaged.getLocation().getX())
		.write(1, damaged.getLocation().getY() + 1.25)
		.write(2, damaged.getLocation().getZ());
		fakeExplosion.getFloat().write(0,(float) radius);
		try {
			// Send it to all players within a 50 range
			for (Entity ent : damaged.getNearbyEntities(50, 50, 50)) {
				if (ent instanceof LivingEntity) {
					LivingEntity entLV = (LivingEntity) ent;
					if (entLV instanceof Player) {
						CustomEnchantLoader.protocolManager
								.sendServerPacket((Player) entLV,
										fakeExplosion);
					}
				}
			}
			damaged.getWorld().playSound(damaged.getLocation(),
					Sound.EXPLODE, 10, 1);
		} catch (Exception e) {
		}
		
	}*/
	
	private void doFakeExplosion(Location location, int radius) {
		PacketContainer fakeExplosion = CustomEnchantLoader.protocolManager.createPacket(Packets.Server.EXPLOSION);
		
		fakeExplosion.getDoubles()
		.write(0, location.getX())
		.write(1, location.getY() + 1.25)
		.write(2, location.getZ());
		fakeExplosion.getFloat().write(0,(float) radius);
		
		try {
			
			// Send it to all players within a 100 range
			for (Player player : plugin.getServer().getWorld(location.getWorld().getName()).getPlayers()) {

						if(player.getLocation().distance(location)<100) CustomEnchantLoader.protocolManager.sendServerPacket(player,fakeExplosion);
			}
			location.getWorld().playSound(location,
					Sound.EXPLODE, 10, 1);
		} catch (Exception e) {
		}
		
	}



	@EventHandler(priority = EventPriority.NORMAL)
	public void onEnchantItemEvent(EnchantItemEvent event) {

		/*
		 * int levelcost = event.getExpLevelCost(); ItemStack enchItem =
		 * ((CraftItemStack)(event.getItem())); //0.1 chance to get Poison
		 * enchant ! if(Math.random()<1.0) {
		 * //plugin.getServer().broadcastMessage("qsdss"); enchItem =
		 * addCustomEnchantment(enchItem,0,levelcost); } //0.02 chance to get
		 * explosiv enchant ! if(Math.random()<0.02) {
		 * addCustomEnchantment(enchItem,1,levelcost); }
		 */
		//CraftItemStack cis = (CraftItemStack) event.getItem();
		/*
		 * ItemStack itemStack = CraftItemStack.asNMSCopy(cis); itemStack.id=5;
		 * cis = CraftItemStack.asCraftMirror(itemStack);
		 */
		//cis.setTypeId(5);
		//ItemMeta itemMeta = null;
		//cis.setItemMeta(itemMeta);
		//nameitem(event.getItem(),"test");
		
		// Scroll of Invisibility
		/*org.bukkit.inventory.ItemStack is = event.getItem();
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Scroll of Invisibility");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GREEN + "Makes you invisible for 2:00");
        im.setLore(lore);
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(is);
        recipe.shape("  ", " *&", "  ");
        recipe.setIngredient('*', Material.DIAMOND);
        recipe.setIngredient('&', Material.PAPER);
        plugin.getServer().addRecipe(recipe);
           
        // Cure Scroll
        org.bukkit.inventory.ItemStack is1 = new org.bukkit.inventory.ItemStack(Material.PAPER, 1);
        ItemMeta im1 = is1.getItemMeta();
        im1.setDisplayName("Cure Scroll");
        ArrayList<String> lore1 = new ArrayList<String>();
        lore1.add(ChatColor.GREEN + "Restores health to 100%");
        im1.setLore(lore1);
        is1.setItemMeta(im1);
        ShapedRecipe recipe1 = new ShapedRecipe(is1);
        recipe1.shape("  ", "*&*", "  ");
        recipe1.setIngredient('*', Material.GOLDEN_APPLE);
        recipe1.setIngredient('&', Material.PAPER);
        plugin.getServer().addRecipe(recipe1); */
		/*
		 * ItemStack is = cis.getHandle(); NBTTagCompound itemCompound = new
		 * NBTTagCompound(); itemCompound = is.save(itemCompound);
		 */
		
		//get the newly enchant item.
		org.bukkit.inventory.ItemStack item = event.getItem();
		
		//get it's NBTData
		NBTContainerItem con = new NBTContainerItem(item);
		
		//If it doesn't have any tag, create it.
		if(con.getTag()==null) con.setTag(new me.dpohvar.powernbt.nbt.NBTTagCompound("tag"));
		
		//test if it have already customenchant
		me.dpohvar.powernbt.nbt.NBTTagList existingCustomEnchant = con.getTag().getList("customenchant");
		if (existingCustomEnchant == null) {
			
			/*me.dpohvar.powernbt.nbt.NBTTagList customEnch = new me.dpohvar.powernbt.nbt.NBTTagList("customenchant");
			
			customEnch.add(addCustomEnchant(customEnch,item, event.getExpLevelCost()));
			con.getTag().set("customenchant",customEnch);*/
			
			me.dpohvar.powernbt.nbt.NBTTagList customEnch = addCustomEnchant(new me.dpohvar.powernbt.nbt.NBTTagList("customenchant"),item, event.getExpLevelCost());
			con.getTag().set("customenchant",customEnch);
			
			///////
			/*me.dpohvar.powernbt.nbt.NBTTagList customEnch = new me.dpohvar.powernbt.nbt.NBTTagList("customenchant");
			me.dpohvar.powernbt.nbt.NBTTagCompound enchantment  = new me.dpohvar.powernbt.nbt.NBTTagCompound();
			enchantment.set("id", new me.dpohvar.powernbt.nbt.NBTTagShort((short)0));
			enchantment.set("lvl", new me.dpohvar.powernbt.nbt.NBTTagShort((short)1));
			customEnch.add(enchantment);
			con.getTag().set("customenchant",customEnch);*/
		}
		else {
			
			existingCustomEnchant=addCustomEnchant(existingCustomEnchant,item, event.getExpLevelCost());
			con.getTag().set("customenchant",existingCustomEnchant);
		}
		

	}
	
	private me.dpohvar.powernbt.nbt.NBTTagList addCustomEnchant(me.dpohvar.powernbt.nbt.NBTTagList enchantmentList, org.bukkit.inventory.ItemStack item, int cost) {
		
		//.1 chance to get poisonous weapon
		if(Math.random() < 0.1*cost/30*3) {
			me.dpohvar.powernbt.nbt.NBTTagCompound enchantment  = new me.dpohvar.powernbt.nbt.NBTTagCompound();
			enchantment.set("id", new me.dpohvar.powernbt.nbt.NBTTagShort((short)0));
			short level = (short)Math.max(cost*Math.random()/6.,1);
			enchantment.set("lvl", new me.dpohvar.powernbt.nbt.NBTTagShort(level));
			enchantmentList.add(enchantment);
			
			ItemMeta im1 = item.getItemMeta();
	        //im1.setDisplayName("Cure Scroll");
	        List<String> lore1 = im1.getLore();
	        if(lore1==null) lore1 = new ArrayList<String>();
	        lore1.add(ChatColor.GRAY + "Poisonous "+(new RomanNumeral(level)).toString());
	        im1.setLore(lore1);
	        item.setItemMeta(im1);
		}
		
		//0.02 chance to get explosive weapon (max level 2)
		if(Math.random() < 0.02*cost/30*5) {
			me.dpohvar.powernbt.nbt.NBTTagCompound enchantment  = new me.dpohvar.powernbt.nbt.NBTTagCompound();
			enchantment.set("id", new me.dpohvar.powernbt.nbt.NBTTagShort((short)1));
			short level = (short)Math.max(cost/10*Math.random(),1);
			enchantment.set("lvl", new me.dpohvar.powernbt.nbt.NBTTagShort(level));
			enchantmentList.add(enchantment);
			
			ItemMeta im1 = item.getItemMeta();
	        //im1.setDisplayName("Cure Scroll");
	        List<String> lore1 = im1.getLore();
	        if(lore1==null) lore1 = new ArrayList<String>();
	        lore1.add(ChatColor.GRAY + "Explosive "+(new RomanNumeral(level)).toString());
	        im1.setLore(lore1);
	        item.setItemMeta(im1);
		}
		
		return enchantmentList;
		
	}

	



	/*
	 * public Boolean isPoisonousWeapon(ItemStack item) { //ItemStack itemStack
	 * = CraftItemStack.asNMSCopy(item); try { if(item.hasTag()) {
	 * NBTTagCompound tag = item.tag; if (tag == null) { tag = new
	 * NBTTagCompound(); tag.setCompound("display", new NBTTagCompound());
	 * tag.getCompound("display").set("Lore", new NBTTagList()); item.tag = tag;
	 * }
	 * 
	 * /// short id; short lvl; if(tag.hasKey("customenchant")) { NBTTagList
	 * enchantList = tag.getList("customenchant"); if(enchantList!=null &&
	 * enchantList.size()>0) { NBTTagCompound ench = (NBTTagCompound)
	 * enchantList.get(0); id = ench.getShort("id"); lvl = ench.getShort("lvl");
	 * if(id == 0 && lvl == 1) return true; } }
	 * 
	 * } } catch(NullPointerException e) {
	 * 
	 * } return false; ///
	 * 
	 * tag = item.tag.getCompound("display"); NBTTagList list =
	 * tag.getList("Lore"); list.add(new NBTTagString("", ChatColor.RESET +
	 * lore)); tag.set("Lore", list); item.tag.setCompound("display", tag);
	 * return item; //return CraftItemStack.asCraftMirror(asItem); }
	 */

	/*
	 * private void addCustomEnchantment(ItemStack item, int i, int levelcost) {
	 * //50% to add the enchantment and 50% to replace existing enchantment.
	 * if(Math.random()<1.0) { if(item.hasTag()) {
	 * 
	 * if(!item.tag.hasKey("customenchant")) { item.tag.set("customenchant", new
	 * NBTTagList()); } } else { item.tag = new NBTTagCompound("tag");
	 * item.tag.set("customenchant", new NBTTagList()); } NBTTagList enchantList
	 * = item.tag.getList("customenchant"); NBTTagCompound enchantment = new
	 * NBTTagCompound(); enchantment.setShort("id", (short)0);
	 * enchantment.setShort("lvl", (short)1); enchantList.add(enchantment);
	 * item.tag.set("customenchant", enchantList); item.tag.set("sqsdqsdqdsq",
	 * new NBTTagCompound("zeze"));
	 * plugin.getServer().broadcastMessage("Poisonnn !"); item.save(item.tag); }
	 * else { //item.getEnchantments(); }
	 * 
	 * 
	 * }
	 */


	public Map<Short, Short> getCustomEnchantmentList(ItemStack item) {
		
		Map<Short, Short> customEnchant = new HashMap<Short, Short>();
		try {
			if (item.hasTag()) {
				NBTTagCompound tag = item.tag;
				if (tag.hasKey("customenchant")) {
					NBTTagList enchantList = tag.getList("customenchant");
					for (int i = 0; i < enchantList.size(); i++) {
						NBTTagCompound ench = (NBTTagCompound) enchantList
								.get(i);
						customEnchant.put(ench.getShort("id"),
								ench.getShort("lvl"));
					}
				}

			}
		} catch (NullPointerException e) {

		}
		return customEnchant;
	}

	public Entity processDamageEvent(EntityDamageEvent event) {

		Entity damager = null;
		if ((event instanceof EntityDamageByEntityEvent)) {
			EntityDamageByEntityEvent subEvent = (EntityDamageByEntityEvent) event;
			damager = subEvent.getDamager();
			/*if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
				damager = ((Projectile) damager).getShooter();*/
			return damager;
		}
		return null;
	}
}
