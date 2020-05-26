package org.sm.graphics;

import java.awt.Color;
import java.io.DataOutput;
import java.util.Map;
import java.util.TreeMap;

import org.sm.graphics.animation.AnimatedBoard;
import org.sm.graphics.primitives.IntArrayCanvas;



public class TestBoard009 extends AnimatedBoard {

	public TestBoard009(int boardWidth, int boardHeight) {
		super(boardWidth, boardHeight);
		// TODO Auto-generated constructor stub
	}
	
	IntArrayCanvas canvas;
	int black = Color.BLACK.getRGB();
	double[] noise;
	
	
	@Override
	protected void render() {
		int[] data = canvas.getData();
		for (int i = 0; i < data.length; i++) {
			data[i] = black;
		}
		int yMid = bHeight / 2;
		for (int i = 0; i < bWidth; i++) {
			int y = yMid + (int)(noise[i] * 200) - 100;
			canvas.set(i, y, Color.red.getRGB());
		}
		buffer.getRaster().setDataElements(0, 0, bWidth, bHeight, canvas.getData());
	}

	@Override
	protected void cycle() {
		
	}

	@Override
	protected void onInit() {
		canvas = new IntArrayCanvas(bWidth, bHeight);
		noise = getNoise();
		
	}
	
	double[] getNoise() {
		//System.out.println("test2");
		Map<Double, Double> values = new TreeMap<>();
		double f = 0.5d;
		int k = 2;
		for (int i = 0; i <= k; i++) {
			values.put(i / (double) k, f * Math.random());
		}
		for (int j = 0; j < 9; j++) {
			f /= 2;
			k *= 2;
			Map<Double, Double> nextValues = new TreeMap<>();
			for (int i = 0; i <= k; i++) {
				double key = i / (double) k;
				double value = f * Math.random();
				if (values.containsKey(key)) {
					value += values.get(key);
				} else {
					double leftKey = (i - 1) / (double) k;
					double rightKey = (i + 1) / (double) k;
					double lVal = values.get(leftKey);
					double rVal = values.get(rightKey);
					double l1Val = (lVal + rVal) / 2;
					value += l1Val;
				}
				nextValues.put(key, value);
			}
			values = nextValues;
		}
		//System.out.println(values.size());
		double[] noise = new double[values.size()];
		int i = 0;
		for (Map.Entry<Double, Double> keyVal : values.entrySet()) {
			noise[i++] = keyVal.getValue();
		}
		return noise;
		//System.out.println();
	}
	
	
}
