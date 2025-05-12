import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * @author S. Park
 *
 */
public class CryptoUtilitiesTest {

    /*
     * Tests of reduceToGCD
     */

    @Test
    public void testReduceToGCD_0_0() {
        NaturalNumber n = new NaturalNumber2(0);
        NaturalNumber nExpected = new NaturalNumber2(0);
        NaturalNumber m = new NaturalNumber2(0);
        NaturalNumber mExpected = new NaturalNumber2(0);
        CryptoUtilities.reduceToGCD(n, m);
        assertEquals(nExpected, n);
        assertEquals(mExpected, m);
    }

    @Test
    public void testReduceToGCD_30_21() {
        NaturalNumber n = new NaturalNumber2(30);
        NaturalNumber nExpected = new NaturalNumber2(3);
        NaturalNumber m = new NaturalNumber2(21);
        NaturalNumber mExpected = new NaturalNumber2(0);
        CryptoUtilities.reduceToGCD(n, m);
        assertEquals(nExpected, n);
        assertEquals(mExpected, m);
    }

    @Test
    public void testReduceToGCD_84_18() {
        /*
         * GCD of 84 and 18 is 6.
         */
        NaturalNumber n = new NaturalNumber2(84);
        NaturalNumber nExpected = new NaturalNumber2(6);
        NaturalNumber m = new NaturalNumber2(18);
        NaturalNumber mExpected = new NaturalNumber2(0);
        CryptoUtilities.reduceToGCD(n, m);
        assertEquals(nExpected, n);
        assertEquals(mExpected, m);
    }

    /*
     * Tests of isEven
     */

    @Test
    public void testIsEven_0() {
        NaturalNumber n = new NaturalNumber2(0);
        NaturalNumber nExpected = new NaturalNumber2(0);
        boolean result = CryptoUtilities.isEven(n);
        assertEquals(nExpected, n);
        assertEquals(true, result);
    }

    @Test
    public void testIsEven_1() {
        NaturalNumber n = new NaturalNumber2(1);
        NaturalNumber nExpected = new NaturalNumber2(1);
        boolean result = CryptoUtilities.isEven(n);
        assertEquals(nExpected, n);
        assertEquals(false, result);
    }

    @Test
    public void testIsEven_Additional() {
        NaturalNumber n = new NaturalNumber2(100);
        NaturalNumber nExpected = new NaturalNumber2(100);
        boolean result = CryptoUtilities.isEven(n);
        assertEquals(nExpected, n);
        assertEquals(true, result);
    }

    /*
     * Tests of powerMod
     */

    @Test
    public void testPowerMod_0_0_2() {
        NaturalNumber n = new NaturalNumber2(0);
        NaturalNumber nExpected = new NaturalNumber2(1);
        NaturalNumber p = new NaturalNumber2(0);
        NaturalNumber pExpected = new NaturalNumber2(0);
        NaturalNumber m = new NaturalNumber2(2);
        NaturalNumber mExpected = new NaturalNumber2(2);
        CryptoUtilities.powerMod(n, p, m);
        assertEquals(nExpected, n);
        assertEquals(pExpected, p);
        assertEquals(mExpected, m);
    }

    @Test
    public void testPowerMod_17_18_19() {
        NaturalNumber n = new NaturalNumber2(17);
        NaturalNumber nExpected = new NaturalNumber2(1);
        NaturalNumber p = new NaturalNumber2(18);
        NaturalNumber pExpected = new NaturalNumber2(18);
        NaturalNumber m = new NaturalNumber2(19);
        NaturalNumber mExpected = new NaturalNumber2(19);
        CryptoUtilities.powerMod(n, p, m);
        assertEquals(nExpected, n);
        assertEquals(pExpected, p);
        assertEquals(mExpected, m);
    }

    @Test
    public void testPowerMod_3_3_5() {
        /*
         * 3^3 = 27; 27 mod 5 = 2.
         */
        NaturalNumber n = new NaturalNumber2(3);
        NaturalNumber nExpected = new NaturalNumber2(2);
        NaturalNumber p = new NaturalNumber2(3);
        NaturalNumber m = new NaturalNumber2(5);
        CryptoUtilities.powerMod(n, p, m);
        assertEquals(nExpected, n);
    }

    /*
     * Test of isWitnessToCompositeness
     */

    @Test
    public void testIsWitnessToCompositeness_Witness() {
        /*
         * For composite n = 15, let w = 4. 4^2 = 16 mod 15 = 1, so w is a
         * witness.
         */
        NaturalNumber n = new NaturalNumber2(15);
        NaturalNumber w = new NaturalNumber2(4);
        boolean result = CryptoUtilities.isWitnessToCompositeness(w, n);
        assertEquals(true, result);
    }

    @Test
    public void testIsWitnessToCompositeness_NonWitness() {
        /*
         * For prime n = 7 and candidate w = 2, 2^6 mod 7 = 64 mod 7 = 1 and 2^2
         * mod 7 = 4, so w is not a witness.
         */
        NaturalNumber n = new NaturalNumber2(7);
        NaturalNumber w = new NaturalNumber2(2);
        boolean result = CryptoUtilities.isWitnessToCompositeness(w, n);
        assertEquals(false, result);
    }

    /*
     * Tests of isPrime2
     */

    @Test
    public void testIsPrime2_PrimeNumbers() {
        /*
         * Test small prime numbers.
         */
        assertEquals(true, CryptoUtilities.isPrime2(new NaturalNumber2(2)));
        assertEquals(true, CryptoUtilities.isPrime2(new NaturalNumber2(3)));
        assertEquals(true, CryptoUtilities.isPrime2(new NaturalNumber2(5)));
        assertEquals(true, CryptoUtilities.isPrime2(new NaturalNumber2(7)));
        assertEquals(true, CryptoUtilities.isPrime2(new NaturalNumber2(11)));
    }

    @Test
    public void testIsPrime2_CompositeNumbers() {
        /*
         * Test some composite numbers.
         */
        assertEquals(false, CryptoUtilities.isPrime2(new NaturalNumber2(4)));
        assertEquals(false, CryptoUtilities.isPrime2(new NaturalNumber2(9)));
        assertEquals(false, CryptoUtilities.isPrime2(new NaturalNumber2(15)));
        assertEquals(false, CryptoUtilities.isPrime2(new NaturalNumber2(21)));
    }

    /*
     * Tests of generateNextLikelyPrime
     */

    @Test
    public void testGenerateNextLikelyPrime() {
        /*
         * For a composite number such as 14, generateNextLikelyPrime should
         * update n to a prime >= 14.
         */
        NaturalNumber n = new NaturalNumber2(14);
        NaturalNumber original = new NaturalNumber2(n);
        CryptoUtilities.generateNextLikelyPrime(n);
        /*
         * Check that the updated n is at least as large as the original value.
         */
        assertTrue(n.compareTo(original) >= 0);
        /*
         * Optionally, check that isPrime2 returns true on n.
         */
        assertEquals(true, CryptoUtilities.isPrime2(n));
    }

}
