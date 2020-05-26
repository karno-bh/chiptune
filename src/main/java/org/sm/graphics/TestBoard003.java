package org.sm.graphics;

import java.awt.Color;
import java.awt.Graphics;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;
import org.sm.graphics.primitives.Primitives;

public class TestBoard003 extends AnimatedBoard {
	
	private IntArrayCanvas canvas;
	
	public TestBoard003(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}
	
	
	@Override
	protected void onInit() {
		canvas = new IntArrayCanvas(bWidth, bHeight);
	}
	
	@Override
	protected void cycle() {
		
	}
	
	@Override
	protected void render() {
		int color = 255 << 24 | 255 << 16 | 0 << 8 | 0;
		long before = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Primitives.line(20 + (int)(Math.random() * 100), 20 + (int)(Math.random() * 100), 100 + (int)(Math.random() * 100), 100 + (int)(Math.random() * 100), color, canvas);
		}
		long diff = System.currentTimeMillis() - before;
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, canvas.getData());
		System.out.println("AB2 Diff: " + diff);
		Graphics g = buffer.getGraphics();
		g.setColor(Color.BLACK);
		g.drawString("FPS: " + fps, 500, 500);
		g.dispose();
	}
}
