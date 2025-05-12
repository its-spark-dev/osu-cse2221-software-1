import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import components.set.Set;
import components.set.Set1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * JUnit tests for the StringReassembly methods.
 *
 * @author S. Park
 */
public class StringReassemblyTest {

    /**
     * Test the combination method with a simple overlapping case. For example,
     * "ABCD" and "CDEF" with and overlap of 2 should yield "ABCDEF".
     */
    @Test
    public void testCombinationSimple() {
        String result = StringReassembly.combination("ABCD", "CDEF", 2);
        assertEquals("ABCDEF", result);
    }

    /**
     * Test the combination method with no overlap (overlap = 0).
     */
    @Test
    public void testCombinationNoOverlap() {
        String result = StringReassembly.combination("Hello", "World", 0);
        assertEquals("HelloWorld", result);
    }

    /**
     * Test the combination method with a more challenging overlap. For example,
     * "HELLO" and "LO WORLD" with an overlap of 2 should yield "HELLO WORLD".
     */
    @Test
    public void testCombinationChallenging() {
        String result = StringReassembly.combination("HELLO", "LO WORLD", 2);
        assertEquals("HELLO WORLD", result);
    }

    /**
     * Test addToSetAvoidingSubstrings when the string should not be added
     * because it is a substring of an existing string.
     */
    @Test
    public void testAddToSetAvoidingSubstringsNotAdded() {
        Set<String> set = new Set1L<>();
        set.add("hello");
        /*
         * "hell" is a substring of "hello", so it should not be added.
         */
        StringReassembly.addToSetAvoidingSubstrings(set, "hell");
        assertTrue(set.contains("hello"));
        assertFalse(set.contains("hell"));
    }

    /**
     * Test addToSetAvoidingSubstrings when the string can be added without
     * removal.
     */
    @Test
    public void testAddToSetAvoidingSubstringsAddWithoutRemoval() {
        Set<String> set = new Set1L<>();
        set.add("hello");
        /*
         * "world" is not a substring of "hello" and vice versa.
         */
        StringReassembly.addToSetAvoidingSubstrings(set, "world");
        assertTrue(set.contains("hello"));
        assertTrue(set.contains("world"));
    }

    /**
     * Test addToSetAvoidingSubstrings when the new string causes removal of a
     * substring.
     */
    @Test
    public void testAddToSetAvoidingSubstringsAddWithRemoval() {
        Set<String> set = new Set1L<>();
        set.add("hello");
        /*
         * "hello world" contains "hello" as a substring, so "hello" should be
         * removed and "hello world" added.
         */
        StringReassembly.addToSetAvoidingSubstrings(set, "hello world");
        assertFalse(set.contains("hello"));
        assertTrue(set.contains("hello world"));
    }

    /**
     * Test linesFromInput by reading from a sample file. The file
     * "testLines.txt" should exist in the working directory with these lines:
     * Line one Line two Line three
     */
    @Test
    public void testLinesFromInput() {
        SimpleReader reader = new SimpleReader1L("testLines.txt");
        Set<String> lines = StringReassembly.linesFromInput(reader);
        /*
         * Check that each expected line is present and that there are exactly 3
         * lines.
         */
        assertTrue(lines.contains("Line one"));
        assertTrue(lines.contains("Line two"));
        assertTrue(lines.contains("Line three"));
        assertEquals(3, lines.size());
    }

    /**
     * Test printWithLineSeparators by writing to a temporary file and reading
     * the contents.
     */
    @Test
    public void testPrintWithLineSeparators() {
        /*
         * Use a temporary file for output
         */
        String tempFileName = "tempOutput.txt";
        SimpleWriter out = new SimpleWriter1L(tempFileName);
        String text = "Hello~World";
        StringReassembly.printWithLineSeparators(text, out);
        out.close();

        /*
         * Now read the file and verify its content.
         */
        SimpleReader in = new SimpleReader1L(tempFileName);
        StringBuilder sb = new StringBuilder();
        while (!in.atEOS()) {
            sb.append(in.nextLine());
            if (!in.atEOS()) {
                sb.append(System.lineSeparator());
            }
        }
        in.close();

        /*
         * Delete the temporary file
         */
        new File(tempFileName).delete();

        /*
         * Expected output is "Hello" followed by a newline and then "World".
         */
        String expected = "Hello" + System.lineSeparator() + "World";
        assertEquals(expected, sb.toString());
    }

}
