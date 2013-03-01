package fr.enchantments.custom.loader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginLoader extends JavaPlugin
{

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
        LOGGER = this.getLogger();
        CONFIG = this.getConfig();

        //Loading message
        LOGGER.log(Level.INFO, CONFIG.getString("Loading_Message"));

        //this.getServer().getPluginManager().registerEvents(new DamageHandler(this), this);
    }

}
