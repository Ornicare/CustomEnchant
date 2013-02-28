package fr.ornicare.loader;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import fr.ornicare.handlers.DamageHandler;

/**
 * The main class of the plugin
 * 
 * @author Ornicare
 *
 */
public class CustomEnchantLoader extends JavaPlugin{
	
	
	
	public static Logger LOGGER;
	public static FileConfiguration CONFIG;
	public static ProtocolManager protocolManager;
	
	/**
	 * Actions to perform on plugin load.
	 */
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		
		/////////////
		
		
		
		/////////////
		
		// Disable all sound effects
		/*protocolManager.addPacketListener(
		  new PacketAdapter(this, ConnectionSide.SERVER_SIDE, 
		  ListenerPriority.NORMAL, Packets.Server.NAMED_SOUND_EFFECT) {
		    @Override
		    public void onPacketSending(PacketEvent event) {
		        // Item packets
		        switch (event.getPacketID()) {
		        case Packets.Server.NAMED_SOUND_EFFECT: // 0x3E
		            event.setCancelled(true);
		            break;
		        }
		    }
		});*/
		
		//If the config file doesn't exists, create it.
		this.saveDefaultConfig();

		// Registering the logger
		LOGGER = this.getLogger();
		
		// Registering the config file
		CONFIG = this.getConfig();

		//Loading message
		LOGGER.log(Level.INFO,CONFIG.getString("Loading_Message"));
		
		this.getServer().getPluginManager().registerEvents(new DamageHandler(this), this);
	}
}