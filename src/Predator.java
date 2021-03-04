import java.util.List;

/**
 * Interface responsavel pelos predadores, animais que cacam outros
 * Exemplo (Fox e Wolf)
 * 
 * @author Juliano Andrade
 */
public interface Predator {

    public Location findFood(Field field, Location location);
    
    private void incrementHunger();
    
}
