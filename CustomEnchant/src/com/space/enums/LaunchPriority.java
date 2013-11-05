package com.space.enums;



public enum LaunchPriority {
	HIGHEST (2),
	HIGH (1),
	NORMAL (0),
	LOW (-1),
	LOWEST (-2);
	
	private int priority;
	
	private LaunchPriority(int i) {
		this.priority = i;
	}
	
	public int getPriority() {
		return priority;
	}

	public static LaunchPriority[] getOrder() {
		return new LaunchPriority[]{HIGHEST,HIGH,NORMAL,LOW,LOWEST};
	}
}
