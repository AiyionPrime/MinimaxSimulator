package de.uni_hannover.sra.minimax_simulator.model.signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of {@link SignalTable}.
 *
 * @author Martin L&uuml;ck
 */
public abstract class AbstractSignalTable implements SignalTable {

	private final List<SignalTableListener>	_listeners;

	/**
	 * Constructs an empty {@code AbstractSignalTable}.
	 */
	public AbstractSignalTable() {
		_listeners = new ArrayList<SignalTableListener>(2);
	}

	@Override
	public void addSignalTableListener(SignalTableListener l) {
		if (!_listeners.contains(l)) {
			_listeners.add(l);
		}
	}

	@Override
	public void removeSignalTableListener(SignalTableListener l) {
		_listeners.remove(l);
	}

	/**
	 * Notifies the {@link SignalTableListener}s about a change of the structure.
	 */
	protected void fireStructureChanged() {
		for (SignalTableListener l : _listeners) {
			l.onStructureChanged();
		}
	}

	/**
	 * Notifies the {@link SignalTableListener}s about the addition of a {@link SignalRow}.
	 *
	 * @param index
	 *          the index of the added {@code SignalRow}
	 * @param row
	 *          the added {@code SignalRow}
	 */
	protected void fireRowAdded(int index, SignalRow row) {
		for (SignalTableListener l : _listeners) {
			l.onRowAdded(index, row);
		}
	}

	/**
	 * Notifies the {@link SignalTableListener}s about the deletion of a {@link SignalRow}.
	 *
	 * @param index
	 *          the index of the removed {@code SignalRow}
	 */
	protected void fireRowRemoved(int index) {
		for (SignalTableListener l : _listeners) {
			l.onRowRemoved(index);
		}
	}

	/**
	 * Notifies the {@link SignalTableListener}s about the replacement of a {@link SignalRow}.
	 *
	 * @param index
	 *          the index of the replaced {@code SignalRow}
	 * @param row
	 *          the new {@code SignalRow}
	 */
	protected void fireRowReplaced(int index, SignalRow row) {
		for (SignalTableListener l : _listeners) {
			l.onRowReplaced(index, row);
		}
	}

	/**
	 * Notifies the {@link SignalTableListener}s about the exchange of two {@link SignalRow}s.
	 *
	 * @param index1
	 *          the index of the first {@code SignalRow}
	 * @param index2
	 *          the index of the second {@code SignalRow}
	 */
	protected void fireRowsExchanged(int index1, int index2) {
		for (SignalTableListener l : _listeners) {
			l.onRowsExchanged(index1, index2);
		}
	}

	/**
	 * Notifies the {@link SignalTableListener}s about an update of several {@link SignalRow}s.
	 *
	 * @param fromIndex
	 *          the index of the first {@code SignalRow} that was updated
	 * @param toIndex
	 *          the index of the last {@code SignalRow} that was updated
	 */
	protected void fireRowsUpdated(int fromIndex, int toIndex) {
		for (SignalTableListener l : _listeners) {
			l.onRowsUpdated(fromIndex, toIndex);
		}
	}
}