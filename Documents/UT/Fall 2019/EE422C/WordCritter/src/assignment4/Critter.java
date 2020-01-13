/*
 * CRITTERS Critter.java
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

import javax.script.ScriptContext;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/* 
 * See the PDF for descriptions of the methods and fields in this
 * class. 
 * You may add fields, methods or inner classes to Critter ONLY
 * if you make your additions private; no new public, protected or
 * default-package code or data can be added to Critter.
 */

public abstract class Critter {

    private int energy = 0;

    private int x_coord;
    private int y_coord;

    private static int timeStep = 0;
    private static List<Critter> population = new ArrayList<Critter>();
    private static List<Critter> babies = new ArrayList<Critter>();

    /* Gets the package name.  This assumes that Critter and its
     * subclasses are all in the same package. */
    private static String myPackage;

    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    private static Random rand = new Random();

    /**
     * Returns a random integer.
     * @param max is a the max value of the random integer.
     * @return a random integer with a max value: parameter max
     */
    public static int getRandomInt(int max) {
        return rand.nextInt(max);
    }

    /**
     * Sets the seed for the random integer generator
     * @param new_seed is the seed value.
     */
    public static void setSeed(long new_seed) {
        rand = new Random(new_seed);
    }

    /**
     * create and initialize a Critter subclass.
     * critter_class_name must be the qualified name of a concrete
     * subclass of Critter, if not, an InvalidCritterException must be
     * thrown. Adds the critter to the population.
     *
     * @param critter_class_name the class name of the critter to be created
     * @throws InvalidCritterException if the critter type is not valid.
     */
    public static void createCritter(String critter_class_name) throws InvalidCritterException {
        try{
            critter_class_name = myPackage+"."+critter_class_name;              
            Class<?> critterClass = Class.forName(critter_class_name);
            Constructor<?> constructor = critterClass.getConstructor();
            Critter newCrit = (Critter) constructor.newInstance();
            newCrit.x_coord = getRandomInt(Params.WORLD_WIDTH);
            newCrit.y_coord = getRandomInt(Params.WORLD_HEIGHT);
            newCrit.energy = Params.START_ENERGY;
            population.add(newCrit);
        } catch(ClassNotFoundException|InstantiationException|IllegalAccessException|
        		NoSuchMethodException|SecurityException|IllegalArgumentException|
        		InvocationTargetException|NoClassDefFoundError e){
            throw new InvalidCritterException(critter_class_name);
        } 
    }

    /**
     * Gets a list of critters of a specific type.
     *
     * @param critter_class_name What kind of Critter is to be listed.
     *        Unqualified class name.
     * @return List of Critters.
     * @throws InvalidCritterException if requesting a critter of that does not exist.
     */
    public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
        try{
            critter_class_name = myPackage+"."+critter_class_name;
            List<Critter> instanceList = new ArrayList<>();
            Class<?> critterClass = Class.forName(critter_class_name);
            Constructor<?> constructor = critterClass.getConstructor();
            Critter myCrit = (Critter) constructor.newInstance();
            for(Critter c:population){
                if(myCrit.getClass().isInstance(c)){
                    instanceList.add(c);
                }
            }
            return instanceList;
        }catch(ClassNotFoundException|InstantiationException|IllegalAccessException|
        		NoSuchMethodException|SecurityException|IllegalArgumentException|
        		InvocationTargetException|NoClassDefFoundError e){
            throw new InvalidCritterException(critter_class_name);
        } 
    }

    /**
     * Clear the world of all critters, dead and alive
     */
    public static void clearWorld() {
        population.clear();
        babies.clear();
    }
    
    /**
     * Performs the timeStep for each critter, does encounters, updates rest energy, adds clovers,
     * adds babies to the population.
     */
    public static void worldTimeStep() {
    	for (Critter crit : population) {
 			crit.moved = false; 						// reset movement tracker
 		}
    	 
        timeStep++;
        
        List<Critter> toDie = new ArrayList<Critter>();
        
        for(int i = 0;i<population.size();i++){
            Critter c = population.get(i);
            
            if(c.energy > 0 ){
                c.doTimeStep();
                                
                if(c.energy<=0){
                	toDie.add(c);
                }
                
            } else {
            	toDie.add(c);
            }
        }
        
        population.removeAll(toDie);
        toDie.clear();
        
        doEncounters();
        
        for (Critter c : population) {
        	 c.updateRestEnergy();
        	 
        	 if(c.energy<=0){
             	toDie.add(c);
             }
        }
        
        population.removeAll(toDie);
        
        genClover();
        
        
        population.addAll(babies);
        babies.clear();
         
    }

    /**
     * Subtracts a constant rest energy cost from the critter.
     */
    private void updateRestEnergy(){
        energy-=Params.REST_ENERGY_COST;
    }
    
	/**
	 * Checks if a critter is in the same spot as other critters. If so, the critters will decide if
	 * they want to fight. Based on their decisions, a dice is rolled. The critter with the highest 
	 * dice roll will win the fight. The loser of each 1v1 fight surrenders half their energy to the
	 * victor and dies.
	 */
    private static void doEncounters(){
        for(int i = 0; i<population.size();i++){
            ArrayList<Critter> CrittersToFight = new ArrayList<>();
            Critter c = population.get(i);
            /*add every critter that is on the same position as c to the arrayList */
            for(int j = 0; j<population.size();j++){
                if(i != j){
                    Critter possibleEnemy = population.get(j);
                    if(c.x_coord == possibleEnemy.x_coord && c.y_coord == possibleEnemy.y_coord && possibleEnemy.energy > 0)
                        CrittersToFight.add(possibleEnemy);
                }
            }
            
            for(int j = 0;j<CrittersToFight.size();j++){
                if(!population.contains(c)) //if there are multiple enemies to fight and A dies before hand break the loop
                    break;
                Critter Opponent = CrittersToFight.get(j);
               
                int aX = c.x_coord;
                int aY = c.y_coord;
                boolean mov = c.moved;
                
                
                boolean wantsToFightA = c.fight(Opponent.toString()); //True if A wants to fight
                boolean anotherOne = false;
                
                if (c.x_coord != aX || c.y_coord != aY) {
                	for (int k = 0; k < population.size(); k++) {
                		if (k != i) {
                			Critter possibleEnemy = population.get(k);
                        	if(c.x_coord == possibleEnemy.x_coord && c.y_coord == possibleEnemy.y_coord && possibleEnemy.energy > 0) {
                        		anotherOne = true;
                        		break;
                        	}
                        		
                		}	
                	}
                }
                
                if (anotherOne) {
                	c.x_coord = aX;
                	c.y_coord = aY;
                	c.moved = mov;
                }
                
                
                aX = Opponent.x_coord;
                aY = Opponent.y_coord;
                mov = Opponent.moved;
                
                boolean wantsToFightB = Opponent.fight(c.toString()); //True if B wants to fight
                anotherOne = false;
                
                if (Opponent.x_coord != aX || Opponent.y_coord != aY) {
                	for (int k = 0; k < population.size(); k++) {
                		if (k != j) {
                			Critter possibleEnemy = population.get(k);
                        	if(Opponent.x_coord == possibleEnemy.x_coord && Opponent.y_coord == possibleEnemy.y_coord && possibleEnemy.energy > 0) {
                        		anotherOne = true; 
                        		break;
                        	}
                		}	
                	}
                }
                
                if (anotherOne) {
                	Opponent.x_coord = aX;
                	Opponent.y_coord = aY;
                	Opponent.moved = mov;
                }
                
                
                
                if (c.energy <= 0) {
                	population.remove(c); //If either of them died while running away
                	i--;
                	break;
                }
                else if(Opponent.energy <= 0)
                    population.remove(Opponent);
                else if(c.x_coord != Opponent.x_coord || c.y_coord != Opponent.y_coord)
                    continue;
                else{
                    //Do rest of fight
                    int aDiceroll = 0;
                    int bDicerool = 0;
                    if(wantsToFightA)
                        aDiceroll = getRandomInt(c.energy);
                    if(wantsToFightB)
                        bDicerool = getRandomInt(Opponent.energy);
                    if(aDiceroll >= bDicerool){
                        //A survives
                        population.remove(Opponent);
                        c.energy += Opponent.energy/2;
                    }
                    else{
                        //B survives
                        population.remove(c);
                        Opponent.energy += c.energy/2;
                        population.set(population.indexOf(CrittersToFight.get(j)),Opponent);
                        i--;
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Generates as many clovers as the refresh clover count specifies.
     */
    private static void genClover(){
        for(int i =0;i<Params.REFRESH_CLOVER_COUNT;i++){
            try {
                createCritter("Clover");
            }
            catch (Exception ignored){}
        }
    }

    /** 
     * Displays the alive critters in the 2x2 grid format with their respective character representations
     */
    public static void displayWorld() {
        String[][] world = new String[Params.WORLD_HEIGHT][Params.WORLD_WIDTH];
        for(int i =0;i<Params.WORLD_HEIGHT;i++){
            for(int j = 0;j<Params.WORLD_WIDTH;j++){
                world[i][j] = " ";
            }
        }
        for (Critter c : population) {
        	if (c.energy > 0)
        		world[c.y_coord][c.x_coord] = c.toString();
        }
        
        System.out.print("+");
        for(int i = 0;i<Params.WORLD_WIDTH;i++){
                System.out.print("-");
        }
        System.out.print("+");
        
        System.out.println();
        for(int i =0;i<Params.WORLD_HEIGHT;i++){
            System.out.print("|");
            for(int j = 0;j<Params.WORLD_WIDTH;j++){
                System.out.print(world[i][j]);
            }
            System.out.println("|");
        }
        
        System.out.print("+");
        for(int i = 0;i<Params.WORLD_WIDTH;i++){
                System.out.print("-");
        }
        System.out.println("+");
    }

    /**
     * Prints out how many Critters of a certain type there are on the
     * board.
     *
     * @param critters List of Critters.
     */
    public static void runStats(List<Critter> critters) {
        System.out.print("" + critters.size() + " critters as follows -- ");
        Map<String, Integer> critter_count = new HashMap<String, Integer>();
        for (Critter crit : critters) {
            String crit_string = crit.toString();
            critter_count.put(crit_string,
                    critter_count.getOrDefault(crit_string, 0) + 1);
        }
        String prefix = "";
        for (String s : critter_count.keySet()) {
            System.out.print(prefix + s + ":" + critter_count.get(s));
            prefix = ", ";
        }
        System.out.println();
    }

    public abstract void doTimeStep();

    public abstract boolean fight(String oponent);

    /* a one-character long string that visually depicts your critter
     * in the ASCII interface */
    public String toString() {
        return "";
    }

    protected int getEnergy() {
        return energy;
    }

    private boolean moved;
    
    /**
     * Makes the critter moves one unit if it hasn't moved already in a certain timeStep.
     * Regardless, will cut energy.
     * @param direction in which to move the critter.
     */
    protected final void walk(int direction) {
        // TODO: Complete this method
    	energy-=Params.WALK_ENERGY_COST;
    	
    	if (!moved) {
    		move(1,direction);
    		moved = true;
    	}
    }
    
    /**
     * Makes the critter move two units if it hasn't moved already in a certain timeStep.
     * Regardless, will cut energy.
     * @param direction in which to move the critter
     */
    protected final void run(int direction) {
        // TODO: Complete this method
    	energy-=Params.RUN_ENERGY_COST;
    	
    	if (!moved) {
    		move(2,direction);
    		moved = true;
    	}
    }

    /**
     * Will moves the critter in a certain direction by a certain amount. 
     * If the critter moves off the grid, will wrap around. 
     * @param amount is the number of units to move in a certain direction
     * @param Direction to move the critter.
     */
    private void move(int amount,int Direction){
        //Pick a direction to move
        /*
        2 - North         
        1 - North EAst    
        0 - East          
        7 - South East    
        6 - South      
        5 - South West
        4 - West       
        3 - North West 
         */
        if(Direction == 2){
        	this.y_coord-=amount;
        	if(this.y_coord <0)
                this.y_coord += Params.WORLD_HEIGHT;
        }
        else if(Direction == 1){
            this.x_coord+=amount;
            this.y_coord-= amount;
            
            if(this.x_coord>=Params.WORLD_WIDTH)
                this.x_coord -= Params.WORLD_WIDTH;
            if(this.y_coord<0)
                this.y_coord += Params.WORLD_HEIGHT;            	
        }
        else if(Direction == 0){
            this.x_coord+=amount;
            if(this.x_coord>=Params.WORLD_WIDTH)
                this.x_coord -= Params.WORLD_WIDTH;
        }
        else if(Direction == 7){
            this.x_coord+=amount;
            this.y_coord+=amount;
            if(this.x_coord>=Params.WORLD_WIDTH)
                this.x_coord -= Params.WORLD_WIDTH;
            if(this.y_coord >= Params.WORLD_HEIGHT)
                this.y_coord -= Params.WORLD_HEIGHT; 

        }
        else if(Direction == 6){
            this.y_coord+=amount;
            
            if(this.y_coord >= Params.WORLD_HEIGHT)
                this.y_coord -= Params.WORLD_HEIGHT; 
        }
        else if(Direction == 5){
           
            this.x_coord-=amount;
            this.y_coord+=amount;

            if(this.y_coord >= Params.WORLD_HEIGHT)
                this.y_coord -= Params.WORLD_HEIGHT;
            if(this.x_coord < 0)
                this.x_coord += Params.WORLD_WIDTH;

        }
        else if(Direction == 4){
            this.x_coord-=amount;

            if(this.x_coord < 0)
                this.x_coord += Params.WORLD_WIDTH;
        }
        else{
            
                this.x_coord-=amount;
                this.y_coord-=amount;
                
                if(this.x_coord < 0)
                    this.x_coord += Params.WORLD_WIDTH;
                if(this.y_coord < 0)
                    this.y_coord += Params.WORLD_HEIGHT;
            
        }
    }

    /**
     * If the parent has sufficient energy, it'll will create an offspring giving half
     * it's energy and keeping it next to them.
     * @param offspring references the offspring to be 	filled in if the parent is healthy enough
     * @param direction in relation to the parent where the child will appear next
     *  time step.
     */
    protected final void reproduce(Critter offspring, int direction) {
    	if (this.energy >= Params.MIN_REPRODUCE_ENERGY) {
    		offspring.energy = this.energy/2;
    		this.energy -= offspring.energy;
    		
    		offspring.x_coord = this.x_coord;
    		offspring.y_coord = this.y_coord;
    		
    		offspring.move(1, direction);
    		
    		babies.add(offspring);
    	}
    }

    /**
     * The TestCritter class allows some critters to "cheat". If you
     * want to create tests of your Critter model, you can create
     * subclasses of this class and then use the setter functions
     * contained here.
     * <p>
     * NOTE: you must make sure that the setter functions work with
     * your implementation of Critter. That means, if you're recording
     * the positions of your critters using some sort of external grid
     * or some other data structure in addition to the x_coord and
     * y_coord functions, then you MUST update these setter functions
     * so that they correctly update your grid/data structure.
     */
    static abstract class TestCritter extends Critter {

        protected void setEnergy(int new_energy_value) {
            super.energy = new_energy_value;
        }

        protected void setX_coord(int new_x_coord) {
            super.x_coord = new_x_coord;
        }

        protected void setY_coord(int new_y_coord) {
            super.y_coord = new_y_coord;
        }

        protected int getX_coord() {
            return super.x_coord;
        }

        protected int getY_coord() {
            return super.y_coord;
        }

        /**
         * This method getPopulation has to be modified by you if you
         * are not using the population ArrayList that has been
         * provided in the starter code.  In any case, it has to be
         * implemented for grading tests to work.
         * 
         * @return the ArrayList of critters in the world.
         */
        protected static List<Critter> getPopulation() {
            return population;
        }

        /**
         * This method getBabies has to be modified by you if you are
         * not using the babies ArrayList that has been provided in
         * the starter code.  In any case, it has to be implemented
         * for grading tests to work.  Babies should be added to the
         * general population at either the beginning OR the end of
         * every timestep.
         * 
         * @return the ArrayList of baby critters in the world.
         */
        protected static List<Critter> getBabies() {
            return babies;
        }
    }
}
