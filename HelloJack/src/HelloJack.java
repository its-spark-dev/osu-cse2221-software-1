import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class HelloJack {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private HelloJack() {
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Get user's name
         */
        out.print("Enter your name: ");
        /*
         * Save it into name variable
         */
        String name = in.nextLine();
        out.println("Hello, " + name);
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
