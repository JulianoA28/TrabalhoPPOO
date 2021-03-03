

public class Actor {
	
	private Location location;
	
	public Location getLocation() {
        return location;
    }
	
	/**
     * Set the animal's location.
     * @param row The vertical coordinate of the location.
     * @param col The horizontal coordinate of the location.
     */
    public void setLocation(int row, int col)
    {
        this.location = new Location(row, col);
    }
    
	/**
     * Set the animal's location.
     * 
     * @param location The animal's location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }
	
}

