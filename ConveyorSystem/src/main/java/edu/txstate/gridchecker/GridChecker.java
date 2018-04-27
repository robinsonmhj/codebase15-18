package edu.txstate.gridchecker;

/**
 * @author Haojie Ma
 * @date 05 13, 2017
 */
public class GridChecker 
{
	private final int len = 8;
	private final int min = 4;

	/*
	 * The function checks if there are at least 4 checkers of the same color in
	 * a consecutive line horizontally, vertically or diagonally
	 * 
	 * 
	 * Input: an char array with exactly 8 rows and 8 columns, incorrect input
	 * will result in a false return and a hint of why it get such a result
	 * letter 'a' represents red checker letter 'b' represents black checker for
	 * all the other letters, they are treated as 'no checker'
	 * 
	 * output: if there are at least checkers of the same color in a consecutive
	 * line, the fuction return true otherwise return false
	 */
	public boolean check(char[][] grid) {

		if (grid == null || grid.length != len || grid[0].length != len) {
			System.out.println("the grid should be 8*8 ");
			return false;
		}

		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				char c = grid[i][j];
				if (c == 'a' || c == 'b') {
					if (checkVertical(grid, i, j)
							|| checkHorizontal(grid, i, j)
							|| checkDiagonal1(grid, i, j)
							|| checkDiagonal2(grid, i, j))
						return true;
				}
			}
		}

		return false;

	}

	/*
	 * 
	 * The function is used to check if there are at least min checkers of the same color
	 * in a vertical line. If there is, return true, otherwise, return false
	 */
	private boolean checkVertical(char[][] grid, int r, int c) {

		char ch = grid[r][c];
		int count = 1;
		for (int i = r + 1; i < len; i++) {
			if (grid[i][c] == ch) {
				count++;
				if (count >= min) {
					return true;
				}

			} else
				break;
		}

		return false;
	}

	/*
	 * 
	 * The function is used to check if there are at least min checkers of the same color
	 * in a horizontal line. If there is, return true, otherwise, return false
	 */
	private boolean checkHorizontal(char[][] grid, int r, int c) {

		char ch = grid[r][c];
		int count = 1;
		for (int i = c + 1; i < len; i++) {
			if (grid[r][i] == ch) {
				count++;
				if (count >= min) {
					return true;
				}
			} else
				break;
		}

		return false;

	}

	/*
	 * The function is used to check if there are at least min checkers with the same color in the 
	 * diagonal line. If there is, return true, otherwise, return false
	 * 
	 * 
	 */
	
	private boolean checkDiagonal1(char[][] grid, int r, int c) {

		char ch = grid[r][c];
		int count = 1;
		for (int i = r + 1, j = c + 1; i < len && j < len; i++, j++) {
			if (grid[i][j] == ch) {
				count++;
				if (count >= min) {
					return true;
				}
			} else
				break;
		}

		return false;

	}

	/*
	 * The function is used to check if there are at least min checkers with the same color in the 
	 * back diagonal line. If there is, return true, otherwise, return false.
	 * 
	 * 
	 */
	private boolean checkDiagonal2(char[][] grid, int r, int c) {

		char ch = grid[r][c];
		int count = 1;
		for (int i = r + 1, j = c - 1; i < len && j >= 0; i++, j--) {
			if (grid[i][j] == ch) {
				count++;
				if (count >= min) {
					return true;
				}
			} else
				break;
		}

		return false;

	}
}
