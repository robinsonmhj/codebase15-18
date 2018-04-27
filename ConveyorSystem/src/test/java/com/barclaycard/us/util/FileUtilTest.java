/**
 * 
 */
package com.barclaycard.us.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author Haojie Ma Mar 25, 2018
 */
public class FileUtilTest {
	
	String dir="TestCase/UtilTestCase/";

	// testcase which test if file Not exists
	@Test
	public void testFileNotExist() {
		assertTrue(FileUtil.readFile("").size() == 0);
	}

	// testcase which test if file Not exists
	@Test
	public void testEmptyFile() {
		assertTrue(FileUtil.readFile(dir+"emptyFile.txt").size() == 0);
	}

	// testcase which test if file Not exists
	@Test
	public void testNormalFile() {
		String fileName=dir+"4Lines.txt";
		List<String> res=FileUtil.readFile(fileName);
		assertTrue( res.size()== 4);
		List<String> l= new ArrayList<String>();
		l.add("line 1");
		l.add("line 2");
		l.add("line 3");
		l.add("line 4");
		assertTrue(res.equals(l));
	}

	// testcase which test if file Not exists
	@Test
	public void testNormalFileWithInvalid() {
		String fileName=dir+"5LinesAndInvalidLine.txt";
		List<String> res=FileUtil.readFile(fileName);
		assertTrue(res.size() == 5);
		List<String> l= new ArrayList<String>();
		l.add("l1");
		l.add("l2");
		l.add("l3");
		l.add("l4");
		l.add("l5");
		assertTrue(res.equals(l));
	}

}
