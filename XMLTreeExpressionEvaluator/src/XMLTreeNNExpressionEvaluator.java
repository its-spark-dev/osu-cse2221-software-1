import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;
import components.xmltree.XMLTree;
import components.xmltree.XMLTree1;

/**
 * Program to evaluate XMLTree expressions of {@code int}.
 *
 * @author S. Park
 *
 */
public final class XMLTreeNNExpressionEvaluator {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private XMLTreeNNExpressionEvaluator() {
    }

    /**
     * Evaluate the given expression.
     *
     * @param exp
     *            the {@code XMLTree} representing the expression
     * @return the value of the expression
     * @requires * [exp is a subtree of a well-formed XML arithmetic expression]
     *           and [the label of the root of exp is not "expression"]
     *
     * @ensures evaluate = [the value of the expression]
     */
    private static NaturalNumber evaluate(XMLTree exp) {
        assert exp != null : "Violation of: exp is not null";

        NaturalNumber result = new NaturalNumber1L();

        /*
         * Base Case: The current node is a number
         */
        if (exp.label().equals("number")) {
            String value = exp.attributeValue("value");
            Reporter.assertElseFatalError(value != null,
                    "Missing value attribute in <number> node.");
            result.setFromString(value);
        } else {
            /*
             * Recursive Case: Evaluate left and right children
             */
            NaturalNumber left = evaluate(exp.child(0));
            NaturalNumber right = evaluate(exp.child(1));

            if (exp.label().equals("plus")) {
                result.copyFrom(left);
                result.add(right);
            } else if (exp.label().equals("minus")) {
                Reporter.assertElseFatalError(left.compareTo(right) >= 0,
                        "Cannot subtract: Left operand is smaller than right operand.");
                result.copyFrom(left);
                result.subtract(right);
            } else if (exp.label().equals("times")) {
                result.copyFrom(left);
                result.multiply(right);
            } else if (exp.label().equals("divide")) {
                Reporter.assertElseFatalError(!right.isZero(),
                        "Division by zero is not allowed.");
                result.copyFrom(left);
                result.divide(right);
            } else {
                Reporter.fatalErrorToConsole("Unknown operator: " + exp.label());
            }
        }

        return result;
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

        out.print("Enter the name of an expression XML file: ");
        String file = in.nextLine();
        while (!file.equals("")) {
            XMLTree exp = new XMLTree1(file);
            out.println(evaluate(exp.child(0)));
            out.print("Enter the name of an expression XML file: ");
            file = in.nextLine();
        }

        in.close();
        out.close();
    }

}
