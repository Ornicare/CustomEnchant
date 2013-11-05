package com.space.plugin;

import java.util.Properties;

/**
 * Just a non-runnable plugin.
 * 
 * @author Ornicare
 *
 */
//TODO add some methods ? (getObject, ...)
public class PluginContentProviderWrapper extends PluginBase implements IPluginContentProvider {

	public PluginContentProviderWrapper(String pluginJarName, PluginManager pluginManager, Properties config) {
		super(pluginJarName, pluginManager, config);
	}

	/**
	 * Stub
	 */
	public Object getObject(Object... args) {
		return null;
	}

}
