package main;

public class Disc {

	private int color;
	private boolean stable;
	private CustomPoint coords;

	public Disc(CustomPoint coords, int color) {
		this.color = color;
		this.coords = coords;
	}
	
	public Disc() { } 

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public boolean isStable() {
		return stable;
	}

	public void setStable(boolean stable) {
		this.stable = stable;
	}

	public CustomPoint getCoords() {
		return coords;
	}

	public void setCoords(CustomPoint coords) {
		this.coords = coords;
	}

	@Override
	public String toString() {
		return "Disc at " + coords + " for " + color + " (Stable: " + stable+")";
	}

}
