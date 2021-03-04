import java.util.List;
import java.util.Random;

/**
 * Um modelo simples de  coelho. Coelhos envelhecem, se movimentam, se reproduzem e morrem. 
 * 
 * @author Leonardo Basso
 */
public class Rabbit extends Animal {

    /**
     * Cria um novo coelho. Um coelho pode ser criado com a idade zero(recém nascido) ou com uma idade aleatória.
     * 
     * 
     * @param randomAge Caso verdadeiro, o coelho terá uma idade aleatória.
     */
    public Rabbit(boolean randomAge) {
        super(randomAge);
    }

    /**
     * Isso é o que os coelhos fazem na maior parte do tempo - eles correm por aí. As vezes eles reproduzem ou morrem de velhice.
     * 
     */
	@Override
    public void act(Field currentField, Field updatedField, List newRabbits) {
        incrementAge();
        if (getAlive()) {
            int births = breed(currentField);
            for (int b = 0; b < births; b++) {
                Rabbit newRabbit = new Rabbit(false);
                newRabbits.add(newRabbit);
                Location loc = updatedField.randomAdjacentLocation(getLocation());
                newRabbit.setLocation(loc);
                updatedField.place(newRabbit, loc);
            }
            Location newLocation = updatedField.freeAdjacentLocation(getLocation());
            // Apenas transfere para um campo atualizado se houver um local livre
            if (newLocation != null) {
                setLocation(newLocation);
                updatedField.place(this, newLocation);
            } else {
                // não pode se mover nem ficar - caso haja superlotação - todos os locais cheios
                setAlive(false);
            }
        }
    }
}
