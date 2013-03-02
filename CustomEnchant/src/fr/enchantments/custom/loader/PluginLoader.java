package fr.enchantments.custom.loader;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.enchantments.custom.commands.AddEnchantCommand;
import fr.enchantments.custom.commands.TestCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.implementation.DirectExplosion;
import fr.enchantments.custom.implementation.ProjectileExplosion;
import fr.enchantments.custom.listener.ActionListener;

public class PluginLoader extends JavaPlugin {

    public static Logger LOGGER;
    public static FileConfiguration CONFIG;
    public static ProtocolManager protocolManager;

    /**
     * Actions to perform on plugin load.
     */
    public void onEnable()
    {
    	//Used to modify packets
        protocolManager = ProtocolLibrary.getProtocolManager();

        // 1] If the config file doesn't exists, create it.
        this.saveDefaultConfig();

        // 2] Registering the logger and its config file
        LOGGER  = this.getLogger();
        CONFIG = this.getConfig();

        // 2] Initialize Enchantments Classes/Instances
        LOGGER.log(Level.INFO, "Initialisation des enchantments...");
        ListenerRegistrationFactory.initializeListenerFactory();
        ListenerRegistrationFactory.listenerFactory.registerEnchantment(new DirectExplosion("Explosive", 0, 2));
        ListenerRegistrationFactory.listenerFactory.registerEnchantment(new ProjectileExplosion("Explosive", 1, 2));
        LOGGER.log(Level.INFO, "Initialisation des enchantments terminée !");

        // 2] Initialize Hookers & Blabla
        LOGGER.log(Level.INFO, "Initialisation des hookers...");
        this.getServer().getPluginManager().registerEvents(new ActionListener(this), this);
        LOGGER.log(Level.INFO, "Initialisation des hookers terminée !");

        this.getCommand("setitemname").setExecutor(new TestCommand(this));
        this.getCommand("addenchant").setExecutor(new AddEnchantCommand(this));
    }

}
