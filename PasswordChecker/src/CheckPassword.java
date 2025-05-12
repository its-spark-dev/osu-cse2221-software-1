import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 *
 * @author S. Park
 *
 */
public final class CheckPassword {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private CheckPassword() {
    }

    /**
     * Checks whether the given String satisfies the OSU criteria for a valid
     * password. Prints an appropriate message to the given output stream.
     *
     * @param passwordCandidate
     *            the String to check
     * @param out
     *            the output stream
     */
    private static void checkPassword(String passwordCandidate, SimpleWriter out) {

        boolean hasUpperCase = false; //checker default false
        boolean hasLowerCase = false; //checker default false
        boolean hasDigit = false; //checker default false

        for (int i = 0; i < passwordCandidate.length(); i++) { //use .length()
            char currentChar = passwordCandidate.charAt(i); //use .charAt

            hasUpperCase = containsUpperCaseLetter(passwordCandidate);

            if (Character.isLowerCase(currentChar)) {
                hasLowerCase = true;
            }

            if (Character.isDigit(currentChar)) {
                hasDigit = true;
            }
        }

        int criteriaMet = 0;

        if (hasUpperCase) {
            criteriaMet++;
        }
        if (hasLowerCase) {
            criteriaMet++;

        }
        if (hasDigit) {
            criteriaMet++;
        }

        if (passwordCandidate.length() < 8) {
            out.println("Password must be at least 8 characters long.");
        }

        if (criteriaMet < 3) {
            out.println("Password must include at least three of the following: ");
            out.println("- Upper case letters");
            out.println("- Lower case letters");
            out.println("- Digits");
        }

        if (passwordCandidate.length() >= 8 && criteriaMet >= 3) {
            out.println("Password is valid.");
        }

    }

    /**
     * Checks if the given String contains an upper case letter.
     *
     * @param str
     *            the String to check
     * @return true if str contains an upper case letter, false otherwise
     */
    private static boolean containsUpperCaseLetter(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isUpperCase(str.charAt(i))) {
                return true;
            }
        }
        return false;
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

        out.print("Enter your password: ");
        checkPassword(in.nextLine(), out);

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
