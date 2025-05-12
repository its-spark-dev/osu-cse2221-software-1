import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Glossary generates a set of HTML files from an input file containing terms
 * and definitions.
 * <p>
 * The input file consists of a term (a single word) on one line followed by one
 * or more lines of its definition (terminated by an empty line). The program
 * then produces an "index.html" file listing all terms in alphabetical order,
 * along with separate HTML pages for each term. Within each term's page, the
 * term appears at the top in red, bold, and italicized text. Additionally, any
 * term appearing within a definition is hyperlinked to its corresponding page.
 * </p>
 *
 * <p>
 * Only OSU CSE Components (SimpleReader, SimpleWriter, Map, Queue) and standard
 * Java libraries (e.g., Scanner) are used.
 * </p>
 *
 * @author S. Park
 */
public class Glossary {

    /**
     * A map that stores the glossary terms and their corresponding definitions.
     * The key is the term (a single word), and the value is the definition
     * text.
     */
    private Map<String, String> glossary;

    /**
     * Constructs an empty Glossary.
     */
    public Glossary() {
        this.glossary = new Map1L<String, String>();
    }

    /**
     * Reads the glossary from an input file and populates the glossary map.
     * <p>
     * The file is read using OSU Components' SimpleReader. Each term is assumed
     * to be a single word on a line, followed by one or more lines of
     * definition text. A blank line terminates the definition for a term.
     * </p>
     *
     * @param inputFileName
     *            the complete path of the input file
     */
    public void readGlossary(String inputFileName) {
        SimpleReader reader = new SimpleReader1L(inputFileName);
        /*
         * Read until end of file
         */
        while (!reader.atEOS()) {
            /*
             * 1) Skip any blank lines
             */
            String line = reader.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            /*
             * 2) We've found a term
             */
            String term = line;
            StringBuilder defBuilder = new StringBuilder();
            /*
             * 3) Read definition lines until we hit a blank line or EOF
             */
            boolean moreDefLines = true;
            while (!reader.atEOS() && moreDefLines) {
                String defLine = reader.nextLine();
                if (defLine.trim().isEmpty()) {
                    /*
                     * blank line ⇒ stop collecting definition lines
                     */
                    moreDefLines = false;
                } else {
                    defBuilder.append(defLine).append(" ");
                }
            }
            /*
             * 4) Add the term + definition to the map
             */
            String definition = defBuilder.toString().trim();
            this.glossary.add(term, definition);
        }
        reader.close();
    }

    /**
     * Generates the HTML files for the glossary.
     * <p>
     * Creates an index HTML file listing all terms (sorted alphabetically) and
     * individual HTML pages for each term. In each term's page, the term is
     * displayed in red, bold, italicized text, and its definition is processed
     * so that any occurrence of another term is replaced with a hyperlink.
     * </p>
     *
     * @param outputFolder
     *            the path of the folder where the HTML files will be saved; the
     *            folder must exist.
     */
    public void generateFiles(String outputFolder) {
        /*
         * 1) Extract keys from the glossary map into a Queue.
         */
        Queue<String> keysQueue = new Queue1L<String>();
        for (Map.Pair<String, String> pair : this.glossary) {
            keysQueue.enqueue(pair.key());
        }
        /*
         * Convert the queue to an array.
         */
        String[] keysArray = new String[keysQueue.length()];
        int idx = 0;
        while (keysQueue.length() > 0) {
            keysArray[idx++] = keysQueue.dequeue();
        }
        /*
         * 2) Sort the array of keys using a simple bubble sort algorithm.
         */
        for (int i = 0; i < keysArray.length - 1; i++) {
            for (int j = 0; j < keysArray.length - i - 1; j++) {
                if (keysArray[j].compareTo(keysArray[j + 1]) > 0) {
                    String temp = keysArray[j];
                    keysArray[j] = keysArray[j + 1];
                    keysArray[j + 1] = temp;
                }
            }
        }
        /*
         * 3) Generate the index.html file.
         */
        String indexFilePath = outputFolder + "/index.html";
        SimpleWriter indexWriter = new SimpleWriter1L(indexFilePath);
        indexWriter.println("<html>");
        indexWriter.println("<head><title>Glossary Index</title></head>");
        indexWriter.println("<body>");
        indexWriter.println("<h2>Glossary</h2>");
        indexWriter.println("<hr>");
        indexWriter.println("<h3>Index</h3>");
        indexWriter.println("<ul>");
        /*
         * Create a list item with hyperlink for each term.
         */
        for (String term : keysArray) {
            indexWriter
                    .println("<li><a href=\"" + term + ".html\">" + term + "</a></li>");
        }
        indexWriter.println("</ul>");
        indexWriter.println("</body>");
        indexWriter.println("</html>");
        indexWriter.close();
        /*
         * 4) Generate an individual HTML page for each term.
         */
        for (String term : keysArray) {
            String termDefinition = this.glossary.value(term);
            /*
             * Process the definition to replace occurrences of terms with
             * hyperlinks.
             */
            String processedDefinition = this.hyperlinkDefinition(termDefinition);
            String termFilePath = outputFolder + "/" + term + ".html";
            SimpleWriter termWriter = new SimpleWriter1L(termFilePath);
            termWriter.println("<html>");
            termWriter.println("<head><title>" + term + "</title></head>");
            termWriter.println("<body>");
            /*
             * Display the term in red, bold, and italicized text.
             */
            termWriter.println("<h1><span style=\"color:red;\"><b><i>" + term
                    + "</i></b></span></h1>");
            termWriter.println("<p>" + processedDefinition + "</p>");
            termWriter.println("<hr>");
            termWriter.println("<p>Return to <a href=\"index.html\">Index</a>.</p>");
            termWriter.println("</body>");
            termWriter.println("</html>");
            termWriter.close();
        }
    }

    /**
     * Processes the given definition string by replacing any occurrence of a
     * term (from the glossary) with a hyperlink to that term's HTML page.
     * <p>
     * The method splits the definition into tokens based on whitespace and
     * removes any leading or trailing punctuation from each token before
     * checking if it matches a term. If a match is found, the token is replaced
     * with an HTML hyperlink.
     * </p>
     *
     * @param definition
     *            the definition text to be processed
     * @return the processed definition with hyperlinks inserted where
     *         applicable
     */
    private String hyperlinkDefinition(String definition) {
        /*
         * Split the definition based on spaces.
         */
        String[] tokens = definition.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            String stripped = token;
            /*
             * Remove leading punctuation.
             */
            while (stripped.length() > 0
                    && !Character.isLetterOrDigit(stripped.charAt(0))) {
                stripped = stripped.substring(1);
            }
            /*
             * Remove trailing punctuation.
             */
            while (stripped.length() > 0 && !Character
                    .isLetterOrDigit(stripped.charAt(stripped.length() - 1))) {
                stripped = stripped.substring(0, stripped.length() - 1);
            }
            /*
             * If the token (after stripping punctuation) is a glossary key,
             * replace it with a hyperlink.
             */
            if (this.glossary.hasKey(stripped)) {
                token = token.replace(stripped,
                        "<a href=\"" + stripped + ".html\">" + stripped + "</a>");
            }
            result.append(token);
            if (i < tokens.length - 1) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    /**
     * The main method that prompts the user for the input file path and output
     * folder path, reads the glossary data, and generates the HTML files.
     *
     * @param args
     *            command line arguments (not used)
     */
    public static void main(String[] args) {
        /*
         * Create a SimpleWriter for console output.
         */
        SimpleWriter console = new SimpleWriter1L();
        console.print("Enter input file path: ");
        /*
         * Use the standard Scanner to read console input.
         */
        java.util.Scanner sc = new java.util.Scanner(System.in);
        String inputFile = sc.nextLine();
        console.print("Enter output folder path: ");
        String outputFolder = sc.nextLine();
        sc.close();
        console.close();

        Glossary glossary = new Glossary();
        glossary.readGlossary(inputFile);
        glossary.generateFiles(outputFolder);

        /*
         * Output a success message.
         */
        SimpleWriter out = new SimpleWriter1L();
        out.println("Glossary HTML files generated successfully.");
        out.close();
    }
}
