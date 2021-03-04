import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

/**
 * Classe responsável por definir a localizacaos dos atores dentro do sistema.
 * 
 * @author Amanda Cassiano Leal
 */
public abstract class Animal extends Actor {
    // Idade que o animal pode procriar.
    private static final HashMap<String, Integer> BREEDING_AGE = new HashMap<String, Integer>();
    // Idade maxima que um animal pode viver
    private static final HashMap<String, Integer> MAX_AGE = new HashMap<String, Integer>();
    // A probabilidade de um animal se reproduzir
    private static final HashMap<String, Double> BREEDING_PROBABILITY = new HashMap<String, Double>();
    // O maximo de numero de nascimentos
    private static final HashMap<String, Integer> MAX_LITTER_SIZE = new HashMap<String, Integer>();

    private static final Random rand = new Random();

    // Mapeamento dos atributos atraves de um par chave, valor
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

    // gera um numero aleatorio
    public Random getRand() {
        return rand;
    }

    /**
     * Busca a idade do animal.
     * 
     * @return a idade do animal.
     */
    public int getAge() {
        return age;
    }

    /**
     * Busca se o animal está vivo ou morto.
     * 
     * @return true ou false.
     */
    public boolean getAlive() {
        return alive;
    }

    /**
     * Define a idade do animal
     * 
     * @param age uma idade, e.g 2
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Define se o animal está vivo ou morto.
     * 
     * @param alive se true o animal estara vivo
     */
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    /**
     * Cria um animal. Um animal pode ser criado como recem nascido (idade zero) ou
     * com uma idade aleatoria.
     * 
     * @param randomAge se true, o animal tera uma idade aleatoria.
     */
    public Animal(boolean randomAge) {
        age = 0;
        alive = true;
        if (randomAge) {
            age = rand.nextInt(MAX_AGE.get(this.getClass().getName()));
        }

    }

    /**
     * Responsavel pela acao que o animal devera fazer (run ou hunt), metodo
     * abstrato.
     * 
     * @param currentField o campo atual que a simulacao esta acontecendo.
     * @param updatedField o campo que devera ser atualizado.
     * @param newAnimals   a lista dos novos animais que farao parte da simulacao.
     */
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
     * Gera um numero que representa a quantidade de nascimento, caso o animal tenha
     * idade para procriar.
     * 
     * @param currentField o campo atual que a simulacao esta rodando.
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
     * 
     * @return a idade de pocriação do animal em questão.
     */
    private boolean canBreed() {
        return age >= BREEDING_AGE.get(this.getClass().getName());
    }

    /**
     * Define o animal como morto ou vivo
     * 
     * @return true ou false.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Define o animal como morto
     */
    public void setEaten() {
        setAlive(false);
    }

}
