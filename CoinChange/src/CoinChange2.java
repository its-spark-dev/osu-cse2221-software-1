import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class CoinChange2 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private CoinChange2() {
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

        final int[] COIN = { 100, 50, 25, 10, 5, 1 };
        int[] coinCount = new int[6];

        out.print("Change in cent: ");
        int change = in.nextInteger();

        for (int i = 0; i < 6; i++) {
            coinCount[i] = change / COIN[i];
            change = change % COIN[i];
        }

        out.println("Dollar: " + coinCount[0]);
        out.println("Half-Dollar: " + coinCount[1]);
        out.println("Quater: " + coinCount[2]);
        out.println("Dime: " + coinCount[3]);
        out.println("Nickel: " + coinCount[4]);
        out.println("Penny: " + coinCount[5]);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
