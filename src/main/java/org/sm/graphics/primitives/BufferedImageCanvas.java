package org.sm.graphics.primitives;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class BufferedImageCanvas implements Canvas {
	
	private final WritableRaster wr;
	private final int[] pixelColor = new int[1];
	
	private final int xMax, yMax;
	
	public BufferedImageCanvas(BufferedImage bi) {
		wr = bi.getRaster();
		xMax = bi.getWidth() - 1;
		yMax = bi.getWidth() - 1;
	}
	
	@Override
	public void getBounds(int[] bounds) {
		bounds[0] = xMax;
		bounds[1] = yMax;
	}
	
	@Override
	public void set(int x, int y, int color) {
		pixelColor[0] = color;
		wr.setDataElements(x, y, pixelColor);
	}
}
