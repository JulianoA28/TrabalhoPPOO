import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

public class Enviroment extends Actor{
	
	private static final HashMap<String, Integer> AREA = new HashMap<String, Integer>();
	
	private static final HashMap<String, Double> BREEDING_BONUS = new HashMap<String, Double>();
	
	static {
		AREA.put("Crop", 4);
		AREA.put("Hole", 1);
		AREA.put("Rain", 6);
		
		BREEDING_BONUS.put("Crop", 2.0);
		BREEDING_BONUS.put("Hole", 1.0);
		BREEDING_BONUS.put("Rain", -2.2);
	}
	
	private Field field;
	
	//private Location location;
	
//	public Enviroment(Field field, Location location)
//	{
//		this.field = field;
//		this.location = location;
//	}
	

    

}

