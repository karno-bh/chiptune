package org.sm.snippets.animations;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;

public class TestBoard008 extends AnimatedBoard {
	
	private static final long serialVersionUID = 1L;
	final int size = 100;
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
	
	private int[] pallete;
	private IntArrayCanvas canvas;
	private IntArrayCanvas palleteCanvas;
	private int counter = 0;
	private IntArrayCanvas textCanvas;
	private int flameLength;
	private int whiteColor = Color.WHITE.getRGB();
	
	public TestBoard008(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}

	@Override
	protected void onInit() {
		Color[] awtColorPallete = generatePallete();
		pallete = new int[awtColorPallete.length];
		for (int i = 0; i < awtColorPallete.length; i++) {
			pallete[i] = awtColorPallete[i].getRGB();
		}
		palleteCanvas = new IntArrayCanvas(bWidth, bHeight);
		canvas = new IntArrayCanvas(bWidth, bHeight);
		textCanvas = new IntArrayCanvas(bWidth, bHeight);
		flameLength = 1200;
		BufferedImage bi = new BufferedImage(bWidth, bHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, bWidth, bHeight);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		g.drawString("MBM RULZ!!!", 280, 270);
		
		for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				textCanvas.set(x, bHeight - y - 1, bi.getRGB(x, y));
			}
		}
		g.dispose();
	}
	
	private Color[] generatePallete() {
		
		ColorRange[] colorSeeds = new ColorRange[] {
				new ColorRange(Color.BLACK, Color.RED, 0, 33),
				new ColorRange(Color.RED, Color.ORANGE, 33, 66),
				new ColorRange(Color.ORANGE, Color.WHITE, 66, size - 1)
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
		for (int i = 0; i < bWidth; i++) {
			boolean setPixel = Math.random() < 0.3;
			palleteCanvas.set(i, 0, setPixel ? flameLength : 0);
		}
		for (int y = 1; y < bHeight; y++) {
			for (int x = 1; x < bWidth - 1; x++) {
				int prevY = y - 1;
				int prevYLeft = palleteCanvas.get(x - 1, prevY);
				int prevYMiddle = palleteCanvas.get(x, prevY);
				int prevYRight = palleteCanvas.get(x + 1, prevY);
				int prevVal = palleteCanvas.get(x, y);
				float nowValF = (prevVal + prevYLeft + prevYMiddle + prevYRight) / 4f - 0.1f;
				int nowVal = (int)nowValF;
				nowVal = nowVal < 0 ? 0 : nowVal;
				nowVal = nowVal > flameLength ? flameLength  : nowVal;
				palleteCanvas.set(x, y, nowVal);
			}
		}
		for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				int palleteColor = palleteCanvas.get(x, y) * (size - 1) / flameLength;
				if (textCanvas.get(x, y) == whiteColor) {
					palleteColor += 4;
					if (palleteColor >= size - 1) {
						palleteColor = size - 1;
					}
				}
				int pixelColor = pallete[palleteColor];
				canvas.set(x,bHeight -  y -1, pixelColor);
			}
		}
		/*for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				int color = textCanvas.get(x, y);
				if (color == whiteColor) {
					
				}
			}
		}*/
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
