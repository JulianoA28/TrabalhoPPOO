import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * 
 * Representa uma grade retangular de posicoes de campo. Cada posicao eh capaz
 * de armazenar um unico animal.
 * 
 * @author Amanda Cassiano Leal
 */
public class Field {
    private static final Random rand = new Random();

    // A profundidade e largura do campo.
    private int depth, width;
    // Armazenamento para os animais.
    private Object[][] field;

    /**
     * Representa um campo com as dimensoes enviadas.
     * 
     * @param depth a profundidade do campo.
     * @param width a largura do campo.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
    }

    public Object[][] getField() {
        return field;
    }

    /**
     * Limpa o campo.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Coloca o animal na localizacao passada. Se houver algum ator nesta
     * localizacao ele sera sobreescrito.
     * 
     * @param animal o animal que devera ser colocado.
     * @param row    Coordenada vertical da localização.
     * @param col    Coordenada horizontal da localização.
     */
    public void place(Object animal, int row, int col) {
        place(animal, new Location(row, col));
    }

    /**
     * Coloca o animal na localizacao passada. Se houver algum ator nesta
     * localizacao ele sera sobreescrito.
     * 
     * @param animal   o animal que devera ser colocado.
     * @param location onde colocar o animal.
     */
    public void place(Object animal, Location location) {
        field[location.getRow()][location.getCol()] = animal;
    }

    /**
     * Retorna o animal na localizacao passada,caso haja algum.
     * 
     * @param location onde no campo.
     * @return O animal que esta na localizacao passada,ou null caso nao tenha
     *         nenhum.
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Retorna o animal na localizacao passada,caso haja algum.
     * 
     * @param row A linha desejada.
     * @param col A coluna desejada.
     * @return O animal que esta na localizacao passada,ou null caso nao tenha
     *         nenhum.
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Gera um local aleatório que seja adjacente ao local determinado, ou seja o
     * mesmo local. O local retornado estará dentro dos limites válidos do campo.
     * 
     * @param location O local a partir do qual gerar uma adjacência.
     * @return Um local válido dentro da área da grade. Este pode ser o mesmo objeto
     *         que o parâmetro de localização.
     */
    public Location randomAdjacentLocation(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        // Gera um deslocamento de -1, 0 ou +1 para a linha e coluna atuais.
        int nextRow = row + rand.nextInt(3) - 1;
        int nextCol = col + rand.nextInt(3) - 1;
        // Verifique se o novo local está fora dos limites.
        if (nextRow < 0 || nextRow >= depth || nextCol < 0 || nextCol >= width) {
            return location;
        } else if (nextRow != row || nextCol != col) {
            return new Location(nextRow, nextCol);
        } else {
            return location;
        }
    }

    /**
     * Tente encontrar um local livre adjacente ao local fornecido. Se nao houver
     * nenhum, entao, retornw a localização atual se estiver livre. Caso contrário,
     * retorne null. O local retornado estará dentro dos limites válidos do campo.
     * 
     * @param location O local a partir do qual gerar uma adjacência.
     * @return Um local válido dentro da área da grade. Este pode ser o mesmo objeto
     *         que o parâmetro de localização, ou nulo se todas as localizações ao
     *         redor estiverem cheias.
     */
    public Location freeAdjacentLocation(Location location) {
        Iterator adjacent = adjacentLocations(location);
        while (adjacent.hasNext()) {
            Location next = (Location) adjacent.next();
            if (field[next.getRow()][next.getCol()] == null) {
                return next;
            }
        }
        // checa se a localizao atual esta livre
        if (field[location.getRow()][location.getCol()] == null) {
            return location;
        } else {
            return null;
        }
    }

    /**
     * Gera um iterador sobre uma lista embaralhada de locais adjacentes. A lista
     * não incluirá o local em si. Todos os locais estarao dentro da grade.
     * 
     * @param location O local a partir do qual gerar uma adjacência.
     * @return Um iterador sobre locais adjacentes.
     */
    public Iterator adjacentLocations(Location location) {
        int row = location.getRow();
        int col = location.getCol();
        LinkedList locations = new LinkedList();
        for (int roffset = -1; roffset <= 1; roffset++) {
            int nextRow = row + roffset;
            if (nextRow >= 0 && nextRow < depth) {
                for (int coffset = -1; coffset <= 1; coffset++) {
                    int nextCol = col + coffset;
                    // Exclue locais inválidos e o local original.
                    if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                        locations.add(new Location(nextRow, nextCol));
                    }
                }
            }
        }
        Collections.shuffle(locations, rand);
        return locations.iterator();
    }

    /**
     * @return A profundidade do campo.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * @return A largura do campo .
     */
    public int getWidth() {
        return width;
    }
}
