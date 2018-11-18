package main;

import java.awt.Point;

public class CustomPoint extends Point implements Comparable<Point> {

	public CustomPoint(int x, int y) {
		super.setLocation(x, y);
	}

	@Override
	public int compareTo(Point p2) {
		if (this.getX() == p2.getX()) {
			if (this.getY() == p2.getY()) {
				return 0;
			} else if (this.getY() > p2.getY()) {
				return 1;
			} else {
				return -1;
			}
		} else if (this.getX() > p2.getX()) {
			return 1;
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return "(" + (this.getX() + 1) + ", " + (this.getY() + 1) + ")";
	}

}
