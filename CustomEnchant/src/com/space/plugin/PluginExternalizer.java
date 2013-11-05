package com.space.plugin;

import java.util.ArrayList;
import java.util.List;

import com.space.annotations.LaunchInfo;
import com.space.enums.LaunchPriority;

public class PluginExternalizer extends PluginRunnable{

	@Override
	@LaunchInfo(priority = LaunchPriority.HIGHEST)
	public void run() throws Exception {
		System.out.println("Main hook created !");
	}
	
	/**
	 * Return a proxy of an instance of the main class of the plugin <code>name</code> if found, null otherwise.
	 * 
	 * @param name plugin's name.
	 * @return
	 */
	public Object getPlugin(String name) {
		return super.getPlugin(name);
	}
	
	/**
	 * Return plugins that are assignables from <code>clazz</code>.
	 * 
	 * @param clazz
	 * @return
	 */
	public List<String> getPluginImplementationsOf(Class<?> clazz) {
		return super.getPluginImplementationsOf(clazz);
	}
	
	/**
	 * Return plugin's names list.
	 * 
	 * @return
	 */
	public ArrayList<String> getPluginList() {
		return super.getPluginList();
	}
	
	public String getPluginPath(String name) {
		return super.getPluginPath(name);
	}
	
	/**
	 * Return a proxy of an instance of the main class using constructor based on args model of the plugin <code>name</code> if found, null otherwise.
	 * Attention : incompatible with singleton attribut ! (always create a new object)
	 * 
	 * @param name plugin's name.
	 * @return
	 */
	public Object getPlugin(String name, Object... args) {
		return super.getPlugin(name, args);
	}
	
	/**
	 * Return a proxy of an instance of the main class using constructor based on argType model of the plugin <code>name</code> if found, null otherwise.
	 * Attention : incompatible with singleton attribut ! (always create a new object)
	 * 
	 * @param name plugin's name.
	 * @return
	 */
	public Object getPluginUsingConstructor(String name, Class<?>[] argsType, Object... args) {
		return super.getPluginUsingConstructor(name, argsType, args);
	}

}
