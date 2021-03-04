import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Um modelo simples de Raposa. Raposas envelhecem, comem coelhos e
 * morrem.
 * 
 * @author Juliano Andrade
 */
public class Fox extends Animal implements Predator {

    // O valor em comida de um unico coelho. Em efeito, isso e o numero
    // de estapas que uma raposa consegue sobreviver sem ter que se
    // alimentar novamente
    private static final int RABBIT_FOOD_VALUE = 4;

    // O nivel de comida da raposa, que e aumentado ao comer coelhos.
    private int foodLevel;

    /**
     * Cria-se uma raposa. A raposa e criada como recem-nascida (idade 0
     * e sem fome) ou com uma idade aleatoria
     * 
     * @param randomAge Se verdadeiro, a raposa tera uma idade e um 
     * nivel de fome aleatorios
     */
    public Fox(boolean randomAge) {
        super(randomAge);
        if (randomAge) {
            foodLevel = getRand().nextInt(RABBIT_FOOD_VALUE);
        } else {
            // leave age at 0
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }

    /**
     * Isso e o que a raposa faz a maior parte do seu tempo. Ela caca
     * coelhos. No processo, ela pode se reproduzir, morrer de fome ou
     * morrer de velhice.
     */
	@Override
    public void act(Field currentField, Field updatedField, List newPredator) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // New foxes are born into adjacent locations.
            int births = breed(currentField);
            for (int b = 0; b < births; b++) {
                Fox newFox = new Fox(false);
                newPredator.add(newFox);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newFox.setLocation(loc);
                updatedField.place(newFox, loc);
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
     * Faz a raposa ter mais fome. Isso pode resultar na morte dela.
     */
    @Override
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }

    /**
     * Indica para a raposa para buscar por coelhos adjacentes de sua
     * localizacao atual.
     * 
     * @param field O campo que ela procurar
     * @param location Aonde o campo esta localizados
     * @return Aonde a comida foi encontrada, ou null se nao ha
     */
	@Override
    public Location findFood(Field field, Location location) {
        Iterator adjacentLocations = field.adjacentLocations(location);
        while (adjacentLocations.hasNext()) {
            Location where = (Location) adjacentLocations.next();
            Object animal = field.getObjectAt(where);
            if (animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
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
