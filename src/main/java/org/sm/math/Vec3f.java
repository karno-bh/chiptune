package org.sm.math;

public class Vec3f {
	
	private final float x, y, z;
	
	public Vec3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vec3f(float[] data) {
		this.x = data[0];
		this.y = data[1];
		this.z = data[2];
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
}
