package de.uni_hannover.sra.minimax_simulator.ui;

import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.WaitingDialog;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides miscellaneous utility methods related to the UI, e.g. invoking {@link Runnable}s
 * in the FX application thread.
 *
 * @author Philipp Rohde
 * @author Martin L&uuml;ck
 */
public class UIUtil {

    private static final Logger LOG = Logger.getLogger("de.uni_hannover.sra.minimax_simulator");

    /**
     * Prevents instance creation of this utility class.
     */
    private UIUtil() {

    }

    /**
     * Like {@link #executeWorker(Runnable, String, String)}, using an empty title and message for
     * the progress dialog.
     *
     * @param runnable
     *          the {@code Runnable} to execute
     */
    public static void executeWorker(Runnable runnable) {
        executeWorker(runnable, "", "", null);
    }

    /**
     * Like {@link #executeWorker(Runnable, String, String, Runnable)}, but the user cannot cancel
     * the computation.
     *
     * @param runnable
     *          the {@code Runnable} to execute
     * @param waitingTitle
     *          the title of the progress dialog
     * @param waitingMessage
     *          the message of the progress dialog
     */
    public static void executeWorker(Runnable runnable, String waitingTitle, String waitingMessage) {
        executeWorker(runnable, waitingTitle, waitingMessage, null);
    }

    /**
     * Executes a {@link Runnable} instance and shows a cancelable progress dialog until the
     * computation finished.<br>
     * Canceling the action in the dialog will NOT automatically interrupt the {@link Runnable} or
     * prevent it from executing, this must be done manually inside the {@code run()} method.<br>
     * The user can specify a second {@link Runnable} that is executed if the user wishes to cancel
     * the computation.
     *
     * @param runnable
     *            the {@code Runnable} to execute
     * @param waitingTitle
     *            the title of the progress dialog
     * @param waitingMessage
     *            the message of the progress dialog
     * @param cancelAction
     *            the {@code Runnable} to execute if the user chooses to cancel the computation
     */
    public static void executeWorker(final Runnable runnable, String waitingTitle, String waitingMessage, Runnable cancelAction) {
        checkNotNull(runnable);

        WaitingDialog waitingDialog = new WaitingDialog(waitingTitle, waitingMessage);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                runnable.run();
                return null;
            }
        };

        task.setOnSucceeded(workerStateEvent -> waitingDialog.close());

        task.setOnCancelled(workerStateEvent -> {
            if (cancelAction != null) {
                cancelAction.run();
            }
        });

        Thread th = new Thread(task);
        th.setDaemon(false);
        th.start();

        if (waitingDialog.isCanceled()) {
            task.cancel(true);
            LOG.fine("executeWorker: execution canceled");
        }

    }

    /**
     * Removes the table header of a {@link TableView} by looking up the {@code TableHeaderRow} and making it invisible.
     *
     * @param table
     *          the {@code TableView} for that the header should be removed
     */
    public static void removeTableHeader(TableView table) {
        Pane header = (Pane) table.lookup("TableHeaderRow");
        header.setMaxHeight(0);
        header.setMinHeight(0);
        header.setPrefHeight(0);
        header.setVisible(false);
    }

    /**
     * Clears the standard text component of a {@link TableColumn} and adds a new {@link Label} which
     * is rotated by -90 degrees.
     *
     * @param col
     *          the {@code TableColumn} for that the header should be rotated
     * @param text
     *          the text to set as column description
     */
    public static void rotateColumnHeader(TableColumn col, String text) {
        col.setText("");
        Label lbl = new Label(text);
        lbl.setRotate(-90);
        Group grp = new Group(lbl);
        col.setGraphic(grp);
    }

    /**
     * If called from the FX application thread, just calls {@link Runnable#run()} on {@code r}.<br>
     * Otherwise schedules its execution there.
     *
     * @param r
     *          the {@code Runnable} to execute in FX application thread
     */
    public static void invokeInFAT(Runnable r) {
        checkNotNull(r);
        if (Platform.isFxApplicationThread()) {
            r.run();
        }
        else {
            Platform.runLater(r);
        }
    }
}