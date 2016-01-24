package de.uni_hannover.sra.minimax_simulator.ui.gui.components.tableview;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * An extended {@link SignalTableColumn} for the UI representation of the label attribute of a {@link SignalRow}.
 *
 * @author Philipp Rohde
 */
public class LabelColumn extends SignalTableColumn {

    /**
     * Creates a {@link TableColumn} for the label attribute of a {@link SignalRow}.
     *
     * @param label
     *          the text for the column header
     * @param index
     *          the column index
     */
    public LabelColumn(String label, int index) {
        super(label, "LABEL", index);
        setPrefWidth(100);
        setMaxWidth(200);

        setCellFactory(param -> {
            TableCell<ObservableList, String> cell = new TableCell<ObservableList, String>() {

                @Override
                public void updateItem(String item, boolean empty) {

                    if (item == null) {
                        setGraphic(null);
                    } else {
                        setGraphic(new CenteredCellPane(item));
                    }
                }
            };

            cell.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                if (event.getClickCount() == 2) {
                    int cIndex = cell.getTableView().getSelectionModel().getSelectedIndex();
                    SignalTable signalTable = Main.getWorkspace().getProject().getSignalTable();
                    SignalRow signalRow = signalTable.getRow(cIndex);

                    TextField txtLabel = new TextField(signalRow.getLabel());

                    txtLabel.setOnKeyReleased(evt -> {
                        if (KeyCode.ENTER == evt.getCode()) {
                            signalRow.setLabel(txtLabel.getText());
                            txtLabel.cancelEdit();
                            signalTable.setSignalRow(cIndex, signalRow);
                            Main.getWorkspace().setProjectUnsaved();
                        }
                        else if (KeyCode.ESCAPE == evt.getCode()) {
                            txtLabel.cancelEdit();
                            cell.setGraphic(new Label(signalRow.getLabel()));
                        }
                    });

                    txtLabel.focusedProperty().addListener((val, oldVal, newVal) -> {
                        if (!newVal) {
                            txtLabel.cancelEdit();
                            cell.setGraphic(new Label(signalRow.getLabel()));
                        }
                    });


                    cell.setGraphic(txtLabel);
                    txtLabel.requestFocus();
                }
            });

            return cell;
        });
    }
}
