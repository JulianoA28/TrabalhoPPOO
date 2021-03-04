public class Actor {

    private Location location;

    public Location getLocation() {
        return location;
    }

    /**
     * Definir a localizacao do animal.
     * 
     * @param row a coordenada vertical da localizacao.
     * @param col a coodenada horizontal da localizacao.
     */
    public void setLocation(int row, int col) {
        this.location = new Location(row, col);
    }

    /**
     * Definir a localizao do animal.
     * 
     * @param location A localizacao do animal.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

}
