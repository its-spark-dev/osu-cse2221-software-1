import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.FormatChecker;

/**
 * De Jager Approximation Program
 *
 * @author S. Park
 *
 */
public final class ABCDGuesser2 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private ABCDGuesser2() {
    }

    /**
     * Repeatedly asks the user for a positive real number until the user enters
     * one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number entered by the user
     */
    private static double getPositiveDouble(SimpleReader in, SimpleWriter out) {
        boolean check = false; // Set default as false
        double num = -1; // Set default value as negative number
        do {
            out.print("Enter a positive real number: ");
            String s = in.nextLine(); // Receive as a String at the first time
            /*
             * Check user entered number is positive or not. If the user entered
             * number is positive, return true, else return false.
             */
            if (FormatChecker.canParseDouble(s)) {
                num = Double.parseDouble(s); // Type change to double
                if (num > 0) {
                    check = true; // If it is a positive number, false -> true
                } else {
                    out.println("Error: The number must be positive.");
                }
            } else {
                out.println("Error: Invalid input.");
                out.println("Please enter a positive real number.");
            }
        } while (!check);
        return num;
    }

    /**
     * Repeatedly asks the user for a positive real number not equal to 1.0
     * until the user enters one. Returns the positive real number.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return a positive real number not equal to 1.0 entered by the user
     */
    private static double getPositiveDoubleNotOne(SimpleReader in, SimpleWriter out) {
        boolean check = false; // Set default as false
        double num = -1;
        /*
         * Keep asking user a real number which is not equal to 1.0
         */
        do {
            out.print("Enter a positive real number which is not equal to 1.0: ");
            String s = in.nextLine(); // Receive as a String at the first time
            /*
             * First, check user's entry is double or not. If it is double, type
             * change, String -> double.
             */
            if (FormatChecker.canParseDouble(s)) {
                num = Double.parseDouble(s);
                /*
                 * Second, check is it if one or not. If it is one, print out
                 * error message. If it is negative value, print out error
                 * message. Otherwise, return true.
                 */
                if (num > 0) {
                    if (num == 1) {
                        out.println("Error: The number must not be 1.0.");
                    } else {
                        check = true;
                    }
                } else {
                    out.println("Error: The number must be positive.");
                }
            } else {
                out.println("Error: Invalid input.");
                out.println("Please enter a positive real number.");
            }
        } while (!check);
        return num;
    }

    /**
     * Finds the best approximation for the given μ, w, x, y, z.
     *
     * @param mu
     *            entered constant μ
     * @param w
     *            personal number w
     * @param x
     *            personal number x
     * @param y
     *            personal number y
     * @param z
     *            personal number z
     * @return a Result which is the best approximation with details
     */
    private static double[] findBestApproximation(double mu, double w, double x, double y,
            double z) {
        double[] exponents = { -5, -4, -3, -2, -1, -1.0 / 2, -1.0 / 3, -1.0 / 4, 0,
                1.0 / 4, 1.0 / 3, 1.0 / 2, 1, 2, 3, 4, 5 }; //possible exponents in array

        double minError = Double.MAX_VALUE; // Set default error value
        double bestApproximate = 0; // Set default approximation
        double bestA = 0, bestB = 0, bestC = 0, bestD = 0; // Make spaces to store best value of each exponents

        int i, j, k, l;
        double a, b, c, d;

        /*
         * Explore all the possible mix
         */
        for (i = 0; i < exponents.length; i++) {
            a = exponents[i];
            for (j = 0; j < exponents.length; j++) {
                b = exponents[j];
                for (k = 0; k < exponents.length; k++) {
                    c = exponents[k];
                    for (l = 0; l < exponents.length; l++) {
                        d = exponents[l];

                        double approximation = Math.pow(w, a) * Math.pow(x, b)
                                * Math.pow(y, c) * Math.pow(z, d);
                        double relativeError = Math.abs((approximation - mu) / mu) * 100;

                        /*
                         * Update minimum relative error
                         */
                        if (relativeError < minError) {
                            minError = relativeError;
                            bestApproximate = approximation;
                            bestA = a;
                            bestB = b;
                            bestC = c;
                            bestD = d;
                        }
                    }
                }
            }
        }
        return new double[] { bestA, bestB, bestC, bestD, bestApproximate, minError };
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

        double mu = 0, w = 1, x = 1, y = 1, z = 1;

        out.print("For μ, ");
        mu = getPositiveDouble(in, out);

        out.print("For w, ");
        w = getPositiveDoubleNotOne(in, out);

        out.print("For x, ");
        x = getPositiveDoubleNotOne(in, out);

        out.print("For y, ");
        y = getPositiveDoubleNotOne(in, out);

        out.print("For z, ");
        z = getPositiveDoubleNotOne(in, out);

        double[] result = findBestApproximation(mu, w, x, y, z);

        /*
         * Print out the result
         */
        out.println("");
        out.println("μ: " + mu);
        out.println("");
        out.println("(w^a)(x^b)(y^c)(z^d) =");
        out.println("(" + w + "^" + result[0] + ")(" + x + "^" + result[1] + ")(" + y
                + "^" + result[2] + ")(" + z + "^" + result[3] + ")");
        out.println("");
        out.println("Thus, the best approximatation is " + result[4]);
        out.print("In relative error of ");
        out.print(result[5], 2, false);
        out.print("%");
        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }
}
