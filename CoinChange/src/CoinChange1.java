import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class CoinChange1 {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private CoinChange1() {
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

        final int DOLLAR = 100; // A dollar is 100 cents
        final int HALF_DOLLAR = 50;
        final int QUATER = 25;
        final int DIME = 10;
        final int NICKEL = 5;
        final int PENNY = 1;

        int countDollar = 0, countHalfDollar = 0, countQuater = 0, countDime = 0,
                countNickel = 0, countPenny = 0;

        int change;

        out.print("Change in cent: ");
        change = in.nextInteger();

        if (change >= DOLLAR) {
            countDollar = change / DOLLAR;
            change = change % DOLLAR;
        }
        if (change > HALF_DOLLAR) {
            countHalfDollar = change / HALF_DOLLAR;
            change = change % HALF_DOLLAR;
        }
        if (change > QUATER) {
            countQuater = change / QUATER;
            change = change % QUATER;
        }
        if (change > DIME) {
            countDime = change / DIME;
            change = change % DIME;
        }
        if (change > NICKEL) {
            countNickel = change / NICKEL;
            change = change % NICKEL;
        }
        if (change > PENNY) {
            countPenny = change;
        }

        out.println("Dollar: " + countDollar);
        out.println("Half-Dollar: " + countHalfDollar);
        out.println("Quater: " + countQuater);
        out.println("Dime: " + countDime);
        out.println("Nickel: " + countNickel);
        out.print("Penny: " + countPenny);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
