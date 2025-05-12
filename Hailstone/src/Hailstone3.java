import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class Hailstone3 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Hailstone3() {
    }

    /**
     * Generates and outputs the Hailstone series starting with the given
     * integer.
     *
     * @param n
     *            the starting integer
     * @param out
     *            the output stream
     */
    private static void generateSeries(int n, SimpleWriter out) {
        int l = 0; //l is for counting up how many while loops counted

        out.print(n);
        l++; //Count up for the first input value

        int max = n; //max is for storing the maximum value of the series

        while (n != 1) {
            if (n % 2 != 0) { // Odd number
                out.print(", ");
                n = ((3 * n) + 1);
                out.print(n);
                l++;

                if (max < n) {
                    max = n;
                }

            } else { // Even number
                out.print(", ");
                n = n / 2;
                out.print(n);
                l++;

                if (max < n) {
                    max = n;
                }

            }
        }
        out.println("");
        out.print("The length of the Series: " + l);
        out.println("");
        out.print("The maximum value of the Series is: " + max);
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

        out.print("Enter your starting value: ");
        int x = in.nextInteger();
        generateSeries(x, out);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
