public interface Predator {

    public void hunt(Field currentField, Field updatedField, List newPredator);

    public Location findFood(Field field, Location location);
}
