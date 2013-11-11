package com.space.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarFile;



/**
 * 
 * Global manager of all plugins : store the plugin's list, load all plugins, manage communication between plugins.
 * 
 * @author Ornicare
 *
 */
public class PluginManager {
	
	/**
	 * The main classLoader : it will know all others pluginClassLoaders
	 */
	private ClassLoader parent;
	
	/**
	 * Global list of plugins.
	 */
	private List<PluginBase> loadedPlugins = new ArrayList<PluginBase>();
	
	/**
	 * List of grouped plugins by dependencies.
	 */
	public List<ArrayList<PluginBase>> groupedLoadedPlugins = new ArrayList<ArrayList<PluginBase>>();
	
	/**
	 * List of groups ClassLoader
	 */
	private Map<Integer,URLClassLoader> groupedClassLoader = new HashMap<Integer,URLClassLoader>();
	
	public PluginManager(ClassLoader parent) {
		this.parent = parent;
	}
    
    /**
     * Loads the plugins contained within the specified directory
     *
     * @param directory Directory to check for plugins
     * @return A list of all plugins loaded
	 * @throws Exception 
     */
    public List<PluginBase> preLoadPlugins(File directory) throws Exception {
        Validate.notNull(directory, "Directory cannot be null");
        Validate.isTrue(directory.isDirectory(), "Directory must be a directory ["+directory.getAbsolutePath()+"]");

        List<PluginBase> result = new ArrayList<PluginBase>();
        
        // All plugins are in folder /plugins
        for (File file : directory.listFiles()) {
        	/*if(file.isFile() && file.getName().endsWith(".jar")) {
        		Properties prop = loadConfig(file);
            	if(prop!=null) {
            		if(prop.containsKey("runnable") && prop.getProperty("runnable").equals("true")) {
                		register(new PluginRunnableWrapper(file.getAbsolutePath(),this, prop));
                	}
                	else {
                		register(new PluginRunnableWrapper(file.getAbsolutePath(),this, prop));
                	}
            	}
        	}
        	else */
        	if(file.isDirectory()){
        		//TODO checkyChekoCheka
        		Properties config = new Properties();
        		config.load(new FileInputStream(new File(file.getAbsoluteFile()+"/plugin.properties")));
        		
        		if(config.containsKey("runnable") && config.getProperty("runnable").equals("true")) {
            		register(new PluginRunnableWrapper(file.getAbsolutePath()+"/bin/",this, config));
            	}
            	else {
            		register(new PluginRunnableWrapper(file.getAbsolutePath()+"/bin/",this, config));
            	}
        	}
        }
        
        createDependenciesGroups();
        createGroupedClassLoaders(directory.getAbsolutePath());
        
        /*for(Integer i : groupedClassLoader.keySet()) {
        	System.out.println(groupedClassLoader.get(i));
        	URLClassLoader gCL = groupedClassLoader.get(i);
        	for(URL path : gCL.getURLs()) {
        		System.out.println(path.getPath());
        	}
        }*/
        return result;
    }

    
    private void createGroupedClassLoaders(String path) {
    	
    	/*int id = 0;
    	for(ArrayList<PluginBase> pBL : groupedLoadedPlugins) {
    		for(PluginBase pB : pBL) {
        		System.out.println(pB.getName()+" ["+id+"]");
        	}
    		id++;
    	}*/
    	
    	
    	int groupId = 0;
    	for(ArrayList<PluginBase> pBL : groupedLoadedPlugins) {
    		List<URL> plugins = new ArrayList<URL>();
    		for(PluginBase pB : pBL) {
    			URL fileURL = null;
    			try {
    				fileURL = new URL("file:///"+pB.getPath());
    			} catch (MalformedURLException e) {
    				e.printStackTrace();
    			}
    			
    			plugins.add(fileURL);
    			
    			pB.setGrouId(groupId);
    		}

			URL[] tabURL = new URL[plugins.size()];
			
			for(int i = 0;i<plugins.size();i++) {
				tabURL[i] = plugins.get(i);
			}
    		groupedClassLoader.put(groupId, getNewClassLoader(tabURL));
    		groupId++;
    	}	
	}

	/**
     * Create dependencies group.
     */
    private void createDependenciesGroups() {
   	
    	List<ArrayList<String>> groupedLoadedPluginsStringVersion = new ArrayList<ArrayList<String>>();

    	/*
    	 * Create grouped list.
    	 */
    	for(PluginBase pB: loadedPlugins) {
    		String pluginName = pB.getName();
    		boolean find = false;
    		for(int index = 0; index < groupedLoadedPluginsStringVersion.size() ; index++) {
    			ArrayList<String> pluginGroup = groupedLoadedPluginsStringVersion.get(index);
    			boolean dependencieAlreadyInList = false;
    			for(String plugin : pB.getDependencies()) {
					if(pluginGroup.contains(plugin)) dependencieAlreadyInList = true;
				}
    			if(pluginGroup.contains(pluginName) || dependencieAlreadyInList) {
    				for(String plugin : pB.getDependencies()) {
    					if(!pluginGroup.contains(plugin)) pluginGroup.add(plugin);
    				}
    				if(!pluginGroup.contains(pluginName)) pluginGroup.add(pluginName);
        			groupedLoadedPluginsStringVersion.set(index, pluginGroup);
    				find = true;
    			}
    		}
    		if(!find) {
    			ArrayList<String> newGroup = new ArrayList<String>(Arrays.asList(pB.getDependencies()));
    			newGroup.add(pluginName);
    			groupedLoadedPluginsStringVersion.add(newGroup);
    		}
    		
        	/*int i = 0;
        	for(ArrayList<String> pluginGroup : groupedLoadedPluginsStringVersion) {
        		i++;
        		for(String plugin : pluginGroup) {
        			System.out.println("["+i+"] "+plugin);
        		}
        	}
        	System.out.println("___________________________________");*/
    	}
    	
    	/*
    	 * Transfrom it inti plugins
    	 */
    	for(ArrayList<String> pluginGroup : groupedLoadedPluginsStringVersion) {
    		ArrayList<PluginBase> newGroup = new ArrayList<PluginBase>();
    		for(String plugin : pluginGroup) {
    			newGroup.add(getPlugin(plugin));
    		}
    		groupedLoadedPlugins.add(newGroup);
    	}
    	
    	/*int i = 0;
    	for(ArrayList<String> pluginGroup : groupedLoadedPluginsStringVersion) {
    		i++;
    		for(String plugin : pluginGroup) {
    			System.out.println("["+i+"] "+plugin);
    		}
    	}*/
	}

	/**
     * Load the plugin's attributs.
     * 
     * @param pluginJarName
     * @return
     */
	@SuppressWarnings("resource")
	public Properties loadConfig(File pluginJarName) {
    	JarFile jarFileContainer = null;
		try {
			jarFileContainer = new JarFile(pluginJarName);
			InputStream defConfigStream = jarFileContainer.getInputStream(jarFileContainer.getEntry("plugin.properties"));
			if (defConfigStream != null) {
	        	Properties config = new Properties();
	        	config.load(defConfigStream);
	        	return config;
			}
		}
		catch (Throwable e1) {
			System.out.println("Error while opening "+pluginJarName+". Is it a valid plugin jar file ?");
			e1.printStackTrace();
		}
		return null;       
    }

	/**
	 * Add the plugin to the global list
	 * 
	 * @param plugin
	 */
	public void register(PluginBase plugin) {
		for(PluginBase pB : loadedPlugins) {
			if(pB.getName().equals(plugin.getName())) {
				try {
					throw new Exception("Plugin "+pB.getName()+" present twice !");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
		loadedPlugins.add(plugin);
	}

	/**
	 * Search to find class into plugin's dependencies.
	 * 
	 * @param name
	 * @param dependanciesNames
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public Class<?> findClass(String name, String... dependanciesNames) throws ClassNotFoundException, Exception {
		
		List<String> dependanciesNamesList = new ArrayList<String>(Arrays.asList(dependanciesNames));
		for(PluginBase plugin : loadedPlugins) {
			if(dependanciesNamesList.contains(plugin.getJarName())) {
				Class<?> clazz = plugin.getClassLoader().loadClass(name);
				return clazz;
			}
		}
		return null;
	}

	/**
	 * Try to find the plugin identified by <code>name</code>. If it doesn't find it, return null.
	 * 
	 * @param name
	 * @return
	 */
	public PluginBase getPlugin(String name) {
		for(PluginBase plugin : loadedPlugins) {
			if(plugin.getName().equals(name)) {
				return plugin;
			}
		}
		
		try {
			throw new Throwable("Plugin not found : "+name);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Create a new class loader for a plugin.
	 * 
	 * @param tabURL
	 * @return
	 */
	public URLClassLoader getNewClassLoader(URL[] tabURL) {
		return new URLClassLoader(tabURL,parent);
	}

	public URLClassLoader getGroupClassLoader(int groupId) {
		return groupedClassLoader.get(new Integer(groupId));
	}

	public ArrayList<String> getPluginList() {
		ArrayList<String> retour = new ArrayList<String>();
		for(PluginBase pB : loadedPlugins) {
			retour.add(pB.getName());
		}
		return retour;
	}

}
