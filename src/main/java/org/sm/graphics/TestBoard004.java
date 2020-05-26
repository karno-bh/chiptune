package org.sm.graphics;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;
import org.sm.graphics.primitives.Primitives;

public class TestBoard004 extends AnimatedBoard{
	
	private static final long serialVersionUID = 1L;

	private IntArrayCanvas canvas;
	
	int x = 0;
	int y = 0;
	double theta = 0;
	
	private static final double TO_RAD_FACTOR = Math.PI / 180;
	
	private double toRad(double degree) {
		return degree * TO_RAD_FACTOR;
	}
	
	public TestBoard004(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}

	@Override
	protected void onInit() {
		canvas = new IntArrayCanvas(bWidth, bHeight);
	}
	
	@Override
	protected void cycle() {
		x++;
		if (x > 300) {
			y++;
		}
		theta ++;
	}
	@Override
	protected void render() {
		for (int i = 0; i < canvas.getData().length; i++) {
			canvas.getData()[i] = 0;
		}
		int color = 255 << 24 | 255 << 16 | 0 << 8 | 0;
		//canvas.set(x, y, color);
		int xCenter = bWidth / 2;
		int yCenter = bHeight / 2;
		for (double d = theta; d < theta + 360; d += (360d / 3)) {
			double dRad = toRad(d);
			Primitives.line(xCenter, yCenter, xCenter + (int)(300 * Math.cos(dRad)), yCenter + (int)(300 * Math.sin(dRad)), color, canvas);
			//System.out.println(d);
		}
		//System.out.println("==================================????????????");
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, canvas.getData());
		
	}
}
