/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Rishabh Parekh
 * rbp668
 * 16185
 * Christopher Saenz
 * cgs2258
 * 16185
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/assignment3-fa-19-fa19-pr3-pair-8
 * Fall 2019
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static Set<String> dictionary;
	static Comparator<String> wordComp;
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		
		ArrayList<String> initWords = parse(kb);
		String firstWord;
		String secondWord;
		
		while (initWords != null) {
			firstWord = initWords.get(0).toUpperCase();
			secondWord = initWords.get(1).toUpperCase();
			
			System.out.println("DFS");
			ArrayList<String> ladderDFS = getWordLadderDFS(firstWord, secondWord);
			printLadder(ladderDFS);

			System.out.println("\nBFS");
			ArrayList<String> ladderBFS = getWordLadderBFS(firstWord, secondWord);
			printLadder(ladderBFS);	
			
			System.out.println();
			initWords = parse(kb);
		}

	}
	
	public static void initialize(){
		dictionary = makeDictionary();
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		String guess = keyboard.next();
		
		if (guess.compareTo("/quit") == 0)
			return null;
		else {
			String guess2 = keyboard.next();
			ArrayList<String> ans = new ArrayList<String>();
			ans.add(guess);
			ans.add(guess2);
			return ans;
		}
	}
	
	/**
	 * Uses depth-first search to find word ladder
	 * @param start is the beginning word of the ladder
	 * @param end is the destination word of the ladder
	 * @return a ladder in the form of an ArrayList of Strings
	 */	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Dictionary's all uppercase, so our inputs need to be as well
		start = start.toUpperCase();
		end = end.toUpperCase();
		
		// if things fail
		ArrayList<String> ans = new ArrayList<String>();
		ans.add(start);
		ans.add(end);
		
		if (!dictionary.contains(start) || !dictionary.contains(end) || start.compareTo(end) == 0)
			return ans;
	
		ArrayList<String> ladder = new ArrayList<String>();
		Set<String> discovered_DFS = new HashSet<String>();
		
		wordComp = new WordComparator(end);
		
		if (findLadder(start, end, discovered_DFS, ladder))
			return ladder;
		else 
			return ans;
	}

	/**
	 * Uses breadth-first search to find word ladder
	 * @param start is the beginning word of the ladder
	 * @param end is the destination word of the ladder
	 * @return a ladder in the form of an ArrayList of Strings
	 */	
	public static ArrayList<String> getWordLadderBFS(String start, String end){
		
		start = start.toUpperCase();
		end = end.toUpperCase();
    	
    	ArrayList<String> ans = new ArrayList<String>();	// this is created for fail cases - basic 0-rung ladder
		ans.add(start);
		ans.add(end);
		
		if (!dictionary.contains(start) || !dictionary.contains(end) || start.compareTo(end) == 0)
			return ans;			//	return if the words are invalid or equal
		
        	
    	Queue<String[]> q = new LinkedList<String[]>();				// queue of arraylists because we want
    	String[] temp = {start, null};								// to store the word and it's parent 
    	q.add(temp);  												// in the form: (word, parent)
    	    	
    	Set<String> discovered = new HashSet<String>();
    	
    	ArrayList<String[]> ladder = new ArrayList<String[]>();		// the ladder with all tries
    	
    	ArrayList<String> shortestLadder = new ArrayList<String>();						// ladder with final answer
 
//    	PROCESS: go through the queue adding all the words to discovered, so we don't have repeats in the ladders
    	while(!q.isEmpty()) {
    		String[] dequed = q.remove();
    		discovered.add(dequed[0]);
        	
    		ladder.add(dequed);
    		
    		ArrayList<String[]> adjacent = new ArrayList<String[]>();
    		ArrayList<String> possibleAdj = allOneDifferent(dequed[0]);
    		
    		for (String word : possibleAdj) {
    			if(!discovered.contains(word)) {
    				String[] temp2 = {word, dequed[0]};    				
    				adjacent.add(temp2);
    			}
    		}
    		
    		for (String[] word : adjacent) { 			
        		if(dequed[0].compareToIgnoreCase(end) == 0) 
        			return processLadder(start, end, ladder, shortestLadder);
        		else {
        			if(!discovered.contains(word[0])) {
        				String[] temp2 = {word[0], dequed[0]};
        				q.add(temp2);
        			}
        		}
        			
    		}	
    	}	
    	
		return ans; 
	}
    
	/**
	 * Prints ladder in correct format.
	 * @param ladder, an existing word ladder found through either BFS or DFS
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder.size() <= 2) {
			System.out.println("no word ladder can be found between " + ladder.get(0).toLowerCase() + 
						" and " + ladder.get(ladder.size()-1).toLowerCase() + ".");
		} else {
			System.out.println("a " + (ladder.size() - 2) + "-rung word ladder exists between " + 
					ladder.get(0).toLowerCase() + " and " + ladder.get(ladder.size()-1).toLowerCase() + ".");

			for (int i = 0; i < ladder.size(); i++)
				System.out.println(ladder.get(i).toLowerCase());
		}	
	}

	
	/**
	 * Recursive helper method for getWordLadderDFS
	 * @param begin is the beginning word of the sub-ladder
	 * @param end is the destination word of the ladder
	 * @param discovered, a list of words we've already traversed
	 * @param ladder is a local ladder attempt. Eventually holds the final
	 * 			ladder
	 * @return a boolean that lets us know if we've found a ladder
	 */
	private static boolean findLadder(String begin, String end, Set<String> discovered_DFS, ArrayList<String> ladder) {
		if (begin == null)
			return false;
		
		
		discovered_DFS.add(begin);
		ladder.add(begin);
		
		if (begin.compareTo(end) == 0)   //	base case: begin equals end
			return true;
		else {
			ArrayList<String> adjacent = new ArrayList<String>();   //	holds all words one letter away from adjacent
    		
			ArrayList<String> possibleAdj = allOneDifferent(begin);
    		
    		for (String word : possibleAdj)  	//	make a list of all words one letter away from begin
    			if(!discovered_DFS.contains(word)) 
    				adjacent.add(word);
    		
   		Collections.sort(adjacent, wordComp);
    			
    		for (String adj : adjacent) //	recursive case: do findLadder for all words in adjacent
    			if (findLadder(adj, end, discovered_DFS, ladder))
    				return true;
    		
    		ladder.remove(begin);  //	fail case: if we've run out of words to check for ladder from, we can't find a ladder from this path
    		return false;	
		}
		
	}
	
	/**
	 * Finds all valid words one letter different from str and returns them in an ArrayList
	 * @param str, a word
	 * @return arrayList of words one letter different from str
	 */	
	private static ArrayList<String> allOneDifferent(String str){
		ArrayList<String> ret = new ArrayList<String>();
		String tempCheck = "";
		char[] chArr = str.toCharArray();
		for(int i = 0; i < chArr.length; i++){
			char c = chArr[i];
			
			for(char change = 'A'; change <= 'Z'; change++){
				chArr[i] = change;
				tempCheck = new String(chArr);
				if(dictionary.contains(tempCheck) && tempCheck.compareTo(str) != 0){
					ret.add(tempCheck);
				}
			}
			chArr[i] = c;
		}
		return ret;
	}
	
	/**
	 * finds s in l; used in processLadder
	 * @param l, an arrayList of strings
	 * @param s, the string we want to find
	 * @return
	 */
	private static int findInd(ArrayList<String[]> l, String s) {
		int i = 0;
		for(String[] pair : l) {
			if (pair[0].compareTo(s) == 0)
				break;
			i++;
		}
		
		return i;
	}
	
	/**
	 * Creates the shortest ladder path from all attempts, using parents
	 * @param start is the beginning word of the ladder
	 * @param end is the destination of the ladder
	 * @param ladders is the collection of all attempts
	 * @param shortestladder holds the shortest ladder
	 * @return
	 */
	private static ArrayList<String> processLadder(String s, String e, ArrayList<String[]> l, ArrayList<String> sl){
		
		String[] destination = l.get(l.size() - 1);
		sl.add(0, destination[0]);
		
		boolean foundStart = false;
		
		while (!foundStart) {
			int idx = findInd(l, destination[1]);
			destination = l.get(idx);
			
			sl.add(0, destination[0]);
			
			if(destination[0].compareTo(s) == 0)
				foundStart = true;
		}

		return sl;
	}

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	
	
}
