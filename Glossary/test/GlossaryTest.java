import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * JUnit 4 test fixture for the Glossary program.
 *
 * This class systematically tests the core functionality of Glossary: -
 * readGlossary: reading terms and definitions from an input file -
 * generateFiles: producing index.html and per‐term HTML pages - correct
 * formatting (red, bold, italic term headings) - return‐to‐index links -
 * intra‐definition hyperlinks - alphabetical ordering in the index
 *
 * All tests use TemporaryFolder to avoid side effects on the file system.
 *
 * @author S. Park
 */
public class GlossaryTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Helper: write the given content to a file named fileName in the temp
     * folder.
     */
    private Path writeInputFile(String fileName, String content) throws IOException {
        Path p = this.tempFolder.newFile(fileName).toPath();
        Files.write(p, content.getBytes());
        return p;
    }

    /**
     * Test that generateFiles creates index.html and one HTML file per term.
     */
    @Test
    public void testGenerateFilesCreatesAllPages() throws IOException {
        String input = "apple\n" + "A fruit.\n" + "\n" + "banana\n" + "Another fruit.\n"
                + "\n";
        Path inputFile = this.writeInputFile("terms.txt", input);
        String outDir = this.tempFolder.getRoot().getAbsolutePath();

        Glossary g = new Glossary();
        g.readGlossary(inputFile.toString());
        g.generateFiles(outDir);

        /*
         * index.html must exist
         */
        Path index = this.tempFolder.getRoot().toPath().resolve("index.html");
        assertTrue("index.html should be generated", Files.exists(index));

        /*
         * apple.html and banana.html must exist
         */
        assertTrue("apple.html should be generated",
                Files.exists(this.tempFolder.getRoot().toPath().resolve("apple.html")));
        assertTrue("banana.html should be generated",
                Files.exists(this.tempFolder.getRoot().toPath().resolve("banana.html")));
    }

    /**
     * Test that index.html lists terms in alphabetical order and contains
     * correct headings.
     */
    @Test
    public void testIndexHtmlContentAndOrder() throws IOException {
        String input = "zebra\nDefinition.\n\n" + "apple\nDefinition.\n\n"
                + "mango\nDefinition.\n\n";
        Path in = this.writeInputFile("terms.txt", input);
        String out = this.tempFolder.getRoot().getAbsolutePath();

        Glossary g = new Glossary();
        g.readGlossary(in.toString());
        g.generateFiles(out);

        List<String> lines = Files
                .readAllLines(this.tempFolder.getRoot().toPath().resolve("index.html"));

        /*
         * Check for heading and index title
         */
        assertTrue(lines.stream().anyMatch(l -> l.contains("<h2>Glossary</h2>")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("<h3>Index</h3>")));

        /*
         * Collect <li> entries
         */
        List<String> items = lines.stream().filter(l -> l.trim().startsWith("<li>"))
                .map(String::trim).toList();

        /*
         * Expect exactly 3 items in alpha order: apple, mango, zebra
         */
        assertEquals(3, items.size());
        assertTrue(items.get(0).contains(">apple<"));
        assertTrue(items.get(1).contains(">mango<"));
        assertTrue(items.get(2).contains(">zebra<"));
    }

    /**
     * Test that each term page formats the term correctly and includes a
     * return‐to‐index link.
     */
    @Test
    public void testTermPageFormattingAndReturnLink() throws IOException {
        String input = "word\n" + "A unit of language.\n" + "\n";
        Path in = this.writeInputFile("terms.txt", input);
        String out = this.tempFolder.getRoot().getAbsolutePath();

        Glossary g = new Glossary();
        g.readGlossary(in.toString());
        g.generateFiles(out);

        List<String> page = Files
                .readAllLines(this.tempFolder.getRoot().toPath().resolve("word.html"));

        /*
         * Check term heading: red, bold, italic span inside <h1>
         */
        assertTrue(page.stream()
                .anyMatch(l -> l.contains("<h1>")
                        && l.contains("<span style=\"color:red;\">")
                        && l.contains("<b><i>word</i></b>")));

        /*
         * Check return link
         */
        assertTrue(page.stream().anyMatch(
                l -> l.contains("<p>Return to <a href=\"index.html\">Index</a>.</p>")));
    }

    /**
     * Test that definitions containing other terms are hyperlinked correctly.
     */
    @Test
    public void testIntraDefinitionLinking() throws IOException {
        String input = "java\nA programming language. See python for comparison.\n\n"
                + "python\nA scripting language.\n\n";
        Path in = this.writeInputFile("terms.txt", input);
        String out = this.tempFolder.getRoot().getAbsolutePath();

        Glossary g = new Glossary();
        g.readGlossary(in.toString());
        g.generateFiles(out);

        List<String> javaPage = Files
                .readAllLines(this.tempFolder.getRoot().toPath().resolve("java.html"));

        /*
         * The word "python" in the definition should be an <a
         * href="python.html">python</a>
         */
        assertTrue(javaPage.stream()
                .anyMatch(l -> l.contains("<a href=\"python.html\">python</a>")));
    }
}
