import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Classe coleta e providencia dados estatisticos sobre o estado do
 * campo. E flexivel: ira criar e mantem um contador para qualquer
 * classe de objeto que pode ser encontrado dentro do campo.

 * @author Juliano Andrade

 */
public class FieldStats {
	
    // Contadores para cada tioi de entidade (atores) na simulacao
    private HashMap counters;

    // Se os contadores estao atualmente atualizados
    private boolean countsValid;

    /**
     * Constroi um objeto de estatisticas de campo
     */
    public FieldStats() {
        // Inicia uma colecao de contadores para cada tipo de animal que
        // podemos encontrar
        counters = new HashMap();
        countsValid = true;
    }

    /**
     * @return Uma string descrevendo quais animais estao no campo
     */
    public String getPopulationDetails(Field field) {
        StringBuffer buffer = new StringBuffer();
        if (!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            buffer.append(info.getName());
            buffer.append(": ");
            buffer.append(info.getCount());
            buffer.append(' ');
        }
        return buffer.toString();
    }

    /**
     * Invalida o conjunto atuais de estatisticas; reseta todos
     * contadores para zero
     */
    public void reset() {
        countsValid = false;
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter cnt = (Counter) counters.get(keys.next());
            cnt.reset();
        }
    }

    /**
     * Incrementa o contador para uma classe animal
     */
    public void incrementCount(Class animalClass) {
        Counter cnt = (Counter) counters.get(animalClass);
        if (cnt == null) {
            // we do not have a counter for this species yet - create one
            cnt = new Counter(animalClass.getName());
            counters.put(animalClass, cnt);
        }
        cnt.increment();
    }

    /**
     * Indica que um contador animal foi completado.
     */
    public void countFinished() {
        countsValid = true;
    }

    /**
     * Determina se a simulacao e ainda viavel. Em outras
     * palavras, deveria continuar rodando.
     * @return true Se ha mais de uma especie viva.
     */
    public boolean isViable(Field field) {
        // Quantos contadores nao sao zero
        int nonZero = 0;
        if (!countsValid) {
            generateCounts(field);
        }
        Iterator keys = counters.keySet().iterator();
        while (keys.hasNext()) {
            Counter info = (Counter) counters.get(keys.next());
            if (info.getCount() > 0) {
                nonZero++;
            }
        }
        return nonZero > 1;
    }

    /**
     * Gera contadores do numero de animais. Nao sao mantidos
     * atualizados a medida que os animais sao posicionados no campo,
     * mas apenas quando uma requisicao de informacao e feita.
     */
    private void generateCounts(Field field) {
        reset();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if (animal != null) {
                    incrementCount(animal.getClass());
                }
            }
        }
        countsValid = true;
    }
}
