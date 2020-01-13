/*
 * CRITTERS Critter1.java
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

/**
 * @author Viraj Parikh
 * This critter will run North East every time step and reproduce as well.
 * This critter is very aggresive and will not run away from any fight so it returns
 * true everytime it is challanged to a fight
 */
public class Critter1 extends Critter {

	/**
	 * Every timeStep, the critter will always run northeast and reproduce once, with it's
	 * child placed to the north.
	 */
    @Override
    public void doTimeStep() {
        run(1);
        Critter oneBaby = new Critter1();
        reproduce(oneBaby,2);
    }

    /**
     * The critter will always fight.
     */
    @Override
    public boolean fight(String oponent) {
        return true;
    }

    /**
     * The Critter1 is always represented with a 1 in the grid.
     */
    public String toString() {
        return "1";
    }

}
