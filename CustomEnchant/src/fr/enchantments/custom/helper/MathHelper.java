package fr.enchantments.custom.helper;

public abstract class MathHelper {
	
	/**
	 * Randomize an Integer
	 * 
	 * @param i the integer to randomize
	 * @return an integer between 1 and <code>i</code>
	 */
	public static int randomize(int i) {
		return (int)(Math.random()*i+1);
	}
}
