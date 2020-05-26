package org.sm.graphics.primitives;

public class IntArrayCanvas implements Canvas {
	
	private final int width;
	private final int height;
	private final int[] data;
	
	public IntArrayCanvas(int width, int height) {
		this.width = width;
		this.height = height;
		data = new int[width * height];
	}
	
	@Override
	public void getBounds(int[] bounds) {
		bounds[0] = width;
		bounds[1] = height;
	}
	
	@Override
	public void set(int x, int y, int color) {
		data[x + y * width] = color;
	}
	
	public int[] getData() {
		return data;
	}
	
	@Override
	public int get(int x, int y) {
		return data[x + y * width];
	}
	
	@Override
	public void fillAll(int color) {
		for (int i = 0; i < data.length; i++) {
			data[i] = color;
		}
	}
}
