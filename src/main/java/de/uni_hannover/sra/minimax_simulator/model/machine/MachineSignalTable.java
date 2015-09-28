package de.uni_hannover.sra.minimax_simulator.model.machine;

import static com.google.common.base.Preconditions.*;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListEvent;
import de.uni_hannover.sra.minimax_simulator.model.configuration.event.MachineConfigListener;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.signal.DescriptionFactory;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTable;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalTableListener;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalType;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.ConditionalJump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.UnconditionalJump;

/**
 * Decorator for a SignalTable that calculates the descriptions of the SignalRows
 * and updates the ALUSelect codes, ALUOperation codes and JumpTargets.
 * 
 * @author Martin L&uml;ck
 * @author Philipp Rohde
 */
public class MachineSignalTable implements SignalTable, MachineConfigListener {

	private final SignalTable			_theTable;
	private final DescriptionFactory	_descriptionFactory;
	private final MachineConfiguration	_machineConfig;
	private final SignalConfiguration	_signalConfig;

	public MachineSignalTable(SignalTable table, MachineConfiguration machineConfig,
			DescriptionFactory descriptionFactory, SignalConfiguration	signalConfig)
	{
		_theTable = checkNotNull(table);
		_machineConfig = checkNotNull(machineConfig);
		_descriptionFactory = checkNotNull(descriptionFactory);
		_signalConfig = checkNotNull(signalConfig);

		_machineConfig.addMachineConfigListener(this);

		updateAllDescriptions();
	}

	private void updateDescription(int rowIndex, SignalRow row)
	{
		row.setDescription(_descriptionFactory.createDescription(rowIndex, row));
	}

	private void updateAllDescriptions()
	{
		for (int i = 0, n = _theTable.getRowCount(); i < n; i++)
			updateDescription(i, _theTable.getRow(i));
	}

	@Override
	public int getRowCount()
	{
		return _theTable.getRowCount();
	}

	@Override
	public SignalRow getRow(int index)
	{
		return _theTable.getRow(index);
	}

	@Override
	public void addSignalTableListener(SignalTableListener l)
	{
		_theTable.addSignalTableListener(l);
	}

	@Override
	public void removeSignalTableListener(SignalTableListener l)
	{
		_theTable.removeSignalTableListener(l);
	}

	@Override
	public void addSignalRow(SignalRow row)
	{
		updateDescription(_theTable.getRowCount(), row);
		_theTable.addSignalRow(row);
	}

	@Override
	public void addSignalRow(int index, SignalRow row)
	{
		updateJumpTargetsAdded(index);
		updateDescription(index, row);
		_theTable.addSignalRow(index, row);
	}

	@Override
	public void removeSignalRow(int index) {
		updateJumpTargetsRemoved(index);
		_theTable.removeSignalRow(index);
	}

	private void updateJumpTargetsRemoved(int index) {
		List<SignalRow> rows = _theTable.getRows();

		for (int i = 0; i < rows.size(); i++) {
			Jump j = rows.get(i).getJump();

			if ( (j != null) && (j instanceof UnconditionalJump) ) {
				UnconditionalJump uj = (UnconditionalJump) j;
				int oldTarget = uj.getTargetRow();

				if (oldTarget >= index) {
					int newTarget = (oldTarget > index) ? (oldTarget - 1) : -1;
					setRowJump(i, new UnconditionalJump(newTarget));
				}
			}
			else if ( (j != null) && (j instanceof ConditionalJump) ) {
				ConditionalJump cj = (ConditionalJump) j;
				int oldTarget0 = cj.getTargetRow(0);
				int oldTarget1 = cj.getTargetRow(1);

				if ( (oldTarget0 >= index) || (oldTarget1 >= index) ) {
					int newTarget0 = oldTarget0;
					int newTarget1 = oldTarget1;

					if (oldTarget0 == index) {
						newTarget0 = -1;
					}
					else if (oldTarget0 > index) {
						newTarget0 = oldTarget0 - 1;
					}

					if (oldTarget1 == index) {
						newTarget1 = -1;
					}
					else if (oldTarget1 > index) {
						newTarget1 = oldTarget1 - 1;
					}

					setRowJump(i, new ConditionalJump(newTarget0, newTarget1));
				}
			}
		}
	}

	private void updateJumpTargetsAdded(int index) {
		List<SignalRow> rows = _theTable.getRows();

		for (int i = 0; i < rows.size(); i++) {
			Jump j = rows.get(i).getJump();

			if ( (j != null) && (j instanceof UnconditionalJump) ) {
				UnconditionalJump uj = (UnconditionalJump) j;
				int oldTarget = uj.getTargetRow();

				if (oldTarget >= index) {
					int newTarget = oldTarget + 1;
					setRowJump(i, new UnconditionalJump(newTarget));
				}
			}
			else if ( (j != null) && (j instanceof ConditionalJump) ) {
				ConditionalJump cj = (ConditionalJump) j;
				int oldTarget0 = cj.getTargetRow(0);
				int oldTarget1 = cj.getTargetRow(1);

				if ( (oldTarget0 >= index) || (oldTarget1 >= index) ) {
					int newTarget0 = oldTarget0 + 1;
					int newTarget1 = oldTarget1 + 1;

					setRowJump(i, new ConditionalJump(newTarget0, newTarget1));
				}
			}
		}
	}

	private void updateJumpTargetsMoved(int index, int direction) {
		List<SignalRow> rows = _theTable.getRows();

		for (int i = 0; i < rows.size(); i++) {
			Jump j = rows.get(i).getJump();

			if ( (j != null) && (j instanceof UnconditionalJump) ) {
				UnconditionalJump uj = (UnconditionalJump) j;
				int oldTarget = uj.getTargetRow();

				if ( (i == index) && (index+direction == oldTarget) ) {
					setRowJump(i, new UnconditionalJump(oldTarget-direction));
				}
				else if (oldTarget == index) {
					int newTarget = oldTarget + direction;
					setRowJump(i, new UnconditionalJump(newTarget));
				}
			}
			else if ( (j != null) && (j instanceof ConditionalJump) ) {
				ConditionalJump cj = (ConditionalJump) j;
				int oldTarget0 = cj.getTargetRow(0);
				int oldTarget1 = cj.getTargetRow(1);

				if ( (i == index) && ( (index+direction == oldTarget0) || (index+direction == oldTarget1) ) ) {
					int newTarget0 = oldTarget0;
					int newTarget1 = oldTarget1;

					if (oldTarget0 == index + direction) {
						newTarget0 = oldTarget0 - direction;
					}

					if (oldTarget1 == index + direction) {
						newTarget1 = oldTarget1 - direction;
					}

					setRowJump(i, new ConditionalJump(newTarget0, newTarget1));
				}
				else if ( (oldTarget0 == index) || (oldTarget1 == index) ) {
					int newTarget0 = (oldTarget0 == index) ? (oldTarget0 + direction) : oldTarget0;
					int newTarget1 = (oldTarget1 == index) ? (oldTarget1 + direction) : oldTarget1;

					setRowJump(i, new ConditionalJump(newTarget0, newTarget1));
				}
			}
		}
	}

	@Override
	public void exchangeSignalRows(int index1, int index2)
	{
		_theTable.exchangeSignalRows(index1, index2);
	}

	@Override
	public void setRowSignal(int index, String signal, SignalValue value)
	{
		SignalRow row = _theTable.getRow(index);
		row.setSignal(signal, value);
		updateDescription(index, row);
		_theTable.setRowSignal(index, signal, value);
	}

	@Override
	public void setRowJump(int index, Jump jump)
	{
		SignalRow row = _theTable.getRow(index);
		row.setJump(jump);
		updateDescription(index, row);
		_theTable.setRowJump(index, jump);
	}

	@Override
	public void processEvent(MachineConfigEvent event) {
		// update the ALUSelect codes
		if (event instanceof MachineConfigListEvent.MachineConfigMuxEvent) {
			MachineConfigListEvent.MachineConfigMuxEvent muxEvent = (MachineConfigListEvent.MachineConfigMuxEvent) event;
			if (muxEvent.type == MachineConfigListEvent.EventType.ELEMENT_REMOVED) {
				updateAluSelectCodesRemoved(muxEvent.mux, muxEvent.index);
			}
			else if (muxEvent.type == MachineConfigListEvent.EventType.ELEMENTS_EXCHANGED) {
				updateAluSelectCodesExchanged(muxEvent.mux, muxEvent.index, muxEvent.index2);
			}
		}

		// Any signal may now be invalid
		replaceInvalidSignals();

		// Any signal row can have another description now
		updateAllDescriptions();
	}

	private void replaceInvalidSignals()
	{
		ImmutableList<SignalRow> rows = getRows();
		ListIterator<SignalRow> iter = rows.listIterator();
		while (iter.hasNext())
		{
			int index = iter.nextIndex();
			SignalRow row = iter.next();
			for (SignalType signalType : _signalConfig.getSignalTypes())
			{
				SignalValue currentRowValue = row.getSignal(signalType);
				if (!signalType.getValues().contains(currentRowValue))
				{
					row.setSignal(signalType, SignalValue.DONT_CARE);
					_theTable.setRowSignal(index, signalType.getName(), SignalValue.DONT_CARE);
				}
			}
		}
	}

	/**
	 * Updates the ALUSelect codes after deletion of a muxInput.
	 *
	 * @param mux
	 *          the multiplexer that was changed
	 * @param index
	 *          the index of the removed muxInput
	 */
	private void updateAluSelectCodesRemoved(MuxType mux, int index) {
		String lookUp = (mux == MuxType.A) ? "ALU_SELECT_A" : "ALU_SELECT_B";

		for (SignalRow signalRow : _theTable.getRows()) {
			Map<String, SignalValue> signalValues = signalRow.getSignalValues();

			if (signalValues.containsKey(lookUp)) {
				SignalValue value = signalValues.get(lookUp);
				if (!value.isDontCare() && value.intValue() > index) {
					int newIndex = value.intValue() - 1;
					signalRow.setSignalValue(lookUp, newIndex);
				}
				else if (!value.isDontCare() && value.intValue() == index) {
					signalRow.setSignal(lookUp, null);
				}
			}
		}
	}

	/**
	 * Updates the ALUSelect codes after exchanging two muxInputs.
	 *
	 * @param mux
	 *          the multiplexer that was changed
	 * @param index1
	 *          the first muxInput
	 * @param index2
	 *          the second muxInput
	 */
	private void updateAluSelectCodesExchanged(MuxType mux, int index1, int index2) {
		String lookUp = (mux == MuxType.A) ? "ALU_SELECT_A" : "ALU_SELECT_B";

		for (SignalRow signalRow : _theTable.getRows()) {
			Map<String, SignalValue> signalValues = signalRow.getSignalValues();

			if (signalValues.containsKey(lookUp)) {
				SignalValue value = signalValues.get(lookUp);
				if (!value.isDontCare() && value.intValue() == index1) {
					signalRow.setSignalValue(lookUp, index2);
				}
				else if (!value.isDontCare() && value.intValue() == index2) {
					signalRow.setSignalValue(lookUp, index1);
				}
			}
		}
	}

	@Override
	public void setSignalRow(int index, SignalRow row)
	{
		updateDescription(index, row);
		_theTable.setSignalRow(index, row);
	}

	@Override
	public DescriptionFactory getDescriptionFactory() {
		return _descriptionFactory;
	}

	@Override
	public void moveSignalRows(int firstIndex, int lastIndex, int direction)
	{
		System.out.println("moved SignalRows");
		for (int i = lastIndex; i >= firstIndex; i--) {
			updateJumpTargetsMoved(i, direction);
		}
		_theTable.moveSignalRows(firstIndex, lastIndex, direction);
	}

	@Override
	public ImmutableList<SignalRow> getRows()
	{
		return _theTable.getRows();
	}
}