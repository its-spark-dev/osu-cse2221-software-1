import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class Newton4 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Newton4() {
    }

    /**
     * Computes estimate of square root of x to within relative error of user's
     * given epsilon.
     *
     * @param x
     *            positive number to compute square root of
     * @return estimate of square root
     */
    private static double sqrt(double x, double e) {
        double r = x; // Initial estimated
        double epsilon = e; // Relative error

        /*
         * Calculate square root estimate, check if the given value is zero or
         * not
         */
        if (r == 0) {
            return 0;
        } else {
            while ((Math.abs(r * r - x) / x) >= (epsilon * epsilon)) {
                r = (r + x / r) / 2;
            }
            return r;
        }
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
        double error;

        out.println(
                "Double number should be positive value, or the program will terminate.");
        out.print("Enter a double number: ");
        inputNumber = in.nextDouble();

        while (inputNumber >= 0) {
            out.print("Enter an epsilon value: ");
            error = in.nextDouble();
            sqrtNumber = sqrt(inputNumber, error);
            /*
             * Print out that result
             */
            out.println("Enter square root of " + inputNumber + " is " + sqrtNumber);
            out.print(
                    "If you want to calculate another square root, please enther a positive double value: ");
            inputNumber = in.nextDouble();
        }

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }
}
