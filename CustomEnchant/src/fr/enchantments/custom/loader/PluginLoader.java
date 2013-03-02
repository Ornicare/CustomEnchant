package fr.enchantments.custom.loader;

import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.implementation.DirectExplosion;
import fr.enchantments.custom.implementation.ProjectileExplosion;
import fr.enchantments.custom.logger.LoggerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.enchantments.custom.listener.ActionListener;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoader extends JavaPlugin {

    public static Logger LOGGER;
    public static FileConfiguration CONFIG;
    // public static ProtocolManager protocolManager;

    /**
     * Actions to perform on plugin load.
     */
    public void onEnable()
    {
        // protocolManager = ProtocolLibrary.getProtocolManager();

        // 1] If the config file doesn't exists, create it.
        this.saveDefaultConfig();

        // 2] Registering the logger and its config file
        LoggerManager.setLogger(this.getLogger());
        CONFIG = this.getConfig();

        // 2] Initialize Enchantments Classes/Instances
        LoggerManager.log("Initialisation des enchantements...");
        ListenerRegistrationFactory.initializeListenerFactory();
        ListenerRegistrationFactory.listenerFactory.registerEnchantment(new DirectExplosion());
        ListenerRegistrationFactory.listenerFactory.registerEnchantment(new ProjectileExplosion());
        LoggerManager.log("Initialisation des enchantements terminée !");

        // 2] Initialize Hookers & Blabla
        LoggerManager.log("Initialisation des hookers...");
        this.getServer().getPluginManager().registerEvents(new ActionListener(this), this);
        LoggerManager.log("Initialisation des hookers terminée !");
    }

}
