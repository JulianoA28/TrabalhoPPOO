import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a wolf. Wolf age, move, breed, and die.
 * 
 */

public class Wolf extends Animal implements Predator {

    // The wolf's food level, which is increased by eating rabbits.
    private int foodLevel;
    // In effect, this is the number of steps a wolf can go before it has to eat
    // again.
    private static final int FOX_FOOD_VALUE = 7;
    private static final int RABBIT_FOOD_VALUE = 4;

    /**
     * Create a wolf. A wolf can be created as a new born (age zero and not hungry)
     * or with random age.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     */
    public Wolf(boolean randomAge) {
        super(randomAge);
        if (randomAge) {
            foodLevel = getRand().nextInt(FOX_FOOD_VALUE);
        } else {
            // leave age at 0
            foodLevel = FOX_FOOD_VALUE;
        }
    }

    /**
     * This is what the fox does most of the time: it hunts for rabbits. In the
     * process, it might breed, die of hunger, or die of old age.
     */
	@Override
    public void act(Field currentField, Field updatedField, List newPredator) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // New foxes are born into adjacent locations.
            int births = breed(currentField);
            for (int b = 0; b < births; b++) {
                Wolf newWolf = new Wolf(false);
                newPredator.add(newWolf);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newWolf.setLocation(loc);
                updatedField.place(newWolf, loc);
            }
            // Move towards the source of food if found.
            Location newLocation = findFood(currentField, getLocation());
            if (newLocation == null) { // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // can neither move nor stay - overcrowding - all locations taken
                setAlive(false);
            }
        }
    }

    /**
     * Make this wolf more hungry. This could result in the fox's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }

    /**
     * Tell the wolf to look for foxes adjacent to its current location.
     * 
     * @param field    The field in which it must look.
     * @param location Where in the field it is located.
     * @return Where food was found, or null if it wasn't.
     */
	@Override
    public Location findFood(Field field, Location location) {
        Iterator adjacentLocations = field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Actor actor = (Actor)field.getObjectAt(where);
            if (actor instanceof Fox) {
                Fox fox = (Fox) actor;
                if (fox.isAlive()) {
                    fox.setEaten();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
            else if (actor instanceof Rabbit) {
				Rabbit rabbit = (Rabbit) actor;
				if (rabbit.isAlive()) {
					rabbit.setEaten();
					foodLevel = RABBIT_FOOD_VALUE;
					return where;
				}
			}
        }
        return null;
    }
}
