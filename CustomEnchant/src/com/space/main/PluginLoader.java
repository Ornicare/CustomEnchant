package com.space.main;

import java.io.File;
import java.util.ArrayList;

import com.space.plugin.PluginBase;
import com.space.plugin.PluginCommonMethods;
import com.space.plugin.PluginExternalizer;
import com.space.plugin.PluginManager;

public class PluginLoader {
	
	public PluginExternalizer createPluginManager(String directory) throws Exception {
		
		/*
		 * Create the pluginManager
		 */
		PluginManager pluginManager = new PluginManager(this.getClass().getClassLoader());
		
		/*
		 * Register it.
		 */
		PluginCommonMethods.registerPluginManager(pluginManager);
		
		/*
		 * Load the "plugins" folder.
		 */
		File pluginFolder = new File(directory);
//		System.out.println("Plugins folder in use : "+pluginFolder.getAbsolutePath());
		
		pluginManager.preLoadPlugins(pluginFolder);
		
		
		/*
		 * See groups of plugins dependencies.
		 */
		int id = 0;
		System.out.println("\nDependencies groups (linked plugins) : ");
    	for(ArrayList<PluginBase> pBL : pluginManager.groupedLoadedPlugins) {
    		System.out.println("Group "+id+" : ");
    		for(PluginBase pB : pBL) {
        		System.out.println("    "+pB.getName()+" [jar_name = "+pB.getJarName()+"]");
        	}
    		System.out.println();
    		id++;
    	}
		
		/*
		 * Create a hook plugin for the main core
		 */
    	PluginExternalizer externalizer = new PluginExternalizer();
	
		return externalizer;	
	}

}
