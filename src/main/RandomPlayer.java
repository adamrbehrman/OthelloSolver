package main;

import java.util.List;
import java.util.Random;

public class RandomPlayer extends Player {

	private Random rand;

	public RandomPlayer(int c, GameBoard g) {
		super(c, g);
		rand = new Random();
	}

	public int getColor() {
		return color;
	}

	public boolean makeMove() {
		do {
			List<Move> moves = gb.getPossibleMoves(color);
			return gb.makeMove(moves.get(rand.nextInt(moves.size())));
		} while (true);
	}

}
