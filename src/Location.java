
/**
 * Representa a localizacao em uma grade retangular
 * 
 * @author Juliano Andrade
 */
public class Location {
    // Posicoes das linhas e colunas.
    private int row;
    private int col;

    /**
     * Representa a linha e coluna
     * 
     * @param row A linha
     * @param col A coluna
     */
    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Implementa a igualdade de conteudo
     */
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location other = (Location) obj;
            return row == other.getRow() && col == other.getCol();
        } else {
            return false;
        }
    }

    /**
     * Retorna uma string na forma de linha,coluna
     * 
     * @return Uma string representando uma localizacao
     */
    public String toString() {
        return row + "," + col;
    }

    /**
     * Usa os 16 bits do topo para o valor da linha e de baixo para o da
     * coluna. Exceto para grades grandes, isso deve dar um codigo hash
     * unico para cada par (linha, coluna)
     */
    public int hashCode() {
        return (row << 16) + col;
    }

    /**
     * @return A linha.
     */
    public int getRow() {
        return row;
    }

    /**
     * @return A coluna.
     */
    public int getCol() {
        return col;
    }
}
