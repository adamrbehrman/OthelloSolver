package main;

import java.util.List;

public class Move implements Comparable<Move> {

	private Disc disc;
	private List<Move> nextMoves;
	private double playScore;
	private int numOfFlips;

	public static final double GIVES_UP_EDGE = -.06;
	public static final double GIVES_UP_CORNER = -.99;
	public static final double TAKES_EDGE = .06;
	public static final double TAKES_CORNER = .99;

	public Move() {
		disc = new Disc();
	}

	public Move(Disc disc) {
		this.disc = disc;
	}

	public Disc getDisc() {
		return disc;
	}

	public void setDisc(Disc disc) {
		this.disc = disc;
	}

	public int getNumOfFlips() {
		return numOfFlips;
	}

	public void setNumOfFlips(int numOfFlips) {
		this.numOfFlips = numOfFlips;
	}

	public double getPlayScore() {
		return playScore;
	}

	public void setPlayScore(double playScore) {
		this.playScore = playScore;
	}

	public void addNextMove(Move move) {
		nextMoves.add(move);
	}

	public List<Move> getNextMoves() {
		return nextMoves;
	}

	public void setNextMoves(List<Move> nextMoves) {
		this.nextMoves = nextMoves;
	}

	public boolean givesUpCorner() {
		if (playScore == Move.GIVES_UP_CORNER) {
			return true;
		}
		for (Move nMove : this.getNextMoves()) {
			if (nMove.getPlayScore() == Move.GIVES_UP_CORNER) {
				return true;
			}
		}
		return false;
	}

	public boolean givesUpEdge() {
		return playScore == Move.GIVES_UP_EDGE;
	}

	public boolean takesCorner() {
		return playScore == Move.TAKES_CORNER;
	}

	public boolean takesEdge() {
		return playScore == Move.TAKES_EDGE;
	}

	@Override
	public String toString() {
		if (nextMoves != null && !nextMoves.isEmpty()) {
			String str = "";
			for (Move move : this.nextMoves) {
				str += move.getPlayScore() + "\n";
			}
			return disc + "\nScore: " + this.getPlayScore() + "\nNext moves scores: \n" + str + "\n";
		} else {
			return disc + "\nScore: " + this.getPlayScore() + "\n";
		}
	}

	@Override
	public int compareTo(Move move) {
		return (int) ((move.getPlayScore() * 100) - (this.getPlayScore() * 100));
	}
}
