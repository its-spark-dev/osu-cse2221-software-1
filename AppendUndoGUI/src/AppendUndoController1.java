import components.stack.Stack;
import components.stack.Stack1L;

/**
 * Controller class.
 *
 * @author Sang Park
 */
public final class AppendUndoController1 implements AppendUndoController {

    /**
     * Model object.
     */
    private final AppendUndoModel model;

    /**
     * View object.
     */
    private final AppendUndoView view;

    /**
     * OSU Stack Components
     */
    private final Stack<String> undoStack = new Stack1L<>();

    /**
     * Updates view to display model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     */
    private static void updateViewToMatchModel(AppendUndoModel model,
            AppendUndoView view) {
        /*
         * Get model info
         */
        String currentInput = model.input();
        Stack<String> outputStack = model.output();
        StringBuilder sb = new StringBuilder();

        for (String s : outputStack) {
            sb.append(s);
        }
        String currentOutput = sb.toString();
        /*
         * Update view to reflect changes in model
         */
        view.updateInputDisplay(currentInput);
        view.updateOutputDisplay(currentOutput);
    }

    /**
     * Constructor; connects {@code this} to the model and view it coordinates.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public AppendUndoController1(AppendUndoModel model, AppendUndoView view) {
        this.model = model;
        this.view = view;
        /*
         * Update view to reflect initial value of model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    /**
     * Processes reset event.
     */
    @Override
    public void processResetEvent() {
        this.undoStack.clear();
        /*
         * Update model in response to this event
         */
        this.model.setInput("");
        this.model.setOutput("");
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    /**
     * Processes append event.
     */
    @Override
    public void processAppendEvent(String input) {
        this.undoStack.push(this.model.output());
        String newOutput = this.model.output() + input;
        this.model.setOutput(newOutput);
        this.model.setInput("");
        updateViewToMatchModel(this.model, this.view);
    }

    /**
     * Processes undo event.
     */
    @Override
    public void processUndoEvent() {
        if (this.undoStack.length() > 0) {
            String prevOutput = this.undoStack.pop();
            String currentOutput = this.model.output();
            String appended = currentOutput.substring(prevOutput.length());
            this.model.setOutput(prevOutput);
            this.model.setInput(appended);
            updateViewToMatchModel(this.model, this.view);
        }
    }
}
