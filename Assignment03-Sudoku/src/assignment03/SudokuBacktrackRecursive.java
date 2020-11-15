package assignment03;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Brute force, recursive implementation of the sudoku solver.
 * 
 * @author Aaron Morgan (u0393600)
 * @author Xavier Davis (u1197851) 
 *
 */
public class SudokuBacktrackRecursive implements Sudoku {

	private int[] sudoku;
	private String[] stringArray;
	private int count = 0;
	private final static int THOUSAND = 1000;
	private static long accumulatedTime = System.nanoTime();
	
	
	/**
	 * This method returns the number of guesses as the solve() method runs.
	 * Count is incremented within the aforementioned method.
	 * 
	 * @return The number of guesses attempted in the solve() method.
	 */
	private int getGuessCount() {
		return count;
	}

	/**
	 * Creates a new puzzle by reading a file.
	 *
	 * @param filename Relative path of the file containing the puzzle in the given
	 *                 format
	 * @requires The file must be 9 rows of 9 numbers separated by whitespace the
	 *           numbers should be 1-9 or 0 representing an empty square
	 */
	public SudokuBacktrackRecursive(String filename) {
		File file = new File(filename);
		sudoku = new int[81];
		Scanner scanner;
		// a String array needs to be established first to hold the lines.
		stringArray = new String[9]; 
		
		try {
			scanner = new Scanner(file);
			for (int i = 0; i < stringArray.length; i++) {
				stringArray[i] = scanner.nextLine();
			}
			// a separate variable is declared for indexing the int array.
			int k = 0;
			for (int i = 0; i < stringArray.length; i++) {
				for (int j = 0; j < stringArray[i].length(); j++) {
					//This takes each line and goes over each character, then converts that character into an int.
					sudoku[k] = Integer.parseInt(String.valueOf(stringArray[i].charAt(j)));
					k++;
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}

	}

	/**
	 * Determines whether the given {@code number} can be placed in the given
	 * {@code row} without violating the rules of sudoku.
	 *
	 * @param row    which row to see if the number can go into
	 * @param number the number of interest
	 *
	 * @requires [{@code row} is a valid row index] and [{@code number} is a valid
	 *           digit]
	 * 
	 * @return true iff it is possible to place that number in the row without
	 *         violating the rule of 1 unique number per row.
	 */
	public boolean isValidForRow(int row, int number) {
		assert 0 < number && number <= PUZZLE_HEIGHT_WIDTH : "Violation of valid cadidate";
		assert 0 <= row && row < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";

		//In this class, the Sudoku Array is a 1D Array. However, the puzzle is still printed
		// in 2D. For this reason, to search a "row", the proper starting point is the
		// given row multiplied by the # of elements that should be in the row (here, 9).
		int j = row*9;
		for (int i = 0; i < PUZZLE_HEIGHT_WIDTH; i++) {
			if (number == sudoku[j]) {
				return false;
			}
			j++;
		}
		return true;
	}

	/**
	 * Determines whether the given {@code number} can be placed in the given column
	 * without violating the rules of sudoku.
	 *
	 * @param col    which column to see if the number can go into
	 * @param number the number of interest
	 *
	 * @requires [{@code col} is a valid column index] and [{@code number} is a
	 *           valid digit]
	 * 
	 * @return true iff it is possible to place that number in the column without
	 *         violating the rule of 1 unique number per row.
	 */
	public boolean isValidForColumn(int col, int number) {
		assert 0 <= col && col < PUZZLE_HEIGHT_WIDTH : "Violation of valid column index";
		assert 0 < number && number <= PUZZLE_HEIGHT_WIDTH : "Violation of valid cadidate";

		int j = col;
		for (int i = 0; i < PUZZLE_HEIGHT_WIDTH; i++) {
			if (number == sudoku[j]) {
				return false;
			} else {
				// For the same reasons with Row, the index needs to properly "jump"
				// to the next "row" of 9 elements in order to check the rest of the column.
				j += 9;
			}
		}
		return true;
	}

	/**
	 * Determines whether the given {@code number} can be placed in "box" starting
	 * at the given position without violating the rules of sudoku.
	 * 
	 * The positions marked # are the valid positions of start of a box. They are
	 * (0,0), (0,3), (0, 6), (3,0), (3,3), (3,6), (6,0), (6,3), (6,6).
	 * 
	 * <pre>
	 * #00|#00|#00|
	 * 000|000|000|
	 * 000|000|000|
	 * ---+---+---+
	 * #00|#00|#00|
	 * 000|000|000|
	 * 000|000|000|
	 * ---+---+---+
	 * #00|#00|#00|
	 * 000|000|000|
	 * 000|000|000|
	 * ---+---+---+
	 * </pre>
	 *
	 * @param boxStartRow row index at which the box of interest starts
	 * @param boxStartCol column index at which the box of interest starts
	 * @param number      the number of interest
	 *
	 * @requires [{@code boxStartRow} and {@code boxStartCol} are valid box start
	 *           indices] and [{@code number} is a valid digit]
	 *
	 * @return true iff it is possible to place that number in the column without
	 *         violating the rule of 1 unique number per row.
	 */
	public boolean isValidForBox(int boxStartRow, int boxStartCol, int number) {
		assert 0 < number && number <= PUZZLE_HEIGHT_WIDTH : "Violation of valid cadidate";
		assert boxStartRow % BOX_HEIGHT_WIDTH == 0 : "Violation of valid boxStartRow";
		assert boxStartCol % BOX_HEIGHT_WIDTH == 0 : "Violation of valid boxStartCol";

		//A variable is initialized to indicate the proper starting index. Since the
		//box starting position is taken with coordinates (row, col), that position
		//needs to be transformed to the single digit index number.
		int k = (boxStartRow*9 + boxStartCol);
		
		//The validity needs to check a maximum of 9 times (it checks in a 3x3 square).
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				//An extra variable was used for iterating over the 3x3 square.
				//The variable needs to start at k, then move with j (aka the columns),
				//then "jump" a row by adding 9.
				int l = k + j;
				if (number == sudoku[l]) {
					return false;
				}
			}
			k+=9;
		}
		return true; 
	}

	/**
	 * Determines whether the given {@code number} can be placed in the given
	 * position without violating the rules of sudoku.
	 *
	 * @param row    which row to see if the number can go into
	 * @param col    which column to see if the number can go into
	 * @param number the number of interest
	 *
	 * @requires [{@code row} is a valid row index] and [{@code col} is a valid
	 *           column index] and [{@code number} is a valid digit]
	 * 
	 * @return true iff it is possible to place that number in the column without
	 *         violating the rule of 1 unique number per row.
	 */
	public boolean isValidForPosition(int row, int col, int number) {
		assert 0 <= row && row < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 <= col && col < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 < number && number <= PUZZLE_HEIGHT_WIDTH : "Violation of valid cadidate";

		if(this.isValidForRow(row, number) && this.isValidForColumn(col, number)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Methods from sudoku interface implemented here
	 */
	@Override
	public int element(int i, int j) {
		assert 0 <= i && i < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 <= j && j < PUZZLE_HEIGHT_WIDTH : "Violation of valid column index";

		//Same equation from the Box method, to convert (row,col) to a single index digit.
		int position = j + (i*9);
		return sudoku[position];
	}

	@Override
	public void setElement(int i, int j, int number) {
		assert 0 <= i && i < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 <= j && j < PUZZLE_HEIGHT_WIDTH : "Violation of valid column index";
		assert 0 <= number && number <= PUZZLE_HEIGHT_WIDTH : "Violation of valid cadidate";

		int position = j + (i*9);
		sudoku[position] = number;
	}	

	@Override
	public boolean solve() {
			for (int row = 0; row < PUZZLE_HEIGHT_WIDTH; row++) {
				for(int col = 0; col < PUZZLE_HEIGHT_WIDTH; col++) {
					//Conversion of (row, col) to index
					int position = col + (row*9);
					
					//This is for the isValidForBox method to run correctly.
					int boxStartRow = row - (row%3);
					int boxStartCol = col - (col%3);
					
					if(sudoku[position] == 0) {
						//Starts to check each number.
						for(int number = 1; number < 10; number++) {
							boolean validBox = this.isValidForBox(boxStartRow, boxStartCol, number);
							boolean validPosition = this.isValidForPosition(row, col, number);
							if(validBox == true && validPosition == true) {
								//If the number can be entered, it sets the number in the puzzle.
								this.setElement(row, col, number);
								count+=1;
								//This is the recursion. If it has completed all the numbers for the row,
								//It will return true and move on to the next row.
									if(solve()) {
										return true;
									}else {//Otherwise it will reset the number and try again.
										this.setElement(row, col, 0);;
									}
							}
						}
						//If it manages to fully complete the "number" loop, and it's not solved ("solve()"
						//never returns true), then it returns false and it can't be solved.
						return false;
					}
				}
			}
		//If it manages to fully complete all rows and columns, it returns true.
		accumulatedTime = System.nanoTime() - accumulatedTime;
		return true;
		}

	@Override
	public boolean verify() {
		if(this.solve() == false) {
			return false;
		}else {

		return true;
		}
	}

	/*
	 * Methods inherited from Object
	 */
	@Override
	public String toString() {
		String sudokuString = "";
		int numberAdded = 0;
		int rowMarker = 0;
		for (int i = 0; i < sudoku.length; i++) {
			numberAdded++;
			rowMarker++;
			if (sudoku[i] == 0) {
				sudokuString +=  " |";
			} else {
				sudokuString += sudoku[i] + "|";
			}
			if (numberAdded == 9) {
				sudokuString += "\n";
				numberAdded = 0;
			}
			if (rowMarker == 27) {
				sudokuString += "-----+-----+-----+" + "\n";
				rowMarker = 0;
			}
		}
		//Number of Guesses is commented out to pass the JUnit test.
		return sudokuString;// + "Number of Guesses: " + getGuessCount(); 
	}

	/**
	 * Main method. Produces the following output. You can modify it to debug and
	 * refine your implementation.
	 * 
	 * <pre>
	===== Sudoku puzzle =====
	
	| |3| |2| |6| | |
	9| | |3| |5| | |1|
	| |1|8| |6|4| | |
	-----+-----+-----+
	| |8|1| |2|9| | |
	7| | | | | | | |8|
	| |6|7| |8|2| | |
	-----+-----+-----+
	| |2|6| |9|5| | |
	8| | |2| |3| | |9|
	| |5| |1| |3| | |
	-----+-----+-----+
	
	
	
	===== Solving sudoku =====
	
	
	4|8|3|9|2|1|6|5|7|
	9|6|7|3|4|5|8|2|1|
	2|5|1|8|7|6|4|9|3|
	-----+-----+-----+
	5|4|8|1|3|2|9|7|6|
	7|2|9|5|6|4|1|3|8|
	1|3|6|7|9|8|2|4|5|
	-----+-----+-----+
	3|7|2|6|8|9|5|1|4|
	8|1|4|2|5|3|7|6|9|
	6|9|5|4|1|7|3|8|2|
	-----+-----+-----+
	 * </pre>
	 * 
	 * @param args command line arguments, not used
	 */
	public static void main(String[] args) {
		Sudoku s = new SudokuBacktrackRecursive("data/sudoku1.txt");

		System.out.println("===== Sudoku puzzle =====\n");
		System.out.println(s.toString());

		System.out.println("\n\n===== Solving sudoku =====\n\n");
		s.solve();
		System.out.println(s);
		System.out.println("Time ran: " + accumulatedTime/THOUSAND +"ms");
	}
}
