package org.sm.graphics;

import java.awt.Color;
import java.awt.Graphics;

import org.sm.graphics.animation.AnimatedBoard;

public class TestBoard001 extends AnimatedBoard {
	
	
	
	public TestBoard001(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}
	
	
	@Override
	protected void onInit() {
		
	}
	
	@Override
	protected void cycle() {
		
	}
	
	@Override
	protected void render() {
		Graphics g = null;
		try {
			g = buffer.getGraphics();
			g.setColor(Color.RED);
			long before = System.currentTimeMillis();
			for (int i = 0; i < 100000; i++) {
				g.drawLine(20 + (int)(Math.random() * 100), 20 + (int)(Math.random() * 100), 100 + (int)(Math.random() * 100), 100 + (int)(Math.random() * 100));
			}
			long diff = System.currentTimeMillis() - before;
			System.out.println("AB1 Diff: " + diff);
		} finally {
			if (g != null) {
				g.dispose();
			}
		}
		g = buffer.getGraphics();
		g.setColor(Color.BLACK);
		g.drawString("FPS: " + fps, 500, 500);
		g.dispose();
	}
	
	public static void main(String[] args) {
		new TestBoard001(800, 600).start();
	}
}
