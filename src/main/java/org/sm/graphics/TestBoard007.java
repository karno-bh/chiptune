package org.sm.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;
import org.sm.pallete.HSBPallete;

public class TestBoard007 extends AnimatedBoard {
	
	private static final long serialVersionUID = 1L;
	final int size = 400;
	
	private int[] pallete;
	private IntArrayCanvas canvas;
	private IntArrayCanvas palleteCanvas;
	private int counter = 0;
	private IntArrayCanvas textCanvas;
	private IntArrayCanvas shiftedTextCanvas;
	private int flameLength;
	private final int whiteColor = Color.WHITE.getRGB();
	private final int blackColor = Color.BLACK.getRGB();
	private final int textCycle = bWidth / 4;
	private double[] sinTextShiftTable;
	private int nowTextCycle = 0;
	private int shiftFactor = 7;
	
	public TestBoard007(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}
	

	@Override
	protected void onInit() {
		pallete = HSBPallete.mapToInts(HSBPallete.genPallete(size, Color.BLACK, new Color(0.7f, 0.1f ,0.1f), Color.YELLOW));
		palleteCanvas = new IntArrayCanvas(bWidth, bHeight);
		canvas = new IntArrayCanvas(bWidth, bHeight);
		textCanvas = new IntArrayCanvas(bWidth, bHeight);
		shiftedTextCanvas = new IntArrayCanvas(bWidth, bHeight);
		flameLength = 300;
		BufferedImage bi = new BufferedImage(bWidth, bHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, bWidth, bHeight);
		g.setColor(Color.WHITE);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 40));
		g.drawString("\u041A\u043E\u043B\u0431\u0430\u0441\u0430!! \u0416)", 250, 200);
		
		for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				textCanvas.set(x, bHeight - y - 1, bi.getRGB(x, y));
			}
		}
		g.dispose();
		sinTextShiftTable = genSinShiftTextTable();
	}
	
	private double[] genSinShiftTextTable() {
		double[] sinTable = new double[textCycle];
		for (int i = 0; i < textCycle; i++) {
			sinTable[i] = Math.sin(textCycleToRad(i));
		}
		return sinTable;
	}
	
	private double textCycleToRad(double nowTextCycle) {
		return nowTextCycle * 2 * Math.PI / textCycle;
	}
	
	private double getSinShiftTextValue(int fraction) {
		fraction %= textCycle;
		return sinTextShiftTable[fraction];
	}
	
	@Override
	protected void render() {		
		for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				int palleteColor = palleteCanvas.get(x, y) * (size - 1) / flameLength;
				if (shiftedTextCanvas.get(x, y) == whiteColor) {
					palleteColor += 15;
					if (palleteColor >= size - 1) {
						palleteColor = size - 1;
					}
				}
				int pixelColor = pallete[palleteColor];
				canvas.set(x, bHeight - y -1, pixelColor);
			}
		}
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, canvas.getData());
		Graphics g = buffer.getGraphics();
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + fps, 20, 20);
	}

	@Override
	protected void cycle() {
/*		counter++;
		if (counter % 2 != 0) {*/
			nowTextCycle %= textCycle;
			for (int i = 0; i < bWidth; i++) {
				boolean setPixel = Math.random() < 0.8;
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
			int shiftTextCyle = nowTextCycle;
			for (int x = 0; x < bWidth; x++, shiftTextCyle++) {
				int shiftTextY = (int)(shiftFactor * getSinShiftTextValue(shiftTextCyle));
				if (shiftTextY == 0) {
					continue;
				} else if (shiftTextY > 0) {
					for (int y = bHeight - 1; y >= 0; y--) {
						int yToGrab = y - shiftTextY;
						int color = yToGrab < 0 ? blackColor : textCanvas.get(x, yToGrab);
						//System.out.println("(" + x + ", " + y + ")");
						shiftedTextCanvas.set(x, y, color);						
					}
				} else {
					for (int y = 0; y < bHeight; y++) {
						int yToGrab = y - shiftTextY;
						//System.out.println("(" + x + ", " + y + ")");
						int color = yToGrab >= bHeight ? blackColor : textCanvas.get(x, yToGrab);
						shiftedTextCanvas.set(x, y, color);
					}
				}
				//System.out.print(" " + shiftTextY);
			}
			//System.out.println();
			
			nowTextCycle++;
/*		}
		if (counter > 2) {
			counter = 0;
		}*/
	}
	
	public static void main(String[] args) {
		new TestBoard007(800, 400).start();
	}

}
