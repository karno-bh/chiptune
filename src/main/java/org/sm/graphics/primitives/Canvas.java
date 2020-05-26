package org.sm.graphics.primitives;

public interface Canvas {
	
	void set(int x, int y, int color);
	void getBounds(int[] bounds);
	default int get(int x, int y) {
		return -1;
	}
	default void fillAll(int color) {}
}
