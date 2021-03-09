import java.awt.EventQueue;

/**
 * App class.
 */
public class App {

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
            Console.out("Please specify a source file.");
            return;
        }

        // Check for command-line flags.
        boolean flag = args.length > 1 && args[1].equals("--disable-middle-priority");

        // Queue the Render.
        EventQueue.invokeLater(() -> new Render(args[0], flag));
    }
}
