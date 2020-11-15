package assignment03;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test cases for public methods of the {@code SudokuByElimination} class.
 * 
 * @author Swaroop Joshi
 *
 */
public class SudokuByEliminationTest {
	private SudokuByElimination s;

	@Before // executed before every test case
	public void setUp() {
		s = new SudokuByElimination("data/sudoku1.txt");
	}

	@Test
	public void testToString() {
		assertEquals(
				" | |3| |2| |6| | |\n" + //
				"9| | |3| |5| | |1|\n" + //
				" | |1|8| |6|4| | |\n" + //
				"-----+-----+-----+\n" + //
				" | |8|1| |2|9| | |\n" + //
				"7| | | | | | | |8|\n" + //
				" | |6|7| |8|2| | |\n" + //
				"-----+-----+-----+\n" + //
				" | |2|6| |9|5| | |\n" + //
				"8| | |2| |3| | |9|\n" + //
				" | |5| |1| |3| | |\n" + //
				"-----+-----+-----+\n", s.toString());
	}
	
	@Test
	public void testSolve() {
		s.solve();
		assertEquals(
				"4|8|3|9|2|1|6|5|7|\n" + 
				"9|6|7|3|4|5|8|2|1|\n" + 
				"2|5|1|8|7|6|4|9|3|\n" + 
				"-----+-----+-----+\n" + 
				"5|4|8|1|3|2|9|7|6|\n" + 
				"7|2|9|5|6|4|1|3|8|\n" + 
				"1|3|6|7|9|8|2|4|5|\n" + 
				"-----+-----+-----+\n" + 
				"3|7|2|6|8|9|5|1|4|\n" + 
				"8|1|4|2|5|3|7|6|9|\n" + 
				"6|9|5|4|1|7|3|8|2|\n" + 
				"-----+-----+-----+\n", s.toString());
	}
}
