package com.space.plugin;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Commons methods for all plugins
 * @author Ornicare
 *
 */
public abstract class PluginCommonMethods {
	
	private static boolean stopRegistration = false;
	private static PluginManager pluginManager;
	
	/**
	 * Used to register the plugin manager. Dont' touch it !
	 * 
	 * @param pluginManager
	 */
	public static final void registerPluginManager(PluginManager pluginManager) {
		if(!stopRegistration) {
			PluginCommonMethods.pluginManager = pluginManager;
			stopRegistration = true;
		}
	}
	
	/**
	 * Return a proxy of an instance of the main class of the plugin <code>name</code> if found, null otherwise.
	 * 
	 * @param name plugin's name.
	 * @return
	 */
	protected Object getPlugin(String name) {
		PluginBase plugin = pluginManager.getPlugin(name);
		return plugin==null?null:plugin.getProxy();
	}
	
	public String getPluginPath(String name) {
		PluginBase plugin = pluginManager.getPlugin(name);
		return plugin==null?null:plugin.getPath();
	}
	
	/**
	 * Return plugins that are assignables from <code>clazz</code>.
	 * 
	 * @param clazz
	 * @return
	 */
	protected List<String> getPluginImplementationsOf(Class<?> clazz) {
		List<String> retour = new ArrayList<String>();
		for(String plugin : pluginManager.getPluginList()) {
			PluginBase pB = pluginManager.getPlugin(plugin);
			if(pB!=null && clazz.isAssignableFrom(pB.getMainClass())) retour.add(plugin);
		}
		return retour;
	}
	
	/**
	 * Return plugin's names list.
	 * 
	 * @return
	 */
	protected ArrayList<String> getPluginList() {
		return pluginManager.getPluginList();
	}
	
	/**
	 * Return a proxy of an instance of the main class using constructor based on args model of the plugin <code>name</code> if found, null otherwise.
	 * Attention : incompatible with singleton attribut ! (always create a new object)
	 * 
	 * @param name plugin's name.
	 * @return
	 */
	protected Object getPlugin(String name, Object... args) {
		PluginBase plugin = pluginManager.getPlugin(name);
		return plugin==null?null:plugin.getProxy(args, null);
	}
	
	/**
	 * Return a proxy of an instance of the main class using constructor based on argType model of the plugin <code>name</code> if found, null otherwise.
	 * Attention : incompatible with singleton attribut ! (always create a new object)
	 * 
	 * @param name plugin's name.
	 * @return
	 */
	protected Object getPluginUsingConstructor(String name, Class<?>[] argsType, Object... args) {
		PluginBase plugin = pluginManager.getPlugin(name);
		return plugin==null?null:plugin.getProxy(args, argsType);
	}
}
