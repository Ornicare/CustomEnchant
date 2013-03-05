package fr.enchantments.custom.loader;

import java.util.logging.Level;
import java.util.logging.Logger;

import fr.enchantments.custom.implementation.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import fr.enchantments.custom.commands.AddEnchantCommand;
import fr.enchantments.custom.factory.ListenerRegistrationFactory;
import fr.enchantments.custom.listener.ActionListener;

public class PluginLoader extends JavaPlugin {

    private Logger pluginLogger;
    private FileConfiguration config;
    public static ProtocolManager protocolManager;
    private ListenerRegistrationFactory factory;
    public static PluginLoader pluginLoader;

    /**
     * Actions to perform on plugin load.
     */
    public void onEnable()
    {
        pluginLoader = this;

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
        
        factory.registerEnchantment(new LegalDirectHit_Explosion("AdminExplosive", 0, 2));
        factory.registerEnchantment(new Projectile_AdminExplosion("AdminExplosive", 1, 2));
        factory.registerEnchantment(new Projectile_BaseBowL("Base-Bow-L", 2, 1));
        factory.registerEnchantment(new Projectile_OmgWTFPop("oMg PoP", 3, 1));
        factory.registerEnchantment(new Projectile_BlocDispatcher("Blocks dispatchers", 4, 1));
        factory.registerEnchantment(new Projectile_BlockExchanger("Blocks exchanger", 5, 1));
        factory.registerEnchantment(new Projectile_Compressor("Compressor", 6, 10));
        factory.registerEnchantment(new DirectHit_SpinUp("Spin up", 7, 10));
        factory.registerEnchantment(new DirectHit_Starvation("Starvation", 8, 10));
        factory.registerEnchantment(new Projectile_Starvation("Starvation", 9, 10));
        factory.registerEnchantment(new Projectile_Kick("Kick", 10, 10));
        factory.registerEnchantment(new DirectHit_Rider("Rider", 11, 1));
        factory.registerEnchantment(new Projectile_DestructFoundations("Destruct Foundations", 12, 1));
        factory.registerEnchantment(new ArmorHit_ShootDetector("Shoot Detector", 13, 1));
        factory.registerEnchantment(new ArmorDeath_DeathDetector("Die Detector", 14, 1));
        
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
