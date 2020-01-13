/*
 * CRITTERS Critter4.java
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
 * This Critter will on run in TimeStep if it has more than twice the RUN_COST
 * In a fight, it'll only fight if it's energy is greater than REST COST,
 *
 */

public class Critter4 extends Critter{
	/**
	 * Critter4s always run west if it was more than twice the run energy cost.
	 */
	@Override
	public void doTimeStep() {
		if (getEnergy() > Params.RUN_ENERGY_COST * 2)
			run(4);
	}

	/**
	 * Critter4s always fight only if it has more than the rest energy cost.
	 */
	@Override
	public boolean fight(String oponent) {
		if (getEnergy() > Params.REST_ENERGY_COST)
			return true;
		return false;
	}
	
	/**
	 * Critter4s are represented with a 4 in the world grid.
	 */
	@Override
    public String toString() {
        return "4";
    }
}
