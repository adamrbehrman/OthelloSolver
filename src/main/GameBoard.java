package main;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GameBoard {

	public Tile[][] board;

	public static final int EMPTY = 0;
	public static final int WHITE = 1;
	public static final int BLACK = 2;

	public static final int FRONTIER_NORTH = 0;
	public static final int FRONTIER_SOUTH = 1;
	public static final int FRONTIER_EAST = 2;
	public static final int FRONTIER_WEST = 3;

	public GameBoard() {
		board = new Tile[8][8];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				CustomPoint point = new CustomPoint(i, j);
				board[i][j] = new Tile(point);
			}
		}
		board[3][3].setDisc(new Disc(new CustomPoint(3, 3), GameBoard.WHITE));
		board[4][4].setDisc(new Disc(new CustomPoint(4, 4), GameBoard.WHITE));
		board[3][4].setDisc(new Disc(new CustomPoint(3, 4), GameBoard.BLACK));
		board[4][3].setDisc(new Disc(new CustomPoint(4, 3), GameBoard.BLACK));
	}

	/*
	 * Creates and returns a copy of GameBoard given gb
	 */

	public static GameBoard copyOf(GameBoard gb) {
		GameBoard copy = new GameBoard();

		for (int i = 0; i < gb.board.length; i++) {
			for (int j = 0; j < gb.board[i].length; j++) {
				CustomPoint point = new CustomPoint(i, j);
				copy.board[i][j] = new Tile(point);
				if (gb.board[i][j].getDisc() != null) {
					copy.board[i][j].setDisc(gb.board[i][j].getDisc());
				}
			}
		}

		return copy;
	}

	@Override
	public String toString() {
		String str = "  | ";
		for (int i = 1; i <= board.length; i++) {
			str += " " + i + "  ";
		}
		str += "\n--+--------------------------------\n";
		for (int i = 0; i < board.length; i++) {
			str += (i + 1) + " | ";
			for (Tile j : board[i]) {
				str += "[" + j.getDiscColor() + "] ";
			}
			str += "\n\n";
		}
		return str;
	}

	public ArrayList<Move> getPossibleMoves(int color) {
		ArrayList<Move> moves = new ArrayList<Move>();
		Move move;
		// If there are tiles that can be played into, add them to moves and return
		// moves
		for (Tile tile : this.getNullTiles()) {
			move = new Move(new Disc(tile.getCoords(), color));
			int flips = this.getNumOfFlips(move);
			if (flips > 0) {
				// System.out.println("Here");
				move.setNumOfFlips(flips);
				moves.add(move);
				// System.out.println(moves.size());
			}
		}
		// System.out.println(moves.size());
		return moves;
	}

	public int[] getAllFrontiers(int color) {
		// Returns information on all 4 frontiers
		int[] frontiers = new int[4];
		for (int i = 0; i < 4; i++) {
			frontiers[i] = this.getMovesInFrontier(color, i).size();
		}
		return frontiers;
	}

	public List<Tile> getTilesWhereColorCannotPlay(int color) {
		List<Tile> nullTiles = getNullTiles();
		ListIterator<Tile> li = nullTiles.listIterator();
		Tile tile;
		// If there are places where a tile cannot be placed by color, keep them and
		// return the list
		while (li.hasNext()) {
			tile = li.next();
			if (this.getFlipsFromMove(new Move(new Disc(tile.getCoords(), color))).size() > 0) {
				li.remove();
			}
		}
		return nullTiles;
	}

	public List<Tile> getNullTiles() {
		// Returns the null tiles
		List<Tile> nullTiles = new ArrayList<Tile>();
		for (Tile[] tiles : board) {
			for (Tile tile : tiles) {
				if (tile.getDisc() == null) {
					nullTiles.add(tile);
				}
			}
		}
		return nullTiles;
	}

	public List<Move> getMovesInFrontier(int color, int frontier) {
		List<Move> moves = this.getPossibleMoves(color);
		ListIterator<Move> li = moves.listIterator();
		// Checks if there are moves in a given frontier
		while (li.hasNext()) {
			Move move = li.next();
			int[] test = this.getQuadrant(move.getDisc().getCoords());
			if (test[0] != frontier && test[1] != frontier) {
				li.remove();
			}
		}
		return moves;
	}

	public int[] getQuadrant(CustomPoint p) {
		return board[p.x][p.y].getQuadrant();
	}

	public boolean isEdge(CustomPoint p) {
		int i = p.x, j = p.y;
		return i == 7 || j == 7 || i == 0 || j == 0;
	}

	public boolean isCorner(CustomPoint p) {
		int i = p.x, j = p.y;
		return (j == 0 || j == 7) && (i == 0 || i == 7);
	}

	public boolean isXSquare(CustomPoint p) {
		int i = p.x, j = p.y;
		return (i == 1 || i == 6) && (j == 1 || j == 6);
	}

	public boolean isQuietMove(Move move) {
		return this.getNumOfFlips(move) == 1;
	}

	/*
	 * Perfectly quiet moves are moves that do not give any other moves to the
	 * opponent
	 */
	public boolean isPerfectlyQuietMove(Move move, int origNumMoves) {
		GameBoard copy = GameBoard.copyOf(this);
		copy.makeMove(move);
		// System.out.println(copy.getPossibleMoves(Math.abs(move.getDisc().getColor() -
		// 3)).size() > origNumMoves);
		return copy.getPossibleMoves(Math.abs(move.getDisc().getColor() - 3)).size() > origNumMoves;
	}
	
	/*
	 * Checks if the move is either a C-square or an X-square
	 */
	public boolean isBadSquare(Move move) {
		return isCSquare(move.getDisc().getCoords()) || isXSquare(move.getDisc().getCoords());
	}

	public boolean isCSquare(CustomPoint p) {
		int i = p.x, j = p.y;
		return ((j == 7 || j == 0) && (i == 6 || i == 1)) || ((j == 6 || j == 1) && (i == 7 || i == 0));
	}

	public boolean isOnBoard(int i, int j) {
		return (i <= 7 && i >= 0) && (j <= 7 && j >= 0);
	}

	public boolean isEmpty(int i, int j) {
		return board[i][j].getDiscColor() == 0;
	}

	/*
	 * Gain tempo: Get the quadrant index of the highest frontier move count Try all
	 * other quadrants and get quiet moves
	 */
	public List<Move> tempoGain(int[] frontiers, int color) {
		List<Move> moves = new ArrayList<Move>();

		int maxIndex = 0;
		int max = frontiers[0];
		for (int i = 1; i < frontiers.length; i++) {
			if (frontiers[i] > max) {
				maxIndex = i;
				max = frontiers[i];
			}
		}
		// Run through all 4 frontiers
		for (int i = 0; i < 4; i++) {
			if (maxIndex != i) {
				// Run through all moves, see if moves are quiet moves
				for (Move move : this.getMovesInFrontier(color, i)) {
					if (this.isQuietMove(move)) {
						moves.add(move);
					}
				}
			}
		}
		return moves;
	}

	public int tilesOpponentCannotPlayIntoAdded(Move move, int color) {
		int tiles = this.getTilesWhereColorCannotPlay(color).size();
		GameBoard copy = GameBoard.copyOf(this);
		copy.makeMove(move);
		return this.getTilesWhereColorCannotPlay(color).size() - tiles;
	}

	public int tempoAdded(Move move, int[] frontiers, int color) {
		int numTempos = this.tempoGain(frontiers, color).size();
		GameBoard copy = GameBoard.copyOf(this);
		copy.makeMove(move);
		return this.tempoGain(frontiers, color).size() - numTempos;
	}

	public int wedgesAdded(Move move, int color) {
		int numWedges = this.getListOfPossibleWedges(color).size();
		GameBoard copy = GameBoard.copyOf(this);
		copy.makeMove(move);
		return this.getListOfPossibleWedges(color).size() - numWedges;
	}

	public Object[] getPossibleWedges() {
		checkForStableDiscs();

		List<Move> whiteMoves = getListOfPossibleWedges(GameBoard.WHITE);
		List<Move> blackMoves = getListOfPossibleWedges(GameBoard.BLACK);

		Object[] nums = { whiteMoves, blackMoves };

		return nums;
	}

	public List<Move> getListOfPossibleWedges(int color) {
		List<Move> moves = this.getPossibleMoves(color);
		ListIterator<Move> li = moves.listIterator();
		Move move;
		// While there are moves left
		while (li.hasNext()) {
			move = li.next();
			if (!canBeWedge(move.getDisc())) {
				// If the move cannot be a wedge, remove the move
				li.remove();
			}
		}
		return moves;
	}

	private boolean canBeWedge(Disc disc) {
		Tile[] tiles = new Tile[4];

		int[] pass = new int[4];

		boolean[] touchingDiscs = new boolean[4];
		boolean[] touchingWalls = new boolean[4];

		for (int i = 0; i < touchingDiscs.length; i++) {
			touchingDiscs[i] = false;
			touchingWalls[i] = false;
		}

		int row, col, modRow = 0, modCol = 0;

		for (int loopNum = 0; loopNum < 4; loopNum++) {
			row = disc.getCoords().x;
			col = disc.getCoords().y;

			switch (loopNum) {
			case 0:
				// horizontal left
				pass[loopNum] = 0;
				modRow = 0;
				modCol = -1;
				break;
			case 1:
				// horizontal right
				modRow = 0;
				modCol = +1;
				break;
			case 2:
				// vertical up
				pass[loopNum] = 0;
				modRow = -1;
				modCol = 0;
				break;
			case 3:
				// vertical down
				pass[loopNum] += 1;
				modRow = 1;
				modCol = 0;
				break;
			}

			do {
				row += modRow;
				col += modCol;
				pass[loopNum] += 1;
				if (isOnBoard(row, col)) {
					tiles[loopNum] = board[row][col];
				}
			} while (isOnBoard(row, col) && isEmpty(row, col));

			// On the board
			if (isOnBoard(row, col)) {
				// Must end on colored disc
				if (!isEmpty(row, col) && pass[loopNum] == 1) {
					touchingDiscs[loopNum] = true;
				}
			} else {
				touchingWalls[loopNum] = true;
			}
		}

		boolean touchingWall;

		for (int loopNum = 1; loopNum < 4; loopNum += 2) {
			touchingWall = false;
			if (tiles[loopNum] != null && tiles[loopNum - 1] != null) {
				// Disc must be touching at least two other disc...
				if (touchingDiscs[loopNum] && touchingDiscs[loopNum - 1]) {
					if (loopNum == 1) {
						if (touchingWalls[3] || touchingWalls[2]) {
							// ...and a wall to be a wedge
							touchingWall = true;
						}
					} else if (touchingWalls[1] || touchingWalls[0]) {
						touchingWall = true;
					}
					if (touchingWall) {
						// Odd number of tiles
						if ((pass[loopNum] + pass[loopNum - 1] - 1) % 2 == 1) {
							// Other discs on either side
							if (tiles[loopNum].getDiscColor() != disc.getColor()
									&& tiles[loopNum - 1].getDiscColor() != disc.getColor()) {
								return true;
							}
						} else {
							// Even number of tiles
							if ((pass[loopNum] + pass[loopNum - 1] - 1) % 2 == 0) {
								// Other discs on either side
								if ((tiles[loopNum].getDiscColor() == disc.getColor() && !touchingDiscs[loopNum])
										|| (tiles[loopNum - 1].getDiscColor() == disc.getColor()
												&& !touchingDiscs[loopNum - 1])) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public int[] getNumStableDiscs() {

		// First, establish stable discs
		checkForStableDiscs();

		int whiteStableDiscs = findStableDiscs(GameBoard.WHITE);
		int blackStableDiscs = findStableDiscs(GameBoard.BLACK);

		int[] nums = { whiteStableDiscs, blackStableDiscs };
		return nums;
	}

	public int findStableDiscs(int color) {
		int stableDiscs = 0;
		// Go though all of the Tiles on the board
		for (Tile[] tiles : board) {
			for (Tile t : tiles) {
				// See if the disc isn't null, is stable, and matches color
				if (t.getDisc() != null && t.getDisc().isStable() && t.getDiscColor() == color) {
					stableDiscs++;
				}
			}
		}
		return stableDiscs;
	}

	public void checkForStableDiscs() {
		// Go though all of the Tiles on the board
		for (Tile[] tiles : board) {
			for (Tile tile : tiles) {
				// Checks if the disc exists, is not stable, and that it isn't empty
				if (tile.getDisc() != null && !tile.getDisc().isStable() && tile.getDiscColor() != GameBoard.EMPTY) {
					// Checks if the disc can be surrounded
					if (canBeSurrounded(tile.getDisc())) {
						tile.getDisc().setStable(true);
					}
				}
			}
		}
	}

	private boolean canBeSurrounded(Disc disc) {
		boolean[] bothSidesSafe = new boolean[4];

		int index = 0;

		for (int i = 0; i < bothSidesSafe.length; i++) {
			bothSidesSafe[i] = true;
		}

		int row, col, modRow = 0, modCol = 0;

		for (int check = 0; check < 8; check++) {
			row = disc.getCoords().x;
			col = disc.getCoords().y;

			switch (check) {
			case 0:
				// horizontal left
				modRow = 0;
				modCol = -1;
				break;
			case 1:
				// horizontal right
				modRow = 0;
				modCol = +1;
				break;
			case 2:
				// vertical up
				modRow = -1;
				modCol = 0;
				index = 1;
				break;
			case 3:
				// vertical down
				modRow = 1;
				modCol = 0;
				break;
			case 4:
				// left up
				modRow = -1;
				modCol = -1;
				index = 2;
				break;
			case 5:
				// left down
				modRow = 1;
				modCol = -1;
				break;
			case 6:
				// right up
				modRow = -1;
				modCol = 1;
				index = 3;
				break;
			case 7:
				// right down
				modRow = +1;
				modCol = +1;
				break;
			}
			do {
				row += modRow;
				col += modCol;
			} while (isOnBoard(row, col) && !isEmpty(row, col));

			if (isOnBoard(row, col)) {
				if (isEmpty(row, col)) {
					if (!bothSidesSafe[index]) {
						return false;
					} else {
						bothSidesSafe[index] = false;
					}
				}
			}
		}
		return true;
	}

	public ArrayList<ArrayList<CustomPoint>> getFlipsFromMove(Move move) {
		// Initialize variables
		ArrayList<ArrayList<CustomPoint>> moves = new ArrayList<ArrayList<CustomPoint>>();
		for (int i = 0; i < 8; i++) {
			moves.add(new ArrayList<CustomPoint>());
		}
		int i = move.getDisc().getCoords().x, j = move.getDisc().getCoords().y;
		int color = move.getDisc().getColor();
		int row, col, modRow = 0, modCol = 0;

		// Check emptiness
		if (!isEmpty(i, j)) {
			return moves;
		} else {
			// Check all 8 directions
			for (int check = 0; check < 8; check++) {
				row = i;
				col = j;

				switch (check) {
				case 0:
					// horizontal left
					modRow = 0;
					modCol = -1;
					break;
				case 1:
					// horizontal right
					modRow = 0;
					modCol = +1;
					break;
				case 2:
					// vertical up
					modRow = -1;
					modCol = 0;
					break;
				case 3:
					// vertical down
					modRow = 1;
					modCol = 0;
					break;
				case 4:
					// left up
					modRow = -1;
					modCol = -1;
					break;
				case 5:
					// right up
					modRow = -1;
					modCol = 1;
					break;
				case 6:
					// left down
					modRow = 1;
					modCol = -1;
					break;
				case 7:
					// right down
					modRow = +1;
					modCol = +1;
					break;
				}
				do {
					// Modify modRow/modCol by their respective values
					row += modRow;
					col += modCol;
					moves.get(check).add(new CustomPoint(row, col));
				} while (isOnBoard(row, col) && !isEmpty(row, col) && board[row][col].getDiscColor() != color);
				// If not on board, color != current disc's color or the size of moves is 1,
				// clear the move list at index check
				if (!isOnBoard(row, col) || board[row][col].getDiscColor() != color || moves.get(check).size() == 1) {
					moves.get(check).clear();
				}
			}
		}

//		for(ArrayList<CustomPoint> movesN: moves) {
//			for(CustomPoint moveN : movesN) {
//				System.out.println(moveN);
//			}
//			
//		}
		return moves;
	}

	public int getNumOfFlips(Move move) {
		int flips = 0;
		if (move != null) {
			// Returns the number of flips resulting from move
			ArrayList<ArrayList<CustomPoint>> moves = getFlipsFromMove(move);
			for (ArrayList<CustomPoint> pList : moves) {
				flips += pList.size();
			}
		}
		return flips;
	}

	public int getTilesOfColor(int color) {
		int sum = 0;
		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[i].length; j++) {
				if (this.board[i][j].getDisc() != null) {
					if (this.board[i][j].getDisc().getColor() == color) {
						sum++;
					}
				}
			}
		}
		return sum;
	}

	public boolean makeMove(Move move) {
		int i = move.getDisc().getCoords().x, j = move.getDisc().getCoords().y;
		// flip all others in between i,j with !c
		if (i == -1 && j == -1) {
			System.out.println("No moves.");
			return true;
		} else {
			ArrayList<ArrayList<CustomPoint>> flips = getFlipsFromMove(move);
			// Create disc for the location of the move
			this.board[i][j].setDisc(new Disc(move.getDisc().getCoords(), move.getDisc().getColor()));
			// Go through board and set Discs on each Tile to their respective coords
			for (int x = 0; x < flips.size(); x++) {
				for (int y = 0; y < flips.get(x).size(); y++) {
					CustomPoint p = flips.get(x).get(y);
					this.board[p.x][p.y].setDisc(new Disc(new CustomPoint(p.x, p.y), move.getDisc().getColor()));
				}
			}
			return false;
		}

	}
}
