package main;

import java.util.Scanner;

public class Player {

	protected int color;
	protected GameBoard gb;
	private Scanner scan;

	public Player(int c, GameBoard g) {
		color = c;
		gb = g;
	}

	public int getColor() {
		return color;
	}
	
	/*
	 * Makes a move based on move input from getMove()
	 */
	public boolean makeMove() {
		Move move;
		do {
			move = getMove();
			if(move.getDisc().getCoords().x == -1 && move.getDisc().getCoords().y == -1) {
				return gb.makeMove(move);
			} else if (gb.getNumOfFlips(move) > 0) {
				return gb.makeMove(move);
			}
		} while (true);
	}

	/*
	 * Gathers command line user input and returns a Move 
	 */
	private Move getMove() {
		scan = new Scanner(System.in);
		boolean stop = false;
		Move move = null;
		
		do {
			System.out.println("What is player " + this.getColor() + "'s next move? (Insert in format 'x pos,y pos')");	
			if (scan.hasNextLine()) {
				String[] str = scan.nextLine().split(",");
				CustomPoint point = new CustomPoint(Integer.parseInt(str[0]) - 1, Integer.parseInt(str[1]) - 1);
				move = new Move(new Disc(point, this.getColor()));
				stop = true;
			}
		} while (!stop);
		return move;
	}
}
