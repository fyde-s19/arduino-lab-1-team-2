package assignment3;

import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

import org.junit.*;

public class WordLadderTests
{
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

	
	
	/** Has Word Ladder **/
	@Test(timeout = 30000)
	public void testBFSHasLadder1() {
		ArrayList<String> res = Main.getWordLadderBFS("smart", "money");

		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
		assertTrue(res.size() < 12);
	}
	@Test(timeout = 30000)
	public void testDFSHasLadder1() {
		ArrayList<String> res = Main.getWordLadderDFS("hello", "cells");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testBFSHasLadder2() {
		ArrayList<String> res = Main.getWordLadderBFS("TESTS", "WORKS");

		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
		assertTrue(res.size() < 12);
	}
	@Test(timeout = 30000)
	public void testDFSHasLadder2() {
		ArrayList<String> res = Main.getWordLadderDFS("STEAM", "STORE");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testBFSHasLadder3() {
		ArrayList<String> res = Main.getWordLadderBFS("PoWeR", "hApPy");

		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
		assertTrue(res.size() < 12);
	}
	@Test(timeout = 30000)
	public void testDFSHasLadder3() {
		ArrayList<String> res = Main.getWordLadderDFS("EiGhT", "fRiEs");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(verifyLadder(res));
		assertFalse(res == null || res.size() == 0 || res.size() == 2);
	}



	/** No Word Ladder **/
	@Test(timeout = 30000)
	public void testBFSNoLadder1() {
		ArrayList<String> res = Main.getWordLadderBFS("Aldol", "Drawl");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);

	}
	@Test(timeout = 30000)
	public void testDFSNoLadder1() {
		ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	
	
	
	/** Start Word is the End Word **/
	@Test(timeout = 30000)
	public void testBFSSameWord1() {
		ArrayList<String> res = Main.getWordLadderBFS("start", "start");
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testDFSSameWord1() {
		ArrayList<String> res = Main.getWordLadderDFS("aldol", "Aldol");
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	
	/** Start/End Words not in Dictionary **/
	@Test(timeout = 30000)
	public void testBFSInvalidWord1() {
		ArrayList<String> res = Main.getWordLadderBFS("aaaaa", "start");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testDFSInvalidWord1() {
		ArrayList<String> res = Main.getWordLadderDFS("strgg", "end");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testBFSInvalidWord2() {
		ArrayList<String> res = Main.getWordLadderBFS("start", "aaaaa");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testDFSInvalidWord2() {
		ArrayList<String> res = Main.getWordLadderDFS("end", "strgg");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testBFSInvalidWord3() {
		ArrayList<String> res = Main.getWordLadderBFS("lEGhP", "QEfgr");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}
	@Test(timeout = 30000)
	public void testDFSInvalidWord3() {
		ArrayList<String> res = Main.getWordLadderDFS("STOqN", "12345");
		if (res != null) {
			HashSet<String> set = new HashSet<String>(res);
			assertEquals(set.size(), res.size());
		}
		assertTrue(res == null || res.size() == 0 || res.size() == 2);
	}	
}
