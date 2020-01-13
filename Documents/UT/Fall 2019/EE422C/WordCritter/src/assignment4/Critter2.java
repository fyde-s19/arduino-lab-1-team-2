/*
 * CRITTERS Critter2.java
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
 * This Critter will walk in a random direction for every do timestep
 * for the fight the critter will always fight other critters of the same type
 * otherwise it checks if it is above half its energy if so it will also fight else it will not want to fight
 */
public class Critter2 extends Critter {
	/**
	 * Every timestep, the critter will walk in a random direction.
	 */
    @Override
    public void doTimeStep() {
        walk(getRandomInt(8));
    }
    
    /**
     * The critter will always fight another Critter2, else it will only fight if it has above 1/2 
     * of the start energy.
     */
    @Override
    public boolean fight(String oponent) {
        if(oponent.equals("Critter2"))
            return true;
        else return getEnergy() >= Params.START_ENERGY / 2;
    }
    
    /**
     * Critter2s are represented with a 2 in the world grid.
     */
    public String toString() {
            return "2";
    }
}