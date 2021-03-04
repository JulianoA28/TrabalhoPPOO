import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Um simples modelo de lobo. Lobos envelhecem, cacam, se reproduzem e
 * morrem.
 */

public class Wolf extends Animal implements Predator {

    // O nivel de comida do lobo, que aumenta ao comer coelhos/raposas
    private int foodLevel;
    
    // Em efeito, esse e o numero de etapas que o lobo pode permanecer
    // antes de ter que se alimentar de novo (ao comer um dos animais)
    private static final int FOX_FOOD_VALUE = 7;
    private static final int RABBIT_FOOD_VALUE = 4;

    /**
     * Cria um lobo. O lobo pode ser criado como recem-nascido (idade
     * zero e sem fome) ou com uma idade aleatoria.
     * 
     * @param randomAge Se verdadeiro, o lobo tera uma idade e um nivel
     * de fome aleatorios
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
     * Isso e o que o lobo faz a maior parte do seu tempo. Ele caca
     * coelhos e raposas. No processo, ele pode se reproduzir, morrer 
     * de fome ou morrer de velhice.
     */
	@Override
    public void act(Field currentField, Field updatedField, List newPredator) {
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            // Novos lobos nascem em locais adjacentes
            int births = breed(currentField);
            for (int b = 0; b < births; b++) {
                Wolf newWolf = new Wolf(false);
                newPredator.add(newWolf);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newWolf.setLocation(loc);
                updatedField.place(newWolf, loc);
            }
            // Move-se em direcao a fonte de comida se achada
            Location newLocation = findFood(currentField, getLocation());
            if (newLocation == null) { // no food found - move randomly
                newLocation = updatedField.freeAdjacentLocation(getLocation());
            }
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // Nao pode nem se mover nem permancer aonde esta -
                // superlotacao - todos locais tomados
                setAlive(false);
            }
        }
    }

    /**
     * Aumenta a fome do lobo. Isso pode resultar em sua morte.
     */
    @Override
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setAlive(false);
        }
    }

    /**
     * Indica para o lobo procurar por raposas ou coelhos adjacentes a
     * localizacao atual.
     * 
     * @param field O campo que se deve procurar
     * @param location Aonde o campo esta localizado
     * @return Aonde a comida foi enconta, ou null se nao encontrou
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
