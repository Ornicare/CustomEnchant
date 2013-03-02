package fr.enchantments.custom.helper;

public abstract class MathHelper {
	
	/**
	 * Randomize an Integer
	 * 
	 * @param i the integer to randomize
	 * @return an integer between 1 and <code>i</code>
	 */
	public static short randomize(short i) {
		return (short)(Math.random()*i+1);
	}
}
