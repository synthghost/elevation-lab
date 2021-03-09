import java.io.File;
import java.awt.Color;
import java.util.Random;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * Render class.
 */
class Render extends JPanel {

    /**
     * The serialization version number for the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The window object.
     */
    private JFrame window = new JFrame();

    /**
     * The 2D graphics canvas object.
     */
    private Graphics2D canvas;

    /**
     * The console output stream.
     */
    private static PrintStream console = System.out;

    /**
     * The random number generator.
     */
    private Random rand = new Random();

    /**
     * The number of rows in the matrix.
     */
    private int rows;

    /**
     * The number of columns in the matrix.
     */
    private int cols;

    /**
     * The matrix.
     */
    private int[][] matrix;

    /**
     * The maximum value in the matrix.
     */
    private int maxOfMatrix;

    /**
     * The minimum value in the matrix.
     */
    private int minOfMatrix;

    /**
     * Whether the "--disable-middle-priority" command-line switch is set.
     */
    private boolean disableMiddlePriority = false;

    /**
     * Render class constructor.
     *
     * @param file
     * @param flag
     * @return self
     */
    public Render(String file, boolean flag) {
        // Parse the dimensions.
        parseDimensions(file);

        disableMiddlePriority = flag;

        // Fill the matrix.
        matrix = Matrix.fill(file, new int[rows][cols]);

        // Find the extreme values.
        maxOfMatrix = Matrix.findMax(matrix);
        minOfMatrix = Matrix.findMin(matrix);

        // Initialize the UI.
        initWindow();

        // Print some statistics.
        printStatistics();
    }

    /**
     * Paint the canvas.
     *
     * @param g
     * @return void
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        canvas = (Graphics2D) g;

        // Draw the map.
        drawMap();

        // Draw the paths.
        drawPaths();

        // Draw a flag at the peak.
        drawPeak();

        // Draw a tent at the base.
        drawBase();
    }

    /**
     * Parse the dimensions from a given filename.
     *
     * @param file
     * @return void
     */
    private void parseDimensions(String file) {
        int underscore = file.lastIndexOf("_");
        int delimiter = file.lastIndexOf("x");
        int extension = file.lastIndexOf(".");

        try {
            cols = Integer.parseInt(file.substring(underscore + 1, delimiter));
            rows = Integer.parseInt(file.substring(delimiter + 1, extension));
        } catch (Exception e) {
            console.println("Unable to parse values from file. Please make sure the data is intact.");
            System.exit(-1);
        }
    }

    /**
     * Initialize the window.
     *
     * @return void
     */
    private void initWindow() {
        // Set the panel's background color.
        this.setBackground(Colors.background);

        window.add(this);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setTitle("What A Lovely Place For A Walk");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the window to be slightly taller than the number of rows. This is needed
        // to account for the title bar, which factors into the window's height rather
        // than getting added onto it. This "fix" may result in a small black bar at
        // the bottom of the window, but that's better than having the map cut off.
        window.setSize(cols, rows + 30);

        // Prevent redraws by disabling window resizing. Using resizing to scale the map
        // would be better, but not ultimately worth the effort because the relatively
        // low resolution of the data would result in fairly jarring pixelation. Left
        // enabled here to allow retriggering of pathing calculations via resizes.
        // window.setResizable(false);
    }

    /**
     * Print some statistics about the matrix.
     *
     * @return void
     */
    private void printStatistics() {
        console.printf("%nHIGHEST point of elevation on the map: %d%n", maxOfMatrix);
        console.printf("LOWEST point of elevation on the map: %d%n%n", minOfMatrix);
    }

    /**
     * Draw the map to the canvas using the data from the matrix.
     *
     * @return void
     */
    private void drawMap() {
        for (int i = 0; i < rows; i++) {
            drawRow(i);
        }
    }

    /**
     * Calculate and draw the paths through the matrix.
     *
     * @return void
     */
    private void drawPaths() {
        // Collect the total elevation changes for the various paths.
        int[] paths = new int[rows];

        for (int i = 0; i < rows; i++) {
            // Draw all paths.
            paths[i] = drawLowestElevPath(i, Colors.normalPath);
        }

        // Find the path that had the least total elevation change.
        int minElevation = Matrix.findMinOfArray(paths);
        int minIndex = Matrix.findMinIndexOfArray(paths);

        // Draw the path of least resistance.
        drawLowestElevPath(minIndex, Colors.bestPath);

        // Find the path that had the least total elevation change.
        int maxElevation = Matrix.findMaxOfArray(paths);
        int maxIndex = Matrix.findMaxIndexOfArray(paths);

        // Draw the path of most resistance.
        drawLowestElevPath(maxIndex, Colors.worstPath);

        // Print path statistics.
        console.printf("Total elevation change on the path of LEAST resistance: %d%n", minElevation);
        console.printf("Total elevation change on the path of MOST resistance: %d%n%n", maxElevation);
    }

    /**
     * Draw a flag at the highest point of elevation.
     *
     * @return void
     */
    private void drawPeak() {
        drawImage("flag.png", Matrix.findMaxIndex(matrix));
    }

    /**
     * Draw a tent at the lowest point of elevation.
     *
     * @return void
     */
    private void drawBase() {
        drawImage("tent.png", Matrix.findMinIndex(matrix));
    }

    /**
     * Draw a given row from the matrix.
     *
     * @param row
     * @return void
     */
    private void drawRow(int row) {
        for (int i = 0; i < cols; i++) {
            canvas.setColor(calculateColor(matrix[row][i]));
            canvas.fillRect(i, row, 1, 1);
        }
    }

    /**
     * Draw a path of "least resistance" by evaluating one step at a time.
     *
     * @param row
     * @param color
     * @return int
     */
    private int drawLowestElevPath(int row, Color color) {
        int change = 0;

        canvas.setColor(color);

        for (int i = 0; i < cols - 1; i++) {
            // Draw the current position.
            canvas.fillRect(i, row, 1, 1);

            // Find the direction and elevation of the next position.
            int[] next = findNextPosition(row, i);

            // Shift the position to the appropriate row.
            row += next[0];

            // Keep a running total of the change in elevation.
            change += next[1];
        }

        return change;
    }

    /**
     * Draw a given image on the canvas.
     *
     * @param file
     * @param position
     * @return void
     */
    private void drawImage(String file, int[] position) {
        // Give the image space at the edge of the canvas.
        int padding = 3;

        try {
            // Read the image file.
            BufferedImage img = ImageIO.read(new File(file));

            // Get the dimensions of the image.
            int width = img.getWidth();
            int height = img.getHeight();

            // Account for the dimensions of the image during positioning.
            int imgX = forceRange(position[1] - Math.round(width / 2), padding, cols - 1 - width - padding);
            int imgY = forceRange(position[0] - height, padding, rows - 1 - height - padding);

            // Position the image on the canvas.
            canvas.drawImage(img, imgX, imgY, this);
        } catch (IOException e) {
            // Couldn't draw image. No error necessary.
        }
    }

    /**
     * Calculate the color of a given value in the matrix.
     *
     * @param value
     * @return Color
     */
    private Color calculateColor(int value) {
        // Define the color scaling.
        float half = 0.5f;
        int range = maxOfMatrix - minOfMatrix;
        float ratio = ((float) value - minOfMatrix) / range;

        if (ratio >= half) {
            // Color the higher elevations.
            return Colors.interpolateColor(Colors.midElevation, Colors.highElevation, (ratio - half) * 2);
        }

        // Color the lower elevations.
        return Colors.interpolateColor(Colors.lowElevation, Colors.midElevation, ratio * 2);
    }

    /**
     * Find the position of the next "best" step in the path.
     *
     * @param row
     * @param col
     * @return int[]
     */
    private int[] findNextPosition(int row, int col) {
        // Store the current elevation for later comparison.
        int current = matrix[row][col];

        // Advance the search to the next column.
        col++;

        // Calculate the elevations of possible next steps, being careful to avoid
        // overflowing the matrix at the top and bottom edges.
        int fwdChange = Math.abs(current - matrix[row][col]);
        int upChange = row > 0 ? Math.abs(current - matrix[row - 1][col]) : fwdChange;
        int downChange = row < rows - 1 ? Math.abs(current - matrix[row + 1][col]) : fwdChange;

        // Find the minimum value among the three possible directions.
        int min = Matrix.findMinOfArray(new int[] { upChange, fwdChange, downChange });

        // Define relative row movements.
        int up = -1;
        int fwd = 0;
        int down = 1;

        // The disableMiddlePriority switch disables the "middle-priority" rule from the
        // assignment specification in order to achieve the more divergent results
        // observed in the example screenshots. Pass "--disable-middle-priority" as the
        // second command-line argument to activate the chaos!

        if (disableMiddlePriority && min == fwdChange && min == upChange) {
            // Randomly select between forward and up.
            return rand.nextInt(2) == 0 || row == 0 ? tuple(fwd, fwdChange) : tuple(up, upChange);
        }

        if (disableMiddlePriority && min == fwdChange && min == downChange) {
            // Randomly select between forward and down.
            return rand.nextInt(2) == 0 || row == rows - 1 ? tuple(fwd, fwdChange) : tuple(down, downChange);
        }

        if (min == fwdChange) {
            // Move forward.
            return tuple(fwd, fwdChange);
        }

        if (min == upChange && min == downChange) {
            // Randomly select between up and down.
            return rand.nextInt(2) == 0 ? tuple(up, upChange) : tuple(down, downChange);
        }

        if (min == upChange) {
            // Move up.
            return tuple(up, upChange);
        }

        // Move down.
        return tuple(down, downChange);
    }

    /**
     * Force a given number into a range.
     *
     * @param value
     * @param min
     * @param max
     * @return int
     */
    private int forceRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Group two values together, simulating a tuple.
     *
     * @param value1
     * @param value2
     * @return int[]
     */
    private int[] tuple(int value1, int value2) {
        return new int[] { value1, value2 };
    }
}
