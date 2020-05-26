package org.sm.graphics;

import java.awt.Color;
import java.awt.Graphics;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;
import org.sm.graphics.primitives.Primitives;

public class TestBoard006 extends AnimatedBoard {
	
	private static final long serialVersionUID = 1L;
	final int size = 300;
	final int[] palleteAccessor = new int[2];
	private static class ColorRange {
		public Color c1;
		public Color c2;
		public int from;
		public int to;
		public ColorRange(Color c1, Color c2, int from, int to) {
			this.c2 = c2;
			this.c1 = c1;
			this.from = from;
			this.to = to;
		}
	}
	
	private static final double TO_RAD_FACTOR = Math.PI / 360;
	
	private double toRad(double degree) {
		return degree * TO_RAD_FACTOR;
	}
	
	private int[] pallete;
	
	private IntArrayCanvas canvas;
	private IntArrayCanvas palleteCanvas;
	private int counter = 0;
	
	public TestBoard006(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}

	@Override
	protected void onInit() {
		Color[] awtColorPallete = generatePallete();
		pallete = new int[awtColorPallete.length];
		for (int i = 0; i < awtColorPallete.length; i++) {
			pallete[i] = awtColorPallete[i].getRGB();
		}
		palleteCanvas = new IntArrayCanvas(750, size);
		canvas = new IntArrayCanvas(bWidth, bHeight);
		
	}
	
	private Color[] generatePallete() {
		
		ColorRange[] colorSeeds = new ColorRange[] {
				new ColorRange(Color.BLACK, Color.RED, 0, 100),
				new ColorRange(Color.RED, Color.ORANGE, 100, 200),
				new ColorRange(Color.ORANGE, Color.WHITE, 200, size - 1)
		};
		Color[] pallete = new Color[size];
		
		for (ColorRange colorRange : colorSeeds) {
			Color c1 = colorRange.c1;
			Color c2 = colorRange.c2;
			float c1NormRed = c1.getRed() / 255f;
			float c1NormGreen = c1.getGreen() / 255f;
			float c1NormBlue = c1.getBlue() / 255f;
			
			float c2NormRed = c2.getRed() / 255f;
			float c2NormGreen = c2.getGreen() / 255f;
			float c2NormBlue = c2.getBlue() / 255f;
			
			int colorsInRange = colorRange.to - colorRange.from + 1;
			
			for (int i = colorRange.from, k = 0; i <= colorRange.to; i++, k++) {
				float percentage = k / (float) (colorsInRange);
				float r = c1NormRed + percentage * (c2NormRed - c1NormRed);
				float g = c1NormGreen + percentage * (c2NormGreen - c1NormGreen);
				float b = c1NormBlue + percentage * (c2NormBlue - c1NormBlue);
				pallete[i] = new Color (r,g,b);
			}
		}
		return pallete;
	}
	
	@Override
	protected void render() {
		if (counter % 2 != 0) {
			return;
		}
		palleteCanvas.getBounds(palleteAccessor);
		int palleteCanvasWidth = palleteAccessor[0];
		int palleteCanvasHeigh = palleteAccessor[1];
		for (int i = 0; i < palleteAccessor[0]; i++) {
			boolean setPixel = Math.random() < 0.7;
			palleteCanvas.set(i, 0, setPixel ? size - 1 : 0);
		}
		for (int y = 1; y < palleteCanvasHeigh; y++) {
			for (int x = 1; x < palleteCanvasWidth - 1; x++) {
				int prevY = y - 1;
				int prevYLeft = palleteCanvas.get(x - 1, prevY);
				int prevYMiddle = palleteCanvas.get(x, prevY);
				int prevYRight = palleteCanvas.get(x + 1, prevY);
				int prevVal = palleteCanvas.get(x, y);
				float nowValF = (prevVal + prevYLeft + prevYMiddle + prevYRight) / 4f - 0.01f;
				int nowVal = (int) nowValF;
				nowVal = nowVal < 0 ? 0 : nowVal;
				nowVal = nowVal > size - 1 ? size - 1  : nowVal;
				palleteCanvas.set(x, y, nowVal);
			}
		}
		int[] canvasData = canvas.getData();
		for (int i = 0; i < canvasData.length; i++) {
			canvasData[i] = Color.black.getRGB();
		}
		int centerX = bWidth / 2;
		int centerY = bHeight / 2;
		for (double radius = 0; radius < palleteCanvasHeigh; radius++) {
			for (double angle = 0; angle < palleteCanvasWidth; angle++) {
				int pixelColor = pallete[palleteCanvas.get((int)angle, (int)radius)];
				double angleRad = toRad(angle);
				double xDouble = radius *  Math.cos(angleRad);
				double yDouble = radius * Math.sin(angleRad);
				int xCoord = (int) (centerX + xDouble);
				int yCoord = (int) (centerY + yDouble);
				//System.out.println("xCoord: " + xCoord + " yCoord: " + yCoord);
				canvas.set(xCoord, yCoord, pixelColor);
			}
		}
		//Primitives.line(10, 10, 100, 100, Color.red.getRGB(), canvas);
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, canvas.getData());
	}

	@Override
	protected void cycle() {
		counter++;
		if (counter > 2) {
			counter = 0;
		}
	}

}
