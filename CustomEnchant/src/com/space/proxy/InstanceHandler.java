package com.space.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.space.plugin.PluginBase;

import fr.enchantments.custom.loader.PluginLoader;

/**
 * 
 * Handler for the lazy proxy.
 * @author Ornicare
 *
 */
public class InstanceHandler implements InvocationHandler{
	
	private Object realObject = null;
	private PluginBase pluginBase;
	private Object[] args;
	private Class<?>[] argsType;
	
	/**
	 * Create a new handler.
	 * 
	 * @param pluginBase plugin to proxyfie
	 * @param lazy is it lazy ?
	 */
	public InstanceHandler(PluginBase pluginBase, boolean lazy) {
		super();
		this.pluginBase = pluginBase;
		if(!lazy) createNewInstance();
	}
	
	public InstanceHandler(PluginBase pluginBase, boolean lazy,
			Object[] args, Class<?>[] argsType) {
		super();
		this.pluginBase = pluginBase;
		this.args = args;
		this.argsType = argsType;
		if(!lazy) createNewInstance();
	}

	public Object invoke(Object pseudoObject, Method m, Object[] args)
			throws Throwable {
		if(realObject == null) createNewInstance();
		return m.invoke(realObject, args);
	}

	/**
	 * Give an instance of the plugin.
	 */
	private void createNewInstance() {
		//System.out.println("Instanciation !");
		try {
			if(args==null) {
				this.realObject = pluginBase.getInstance();
			}
			else {
				this.realObject = pluginBase.getInstance(args, argsType);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}