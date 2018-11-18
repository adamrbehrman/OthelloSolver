package main;

public class Tile {

	private CustomPoint coords;
	private Disc disc;
	private int[] quadrant;
	
	public Tile(CustomPoint coords) {
		this.coords = coords;
		this.setQuadrant(coords);
		this.disc = null;
	}
	
	public int getDiscColor() {
		if (this.disc == null) {
			return 0;
		}
		return disc.getColor();
	}
	
	public int[] getQuadrant() {
		return quadrant;
	}

	public void setQuadrant(CustomPoint point) {
		if(point.x > 4) {
			if(point.y > 4) {
				quadrant = new int[] {GameBoard.FRONTIER_NORTH, GameBoard.FRONTIER_WEST};
			} else {
				quadrant = new int[] {GameBoard.FRONTIER_NORTH, GameBoard.FRONTIER_EAST};
			}
		} else {
			if(point.y > 4) {
				quadrant = new int[] {GameBoard.FRONTIER_SOUTH, GameBoard.FRONTIER_WEST};
			} else {
				quadrant = new int[] {GameBoard.FRONTIER_SOUTH, GameBoard.FRONTIER_EAST};
			}
		}
	}

	public CustomPoint getCoords() {
		return coords;
	}

	public void setCoords(CustomPoint coords) {
		this.coords = coords;
	}

	public Disc getDisc() {
		return disc;
	}

	public void setDisc(Disc disc) {
		this.disc = disc;
	}
	
}
