import java.awt.Color;

/**
 * Providencia um contador para os participantes da simulacao. Isso inclui uma
 * string de identificação e uma contagem de quantos participantes deste tipo
 * existem atualmente na simulação.
 * 
 * @author Amanda Cassiano Leal
 */

public class Counter {
    // Um nome para esse tipo de participante na simulacao
    private String name;
    // Quantos deste tipo existem na simulacao
    private int count;

    /**
     * Providencia um nome para um dos tipos de simulação.
     * 
     * @param name Um nome, e.g. "Fox".
     */
    public Counter(String name) {
        this.name = name;
        count = 0;
    }

    /**
     * @return Uma descricao simples desse tipo, e.g "Fox".
     */
    public String getName() {
        return name;
    }

    /**
     * @return O atual contador deste tipo.
     */
    public int getCount() {
        return count;
    }

    /**
     * Incrementa +1 no atual contador.
     */
    public void increment() {
        count++;
    }

    /**
     * Reseta o atual contador para zero.
     */
    public void reset() {
        count = 0;
    }
}
