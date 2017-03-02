package de.uni_hannover.sra.minimax_simulator.ui.gui;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryAccessListener;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.gui.components.dialogs.MemoryUpdateDialog;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * <b>FXController of the MemoryTable</b><br>
 * <br>
 * This controller handles every GUI interaction with the memory {@link TableView} and associated {@link Control}s.
 * The MemoryTable is the part of the DebuggerView and MemoryView.
 *
 * @author Philipp Rohde
 */
public class MemoryTable implements MemoryAccessListener {

    private static final Logger LOG = Logger.getLogger("de.uni_hannover.sra.minimax_simulator");

    @FXML private TitledPane paneMemory;

    private String addressFormatString;
    private static MachineMemory mMemory;

    private static final int PAGE_SIZE = 16;
    private int pageCount;
    private int page;
    private int cachedPageStart;

    private final TextResource res;

    @FXML private TableView<MemoryTableModel> memTable;

    @FXML private TableColumn<MemoryTableModel, String> colMemAdr;
    @FXML private TableColumn<MemoryTableModel, String> colMemDec;
    @FXML private TableColumn<MemoryTableModel, String> colMemHex;
    @FXML private TableColumn<MemoryTableModel, String> colMemBin;

    @FXML private TextField txtAddressField;
    @FXML private Label lblMemPage;

    @FXML private Button btnNextPage;
    @FXML Button btnPrevPage;
    @FXML Button btnFirstPage;
    @FXML Button btnLastPage;
    @FXML Button btnHexBin;

    /**
     * Initializes the variables.
     */
    public MemoryTable() {
        res = Main.getTextResource("project");
        page = cachedPageStart = 0;
    }

    /**
     * This method is called during application start up and initializes the {@code MemoryTable}
     * as much as possible without having any project data.
     */
    public void initialize() {
        txtAddressField.textProperty().addListener((observable, oldValue, newValue) -> {
            String text = newValue.trim();
            if (text.isEmpty()) {
                return;
            }
            if (text.startsWith("0x")) {
                text = text.substring(2);
            }
            try {
                int value = Integer.parseInt(text, 16);
                LOG.finest("select address: " + value);
                selectAddress(value);
            } catch (NumberFormatException nfe) {
                // ignore malformed input
            }
        });

        setLocalizedTexts();
        setTooltips();
    }

    /**
     * Sets localized texts from resource for the GUI elements.
     */
    private void setLocalizedTexts() {
        final List<TableColumn> tableColumnsMem = new ArrayList<>(Arrays.asList(colMemAdr, colMemDec, colMemHex));
        for (TableColumn col : tableColumnsMem) {
            col.setText(res.get(col.getId().replace("_", ".")));
        }
        paneMemory.setText(res.get(paneMemory.getId().replace("_", ".")));
        updateMemPageLabel();
    }

    /**
     * Sets the {@link Tooltip}s for the {@link Button}s.
     */
    private void setTooltips() {
        btnFirstPage.setTooltip(new Tooltip(res.get("memtable.first.tip")));
        btnPrevPage.setTooltip(new Tooltip(res.get("memtable.previous.tip")));
        btnNextPage.setTooltip(new Tooltip(res.get("memtable.next.tip")));
        btnLastPage.setTooltip(new Tooltip(res.get("memtable.last.tip")));
        btnHexBin.setTooltip(new Tooltip(res.get("memtable.swap.tip")));

        txtAddressField.setTooltip(new Tooltip(res.get("memtable.address.tip")));
    }

    private void updateMemPageLabel() {
        lblMemPage.setText(res.format("memtable.page", page + 1, pageCount));
    }

    /**
     * Selects a specific address of the memory {@link TableView}.
     *
     * @param address
     *          the address to select
     */
    private void selectAddress(int address) {
        int page;
        int row;
        if (address < 0) {
            page = 0;
            row = 0;
        }
        else {
            if (address > mMemory.getMaxAddress()) {
                address = mMemory.getMaxAddress();
            }

            page = address / PAGE_SIZE;
            if (page >= pageCount) {
                page = pageCount - 1;
                row = PAGE_SIZE - 1;
            }
            else {
                row = address % PAGE_SIZE;
            }
        }

        setPage(page);

        if (row != memTable.getSelectionModel().getSelectedIndex()) {
            memTable.getSelectionModel().select(row);
        }
    }

    /**
     * This method is called from the controller of the views containing the MemoryTable
     * if a new project was created or a opened. It initializes the {@link TableView}
     * of the memory because it needs project data.
     */
    public void initMemTable() {
        mMemory = Main.getWorkspace().getProject().getMachine().getMemory();
        mMemory.addMemoryAccessListener(this);
        addressFormatString = Util.createHexFormatString(mMemory.getAddressWidth(), false);

        int addressRange = mMemory.getMaxAddress() - mMemory.getMinAddress();
        pageCount = (addressRange - 1) / PAGE_SIZE + 1;

        txtAddressField.setText("");
        firstPage();
        updateMemPageLabel();

        colMemAdr.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMemDec.setCellValueFactory(new PropertyValueFactory<>("decimal"));
        colMemHex.setCellValueFactory(new PropertyValueFactory<>("hex"));
        colMemBin.setCellValueFactory(new PropertyValueFactory<>("bin"));

        updateMemTable();

        // open edit dialog at double click
        memTable.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
                int address = memTable.getSelectionModel().getSelectedIndex() + cachedPageStart;
                // open edit dialog
                new MemoryUpdateDialog(address, mMemory).show();
            }
        });

        // set next/previous page at scroll
        memTable.addEventFilter(ScrollEvent.ANY, scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY > 0) {
                prevPage();
            } else {
                nextPage();
            }
        });
    }

    /**
     * Updates the {@link TableView} for the memory.
     */
    public void updateMemTable() {
        ObservableList<MemoryTableModel> data = FXCollections.observableArrayList();

        MemoryState mState = mMemory.getMemoryState();
        for (int i = 0; i < PAGE_SIZE; i++) {
            int value = mState.getInt(cachedPageStart + i);
            data.add(new MemoryTableModel(String.format(addressFormatString, cachedPageStart + i), value));
        }

        memTable.setItems(data);
    }

    /**
     * Sets the next memory page to the {@link TableView}.
     */
    public void nextPage() {
        this.setPage(page +1);
    }

    /**
     * Sets the previous memory page to the {@link TableView}.
     */
    public void prevPage() {
        this.setPage(page - 1);
    }

    /**
     * Sets the first memory page to the {@link TableView}.
     */
    public void firstPage() {
        this.setPage(0);
    }

    /**
     * Sets the last memory page to the {@link TableView}.
     */
    public void lastPage() {
        this.setPage(pageCount - 1);
    }

    /**
     * Sets the memory page with the given page number to the {@link TableView}.
     *
     * @param newPage
     *          the number of the new page
     */
    private void setPage(int newPage) {
        if (newPage >= pageCount || newPage < 0) {
            return;
        }
        page = newPage;
        cachedPageStart = page * PAGE_SIZE + mMemory.getMinAddress();

        updateMemTable();
        updateMemPageLabel();
    }

    /**
     * Switches between hexadecimal and binary representation of the memory values.
     */
    public void swapHexBin() {
        colMemHex.setVisible(!colMemHex.isVisible());
        colMemBin.setVisible(!colMemBin.isVisible());
    }

    @Override
    public void memoryReadAccess(int address, int value) {
        // there is nothing to do if the memory was read
    }

    @Override
    public void memoryWriteAccess(int address, int value) {
        // only update the affected table row
        MemoryTableModel entry = memTable.getItems().get(address % PAGE_SIZE);
        entry.setValue(value);
        memTable.refresh();
    }

    @Override
    public void memoryReset() {
        // update the whole table
        updateMemTable();
    }

    @Override
    public void memoryChanged() {
        //update the whole table
        updateMemTable();
    }

    /**
     * This class represents the table model for the memory {@link TableView}.<br>
     * <br>
     * It stores the address as well as the decimal and hexadecimal value as {@link SimpleStringProperty}.
     *
     * @author Philipp Rohde
     */
    public static class MemoryTableModel {

        private final SimpleStringProperty address;
        private final SimpleIntegerProperty decimal;
        private final SimpleStringProperty hex;
        private final SimpleStringProperty bin;

        private static final String HEX_FORMAT_STRING = "0x%08X";

        /**
         * Constructs a new {@code MemoryTableModel} and sets the properties.
         *
         * @param address
         *          the memory address to represent
         * @param value
         *          the value stored at the address
         */
        private MemoryTableModel(String address, int value) {
            this.address = new SimpleStringProperty(address);
            this.decimal = new SimpleIntegerProperty(value);
            this.hex = new SimpleStringProperty(String.format(HEX_FORMAT_STRING, value));

            this.bin = new SimpleStringProperty(Util.to32BitBinary(value));
        }

        /**
         * Gets the address of the memory address.
         *
         * @return
         *          the address
         */
        public String getAddress() {
            return address.get();
        }

        /**
         * Gets the decimal value stored at the address.
         *
         * @return
         *          the decimal value
         */
        public int getDecimal() {
            return decimal.get();
        }

        /**
         * Gets the hexadecimal value stored at the address.
         *
         * @return
         *          the hexadecimal value
         */
        public String getHex() {
            return hex.get();
        }

        /**
         * Gets the binary value stored at the address.
         *
         * @return
         *          the binary value
         */
        public String getBin() {
            return bin.get();
        }

        /**
         * Sets the decimal and hexadecimal value to the specified decimal value.<br>
         * The hexadecimal value will be converted from decimal to hexadecimal.
         *
         * @param value
         *          the new value
         */
        public void setValue(int value) {
            this.decimal.set(value);
            this.hex.set(String.format(HEX_FORMAT_STRING, value));
            this.bin.set(Util.to32BitBinary(value));
        }
    }

}
