package fr.enchantments.custom.loader;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.space.proxy.InstanceHandler;

import fr.enchantments.custom.commands.AddEnchantCommand;
import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.helper.GlowingHelper;
import fr.enchantments.custom.listener.ActionListener;
import fr.enchantments.custom.listener.EnchantmentListener;
import fr.enchantments.custom.model.IArmorDeathEnchantment;
import fr.enchantments.custom.model.IArmorHitEnchantment;
import fr.enchantments.custom.model.IDirectHitEnchantment;
import fr.enchantments.custom.model.IEnchantment;
import fr.enchantments.custom.model.IInteractEnchantment;
import fr.enchantments.custom.model.IZoneEffectEnchantment;

public class PluginLoader extends JavaPlugin {

	private Logger pluginLogger;
	@SuppressWarnings("unused")
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
				
				//TODO add info porvenant de la config + constructeur complet Deuxième arg : le calculer (on ne vas pas demander à l'user le numéro d'enchant qu'il souhaite...
				Class<?>[] classes = {String.class, short.class, int.class, boolean.class};
				String name = config.getProperty("name");
				short maxLevel = (short)Short.parseShort(config.getProperty("maxLevel"));
				int weight = (int)Integer.parseInt(config.getProperty("weight"));
				boolean isLegit = (boolean)Boolean.parseBoolean(config.getProperty("legit"));
		        Proxy proxy = (Proxy)enchantsPluginsloader.getPluginUsingConstructor(enchant, classes, name, maxLevel, weight, isLegit);
		        InvocationHandler ih = Proxy.getInvocationHandler(proxy);
				Class<?> handledClass = ((InstanceHandler) ih).getHandledClass();
		        List<Class<?>> interfaces = new ArrayList<Class<?>>(Arrays.asList(handledClass.getInterfaces()));
				
				IEnchantment enchantImpl = null;
				//TODO It's here we add new enchants type. Some plugins cannot implements multiples enchants. (risk : some buggy woody)
				Class<?>[] legalEnchantClasses = {IArmorDeathEnchantment.class, IArmorHitEnchantment.class, IDirectHitEnchantment.class, IInteractEnchantment.class, IZoneEffectEnchantment.class};
				List<Class<?>> test = new ArrayList<>(Arrays.asList(legalEnchantClasses));
				
//				ClassWrapper<?>[] test2 = {new ClassWrapper<IZoneEffectEnchantment>()};
				
				if(interfaces.contains(IArmorDeathEnchantment.class)) {
					enchantImpl = (IArmorDeathEnchantment) proxy;
				}
				else if(interfaces.contains(IArmorHitEnchantment.class)) {
					enchantImpl = (IArmorHitEnchantment) proxy;
				}
				else if(interfaces.contains(IDirectHitEnchantment.class)) {
					enchantImpl = (IDirectHitEnchantment) proxy;
				}
				else if(interfaces.contains(IInteractEnchantment.class)) {
					enchantImpl = (IInteractEnchantment) proxy;
				}
				else if(interfaces.contains(IZoneEffectEnchantment.class)) {
					enchantImpl = (IZoneEffectEnchantment) proxy;
				}
				
//				enchantImpl = test2[0].cast(proxy);
				
				factory.registerEnchantment(enchantImpl);
			}
		} catch (Exception e) {
			getLogger().severe("Error while loading enchants ! Aborting.");
			e.printStackTrace();
			return;
		}
		/////////////!!!!!!!!!!!!!.///////////////////////

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
