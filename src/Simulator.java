import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;
import java.util.HashMap;

/**
 * A simple predator-prey simulator, based on a field containing rabbits and
 * foxes.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-09
 */
public class Simulator {
    // The private static final variables represent
    // configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 50;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 50;
    // The probability that a fox will be created in any given grid position.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // The probability that a rabbit will be created in any given grid position.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    // The probability that a wolf will be created in any given grid position.
    private static final double WOLF_CREATION_PROBABILITY = 0.01;

    private static final double CROP_CREATION_PROBABILITY = 0.01;

    private static final double HOLE_CREATION_PROBABILITY = 0.04;

    private static final double RAIN_CREATION_PROBABILITY = 0.03;

    // The list of animals in the field
    private List<Animal> animals;
    // The list of animals just born
    private List<Animal> newAnimals;

    private List<Enviroment> enviromentList;

    private List<Enviroment> newEnviromentList;

    // The current state of the field.
    private Field field;
    // A second field, used to build the next stage of the simulation.
    private Field updatedField;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    private boolean wolf = false;

    private boolean rain = false;

    private boolean crop = false;

    private boolean hole = false;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList<Animal>();
        newAnimals = new ArrayList<Animal>();
        enviromentList = new ArrayList<Enviroment>();
        newEnviromentList = new ArrayList<Enviroment>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setSimulator(this);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Wolf.class, Color.darkGray);
        view.setColor(Crop.class, Color.green);
        view.setColor(Rain.class, Color.cyan);
        view.setColor(Hole.class, Color.pink);

        // Setup a valid starting point.e
        reset();
    }

    public void setWolf() {
        if (wolf) {
            wolf = false;
        } else {
            wolf = true;
        }
    }

    public void setRain() {
        if (rain) {
            rain = false;
        } else {
            rain = true;
        }
    }

    public void setCrop() {
        if (crop) {
            crop = false;
        } else {
            crop = true;
        }
    }

    public void setHole() {
        if (hole) {
            hole = false;
        } else {
            hole = true;
        }
    }

    /**
     * Run the simulation from its current state for a reasonably long period, e.g.
     * 500 steps.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     * Run the simulation from its current state for the given number of steps. Stop
     * before the given number of steps if it ceases to be viable.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over the
     * whole field updating the state of each fox and rabbit.
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();
        newEnviromentList.clear();

        // let all animals act
        for (Iterator iter = animals.iterator(); iter.hasNext();) {
            Animal animal = (Animal) iter.next();
            if (animal instanceof Animal) {
                if (animal.isAlive()) {
                    animal.act(field, updatedField, newAnimals);
                } else {
                    iter.remove();
                }
            } else {
                System.out.println("found unknown animal");
            }
        }

        for (Iterator iter = enviromentList.iterator(); iter.hasNext();) {
            Enviroment enviroment = (Enviroment) iter.next();
            updatedField.place(enviroment, enviroment.getLocation());

        }

        // add new born animals to the list of animals
        animals.addAll(newAnimals);

        // Swap the field and updatedField at the end of the step.
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // display the new field on screen
        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        enviromentList.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * Populate the field with foxes and rabbits.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    animals.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    animals.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                } else if (rand.nextDouble() <= WOLF_CREATION_PROBABILITY && wolf) {
                    Wolf wolf = new Wolf(true);
                    animals.add(wolf);
                    wolf.setLocation(row, col);
                    field.place(wolf, row, col);
                } else if (rand.nextDouble() <= CROP_CREATION_PROBABILITY && crop) {
                    Crop crop = new Crop();
                    enviromentList.add(crop);
                    crop.setLocation(row, col);
                    field.place(crop, row, col);
                } else if (rand.nextDouble() <= RAIN_CREATION_PROBABILITY && rain) {
                    Rain rain = new Rain();
                    enviromentList.add(rain);
                    rain.setLocation(row, col);
                    field.place(rain, row, col);
                } else if (rand.nextDouble() <= HOLE_CREATION_PROBABILITY && hole) {
                    Hole hole = new Hole();
                    enviromentList.add(hole);
                    hole.setLocation(row, col);
                    field.place(hole, row, col);
                }
                // else leave the location empty.
            }
        }
        Collections.shuffle(animals);
        Collections.shuffle(enviromentList);
    }
}
