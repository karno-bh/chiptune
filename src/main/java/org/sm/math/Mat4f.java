package org.sm.math;

public class Mat4f {
	
	private static final int ROWS = 4;
	private static final int COLUMNS = 4;
	
	private static final float[] idMatrix = {
		1f, 0f, 0f, 0f,
		0f, 1f, 0f, 0f,
		0f, 0f, 1f, 0f,
		0f, 0f, 0f, 1f,
	};
	
	private  float[] data = new float[ROWS * COLUMNS];
	
	public Mat4f() {
		for (int i = 0; i < data.length; i++) data[i] = idMatrix[i];
	}
	
	private static void set(float[] data, int i, int j, float value) {
		data[i + j * COLUMNS] = value;
	}
	
	private static float get(float[] data, int i, int j) {
		return data[i + j * COLUMNS];
	}
	
	private void multMatrix(float[] src, float[] dest) {
		for (int i = 0; i < COLUMNS; i++) {
			for (int j = 0; j < ROWS; j++) {
				
			}
		}
	}
	
	public void multVect(Vec3f v, float[] dest) {
		float[] origVect = {
			v.getX(), v.getY(), v.getZ(), 1
		};
		for (int j = 0; j < ROWS; j++) {
			float r = 0;
			for (int i = 0; i < COLUMNS; i++) {
				r += get(data, i, j) * origVect[i];
			}
			dest[j] = r;
		}
	}
	
	public Mat4f rotateY(float thetaRadian) {
		float cosTheta = (float)Math.cos(thetaRadian);
		float sinTheta = (float)Math.sin(thetaRadian);
		float[] rotateYMatrix = {
			cosTheta, 0, -sinTheta, 0,
			0,        1,      0,    0,
			sinTheta, 0,  cosTheta, 0,
			0,        0,      0,    1,
		};
		data = rotateYMatrix;
		return this;
	}
	
	public static void main(String[] args) {
		test001();
	}
	
	static void test001() {
		Mat4f mat4f = new Mat4f();
		Vec3f vec3f = new Vec3f(1f, 2f, 3f);
		float dest[] = new float[4];
		mat4f.multVect(vec3f, dest);
		printVector(dest);
		mat4f = mat4f.rotateY((float)Math.toRadians(90));
		mat4f.multVect(vec3f, dest);
		printVector(dest);
	}
	
	static void printVector(float[] vectData) {
		for (int i = 0; i < vectData.length; i++) {
			System.out.print(i == 0 ? vectData[i] : ", " + vectData[i]);
		}
		System.out.println();
	}
}
