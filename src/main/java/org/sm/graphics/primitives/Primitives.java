package org.sm.graphics.primitives;

public class Primitives {

	public static void line(int x1, int y1, int x2, int y2, int color, Canvas c) {
		int[] bounds = new int[4];
		c.getBounds(bounds);
		int xMax = bounds[0];
		int yMax = bounds[1];
		
		lineWithBoundsChecks(x1, y1, x2, y2, xMax, yMax, color, c);
		
		/*if (x1 < 0 || x2 < 0 || y1 < 0 || y2 < 0 || x1 >= xMax || x2 >= xMax || y1 >= yMax || y2 >= yMax) {
			lineWithBoundsChecks(x1, y1, x2, y2, xMax, yMax, color, c);
		} else {
			lineWithoutBoundsChecks2(x1, y1, x2, y2, color, c);
		}*/
	}

	private static void lineWithoutBoundsChecks(int x1, int y1, int x2, int y2, int color, Canvas c) {
		boolean steep = false;
		int temp;
		if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {

			temp = x1;
			x1 = y1;
			y1 = temp;

			temp = x2;
			x2 = y2;
			y2 = temp;

			steep = true;
		}
		if (x1 > x2) {
			temp = x1;
			x1 = x2;
			x2 = temp;

			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		int dx = x2 - x1;
		int dy = y2 - y1;
		int derror2 = Math.abs(dy) * 2;
		int error2 = 0;
		int y = y1;

		for (int x = x1; x <= x2; x++) {
			if (steep) {
				c.set(y, x, color);
			} else {
				c.set(x, y, color);
			}
			error2 += derror2;

			if (error2 > dx) {
				y += y2 > y1 ? 1 : -1;
				error2 -= dx * 2;
			}
		}
	}

	private static void lineWithoutBoundsChecks2(int x1, int y1, int x2, int y2, int color, Canvas c) {
		int temp;
		if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
			if (y1 > y2) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int derror2 = Math.abs(dx) * 2;
			int error2 = 0;
			int direction = dx < 0 ? -1 : 1;
			int x = x1;
			for (int y = y1; y <= y2; y++) {
				c.set(x, y, color);
				error2 += derror2;
				if (error2 > dy) {
					x += direction;
					error2 -= dy * 2;
				}
			}
		} else {
			if (x1 > x2) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int derror2 = Math.abs(dy) * 2;
			int error2 = 0;
			int direction = dy < 0 ? -1 : 1;
			int y = y1;
			for (int x = x1; x <= x2; x++) {
				c.set(x, y, color);
				error2 += derror2;
				if (error2 > dx) {
					y += direction;
					error2 -= dx * 2;
				}
			}
		}
	}

	private static void lineWithBoundsChecks(int x1, int y1, int x2, int y2, int xMax, int yMax, int color, Canvas c) {

		int temp;
		if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
			if (y1 > y2) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int derror2 = Math.abs(dx) * 2;
			int error2 = 0;
			int direction = dx < 0 ? -1 : 1;
			int x = x1;
			for (int y = y1; y <= y2; y++) {
				if (x >= 0 && y >= 0 && x < xMax && y < yMax) {
					c.set(x, y, color);
				}
				error2 += derror2;
				if (error2 > dy) {
					x += direction;
					error2 -= dy * 2;
				}
			}
		} else {
			if (x1 > x2) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int derror2 = Math.abs(dy) * 2;
			int error2 = 0;
			int direction = dy < 0 ? -1 : 1;
			int y = y1;
			for (int x = x1; x <= x2; x++) {
				if (x >= 0 && y >= 0 && x < xMax && y < yMax) {
					c.set(x, y, color);
				}
				error2 += derror2;
				if (error2 > dx) {
					y += direction;
					error2 -= dx * 2;
				}
			}
		}

	}

	private static void lineWithBoundsChecks2(int x1, int y1, int x2, int y2, int xMax, int yMax, int color, Canvas c) {
		
		int yDiff = y2 - y1;
		int xDiff = x2 - x1;
		if (xDiff == 0) {
			
		}
		
		float yTan = (y2 - y1) / (float) (x2 - x1);
		float xTan = 1 / yTan;

		int temp;
		if (Math.abs(x1 - x2) < Math.abs(y1 - y2)) {
			if (y1 > y2) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int derror2 = Math.abs(dx) * 2;
			int error2 = 0;
			int direction = dx < 0 ? -1 : 1;
			int x = x1;
			for (int y = y1; y <= y2; y++) {
				if (x >= 0 && y >= 0 && x <= xMax && y <= yMax) {
					c.set(x, y, color);
				}
				error2 += derror2;
				if (error2 > dy) {
					x += direction;
					error2 -= dy * 2;
				}
			}
		} else {
			if (x1 > x2) {
				temp = x1;
				x1 = x2;
				x2 = temp;

				temp = y1;
				y1 = y2;
				y2 = temp;
			}
			int dx = x2 - x1;
			int dy = y2 - y1;
			int derror2 = Math.abs(dy) * 2;
			int error2 = 0;
			int direction = dy < 0 ? -1 : 1;
			int y = y1;
			for (int x = x1; x <= x2; x++) {
				if (x >= 0 && y >= 0 && x <= xMax && y <= yMax) {
					c.set(x, y, color);
				}
				error2 += derror2;
				if (error2 > dx) {
					y += direction;
					error2 -= dx * 2;
				}
			}
		}
	}
}
