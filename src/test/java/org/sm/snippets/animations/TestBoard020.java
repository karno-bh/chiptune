package org.sm.snippets.animations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;
import org.sm.graphics.primitives.Primitives;
import org.sm.math.Mat4f;
import org.sm.math.RangeMapper;
import org.sm.math.Vec3f;
import org.sm.model.Model;

public class TestBoard020 extends AnimatedBoard {
	
	private static final long serialVersionUID = 1L;
	
	private Model model;
	private IntArrayCanvas canvas = new IntArrayCanvas(bWidth, bHeight);
	private IntArrayCanvas flippedYCanvas = new IntArrayCanvas(bWidth, bHeight);
	private final int WHITE = Color.WHITE.getRGB();
	private final int BLACK = Color.BLACK.getRGB();
	
	private int degree = 0;
	private float[] destV0 = new float[4];
	private float[] destV1 = new float[4];
	

	public TestBoard020(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);		
	}

	@Override
	protected void render() {
		canvas.fillAll(BLACK);
		List<Vec3f[]> faces = model.getFaces();
		Mat4f m = new Mat4f();
		m.rotateY((float)Math.toRadians(degree));
		for (Vec3f[] face : faces) {
			for (int i = 0; i < face.length; i++) {
				Vec3f v0 = face[i];
				Vec3f v1 = face[(i + 1) % 3];
				m.multVect(v0, destV0);
				m.multVect(v1, destV1);

//				System.out.println(Math.tan(Math.toRadians(30)));
				/*int x0 = (int)((v0.getX()+1.f) * (bWidth/2.f)); 
		        int y0 = (int)((v0.getY()+1.f) * (bHeight/2.f));
		        int x1 = (int)((v1.getX()+1.f) * (bWidth/2.f)); 
		        int y1 = (int)((v1.getY()+1.f) * (bHeight/2.f));*/
//				System.out.println("x: " + destV0[0]);
//				System.out.println("y: " + destV0[1]);
//				System.out.println("z: " + destV0[2]);
//				System.out.println("==============");
//				int x0 = (int)((destV0[0]+1.f) * (bWidth/2.f));
//				int y0 = (int)((destV0[1]+1.f) * (bHeight/2.f));
//				int x1 = (int)((destV1[0]+1.f) * (bWidth/2.f));
//				int y1 = (int)((destV1[1]+1.f) * (bHeight/2.f));
		        int x0 = (int)((destV0[0] / (destV0 [2] * 0.5 + 1.0
				) + 1.f) * (bWidth/2.f));
		        int y0 = (int)((destV0[1] / (destV0 [2] * 0.5 + 1.0) + 1.f) * (bHeight/2.f));
		        int x1 = (int)((destV1[0] / (destV1 [2] * 0.5 + 1.0) + 1.f) * (bWidth/2.f));
		        int y1 = (int)((destV1[1] / (destV1 [2] * 0.5 + 1.0) + 1.f) * (bHeight/2.f));

		        float minZ = destV0[2];
		        if (destV1[2] < minZ) {
		        	minZ = destV1[2];
				}
				float col = (float)RangeMapper.map(minZ, -1, 1, 0, 1);
		        Color c = new Color(1 - col, 1 - col, 1 -  col);

				Primitives.line(x0, y0, x1, y1, c.getRGB(), canvas);
			}
		}
		for (int y = 0; y < bHeight; y++) {
			for (int x = 0; x < bWidth; x++) {
				flippedYCanvas.set(x, bHeight - y - 1, canvas.get(x, y));
			}
		}
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, flippedYCanvas.getData());
		Graphics g = buffer.getGraphics();
		g.drawString("FPS: " + fps, 20, 20);
	}

	@Override
	protected void cycle() {
		degree+=4;
		degree %= 360;
	}

	@Override
	protected void onInit() {
		model = new Model("data/african_head.obj");
	}
	
	public static void main(String[] args) {
		new TestBoard020(800, 600).start();
	}
	
}
