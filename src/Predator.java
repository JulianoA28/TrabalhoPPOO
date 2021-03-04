import java.util.List;

public interface Predator {

    public Location findFood(Field field, Location location);
    
    private void incrementHunger();
    
}
