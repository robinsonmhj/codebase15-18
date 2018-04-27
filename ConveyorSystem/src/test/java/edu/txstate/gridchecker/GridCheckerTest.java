package edu.txstate.gridchecker;


import static org.junit.Assert.*;

import org.junit.Test;

import edu.txstate.gridchecker.GridChecker;

/**
 * @author Haojie Ma
 * @date 05 13, 2017
 */

public class GridCheckerTest 
{
private GridChecker checker= new GridChecker();; 
	
	
	@Test
	public void testNull(){
		//invalid input
		//corner test case, input is null
		char[][] grid=null;
		assertFalse(checker.check(grid));
	}

	
	@Test()
	public void testNotEqualLength1(){
		//invalid input
		//testcase neither row and column meet requirement
		char[][] grid= new char[4][1];
		assertFalse(checker.check(grid));
	}
	
	
	@Test
	public void testNotEqualLength2(){
		//invalid input
		//testcase row is correct but column doesn't meet
		char[][] grid= new char[8][1];
		assertFalse(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence0(){
		/*testcase row and column number correct
		 * max number in a consecutive line is 0 
		 */
		char[][] grid= new char[8][8];
		assertFalse(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence1(){
		/*testcase row and column number correct
		 * max number in a consecutive line is 1 
		 */
		char[][] grid={{'a','0','0','0','0','0','0','0'},{'9','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'p','0','0','b','0','0','0','0'},{'0','0','7','4','3','2','0','0'},{'0','0','0','0','0','0','0','0'},{'-','=','v','t','p','[',']','1'},{'k','l','c','d','e','f','g','h'}};
		assertFalse(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence2(){
		/*testcase row and column number correct
		 * max number in a consecutive line is 2 
		 * this test case include both red and black checker and vertically, horizontally and diagonally
		 */
		char[][] grid={{'a','a','0','0','0','0','a','a'},{'a','a','0','0','0','0','a','a'},{'b','b','0','0','0','0','0','0'},{'b','b','0','b','0','0','0','0'},{'0','-','7','4','3','2','0','0'},{'0','0','0','0','0','0','0','0'},{'a','a','v','t','p','[','b','b'},{'a','a','c','d','e','f','b','b'}};
		assertFalse(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence3(){
		/*testcase row and column number correct
		 * max number in a consecutive line is 3
		 * this test case include both red and black checker and vertically, horizontally and diagonally
		 */
		char[][] grid={{'a','a','a','0','0','a','a','a'},{'a','a','a','0','0','a','a','a'},{'a','a','a','0','0','a','a','a'},{'b','b','b','u','0','0','0','0'},{'b','b','b','4','3','2','0','0'},{'b','b','b','0','0','b','b','b'},{'a','a','v','t','p','b','b','b'},{'a','a','c','d','e','b','b','b'}};
		assertFalse(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4RedH1(){
		/*testcase row and column number correct
		 *4 red on horizontal line
		 *corner case: on the first row and starts from 0,0
		 */
		char[][] grid={{'a','a','a','a','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4RedH2(){
		/*testcase row and column number correct
		 * 4 red on horizontal line
		 * regular case, not in the first or last row, and the last one ends in the last column
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','a','a','a','a'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4RedH3(){
		/*testcase row and column number correct
		 * 4 red on horizontal line
		 * corner case
		 * on the last row and not starts from first column and not ends in the last column  
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','a','a','a','a','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4RedV1(){
		/*testcase row and column number correct
		 * 4 red on vertical line 
		 * corner case
		 * on the first column and starts from 0,0
		 */
		char[][] grid={{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4RedV2(){
		/*testcase row and column number correct
		 * 4 red on vertical line 
		 * regular case
		 * in some column between first column and the last column and not starts from 1st row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4RedV3(){
		/*testcase row and column number correct
		 * 4 red on vertical line 
		 * corner case
		 * on the last column and ends at the last row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4RedD1(){
		/*testcase row and column number correct
		 * 4 red on diagonal line
		 * corner case
		 * starts from 0,0
		 */
		char[][] grid={{'a','0','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4RedD2(){
		/*testcase row and column number correct
		 * 4 red on diagonal line
		 * regular case
		 * not starts from 0,0 and not ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4RedD3(){
		/*testcase row and column number correct
		 * 4 red on diagonal line
		 * corner case
		 * ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','0','0','a','0'},{'0','0','0','0','0','0','0','a'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4RedDA1(){
		/*testcase row and column number correct
		 * 4 red on diagonal line, the other way
		 * corner case 
		 * starts from 0,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','a','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4RedDA2(){
		/*testcase row and column number correct
		 * 4 red on diagonal line, the other way
		 * regular case 
		 * not starts from 0,7 and not ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','a','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4RedDA3(){
		/*testcase row and column number correct
		 * 4 red on diagonal line, the other way
		 * corner case 
		 * ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedH1(){
		/*testcase row and column number correct
		 *5 red on horizontal line
		 *corner case: on the first row and starts from 0,0
		 */
		char[][] grid={{'a','a','a','a','a','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedH2(){
		/*testcase row and column number correct
		 * 5 red on horizontal line
		 * regular case, not in the first or last row, and the last one ends in the last column
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','a','a','a','a','a'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5RedH3(){
		/*testcase row and column number correct
		 * 5 red on horizontal line
		 * corner case
		 * on the last row and not starts from first column and not ends in the last column  
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','a','a','a','a','a','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedV1(){
		/*testcase row and column number correct
		 * 5 red on vertical line 
		 * corner case
		 * on the first column and starts from 0,0
		 */
		char[][] grid={{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedV2(){
		/*testcase row and column number correct
		 * 5 red on vertical line 
		 * regular case
		 * in some column between first column and the last column and not starts from 1st row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5RedV3(){
		/*testcase row and column number correct
		 * 5 red on vertical line 
		 * corner case
		 * on the last column and ends at the last row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','0','a'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedD1(){
		/*testcase row and column number correct
		 * 5 red on diagonal line
		 * corner case
		 * starts from 0,0
		 */
		char[][] grid={{'a','0','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5RedD2(){
		/*testcase row and column number correct
		 * 5 red on diagonal line
		 * regular case
		 * not starts from 0,0 and not ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5RedD3(){
		/*testcase row and column number correct
		 * 5 red on diagonal line
		 * corner case
		 * ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','0','0','a','0'},{'0','0','0','0','0','0','0','a'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5RedDA1(){
		/*testcase row and column number correct
		 * 5 red on diagonal line, the other way
		 * corner case 
		 * starts from 0,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','a'},{'0','0','0','0','0','0','a','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedDA2(){
		/*testcase row and column number correct
		 * 5 red on diagonal line, the other way
		 * regular case 
		 * not starts from 0,7 and not ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','a','0'},{'0','0','0','0','0','a','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5RedDA3(){
		/*testcase row and column number correct
		 * 5 red on diagonal line, the other way
		 * corner case 
		 * ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','a','0','0','0'},{'0','0','0','a','0','0','0','0'},{'0','0','a','0','0','0','0','0'},{'0','a','0','0','0','0','0','0'},{'a','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackH1(){
		/*testcase row and column number correct
		 *4 black on horizontal line
		 *corner case: on the first row and starts from 0,0
		 */
		char[][] grid={{'b','b','b','b','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackH2(){
		/*testcase row and column number correct
		 * 4 black on horizontal line
		 * regular case, not in the first or last row, and the last one ends in the last column
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','b','b','b','b'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4BlackH3(){
		/*testcase row and column number correct
		 * 4 black on horizontal line
		 * corner case
		 * on the last row and not starts from first column and not ends in the last column  
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','b','b','b','b','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackV1(){
		/*testcase row and column number correct
		 * 4 black on vertical line 
		 * corner case
		 * on the first column and starts from 0,0
		 */
		char[][] grid={{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackV2(){
		/*testcase row and column number correct
		 * 4 black on vertical line 
		 * regular case
		 * in some column between first column and the last column and not starts from 1st row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4BlackV3(){
		/*testcase row and column number correct
		 * 4 black on vertical line 
		 * corner case
		 * on the last column and ends at the last row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackD1(){
		/*testcase row and column number correct
		 * 4 black on diagonal line
		 * corner case
		 * starts from 0,0
		 */
		char[][] grid={{'b','0','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4BlackD2(){
		/*testcase row and column number correct
		 * 4 black on diagonal line
		 * regular case
		 * not starts from 0,0 and not ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4BlackD3(){
		/*testcase row and column number correct
		 * 4 black on diagonal line
		 * corner case
		 * ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','0','0','b','0'},{'0','0','0','0','0','0','0','b'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence4BlackDA1(){
		/*testcase row and column number correct
		 * 4 black on diagonal line, the other way
		 * corner case 
		 * starts from 0,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','b','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackDA2(){
		/*testcase row and column number correct
		 * 4 black on diagonal line, the other way
		 * regular case 
		 * not starts from 0,7 and not ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','b','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence4BlackDA3(){
		/*testcase row and column number correct
		 * 4 black on diagonal line, the other way
		 * corner case 
		 * ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5BlackH1(){
		/*testcase row and column number correct
		 *5 black on horizontal line
		 *corner case: on the first row and starts from 0,0
		 */
		char[][] grid={{'b','b','b','b','b','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5BlackH2(){
		/*testcase row and column number correct
		 * 5 black on horizontal line
		 * regular case, not in the first or last row, and the last one ends in the last column
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','b','b','b','b','b'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5BlackH3(){
		/*testcase row and column number correct
		 * 5 black on horizontal line
		 * corner case
		 * on the last row and not starts from first column and not ends in the last column  
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','b','b','b','b','b','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5BlackV1(){
		/*testcase row and column number correct
		 * 5 black on vertical line 
		 * corner case
		 * on the first column and starts from 0,0
		 */
		char[][] grid={{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5BlackV2(){
		/*testcase row and column number correct
		 * 5 black on vertical line 
		 * regular case
		 * in some column between first column and the last column and not starts from 1st row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5BlackV3(){
		/*testcase row and column number correct
		 * 5 black on vertical line 
		 * corner case
		 * on the last column and ends at the last row
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','0','b'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5BlackD1(){
		/*testcase row and column number correct
		 * 5 black on diagonal line
		 * corner case
		 * starts from 0,0
		 */
		char[][] grid={{'b','0','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5BlackD2(){
		/*testcase row and column number correct
		 * 5 black on diagonal line
		 * regular case
		 * not starts from 0,0 and not ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5BlackD3(){
		/*testcase row and column number correct
		 * 5 black on diagonal line
		 * corner case
		 * ends at 7,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','0','0','b','0'},{'0','0','0','0','0','0','0','b'}};				
		assertTrue(checker.check(grid));
	}
	
	
	@Test
	public void testMaxSequence5BlackDA1(){
		/*testcase row and column number correct
		 * 5 black on diagonal line, the other way
		 * corner case 
		 * starts from 0,7
		 */
		char[][] grid={{'0','0','0','0','0','0','0','b'},{'0','0','0','0','0','0','b','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5BlackDA2(){
		/*testcase row and column number correct
		 * 5 black on diagonal line, the other way
		 * regular case 
		 * not starts from 0,7 and not ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','b','0'},{'0','0','0','0','0','b','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
	@Test
	public void testMaxSequence5BlackDA3(){
		/*testcase row and column number correct
		 * 5 black on diagonal line, the other way
		 * corner case 
		 * ends at 7,0
		 */
		char[][] grid={{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','0','0','0','0'},{'0','0','0','0','b','0','0','0'},{'0','0','0','b','0','0','0','0'},{'0','0','b','0','0','0','0','0'},{'0','b','0','0','0','0','0','0'},{'b','0','0','0','0','0','0','0'}};				
		assertTrue(checker.check(grid));
	}
	
}
