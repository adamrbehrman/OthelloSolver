package main;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class AIPlayer extends Player {

	public AIPlayer(int c, GameBoard gb) {
		super(c, gb);
	}

	@Override
	public boolean makeMove() {
		// Get all possible moves
		List<Move> possibleMoves = gb.getPossibleMoves(color);

		// Go through all the moves
		for (Move move : possibleMoves) {
			// Automatically take corner moves
			if (gb.isCorner(move.getDisc().getCoords())) {
				System.out.println("***************\n\n\n" + move);
				return gb.makeMove(move);
			} else if (gb.isBadSquare(move)) {
				// Bad moves should be avoided
				move.setPlayScore(move.getPlayScore() - .99);
			}
		}
		
		for (Move move : possibleMoves) {
			move = this.returnMoveScore(gb, move);
		}

		possibleMoves.sort(null);

		for (Move move : possibleMoves) {
			move = runSimulations(gb, move, color, 3, 0);
		}

		for (Move move : possibleMoves) {
			if (gb.isCorner(move.getDisc().getCoords())) {
				move.setPlayScore(move.getPlayScore() + .99);
			} else if (gb.isXSquare(move.getDisc().getCoords()) || gb.isCSquare(move.getDisc().getCoords())) {
				move.setPlayScore(move.getPlayScore() - .99);
			}
		}

		possibleMoves.sort(null);

		for (Move move : possibleMoves) {
			System.out.println(move);
		}
		
		for (Move move : possibleMoves) {
			if (gb.isPerfectlyQuietMove(move, gb.getPossibleMoves(Math.abs(color - 3)).size()) && (!gb.isCSquare(move.getDisc().getCoords()) && !gb.isXSquare(move.getDisc().getCoords()))) {
				System.out.println("***************\n\n\n" + move);
				return gb.makeMove(move);
			}
		}

		if (possibleMoves != null && !possibleMoves.isEmpty()) {
			System.out.println("***************\n\n\n" + possibleMoves.get(0));
			return gb.makeMove(possibleMoves.get(0));
		} else {
			return gb.makeMove(new Move(new Disc(new CustomPoint(-1, -1), color)));
		}
	}

	private Move returnMoveScore(GameBoard copyGB, Move move) {
		// Wedges: Tiles that cannot be changed by opponent
		// List<Move> wedges = gb.getListOfPossibleWedges(this.color);

		// Tempo: Tiles that force opponent to make "first move"
		// List<Move> movesThatGainTempo = gb.tempoGain(gb.getAllFrontiers(this.color),
		// this.color);

		// Parity: Save tiles that other player cannot play into
		// DO NOT PLAY THESE TILES
		// List<Tile> tilesOpponentCannotPlayInto =
		// gb.getTilesWhereColorCannotPlay(Math.abs(this.color) - 3);
		int count = 0;

		for (int i = 0; i < gb.board.length; i++) {
			for (int j = 0; j < gb.board[i].length; j++) {
				if (gb.board[i][j].getDisc() != null) {
					count++;
				}
			}
		}

		double score = 0;

		if (((double) (gb.board.length * gb.board[0].length) * .75) > count) {
			// Current Move:
			// Quiet moves: .05
			if (gb.isQuietMove(move)) {
				score += .05;
			}

			// Perfectly Quiet Moves: .2
			if (gb.isPerfectlyQuietMove(move, gb.getPossibleMoves(Math.abs(color - 3)).size())) {
				score += .2;
			}

			// Future Moves:
			
			// Wedges: .06
			int wedges = gb.wedgesAdded(move, color);
			// System.out.println(wedges);
			if (wedges > 0) {
				score += wedges * .06;
			}

			// Tempo: .04
			int tempo = gb.tempoAdded(move, gb.getAllFrontiers(this.color), color);
			// System.out.println(tempo);
			if (tempo > 0) {
				score += tempo * .04;
			}

			// Tiles opponent cannot play into: .3
			int tiles = gb.tilesOpponentCannotPlayIntoAdded(move, (Math.abs(this.color) - 3));
			// System.out.println(tiles);
			if (tiles > 0) {
				score += tiles * .3;
			}

			// Flips: -.02
			score -= move.getNumOfFlips() * .02;
		}

		/*
		 * After 2/3 or 3/4 of the GameBoard is filled, need to change strategy. - Play
		 * moves that maximize tiles turned
		 * 
		 */
		else {
			// Flips: .05
			score += move.getNumOfFlips() * .05;
		}
		move.setPlayScore(score);
		return move;
	}

	/*
	 * Plays move over times amount of plays Adjusts color for opponent's moves
	 * 
	 */
	private Move runSimulations(GameBoard copyGB, Move move, int color, int times, int index) {
		move = this.returnMoveScore(copyGB, move);
		System.out.println(move);

		// Catch after scoring
		if (index > times) {
			return move;
		}

		// Copy the GameBoard, change the color and run the other simulations
		color = Math.abs(3 - color);
		// System.out.println(copyGB);
		GameBoard copy = GameBoard.copyOf(copyGB);
		copy.makeMove(move);
		// System.out.println(copy);
		move.setNextMoves(copy.getPossibleMoves(color));
		// System.out.println(copy.getPossibleMoves(color).size());
		for (Move nextMove : move.getNextMoves()) {
			nextMove = runSimulations(copy, nextMove, color, times, index + 1);
		}

		// After running all simulations, return move
		return move;
	}
}
