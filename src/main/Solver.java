package main;

import java.util.Scanner;

public class Solver {

	private Scanner scan;
	private Player player1;
	private Player player2;
	private GameBoard gb;

	public Solver() {
		gb = new GameBoard();
		scan = new Scanner(System.in);
		System.out.println("Who's going first, you (1) or your opponent (2)?");
		if (scan.hasNextLine()) {
			String str = scan.nextLine();
			if (str.equals("1") || str.equalsIgnoreCase("me")) {
				player1 = new Player(GameBoard.WHITE, gb);
				player2 = new AIPlayer(GameBoard.BLACK, gb);
			} else {
				player1 = new AIPlayer(GameBoard.WHITE, gb);
				player2 = new Player(GameBoard.BLACK, gb);
			}
		}
	}

	public void solve(int count) {
		boolean stop1 = false, stop2 = false;
		do {
			stop1 = false;
			stop2 = false;
			System.out.println(gb);
			stop1 = player1.makeMove();
			// System.out.println("Score: \nPlayer " + gb.WHITE + ": " + gb.getTilesOfColor(gb.WHITE) + "\nPlayer "
			//		+ gb.BLACK + gb.getTilesOfColor(gb.BLACK));
			System.out.println(gb);
			stop2 = player2.makeMove();
			// System.out.println("Score: \nPlayer " + gb.WHITE + ": " + gb.getTilesOfColor(gb.WHITE) + "\nPlayer "
			//		+ gb.BLACK + gb.getTilesOfColor(gb.BLACK));
		} while (!stop1 || !stop2);

		System.out.println("Scores \n\tPlayer " + gb.WHITE + ": " + gb.getTilesOfColor(gb.WHITE) + "\n\tPlayer " + gb.BLACK
				+ ": " + gb.getTilesOfColor(gb.BLACK));
	}
}
