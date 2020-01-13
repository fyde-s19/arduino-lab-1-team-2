/*
 * CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * Rishabh Parekh
 * rbp668
 * 16185
 * Viraj Parikh
 * vhp286
 * 16180
 * Slip days used: <0>
 * Spring 2019
 */

package assignment4;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.*;

/*
 * Usage: java <pkg name>.Main <input file> test input file is
 * optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */

public class Main {

    /* Scanner connected to keyboard input, or input file */
    static Scanner kb;

    /* Input file, used instead of keyboard input if specified */
    private static String inputFile;

    /* If test specified, holds all console output */
    static ByteArrayOutputStream testOutputString;

    /* Use it or not, as you wish! */
    private static boolean DEBUG = false;

    /* if you want to restore output to console */
    static PrintStream old = System.out;

    /* Gets the package name.  The usage assumes that Critter and its
       subclasses are all in the same package. */
    private static String myPackage; // package of Critter file.

    /* Critter cannot be in default pkg. */
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     *
     * @param args args can be empty.  If not empty, provide two
     *             parameters -- the first is a file name, and the
     *             second is test (for test output, where all output
     *             to be directed to a String), or nothing.
     */
    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java <pkg name>.Main OR java <pkg name>.Main <input file> <test output>");
            }
            if (args.length >= 2) {
                /* If the word "test" is the second argument to java */
                if (args[1].equals("test")) {
                    /* Create a stream to hold the output */
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    /* Save the old System.out. */
                    old = System.out;
                    /* Tell Java to use the special stream; all
                     * console output will be redirected here from
                     * now */
                    System.setOut(ps);
                }
            }
        } else { // If no arguments to main
            kb = new Scanner(System.in); // Use keyboard and console
        }
        commandInterpreter(kb);

        System.out.flush();
    }

    /* Do not alter the code above for your submission. */
    
    /**
     * commandInterpreter processes the user input for the WordCritter game.
     * The interpreter is open to new input as long as "quit" has not be 
     * instructed by the user. The interpreter will give negative feedback if
     * the user enters a bad line of instructions.
     * @param kb is the scanner of the console or an input file.
     */

    private static void commandInterpreter(Scanner kb) {
    	String entry = kb.nextLine();
    	
    	String[] in = entry.split(" ");
    	
		String invalidCommand = "error processing: ";
		while(entry.compareTo("quit") != 0){
			if (entry.length() >= 4 && in[0].equals("show")) {
			    
                if (entry.length() > 4) {
                    System.out.println(invalidCommand+entry);
                }
                else {
                    Critter.displayWorld();
                }
               entry = kb.nextLine();
               in = entry.split(" ");
               continue;
               
            } else if (entry.length() >= 4 && in[0].equals("step")) {
            	
			    String remainingEntry = entry.substring(4);
			    try {
                    if (!remainingEntry.equals("")) {
                        remainingEntry = remainingEntry.substring(1);
                        int numSteps = Integer.parseInt(remainingEntry);
                        for (int i = 0; i < numSteps; i++) {
                            Critter.worldTimeStep();
                        }
                    } else
                        Critter.worldTimeStep();
                } catch(NullPointerException|NumberFormatException e){
			        System.out.println(invalidCommand+entry);
                } finally{
                    entry = kb.nextLine();
                    in = entry.split(" ");
                }
			    continue;
			    
			} else if (entry.length() >= 4 && in[0].equals("seed")) {
				
			    String remainingEntry = entry.substring(4);
			    try{
                    if (!remainingEntry.equals("")) {
                        remainingEntry = remainingEntry.substring(1);
                        long numSeed = Long.parseLong(remainingEntry);
                        Critter.setSeed((long) numSeed);
                    } else{                    	
                    	System.out.println(invalidCommand+entry);
			        }
                }catch (NullPointerException|NumberFormatException e){
                    System.out.println(invalidCommand+entry);
                } finally{
                    entry = kb.nextLine();
                    in = entry.split(" ");
                }
			    continue;
			    
			} else if (entry.length() >= 6 && in[0].equals("create")) {
				
			    String className = entry.substring(6);
			    try{
			        className = className.substring(1);
                    int indexOfSpace = className.indexOf(" ");
                    String remainingEntry = "";
                    if(indexOfSpace != -1){
                        remainingEntry = className.substring(indexOfSpace+1);
                        className = className.substring(0,indexOfSpace);
                    }
                    if(!remainingEntry.equals("")){
                        try {
                            int numCritters = Integer.parseInt(remainingEntry);
                            for(int i = 0;i<numCritters;i++)
                                Critter.createCritter(className);
                        }
                        catch(InvalidCritterException|NullPointerException|NumberFormatException e){
                            System.out.println("error processing: " + entry); //here if classname is wrong
                        }
                    }
                    else{
                        try {
                            Critter.createCritter(className);
                        }
                        catch(InvalidCritterException|NullPointerException|NumberFormatException e){
                            System.out.println("error processing: " + entry); //here if classname is wrong
                        }
                    }
                } catch(Exception e){
                    System.out.println(invalidCommand+entry); //Here if didnt put class name after create or command is bad after create
                } finally {
                    entry = kb.nextLine();
                    in = entry.split(" ");
                }
			    continue;
			    
            } else if(entry.length() >= 5 && in[0].equals("stats")){
            	
            	String className = entry.substring(5);
 			    try{
 			         className = className.substring(1);
                     int indexOfSpace = className.indexOf(" ");
                     
                     if(indexOfSpace != -1){
                    	 System.out.println(invalidCommand+entry); 
                     } else{
                    	 List<Critter> c = null;
                    	 Class<?> critClass = null;
         				 Class<?>[] types = {List.class};
                         try {
                        	 c = Critter.getInstances(className);
                        	 critClass = Class.forName(myPackage + "." + className);
          					 Method runStats = critClass.getMethod("runStats", types);
          					 runStats.invoke(critClass, c);
                         }
                         catch(InvalidCritterException e){
                             System.out.println("error processing: " + entry); //here if classname is wrong
                         }
                     }
                 } catch(Exception e){
                	 System.out.println(invalidCommand+entry); //Here if didnt put class name after create or command is bad after create
                 } finally {
                     entry = kb.nextLine();
                     in = entry.split(" ");
                 }
			    continue;
			    
            } else if(entry.length() >= 5 && in[0].equals("clear")){
            	
            	if (entry.length() > 5) 
            		 System.out.println(invalidCommand+entry);
                else 
            		Critter.clearWorld();
            	
			    entry = kb.nextLine();
			    in = entry.split(" ");
			    
			    continue;
			    
            } else{
			    //invalid
                System.out.println("invalid command: " + entry);
                entry = kb.nextLine();
                in = entry.split(" ");
            }
		}
    }
}
