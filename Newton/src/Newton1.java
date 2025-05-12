import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class Newton1 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton1() {
    }

    /**
     * Computes estimate of square root of x to within relative error 0.01%.
     *
     * @param x
     *            positive number to compute square root of
     * @return estimate of square root
     */
    private static double sqrt(double x) {
        double r = x; // Initial estimated
        double epsilon = 0.01; // Relative error (0.01%)

        while ((Math.abs(r * r - x) / x) >= (epsilon * epsilon)) {
            r = (r + x / r) / 2;
        }
        return r;
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

        double inputNumber;
        double sqrtNumber;
        String response;

        do {
            out.print("Enter a double number: ");
            inputNumber = in.nextDouble();
            sqrtNumber = sqrt(inputNumber);
            /*
             * Print out that result
             */
            out.println("Enter square root of " + inputNumber + " is " + sqrtNumber);
            /*
             * Ask to continue
             */
            out.print("Press 'y' to calculate another square root: ");
            response = in.nextLine();
        } while (response.equals("y") || response.equals("Y"));

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }
}
