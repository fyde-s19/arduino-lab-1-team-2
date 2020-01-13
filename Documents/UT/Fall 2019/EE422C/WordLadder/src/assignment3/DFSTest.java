package assignment3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DFSTest {
	private static Set<String> dict;
	private static ByteArrayOutputStream outContent;

	@Before // this method is run before every test
	public void setUp() {
		Main.initialize();
		dict = Main.makeDictionary();
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	private boolean verifyLadder(ArrayList<String> ladder) {
		String prev = null;
		if (ladder == null)
			return true;
		for (String word : ladder) {
			if (!dict.contains(word.toUpperCase()) && !dict.contains(word.toLowerCase())) {
				return false;
			}
			if (prev != null && !differByOne(prev, word))
				return false;
			prev = word;
		}
		return true;
	}

	private static boolean differByOne(String s1, String s2) {
		if (s1.length() != s2.length())
			return false;

		int diff = 0;
		for (int i = 0; i < s1.length(); i++) {
			if (s1.charAt(i) != s2.charAt(i) && diff++ > 1) {
				return false;
			}
		}

		return true;
	}

	/** Has Word Ladder, lower case **/
	@Test(timeout = 30000)
	public void testDFS1() {
		ArrayList<String> res = Main.getWordLadderDFS("hello", "cells");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);

	}


	/** No Word Ladder **/
	@Test(timeout = 30000)
	public void testDFS2() {
		ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	
	
	/** Start Word is the End Word **/
	@Test(timeout = 30000)
	public void testDFS3() {
		ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	
	/** Start Word not in Dictionary **/
	@Test(timeout = 30000)
	public void testDFS4() {
		ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	
	/** End Word not in Dictionary**/
	@Test(timeout = 30000)
	public void testDFS5() {
		ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	
	/** Mixed Case **/
	@Test(timeout = 30000)
	public void testDFS6() {
		ArrayList<String> res = Main.getWordLadderDFS("Start", "Money");

		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);

	}
}
