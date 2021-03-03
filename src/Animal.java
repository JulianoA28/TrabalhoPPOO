import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

public abstract class Animal extends Actor{
	
    private static final HashMap<String, Integer> BREEDING_AGE = new HashMap<String, Integer>();

    private static final HashMap<String, Integer> MAX_AGE = new HashMap<String, Integer>();

    private static final HashMap<String, Double> BREEDING_PROBABILITY = new HashMap<String, Double>();

    private static final HashMap<String, Integer> MAX_LITTER_SIZE = new HashMap<String, Integer>();

    private static final Random rand = new Random();

    static {
        BREEDING_AGE.put("Fox", 10);
        BREEDING_AGE.put("Rabbit", 5);
        BREEDING_AGE.put("Wolf", 50);

        MAX_AGE.put("Fox", 150);
        MAX_AGE.put("Rabbit", 50);
        MAX_AGE.put("Wolf", 180);

        BREEDING_PROBABILITY.put("Fox", 0.09);
        BREEDING_PROBABILITY.put("Rabbit", 0.15);
        BREEDING_PROBABILITY.put("Wolf", 0.035);

        MAX_LITTER_SIZE.put("Fox", 3);
        MAX_LITTER_SIZE.put("Rabbit", 5);
        MAX_LITTER_SIZE.put("Wolf", 2);
    }

    private int age;

    private boolean alive;

    //private Location location;

    public Random getRand() {
        return rand;
    }

    public int getAge() {
        return age;
    }

    public boolean getAlive() {
        return alive;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Create an animal. An animal can be created as a new born (age zero) or with
     * random age.
     * 
     * @param randomAge If true, the animal will have random age.
     */
    public Animal(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE.get(this.getClass().getName()));
        }

    }
    
    public abstract void act(Field currentField, Field updatedField, List newAnimals);
		

    /**
     * Increase the age. This could result in the animal's death.
     */
    public void incrementAge() {
        age++;
        if (age > MAX_AGE.get(this.getClass().getName())) {
            alive = false;
        }
    }

    /**
     * Generate a number representing the number of births, if it can breed.
     * 
     * @return The number of births (may be zero).
     */
    public int breed(Field currentField) {
		
		double breeding_bonus = 0.0;
		Iterator adjacent = currentField.adjacentLocations(getLocation());
		Object[][] field = currentField.getField();
		while (adjacent.hasNext()) {
			Location next = (Location) adjacent.next();
			Actor actor = (Actor)field[next.getRow()][next.getCol()];
			if (actor instanceof Enviroment) {
				Enviroment env = (Enviroment)actor;
				breeding_bonus += env.getBonus();
			}
		
		}
		
        int births = 0;
        
        double breeding_probability = BREEDING_PROBABILITY.get(this.getClass().getName()) + breeding_bonus;
        if (canBreed() && rand.nextDouble() <= breeding_probability) {
            births = rand.nextInt(MAX_LITTER_SIZE.get(this.getClass().getName())) + 1;
        }
        return births;
    }

    /**
     * A animal can breed if it has reached the breeding age.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE.get(this.getClass().getName());
    }

    /**
     * Tell the animal that it's dead now :(
     */
    public boolean isAlive() {
        return alive;
    }



    /**
     * Tell the rabbit that it's dead now :(
     */
    public void setEaten() {
        setAlive(false);
    }

}
