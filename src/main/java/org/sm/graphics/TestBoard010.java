package org.sm.graphics;

import java.awt.Color;
import java.awt.Graphics;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;
import org.sm.math.Noise;
import org.sm.math.RangeMapper;
import org.sm.pallete.HSBPallete;

public class TestBoard010 extends AnimatedBoard {

	public TestBoard010(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
	}
	
	private int size = 300;
	private IntArrayCanvas canvas;
	private final int minZ = 0;
	private final int maxZ = 200;
	private int z = minZ;
	//private int zDir = 1;
	private int[] pallete;
	private final RangeMapper toCubeWidth = new RangeMapper(0, bWidth, 0, 3);
	private final RangeMapper toCubeHeight = new RangeMapper(0, bHeight, 0, 3);
	private final RangeMapper toCubeZ = new RangeMapper(minZ, maxZ, 0, 2);
	private final RangeMapper fromCubeMapper = new RangeMapper(-1, 1, 0, size - 1);
	private final Noise noise = new Noise();
	
	
	@Override
	protected void render() {
		double _z = toCubeZ.map(z);
		//System.out.println(_z);
		for (int x = 0; x < bWidth; x++) {
			for (int y = 0; y < bHeight; y++) {
				double _x = toCubeWidth.map(x);
				double _y = toCubeHeight.map(y);
				int cComp = (int)fromCubeMapper.map(noise.noise(_x, _y, _z));
				//Color c = new Color(cComp, cComp, cComp);
				//canvas.set(x, y, c.getRGB());
				canvas.set(x, y, pallete[cComp]);
			}
		}
		//Primitives.line(10, 10, 50, 50, Color.RED.getRGB(), canvas);
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, canvas.getData());
		Graphics g = buffer.getGraphics();
		g.setColor(Color.WHITE);
		g.drawString("FPS: " + fps, 20, 20);
	}

	@Override
	protected void cycle() {
		z++;
	}

	@Override
	protected void onInit() {
		canvas = new IntArrayCanvas(bWidth, bHeight);
		pallete = HSBPallete.mapToInts(HSBPallete.genPallete(size, Color.BLUE, Color.MAGENTA, Color.YELLOW));
	}
	
	public static void main(String[] args) {
		new TestBoard010(640, 480).start();
	}
	
}
