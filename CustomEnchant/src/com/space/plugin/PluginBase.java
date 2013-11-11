package com.space.plugin;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.net.URLClassLoader;
import java.util.Properties;

import com.space.proxy.InstanceHandler;


/**
 * 
 * Contain all attributs and methods for the plugin's wrapper.
 * 
 * @author Ornicare
 *
 */
public abstract class PluginBase{

	
	private Properties config;
	protected URLClassLoader classLoader;
	protected String pluginJarName;
	private String name;
	protected boolean initialized = false;
	private PluginManager pluginManager;
	private boolean runInFirst = false;
	
	protected String mainClass;
	protected boolean internalLazy;
	protected boolean singleton;
	protected boolean isRunnable;
	private int groupId;
	
	/**
	 * The plugin instance
	 */
	protected Object instance;
	
	protected Class<?> classToLoad;
	protected Class<?> mainClassInstance;
	private Object proxy;
	
	/**
	 * 
	 * @param pluginJarName Physical jar name
	 * @param pluginManager Global common manager
	 * @param config The properties version of config.properties
	 */
	public PluginBase(String pluginJarName, PluginManager pluginManager, Properties config) {
		Validate.notNull(pluginJarName, "Jar name cannot be null !");
		this.pluginJarName = pluginJarName;
		this.pluginManager=pluginManager;
		this.config = config;
		loadConfig(config);
	}
	
	/**
	 * Do we need to automatically run it ?
	 * @return
	 */
	public boolean isLaunchable() {
		return runInFirst;
	}

	/**
	 *  Get a new classLoader for this plugin
	 */
	public void initialize() {
		classLoader = pluginManager.getGroupClassLoader(groupId);
		initialized = true;
	}

	
	public Properties getConfig() {
		return config;
	}
	
	/**
	 * Parse and load the plugin configuration.
	 * 
	 * @param config
	 */
	protected void loadConfig(Properties config) {
    	mainClass = config.getProperty("main");
    	
    	internalLazy = getPropertyBool("lazy");
    	singleton = getPropertyBool("singleton");
    	isRunnable = getPropertyBool("runnable");
    	runInFirst = getPropertyBool("launch");
    	
    	if(isRunnable) Validate.notNull(mainClass, getName()+" : Invalid main class in the properties file");
		
	}
	
	/**
	 * Submethod to transform a propertie into boolean.
	 * If it doesn't recognize any boolean, return false;
	 * 
	 * @param prop
	 * @return
	 */
	private boolean getPropertyBool(String prop) {
		if(!config.containsKey(prop)) return false;
		Boolean value = Boolean.valueOf(config.getProperty(prop));
		return value;
	}
    
    
	/**
	 * Return a file contained into the jarfile, in it InputStream state.
	 * 
	 * @param filename
	 * @return
	 */
    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        return getClass().getResourceAsStream(filename);
    }
    
    /**
     * Return the classLoader for this plugin. Create it if it doesn't exists.
     * 
     * @return
     * @throws Exception
     */
    public final URLClassLoader getClassLoader() throws Exception {
    	if(!initialized) initialize();
        return classLoader;
    }
    
    /**
     * Return plugin true name.
     * 
     * @return
     */
    public String getName() {
    	if(name==null) this.name = config.getProperty("name");
    	Validate.notNull(name, pluginJarName+" : invalide properties file : name not found.");
    	return name;
    }

    /**
     * Calculate plugin's dependancies.
     * 
     * @return
     */
	public String[] getDependencies() {
		String rawDependancies = config.getProperty("depend");
		String[] retour = null;
		if(rawDependancies!=null) retour = config.getProperty("depend").split(",");
		if(retour!=null) {
			for(int i = 0 ; i < retour.length ; i++) {
				retour[i] = retour[i].trim();
			}
		}
		return retour==null?new String[0]:retour;
	}

	/**
	 * Return plugin jar name.
	 * 
	 * @return
	 */
	public Object getJarName() {
		String output;

		String pluginJarNameTemp = pluginJarName;
		if(!pluginJarName.endsWith(".jar")) {
			pluginJarNameTemp = pluginJarNameTemp.substring(0, pluginJarNameTemp.length()-5);
		}
	
		if(pluginJarNameTemp.lastIndexOf("/")<0) {
			output = pluginJarNameTemp.substring(pluginJarNameTemp.lastIndexOf("\\"), pluginJarNameTemp.length());
		}
		else {
			output = pluginJarNameTemp.substring(pluginJarNameTemp.lastIndexOf("/"), pluginJarNameTemp.length());
		}
		return output;
	}

	public void setGrouId(int groupId) {
		this.groupId = groupId;
	}
	
	public Object getProxy() {
		InstanceHandler h = new InstanceHandler(this, internalLazy);
		
		//Get the classloader if not exists.
		if(!initialized) initialize();
		if(!singleton || this.proxy==null) this.proxy = Proxy.newProxyInstance(classLoader, getMainClass().getInterfaces(), h);
		return proxy;
	}
	
	public Object getProxy(Object[] args, Class<?>[] argsType) {
		InstanceHandler h = new InstanceHandler(this, internalLazy, args, argsType);
		
		//Get the classloader if not exists.
		if(!initialized) initialize();
		if(!singleton || this.proxy==null) this.proxy = Proxy.newProxyInstance(classLoader, getMainClass().getInterfaces(), h);
		return proxy;
	}

	public Object getInstance() {
		return (!singleton || instance == null)?createInstance():instance;
	}
	
	/**
	 * Create a new instance using args
	 * 
	 * @param args
	 * @param argsType 
	 * @return
	 */
	public Object getInstance(Object[] args, Class<?>[] argsType) {
		if(singleton && instance != null) return instance;
		if(!initialized) initialize();
		
		//If not existing, try to create the args model.
		if(argsType==null) {
			argsType = new Class<?>[args.length];
			
			//Creating args model
			for(int i =0;i<args.length;i++) {
				argsType[i] = args[i].getClass();
			}	
		}
		

		try {
			classToLoad = classLoader.loadClass(mainClass);
			Validate.notNull(classToLoad, "Invalid main class.");
//			PluginLoader.pluginLoader.getLogger().info("@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			for(Constructor<?> c : classToLoad.getConstructors()) {
//				for(Class<?> cl : c.getParameterTypes()) {
//					PluginLoader.pluginLoader.getLogger().info("@@@@@@@@@@@@@@@@@@@@@@@@@@@"+cl.getCanonicalName());
//				}
//			}
			Constructor<?> constructor = classToLoad.getConstructor(argsType);
			instance = constructor.newInstance(args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		

		return instance;
	}
	
	public Class<?> getMainClass() {
		if(!initialized) initialize();
		try {
			mainClassInstance = classLoader.loadClass(mainClass);
		} catch (ClassNotFoundException e) {
			System.err.println("Is the main class valid ?");
			e.printStackTrace();
		}
		return mainClassInstance;
	}

	protected Object createInstance() {
		
		if(singleton && instance != null) return instance;
		
		if(!initialized) initialize();
		
    	/*for(URL path : classLoader.getURLs()) {
    		System.out.println(getName()+" "+path.getPath());
    	}*/
    	
		

		try {
			classToLoad = getMainClass();
			Validate.notNull(classToLoad, "Invalid main class.");
			
			
			instance = classToLoad.newInstance();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		

		return instance;
	}
	
	public String getPath() {
		return pluginJarName;
	}





}
