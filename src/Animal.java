import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

public abstract class Animal extends Actor {

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
     * Cria um animal. Um animal pode ser criado como recem nascido (idade zero) ou com uma idade aleatoria.
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
     * Aumenta a idade. Esse metodo pode resultar na morte do animal.
     */
    public void incrementAge() {
        age++;
        if (age > MAX_AGE.get(this.getClass().getName())) {
            alive = false;
        }
    }

    /**
     * Gera um numero que representa a quantidade de nascimento, caso o animal tenha idade para procriar.
     * 
     * @return O numero de nascimentos (pode ser zero).
     */
    public int breed(Field currentField) {

        double breeding_bonus = 0.0;
        Iterator adjacent = currentField.adjacentLocations(getLocation());
        Object[][] field = currentField.getField();
        while (adjacent.hasNext()) {
            Location next = (Location) adjacent.next();
            Actor actor = (Actor) field[next.getRow()][next.getCol()];
            if (actor instanceof Enviroment) {
                Enviroment env = (Enviroment) actor;
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
     * Um animal pode procriar caso ele tenha atingindo a idade de procriacao.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE.get(this.getClass().getName());
    }

    /**
     * Define o animal como morto :(
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define o coelho como morto :(
     */
    public void setEaten() {
        setAlive(false);
    }

}
