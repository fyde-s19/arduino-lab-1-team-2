/*
 * CRITTERS Critter3.java
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
 * @author Rishabh Parekh
 * This Critter will always run east in TimeStep and Fight
 */

public class Critter3 extends Critter {
	/**
	 * The Critter will always run east in doTimeStep.
	 */
	@Override
	public void doTimeStep() {
		run(0);
	}

	/**
	 * The Critter always flees a fight to the east.
	 */
	@Override
	public boolean fight(String oponent) {
		run(0);
		return false;
	}
	
	/**
	 * Critter3s are represented with a 3 in the world grid.
	 */
	@Override
    public String toString() {
        return "3";
    }

}
