import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.awt.Color;
import java.util.HashMap;

/**
 * Um simulador de presa-predador simples, baseado em um campo contendo coelhos e raposas. 
 * 
 * @author Leonardo Basso
 */
public class Simulator {
    /**
     * As variáveis finais estáticas privadas representam
     * informações de configuração para a simulação
     */
    // Largura padrão para o tamanho da janela.
    private static final int DEFAULT_WIDTH = 50;
    // Profundidade padrão da janela.
    private static final int DEFAULT_DEPTH = 50;
    // A probabilidade de que uma raposa seja criada em qualquer posição da janela.
    private static final double FOX_CREATION_PROBABILITY = 0.02;
    // A probabilidade de um coelho ser criado em qualquer posição da janela.
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    // A probabilidade de que um lobo seja criado em qualquer posição da janela.
    private static final double WOLF_CREATION_PROBABILITY = 0.01;

    private static final double CROP_CREATION_PROBABILITY = 0.01;

    private static final double HOLE_CREATION_PROBABILITY = 0.04;

    private static final double RAIN_CREATION_PROBABILITY = 0.03;

    // Lista de animais no campo
    private List<Animal> animals;
    // Lista de animais nascidos
    private List<Animal> newAnimals;

    private List<Enviroment> enviromentList;

    private List<Enviroment> newEnviromentList;

    // Estado atual do campo
    private Field field;
    // Estado secundário, usado para contruir uma nova fase de simulação.
    private Field updatedField;
    // Fase atual da simulação
    private int step;
    // Uma visão gráfica da simulação.
    private SimulatorView view;

    private boolean wolf = false;

    private boolean rain = false;

    private boolean crop = false;

    private boolean hole = false;

    /**
     * Constrói o campo de simulação com o tamanho padrão.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Crie um campo de simulação com o tamanho fornecido.
     * 
     * @param depth Profundidade de campo. Deve ser maior que zero.
     * @param width Largura de campo. Deve ser maior que zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        animals = new ArrayList<Animal>();
        newAnimals = new ArrayList<Animal>();
        enviromentList = new ArrayList<Enviroment>();
        newEnviromentList = new ArrayList<Enviroment>();
        field = new Field(depth, width);
        updatedField = new Field(depth, width);

        // Crie uma visualização do estado de cada local no campo.
        view = new SimulatorView(depth, width);
        view.setSimulator(this);
        view.setColor(Fox.class, Color.blue);
        view.setColor(Rabbit.class, Color.orange);
        view.setColor(Wolf.class, Color.darkGray);
        view.setColor(Crop.class, Color.green);
        view.setColor(Rain.class, Color.cyan);
        view.setColor(Hole.class, Color.pink);

        // Configure um ponto de partida válido

        reset();
    }

    public void setWolf() {
        if (wolf) {
            wolf = false;
        } else {
            wolf = true;
        }
    }

    public void setRain() {
        if (rain) {
            rain = false;
        } else {
            rain = true;
        }
    }

    public void setCrop() {
        if (crop) {
            crop = false;
        } else {
            crop = true;
        }
    }

    public void setHole() {
        if (hole) {
            hole = false;
        } else {
            hole = true;
        }
    }

    /**
     * Execute a simulação de seu estado atual por um período razoavelmente longo, por exemplo,
     * 500 etapas.
     */
    public void runLongSimulation() {
        simulate(500);
    }

    /**
     *  Execute a simulação de seu estado atual para um determinado número de etapas. Pare
     *  antes de um determinado número de etapas se deixar de ser viável. 
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
        }
    }

    /**
     * Execute a simulação de seu estado atual para uma única etapa.
     * Repita em todo o campo atualizando o estado de cada raposa e coelho. 
     */
    public void simulateOneStep() {
        step++;
        newAnimals.clear();
        newEnviromentList.clear();

        // Deixe todos os animais agirem
        for (Iterator iter = animals.iterator(); iter.hasNext();) {
            Animal animal = (Animal) iter.next();
            if (animal instanceof Animal) {
                if (animal.isAlive()) {
                    animal.act(field, updatedField, newAnimals);
                } else {
                    iter.remove();
                }
            } else {
                System.out.println("found unknown animal");
            }
        }

        for (Iterator iter = enviromentList.iterator(); iter.hasNext();) {
            Enviroment enviroment = (Enviroment) iter.next();
            updatedField.place(enviroment, enviroment.getLocation());

        }

        // adicionar animais recém-nascidos à lista de animais
        animals.addAll(newAnimals);

        // Troque o campo e updatedField no final da etapa. 
        Field temp = field;
        field = updatedField;
        updatedField = temp;
        updatedField.clear();

        // exibir o novo campo na tela
        view.showStatus(step, field);
    }

    /**
     * Redefina a simulação para uma posição inicial.
     */
    public void reset() {
        step = 0;
        animals.clear();
        enviromentList.clear();
        field.clear();
        updatedField.clear();
        populate(field);

        // Mostra o estado inicial na vista.
        view.showStatus(step, field);
    }

    /**
     * Povoe o campo com raposas e coelhos. Caso os atributos relacionados aos
     * atores estejam verdadeiros tambem adiciona lobos, plantacoes, chuvas e tocas.
     */
    private void populate(Field field) {
        Random rand = new Random();
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Fox fox = new Fox(true);
                    animals.add(fox);
                    fox.setLocation(row, col);
                    field.place(fox, row, col);
                } else if (rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Rabbit rabbit = new Rabbit(true);
                    animals.add(rabbit);
                    rabbit.setLocation(row, col);
                    field.place(rabbit, row, col);
                } else if (rand.nextDouble() <= WOLF_CREATION_PROBABILITY && wolf) {
                    Wolf wolf = new Wolf(true);
                    animals.add(wolf);
                    wolf.setLocation(row, col);
                    field.place(wolf, row, col);
                } else if (rand.nextDouble() <= CROP_CREATION_PROBABILITY && crop) {
                    Crop crop = new Crop();
                    enviromentList.add(crop);
                    crop.setLocation(row, col);
                    field.place(crop, row, col);
                } else if (rand.nextDouble() <= RAIN_CREATION_PROBABILITY && rain) {
                    Rain rain = new Rain();
                    enviromentList.add(rain);
                    rain.setLocation(row, col);
                    field.place(rain, row, col);
                } else if (rand.nextDouble() <= HOLE_CREATION_PROBABILITY && hole) {
                    Hole hole = new Hole();
                    enviromentList.add(hole);
                    hole.setLocation(row, col);
                    field.place(hole, row, col);
                }
                // caso contrário, deixe o local vazio.
            }
        }
        Collections.shuffle(animals);
        Collections.shuffle(enviromentList);
    }
}
