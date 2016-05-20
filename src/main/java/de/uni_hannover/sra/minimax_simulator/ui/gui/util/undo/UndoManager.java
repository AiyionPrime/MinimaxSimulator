package de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo;

import de.uni_hannover.sra.minimax_simulator.ui.gui.util.undo.commands.Command;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * The {@code UndoManager} manages all {@link Command}s made by the user and calls their {@link Command#undo()} and
 * {@link Command#redo()} methods if the action should be undone or redone.<br>
 * <br>
 * The {@code UndoManager} is a singleton!
 *
 * @author Philipp Rohde
 */
public class UndoManager {

    private SimpleBooleanProperty undoAvailable;
    private SimpleBooleanProperty redoAvailable;

    private Deque<Command> undos;
    private Deque<Command> redos;

    /** Whether the project was saved since it was opened or not. */
    private boolean saved = false;

    /** Whether the project is currently in a saved state or not. */
    private boolean isSaved = false;

    /** The singleton instance. */
    public static final UndoManager INSTANCE = new UndoManager();

    private List<UndoListener> listeners;

    /**
     * Initializes the instance.
     */
    private UndoManager() {
        undos = new ArrayDeque<>();
        redos = new ArrayDeque<>();

        undoAvailable = new SimpleBooleanProperty(false);
        redoAvailable = new SimpleBooleanProperty(false);

        listeners = new ArrayList<>();
    }

    /**
     * Adds the specified {@code UndoListener} to the list of listeners.
     *
     * @param listener
     *         the listener to add
     */
    public void  addListener(UndoListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners about an action using an {@link UndoEvent}.
     */
    private void notifyOnAction() {
        String undo = undos.isEmpty() ? "" : undos.peek().getCommandName();
        String redo = redos.isEmpty() ? "" : redos.peek().getCommandName();

        UndoEvent e = new UndoEvent(isSaved, undoAvailable.get(), redoAvailable.get(), undo, redo);

        for (UndoListener l : listeners) {
            l.onUndoAction(e);
        }
    }

    /**
     * Adds the specified {@code Command} to the list of performed actions and performs the action.
     *
     * @param command
     *         the {@code Command} to add
     */
    public void addCommand(Command command) {
        if (!redos.isEmpty()) {
            redos.clear();
            redoAvailable.set(false);
        }

        undos.push(command);
        command.execute();
        isSaved = false;
        undoAvailable.set(true);

        notifyOnAction();
    }

    /**
     * Undoes the latest action if there is one.
     */
    public void undo() {
        if (!undoAvailable.get() || undos.isEmpty()) {
            return;
        }

        undos.peek().undo();
        redos.push(undos.pop());

        markProject();

        redoAvailable.set(true);
        if (undos.isEmpty()) {
            undoAvailable.set(false);
        }

        notifyOnAction();
    }

    /**
     * Redoes the latest action if there is one.
     */
    public void redo() {
        if (!redoAvailable.get() || redos.isEmpty()) {
            return;
        }

        redos.peek().redo();
        undos.push(redos.pop());

        markProject();

        undoAvailable.set(true);
        if (redos.isEmpty()) {
            redoAvailable.set(false);
        }

        notifyOnAction();
    }

    /**
     * Resets the {@code UndoManager} by clearing the undo and redo stacks.
     */
    public void reset() {
        undos.clear();
        undoAvailable.set(false);

        redos.clear();
        redoAvailable.set(false);

        saved = false;

        notifyOnAction();
    }

    /**
     * Marks the head of the undo stack as saved.
     */
    public void markSavedState() {
        if (undos.isEmpty()) {
            return;
        }

        saved = true;
        undos.forEach(command -> command.unmark());
        undos.peek().mark();
    }

    /**
     * Marks the project as saved or unsaved according to the {@code isMarked} property of the head of the undo stack.
     * If the stack is empty the project will be marked as saved if the project was never saved after opening, unsaved
     * otherwise.
     */
    private void markProject() {
        if (undos.isEmpty() && !saved) {
            isSaved = true;
            return;
        }
        else if (undos.isEmpty()) {
            isSaved = false;
            return;
        }

        if (undos.peek().isMarked()) {
            isSaved = true;
        }
        else {
            isSaved = false;
        }
    }

    /**
     * Gets the {@code undoAvailableProperty}.
     *
     * @return
     *         the {@code undoAvailableProperty}
     */
    public SimpleBooleanProperty isUndoAvailableProperty() {
        return undoAvailable;
    }

    /**
     * Gets the {@code redoAvailableProperty}.
     *
     * @return
     *         the {@code redoAvailableProperty}
     */
    public SimpleBooleanProperty isRedoAvailableProperty() {
        return redoAvailable;
    }

}
