
/**
 * Classe responsável por definir o comportamento das variaveis do ambiente, 
 * e como o mesmo interfere no comportamento dos animais facilitando ou dificultado.
 * 
 * @author Amanda Cassiano Leal
 */
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

public class Enviroment extends Actor {
	/*
	 * O bonus de procriacao que sera incrmentado na probabilidade de procriacao do
	 * animal que se movimentar pelo ambiente no campo atual.
	 */
	private static final HashMap<String, Double> BREEDING_BONUS = new HashMap<String, Double>();
	// Mapeamento dos atributos atraves de um par chave, valor
	static {
		BREEDING_BONUS.put("Crop", 3.0);
		BREEDING_BONUS.put("Hole", 1.5);
		BREEDING_BONUS.put("Rain", -4.2);
	}

	private Field field;

	// status do ambiente (true = ativado)
	private boolean active;

	/**
	 * @param active true ou false para ativar a acao do ambiente
	 **/
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * 
	 * @return o valor do bonus que aquele ambiente proporciona
	 */
	public double getBonus() {
		return BREEDING_BONUS.get(this.getClass().getName());
	}

	// construtor
	public Enviroment() {

	}

}
