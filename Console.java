import java.io.PrintStream;

/**
 * Console class.
 */
public class Console {

    /**
     * The system output stream.
     */
    private static PrintStream stream = System.out;

    /**
     * Output a message.
     *
     * @param message
     * @return void
     */
    public static void out(String message) {
        stream.println(message);
    }
}
