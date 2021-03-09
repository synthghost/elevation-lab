import java.awt.EventQueue;
import java.io.PrintStream;

/**
 * App class.
 */
public class App {

    /**
     * The console output stream.
     */
    private static PrintStream console = System.out;

    /**
     * Main method.
     *
     * @param args
     * @return void
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Make sure a filename was provided.
        if (args.length == 0) {
            console.println("Please specify a source file.");
            return;
        }

        // Check for command-line flags.
        boolean flag = args.length > 1 && args[1].equals("--disable-middle-priority");

        // Queue the Render.
        EventQueue.invokeLater(() -> new Render(args[0], flag));
    }
}
