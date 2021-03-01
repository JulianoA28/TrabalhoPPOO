import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

public class Enviroment {
	
	private static final HashMap<String, Integer> AREA = new HashMap<String, Integer>();
	
	private static final HashMap<String, Double> BREEDING_BONUS = new HashMap<String, Double>();
	
	static {
		AREA.put("Crop", 4);
		AREA.put("Hole", 1);
		AREA.put("Rain", 6);
		
		BREEDING_BONUS.put("Crop", 2);
		BREEDING_BONUS.put("Hole", 1);
		BREEDING_BONUS.put("Rain", -2);
	}
	
	private Location location;
	
	/**
     * Set the animal's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }

}

