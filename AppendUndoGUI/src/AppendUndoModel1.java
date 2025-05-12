import components.stack.Stack;
import components.stack.Stack1L;

/**
 * Model class.
 *
 * The AppendUndo model consists of the input string and a stack of the strings
 * appended to the output in reverse order.
 *
 * @author Sang Park
 */
public final class AppendUndoModel1 implements AppendUndoModel {

    /**
     * Input text.
     */
    private String input;

    /**
     * Output stack: each entry represents an appended string.
     */
    private final Stack<String> output;

    /**
     * Default constructor.
     */
    public AppendUndoModel1() {
        // Initialize input to empty
        this.input = "";
        // Initialize the output stack (using, e.g., Stack1L from OSU CSE Components)
        this.output = new Stack1L<>();
    }

    @Override
    public String input() {
        return this.input;
    }

    @Override
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Reports this.output.
     *
     * @return the output stack
     */
    @Override
    public Stack<String> output() {
        return this.output;
    }

    /**
     * Helper method: clears the output stack.
     */
    public void clearOutput() {
        this.output.clear();
    }
}
