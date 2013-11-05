package fr.enchantments.custom.loader;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.space.plugin.PluginExternalizer;

import fr.enchantments.custom.commands.AddEnchantCommand;
import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.helper.GlowingHelper;
import fr.enchantments.custom.implementation.ArmorDeath_DeathDetector;
import fr.enchantments.custom.implementation.ArmorHit_ShootDetector;
import fr.enchantments.custom.implementation.DirectHit_Rider;
import fr.enchantments.custom.implementation.DirectHit_SpinUp;
import fr.enchantments.custom.implementation.DirectHit_Starvation;
import fr.enchantments.custom.implementation.Projectile_AdminExplosion;
import fr.enchantments.custom.implementation.Projectile_BaseBowL;
import fr.enchantments.custom.implementation.Projectile_BlackHole;
import fr.enchantments.custom.implementation.Projectile_BlocDispatcher;
import fr.enchantments.custom.implementation.Projectile_BlockExchanger;
import fr.enchantments.custom.implementation.Projectile_BlowMobs;
import fr.enchantments.custom.implementation.Projectile_Compressor;
import fr.enchantments.custom.implementation.Projectile_DestructFoundations;
import fr.enchantments.custom.implementation.Projectile_FreezingArc;
import fr.enchantments.custom.implementation.Projectile_FreezingExplosion;
import fr.enchantments.custom.implementation.Projectile_FreezingShard;
import fr.enchantments.custom.implementation.Projectile_FreezingWater;
import fr.enchantments.custom.implementation.Projectile_Kick;
import fr.enchantments.custom.implementation.Projectile_OmgWTFPop;
import fr.enchantments.custom.implementation.Projectile_OmgWTFPopLessRandom;
import fr.enchantments.custom.implementation.Projectile_OmgWTFPopRebond;
import fr.enchantments.custom.implementation.Projectile_OmgWTFPopTrainee;
import fr.enchantments.custom.implementation.Projectile_Rebond;
import fr.enchantments.custom.implementation.Projectile_Starvation;
import fr.enchantments.custom.implementation.legal.LegalDirectHit_Explosion;
import fr.enchantments.custom.implementation.legal.LegalDirectHit_Poison;
import fr.enchantments.custom.implementation.legal.LegalInteract_MegaJump;
import fr.enchantments.custom.implementation.legal.LegalProjectile_BlackHole;
import fr.enchantments.custom.implementation.legal.LegalProjectile_Explosion;
import fr.enchantments.custom.implementation.legal.LegalProjectile_PoisonExplosion;
import fr.enchantments.custom.listener.ActionListener;
import fr.enchantments.custom.listener.EnchantmentListener;
import fr.enchantments.custom.model.BaseEnchantment;
import fr.enchantments.custom.model.IEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class PluginLoader extends JavaPlugin {

	private Logger pluginLogger;
	private FileConfiguration config;
	public static ProtocolManager protocolManager;
	private ListenerRegistrationFactory factory;
	public static PluginLoader pluginLoader;

	/**
	 * Actions to perform on plugin load.
	 */
	public void onEnable() {
		pluginLoader = this;

		// Used to modify packets
		protocolManager = ProtocolLibrary.getProtocolManager();

		ProtocolLibrary.getProtocolManager().addPacketListener(
				new PacketAdapter(this, ConnectionSide.SERVER_SIDE,
						ListenerPriority.HIGH, Packets.Server.SET_SLOT,
						Packets.Server.WINDOW_ITEMS) {
					@Override
					public void onPacketSending(PacketEvent event) {
						if (event.getPacketID() == Packets.Server.SET_SLOT) {
							GlowingHelper
									.addGlowingEffect(new ItemStack[] { event
											.getPacket().getItemModifier()
											.read(0) });
						} else {
							GlowingHelper.addGlowingEffect(event.getPacket()
									.getItemArrayModifier().read(0));
						}
					}
				});

		// 1] If the config file doesn't exists, create it.
		this.saveDefaultConfig();

		// 2] Registering the logger and its config file
		pluginLogger = this.getLogger();
		config = this.getConfig();

		// Create the factory
		factory = new ListenerRegistrationFactory();

		// 2] Initialize Enchantments Classes/Instances
		pluginLogger.log(Level.INFO, "Enchantments' loading");
		
		
		/////////////!!!!!!!!!!!!!.///////////////////////
		try {
			String folder = "plugins/CustomEnchant/enchantments";
			PluginExternalizer enchantsPluginsloader = new com.space.main.PluginLoader().createPluginManager(folder);
			ArrayList<String> enchants = enchantsPluginsloader.getPluginList();
			for(String enchant : enchants) {
				Properties config = new Properties();
				String path = enchantsPluginsloader.getPluginPath(enchant);
				path = path.substring(0,path.length()-5);
				config.load(new FileInputStream(new File(path+"/enchant.properties")));
				
				IEnchantment enchantImpl = null;
				//TODO add enchant type
				switch (config.getProperty("type")) {
				case "ZoneEffect":
					Class<?>[] classes = {String.class, short.class, short.class};
					//TODO add info porvenant de la config + constructeur complet Deuxième arg : le calculer (on ne vas pas demander à l'user le numéro d'enchant qu'il souhaite...
					enchantImpl = (IZoneEffectEnchantment) enchantsPluginsloader.getPluginUsingConstructor(enchant, classes, "Test", (short)0, (short)10);
					break;

				default:
					break;
				}
				factory.registerEnchantment(enchantImpl);
			}
		} catch (Exception e) {
			getLogger().severe("Error while loading enchants ! Aborting.");
			e.printStackTrace();
			return;
		}
		/////////////!!!!!!!!!!!!!.///////////////////////

		// Legal
		factory.registerEnchantment(new LegalDirectHit_Explosion("Explosive",
				0, 4, 10, true));
		factory.registerEnchantment(new LegalProjectile_Explosion("Explosion",
				15, 2, 10, true));
		factory.registerEnchantment(new LegalDirectHit_Poison("Poison", 25, 5,
				100, true));
		factory.registerEnchantment(new LegalProjectile_PoisonExplosion("Poison Explosion", 26, 10,
				50, true));
		factory.registerEnchantment(new LegalInteract_MegaJump("Mega Jump", 27, 3,
				5, true));
		factory.registerEnchantment(new LegalProjectile_BlackHole("Black Hole", 28, 20,
				5, true));
		
		// Admin
		factory.registerEnchantment(new Projectile_AdminExplosion(
				"AdminExplosive", 1, 2));
		factory.registerEnchantment(new Projectile_BaseBowL("Base-Bow-L", 2, 1));
		factory.registerEnchantment(new Projectile_OmgWTFPop("oMg PoP", 3, 1));
		factory.registerEnchantment(new Projectile_BlocDispatcher(
				"Blocks dispatchers", 4, 1));
		factory.registerEnchantment(new Projectile_BlockExchanger(
				"Blocks exchanger", 5, 1));
		factory.registerEnchantment(new Projectile_Compressor("Compressor", 6,
				10));
		factory.registerEnchantment(new DirectHit_SpinUp("Spin up", 7, 10));
		factory.registerEnchantment(new DirectHit_Starvation("Starvation", 8,
				10));
		factory.registerEnchantment(new Projectile_Starvation("Starvation", 9,
				10));
		factory.registerEnchantment(new Projectile_Kick("Kick", 10, 10));
		factory.registerEnchantment(new DirectHit_Rider("Rider", 11, 1));
		factory.registerEnchantment(new Projectile_DestructFoundations(
				"Destruct Foundations", 12, 1));
		factory.registerEnchantment(new ArmorHit_ShootDetector(
				"Shoot Detector", 13, 1));
		factory.registerEnchantment(new ArmorDeath_DeathDetector(
				"Die Detector", 14, 1));

		factory.registerEnchantment(new Projectile_FreezingExplosion(
				"Freezing explosion", 16, 2));
		factory.registerEnchantment(new Projectile_FreezingShard(
				"Freezing shard", 17, 2));
		factory.registerEnchantment(new Projectile_FreezingArc("Freezing arc",
				18, 2));
		factory.registerEnchantment(new Projectile_OmgWTFPopTrainee("Pop arc",
				19, 2));
		factory.registerEnchantment(new Projectile_OmgWTFPopLessRandom(
				"Realistic explosion", 20, 2));
		factory.registerEnchantment(new Projectile_FreezingWater("Jesus ", 21,
				2));
		factory.registerEnchantment(new Projectile_Rebond("Rebond", 22, 2));
		factory.registerEnchantment(new Projectile_OmgWTFPopRebond(
				"Explosive rebond", 23, 2));
		factory.registerEnchantment(new Projectile_BlowMobs("Blow", 24, 2));
		factory.registerEnchantment(new Projectile_BlackHole("Perpetual Black Hole", 29, 20,
				5, false));

		// Stop the registration
		factory.stopRegistration();

		// 2] Initialize Hookers & Blabla
		pluginLogger.log(Level.INFO, "Hook's loading");
		this.getServer().getPluginManager()
				.registerEvents(new ActionListener(this), this);
		this.getServer()
				.getPluginManager()
				.registerEvents(
						new EnchantmentListener(factory.getEnchantmentFactory()),
						this);

		// Register debug commands
		pluginLogger.log(Level.INFO, "Commands' registration");
		if (this.getCommand("addenchant") == null)
			pluginLogger.log(Level.SEVERE,
					"Fatal error, suspending plugin execution.");
		this.getCommand("addenchant").setExecutor(new AddEnchantCommand(this));
	}

	public Logger getPluginLogger() {
		return pluginLogger;
	}

	public ListenerRegistrationFactory getFactory() {
		return factory;
	}

	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}

}
