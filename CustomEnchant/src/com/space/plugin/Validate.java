package com.space.plugin;

/**
 * Wrapper to validate conditions. Grab exceptions.
 * 
 * @author Ornicare
 *
 */
//TODO Exception manager.
public abstract class Validate {

	/**
	 * Throw an exception if <code>o</code> is null.
	 * 
	 * @param o
	 * @param error
	 */
	public static void notNull(Object o, String error) {
		if(o==null)
			try {
				throw new Throwable(error);
			} catch (Throwable e) {
				e.printStackTrace();
				//In case of plugin misconception
			}
	}

	/**
	 * Throw an exception if <code>bool</code> is false.
	 * 
	 * @param o
	 * @param error
	 */
	public static void isTrue(boolean bool, String error) {
		if(!bool)
			try {
				throw new Throwable(error);
			} catch (Throwable e) {
				e.printStackTrace();
				//In case of plugin misconception
			}
	}

}
