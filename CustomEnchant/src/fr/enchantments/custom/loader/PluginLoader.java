package fr.enchantments.custom.loader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import fr.enchantments.custom.commands.AddEnchantCommand;
import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.implementation.DirectExplosion;
import fr.enchantments.custom.implementation.ProjectileExplosion;
import fr.enchantments.custom.listener.ActionListener;

public class PluginLoader extends JavaPlugin {

    private Logger pluginLogger;
    private FileConfiguration config;
    private ProtocolManager protocolManager;
    private ListenerRegistrationFactory factory;

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
        pluginLogger  = this.getLogger();
        config = this.getConfig();

        //Create the factory
        factory = new ListenerRegistrationFactory();
        
        // 2] Initialize Enchantments Classes/Instances
        pluginLogger.log(Level.INFO, "Enchantments' loading");
        
        factory.registerEnchantment(new DirectExplosion("Explosive", 0, 2));
        factory.registerEnchantment(new ProjectileExplosion("Explosive", 1, 2));

        // 2] Initialize Hookers & Blabla
        pluginLogger.log(Level.INFO, "Hook's loading");
        this.getServer().getPluginManager().registerEvents(new ActionListener(this), this);

        //Register debug commands
        pluginLogger.log(Level.INFO, "Commands' registration");
        this.getCommand("addenchant").setExecutor(new AddEnchantCommand(this));
    }
    
    public Logger getPluginLogger() {
		return pluginLogger;
	}

	public ListenerRegistrationFactory getFactory() {
    	return factory;
    }

}
