import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * A graphical view of the simulation grid. The view displays a colored
 * rectangle for each location representing its contents. It uses a default
 * background color. Colors for each type of species can be defined using the
 * setColor method.
 * 
 * @author David J. Barnes and Michael Kolling
 * @version 2002-04-23
 */
public class SimulatorView extends JFrame implements ActionListener {
    // Colors used for empty locations.
    private static final Color EMPTY_COLOR = Color.white;

    // Color used for objects that have no defined color.
    private static final Color UNKNOWN_COLOR = Color.gray;

    private final String STEP_PREFIX = "Step: ";
    private final String POPULATION_PREFIX = "Population: ";
    private JLabel stepLabel, population;
    private FieldView fieldView;
    // Buttons that are shown in the screen.
    private JButton botaoReiniciar, botaoLongSimulator, botaoSimulate, botaoLobo, botaoChuva, botaoPlantacao, botaoToca;
    // Text for simulate. 
    private JTextField CampoSimulate;
    private JPanel controls;
    private JPanel screen;
    // A map for storing colors for participants in the simulation
    private HashMap colors;
    // A statistics object computing and storing simulation information
    private FieldStats stats;

    private Simulator simulator;

    /**
     * Create a view of the given width and height.
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
		 * Creating the buttons.
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
		 * Adding the action listeners.
		 */
		 
        botaoReiniciar.addActionListener(this);
        botaoLongSimulator.addActionListener(this);
        botaoSimulate.addActionListener(this);
        botaoLobo.addActionListener(this);
        botaoChuva.addActionListener(this);
        botaoPlantacao.addActionListener(this);
        botaoToca.addActionListener(this);

		// Using BorderLayout to adjust the layout
        JPanel test = new JPanel();
        test.setLayout(new BorderLayout(0, 2));
        test.add(CampoSimulate, BorderLayout.NORTH);
        test.add(botaoSimulate, BorderLayout.SOUTH);

        controls = new JPanel();
        controls.setLayout(new FlowLayout());
        
		// Adding the buttons to screen
        controls.add(botaoLobo);
        controls.add(botaoChuva);
        controls.add(botaoPlantacao);
        controls.add(botaoToca);
        controls.add(test);
        controls.add(botaoLongSimulator);
        controls.add(botaoReiniciar);

		// Setting the layouts
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
	* Action Performed method thats implements the button action .
	*/
	
    @Override
    public void actionPerformed(ActionEvent e) {
		// getting wich button is pressed
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
                System.out.println("Lobo");
                break;
            case "Chuva(Ciano)":
                simulator.setRain();
                System.out.println("Chuva");
                break;
            case "Plantacao(Verde)":
                simulator.setCrop();
                System.out.println("Plantacao");
                break;
            case "Toca(Rosa)":
                simulator.setHole();
                System.out.println("Toca");
                break;
        }
    }
    
    /**
     * Method to use actions related to simulator.
     */
    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
    }

    /**
     * Define a color to be used for a given class of animal.
     */
    public void setColor(Class actorClass, Color color) {
        colors.put(actorClass, color);
    }

    /**
     * Define a color to be used for a given class of animal.
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
     * Show the current status of the field.
     * 
     * @param step  Which iteration step it is.
     * @param stats Status of the field to be represented.
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
     * Determine whether the simulation should continue to run.
     * 
     * @return true If there is more than one species alive.
     */
    public boolean isViable(Field field) {
        return stats.isViable(field);
    }

    /**
     * Provide a graphical view of a rectangular field. This is a nested class (a
     * class defined inside a class) which defines a custom component for the user
     * interface. This component displays the field. This is rather advanced GUI
     * stuff - you can ignore this for your project if you like.
     */
    private class FieldView extends JPanel {
        private final int GRID_VIEW_SCALING_FACTOR = 6;

        private int gridWidth, gridHeight;
        private int xScale, yScale;
        Dimension size;
        private Graphics g;
        private Image fieldImage;

        /**
         * Create a new FieldView component.
         */
        public FieldView(int height, int width) {
            gridHeight = height;
            gridWidth = width;
            size = new Dimension(0, 0);
        }

        /**
         * Tell the GUI manager how big we would like to be.
         */
        public Dimension getPreferredSize() {
            return new Dimension(gridWidth * GRID_VIEW_SCALING_FACTOR, gridHeight * GRID_VIEW_SCALING_FACTOR);
        }

        /**
         * Prepare for a new round of painting. Since the component may be resized,
         * compute the scaling factor again.
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
         * Paint on grid location on this field in a given color.
         */
        public void drawMark(int x, int y, Color color) {
            g.setColor(color);
            g.fillRect(x * xScale, y * yScale, xScale - 1, yScale - 1);
        }

        /**
         * The field view component needs to be redisplayed. Copy the internal image to
         * screen.
         */
        public void paintComponent(Graphics g) {
            if (fieldImage != null) {
                g.drawImage(fieldImage, 0, 0, null);
            }
        }
    }
}
