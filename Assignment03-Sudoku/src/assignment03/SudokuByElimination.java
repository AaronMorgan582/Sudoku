package assignment03;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * * Iterative implementation of the sudoku solver. * * @author Swaroop Joshi *
 */
public class SudokuByElimination implements Sudoku {
	/*
	 * Private fields and methods if required
	 */
	/**
	 * Each element is represented as a set of intgers {1,..,9}
	 */
	private ArrayList<Set<Integer>> data;
	private Set<Integer> nums;
	private int[] sudoku;
	private String[] stringArray;

	/**
	 * Creates a new puzzle by reading a file.
	 *
	 * @param filename Relative path of the file containing the puzzle in the given
	 *                 format
	 * @requires The file must be 9 rows of 9 numbers separated by whitespace the
	 *           numbers should be 1-9 or 0 representing an empty square
	 */
	public SudokuByElimination(String filename) {
		File file = new File(filename);
		Scanner scanner;
		nums = new HashSet<Integer>();
		data = new ArrayList<Set<Integer>>();
		sudoku = new int[81];
		stringArray = new String[9];
		
		try {
			scanner = new Scanner(file);

			for (int i = 1; i < 10; i++) {
				nums.add(i);
			}

			for (int i = 0; i < 81; i++) {
				Set<Integer> test = new HashSet<>();
				for (int j = 1; j < 10; j++) {
					test.add(j);
				}
				this.data.add(i, test);
			}

			for (int i = 0; i < stringArray.length; i++) {
				stringArray[i] = scanner.nextLine();
			}
			
			int k = 0;
			for (int i = 0; i < stringArray.length; i++) {
				for (int j = 0; j < stringArray[i].length(); j++) {
					sudoku[k] = Integer.parseInt(String.valueOf(stringArray[i].charAt(j)));
					k++;
				}
			}
			for (int i = 0; i < data.size(); i++) {
				if (sudoku[i] != 0) {
					this.replaceInts(data, i, this.setNum(sudoku[i]));
				}
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
	}

	/**
	 * this helper method converts the number you want in the ArrayList to a
	 * Set<Integer>
	 *
	 * @param num
	 *
	 * @return Set<Integer numSet
	 */
	private Set<Integer> setNum(int num) {
		Set<Integer> numSet = new HashSet<Integer>();
		numSet.add(num);
		return numSet;
	}

	/**
	 * this helper method gets rid of the set 1-9 with the number that belongs in
	 * the puzzle position
	 *
	 * @param data     lemon
	 * @param position
	 * @param num
	 * @return ArrayList<Set<Integer>> data
	 */
	private void replaceInts(ArrayList<Set<Integer>> data, int position, Set<Integer> num) {
		this.data.remove(position);
		this.data.add(position, num);
	}

	/**
	 * This helper method removes an element from a set at ArrayList's position
	 *
	 * @param numSet
	 * @param position
	 * @param element
	 * @return Set<Integer> numSet
	 */
	private void removeElement(Set<Integer> numSet, int position, Integer element) {
		if (numSet.size() > 1 && numSet.contains(element)) {
			numSet.remove(element);
		}
		data.remove(position);
		data.add(position, numSet);
	}

	/**
	 * This helper method gets the set's number
	 *
	 * @param numSet
	 * @return 0 or i : 0 if by some miracle the set doesn't contain 1-9, i if the
	 *         value is in the set
	 */
	private int getElement(Set<Integer> numSet) {
		for (Integer i = 1; i < 10; i++) {
			if (numSet.contains(i)) {
				return i;
			}
		}
		return 0;
	}

	/**
	 * Eliminates the {@code element} from {@code row}, {@code col}, and Box
	 * containing position (row,col).
	 *
	 * @param row     row index
	 * @param col     column index
	 * @param element to be eliminated
	 *
	 * @requires [{@code row} and {@code col} are valid indices]
	 */
	public void eliminate(int row, int col, int element) {
		assert 0 <= row && row < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 <= col && col < PUZZLE_HEIGHT_WIDTH : "Violation of valid col index";

		int position = 0;
		for (int i = 0; i < 9; i++) { // Eliminates every element at the row
			position = (row * 9 + i);
			if (i != col) {
				this.removeElement(data.get(position), position, element);
			}
		}
		position = 0;
		for (int i = 0; i < 9; i++) { // Eliminates the element at the column
			position = col + (i * 9);
			if (i != row) {
				this.removeElement(data.get(position), position, element);
			}

		}

		int startRow = row - (row % 3); // startRow and startCol ensure starting coordinate
		int startCol = col - (col % 3);
		int dataPosition = (startRow * 9 + startCol); // changes startRow, startCol to the position in data
		for (int i = 0; i < 3; i++) { // Eliminates the other elements in the box
			for (int j = 0; j < 3; j++) {
				this.removeElement(data.get(dataPosition), dataPosition, element);
				dataPosition++;
			}
			dataPosition += 6;
		}
	}

	@Override
	public int element(int row, int col) {
		assert 0 <= row && row < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 <= col && col < PUZZLE_HEIGHT_WIDTH : "Violation of valid col index";

		int element = (row * 9 + col); // Converts row & col to a usable variable in the ArrayList
		if (data.get(element).size() > 1) {
			return 0; // Returns 0 if 1-9 are still in the set (not fixed)
		} else {
			return this.getElement(data.get(element)); // Returns the int in the set (fixed)
		}
	}

	@Override
	public void setElement(int row, int col, int number) {
		assert 0 <= row && row < PUZZLE_HEIGHT_WIDTH : "Violation of valid row index";
		assert 0 <= col && col < PUZZLE_HEIGHT_WIDTH : "Violation of valid col index";
		assert 0 <= number && number <= PUZZLE_HEIGHT_WIDTH : "Violation of valid number";

		int element = (row * 9 + col); // Converts row & col to a usable variable in the ArrayList
		Set<Integer> set = new HashSet<>(); // Creates a new set containing number
		set.add(number);
		this.replaceInts(data, element, set); // replaces the old set with the new one
	}

	@Override
	public boolean solve() {
		int position = 0;
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				position = (row * 9 + col);
				if (sudoku[position] != 0) {
					this.eliminate(row, col, sudoku[position]);
				}
			}
		}

		position = 0;
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				position = (row * 9 + col);
				int minimumSize = data.get(position).size();
				int element = this.getElement(data.get(position));
				this.setElement(row, col, element);
				this.eliminate(row, col, element);
				//After eliminate has ran, check to see if any set has been reduced to 1.
			}
		}
		for (int i = 0; i < 81; i++) {
			if (data.get(i).size() > 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean verify() {

		return false; 
	}

	@Override
	public String toString() {
		String sudokuString = "";
		int numberAdded = 0;
		int rowMarker = 0;
		for (int i = 0; i < data.size(); i++) {
			numberAdded++;
			rowMarker++;
			if (data.get(i).size() > 1) {
				sudokuString += " |";
			} else {
				sudokuString += this.getElement(data.get(i)) + "|";
			}
			if (numberAdded == 9) {
				sudokuString += "\n";
				numberAdded = 0;
			}
			if (rowMarker == 27) {
				sudokuString += "-----+-----+-----+\n";
				rowMarker = 0;
			}
		}
		return sudokuString;
	}

	/**
	 * Main method, similar to the one in the other class.
	 *
	 * @param args command line arguments, not used
	 */
	public static void main(String[] args) {
		Sudoku s = new SudokuByElimination("data/sudoku1.txt");

		System.out.println("===== Sudoku puzzle =====\n");
		System.out.println(s.toString());

		System.out.println("\n\n===== Solving sudoku =====\n\n");
		s.solve();
		System.out.println(s);
	}
}
