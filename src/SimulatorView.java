import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * Uma visão gráfica da grade de simulação. A vista mostra um colorido retângulo
 * para cada local que representa seu conteúdo. Ele usa um padrão cor de fundo.
 * As cores para cada tipo de espécie podem ser definidas usando o método
 * setColor.
 * 
 * @author Joao Veronezi
 */
public class SimulatorView extends JFrame implements ActionListener {
    // Cores usadas para os espacos em branco.
    private static final Color EMPTY_COLOR = Color.white;

    // Cor usada para objetos que não tem cor definida.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    // Botoes que estao sendo mostrados na janela.
    private JButton botaoReiniciar, botaoLongSimulator, botaoSimulate, botaoLobo, botaoChuva, botaoPlantacao, botaoToca;
    // Texto para o simulador.
    private JTextField CampoSimulate;
    private JPanel controls;
    private JPanel screen;
    // Um mapa para armazenar cores para os participantes da simulação
    private HashMap colors;
    // Um objeto estatístico computando e armazenando informações de simulação
    private FieldStats stats;

    private Simulator simulator;

    /**
     * Cria uma visão da largura e altura fornecidas.
     */
    public SimulatorView(int height, int width) {
        stats = new FieldStats();
        colors = new HashMap();

        setTitle("Fox and Rabbit Simulation");
        stepLabel = new JLabel(STEP_PREFIX, JLabel.CENTER);
        population = new JLabel(POPULATION_PREFIX, JLabel.CENTER);
        Container contents = getContentPane();

        setLocation(100, 50);

        /**
         * Criando os botoes.
         */
        fieldView = new FieldView(height, width);
        botaoReiniciar = new JButton("Reiniciar");
        botaoLongSimulator = new JButton("Run Long Simulator");
        CampoSimulate = new JTextField(16);
        botaoSimulate = new JButton("Simulate");
        botaoLobo = new JButton("Lobo(Cinza)");
        botaoChuva = new JButton("Chuva(Ciano)");
        botaoPlantacao = new JButton("Plantacao(Verde)");
        botaoToca = new JButton("Toca(Rosa)");
        /**
         * Adicionando os action listeners.
         */
        botaoReiniciar.addActionListener(this);
        botaoLongSimulator.addActionListener(this);
        botaoSimulate.addActionListener(this);
        botaoLobo.addActionListener(this);
        botaoChuva.addActionListener(this);
        botaoPlantacao.addActionListener(this);
        botaoToca.addActionListener(this);

        // Utilizando BorderLayout para ajustar o layout

        JPanel test = new JPanel();
        test.setLayout(new BorderLayout(0, 2));
        test.add(CampoSimulate, BorderLayout.NORTH);
        test.add(botaoSimulate, BorderLayout.SOUTH);

        controls = new JPanel();
        controls.setLayout(new FlowLayout());

        // Adicionando os botoes na tela

        controls.add(botaoLobo);
        controls.add(botaoChuva);
        controls.add(botaoPlantacao);
        controls.add(botaoToca);
        controls.add(test);
        controls.add(botaoLongSimulator);
        controls.add(botaoReiniciar);

        // Ajustando os layouts 

        JPanel screen = new JPanel();
        screen.setLayout(new BorderLayout());
        screen.add(stepLabel, BorderLayout.NORTH);
        screen.add(fieldView, BorderLayout.CENTER);
        screen.add(population, BorderLayout.SOUTH);

        contents.add(screen, BorderLayout.CENTER);
        contents.add(controls, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setVisible(true);

    }

    /**
     * O metodo Action Performed implementa a ação do botão.
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        // pegando qual botao foi pressinoado

        switch (e.getActionCommand()) {
            case "Simulate":
                int value = Integer.parseInt(CampoSimulate.getText());
                if (value > 0) {
                    simulator.simulate(value);
                }

                break;
            case "Reiniciar":
                simulator.reset();

                break;
            case "Run Long Simulator":
                simulator.runLongSimulation();

                break;
            case "Lobo(Cinza)":
                simulator.setWolf();
                System.out.println("Voce adicionou ou retirou Lobos na sua simulacao");
                break;
            case "Chuva(Ciano)":
                simulator.setRain();
                System.out.println("Voce adicionou ou retirou Chuva na sua simulacao");
                break;
            case "Plantacao(Verde)":
                simulator.setCrop();
                System.out.println("Voce adicionou ou retirou Plantacao na sua simulacao");
                break;
            case "Toca(Rosa)":
                simulator.setHole();
                System.out.println("Voce adicionou ou retirou Toca na sua simulacao");
                break;
        }
    }

    /**
     * Método para usar ações relacionadas ao simulador.
     */

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Defina uma cor a ser usada para uma determinada classe de animal.
     */
    public void setColor(Class actorClass, Color color) {
        colors.put(actorClass, color);
    }

    /**
     * Defina uma cor a ser usada para uma determinada classe de animal.
     */
    private Color getColor(Class actorClass) {
        Color col = (Color) colors.get(actorClass);
        if (col == null) {
            // no color defined for this class
            return UNKNOWN_COLOR;
        } else {
            return col;
        }
    }

    /**
     * Mostra o estado atual do campo.
     * 
     * @param step  Em qual interacao esta.
     * @param stats Estado do campo a ser representado.
     */
    public void showStatus(int step, Field field) {
        if (!isVisible())
            setVisible(true);

        stepLabel.setText(STEP_PREFIX + step);

        stats.reset();
        fieldView.preparePaint();

        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                Object animal = field.getObjectAt(row, col);
                if (animal != null) {
                    stats.incrementCount(animal.getClass());
                    fieldView.drawMark(col, row, getColor(animal.getClass()));
                } else {
                    fieldView.drawMark(col, row, EMPTY_COLOR);
                }
            }
        }
        stats.countFinished();

        population.setText(POPULATION_PREFIX + stats.getPopulationDetails(field));
        fieldView.repaint();
    }

    /**
     * Determina se a simulação deve continuar a ser executada.
     * 
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Fornece uma visão gráfica de um campo retangular. Esta é uma classe aninhada
     * (um classe definida dentro de uma classe) que define um componente
     * personalizado para o usuário interface. Este componente exibe o campo. Esta é
     * uma GUI bastante avançada
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Crie um novo componente FieldView.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Diga ao gerente da GUI o quão grande gostaríamos de ser.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR, gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * 
         * Prepare-se para uma nova rodada de pintura. Como o componente pode ser
         * redimensionado, calcule o fator de escala novamente.
         */
        public void preparePaint() {
            if (!size.equals(getSize())) { // if the size has changed...
                size = getSize();
                fieldImage = fieldView.createImage(size.width, size.height);
                g = fieldImage.getGraphics();

                xScale = size.width / gridWidth;
                if (xScale < 1) {
                    xScale = GRID_VIEW_SCALING_FACTOR;
                }
                yScale = size.height / gridHeight;
                if (yScale < 1) {
                    yScale = GRID_VIEW_SCALING_FACTOR;
                }
            }
        }

        /**
         * Pinte no local da grade neste campo em uma determinada cor.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * O componente de visualização de campo precisa ser exibido novamente. Copie a
         * imagem interna para tela.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
