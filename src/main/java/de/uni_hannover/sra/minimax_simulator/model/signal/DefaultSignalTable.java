package de.uni_hannover.sra.minimax_simulator.model.signal;

import com.google.common.collect.ImmutableList;
import de.uni_hannover.sra.minimax_simulator.model.signal.jump.Jump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public class DefaultSignalTable extends AbstractSignalTable
{
	private final List<SignalRow>		_rows;

	public DefaultSignalTable()
	{
		_rows = new ArrayList<SignalRow>();
	}

	@Override
	public int getRowCount()
	{
		return _rows.size();
	}

	@Override
	public SignalRow getRow(int index)
	{
		return _rows.get(index);
	}

	@Override
	public ImmutableList<SignalRow> getRows()
	{
		return ImmutableList.copyOf(_rows);
	}

	@Override
	public void addSignalRow(int index, SignalRow row)
	{
		_rows.add(index, row);
		fireRowAdded(index, row);
	}

	@Override
	public void removeSignalRow(int index) {
		_rows.remove(index);
		fireRowRemoved(index);
	}

	@Override
	public void exchangeSignalRows(int index1, int index2)
	{
		Collections.swap(_rows, index1, index2);
		fireRowsExchanged(index1, index2);
	}

	@Override
	public void addSignalRow(SignalRow row)
	{
		_rows.add(row);
		int index = getRowCount() - 1;
		fireRowAdded(index, row);
	}

	// TODO: make SignalRow immutable?
	@Override
	public void setRowSignal(int index, String signal, SignalValue value)
	{
		SignalRow row = _rows.get(index);
		row.setSignal(signal, value);
		fireRowReplaced(index, row);
	}

	@Override
	public void setRowJump(int index, Jump jump)
	{
		SignalRow row = _rows.get(index);
		row.setJump(jump);
		fireRowReplaced(index, row);
	}

	@Override
	public void setSignalRow(int index, SignalRow row)
	{
		_rows.set(index, row);
		fireRowReplaced(index, row);
	}

	@Override
	public DescriptionFactory getDescriptionFactory() {
		return null;
	}

	@Override
	public void moveSignalRows(int firstIndex, int lastIndex, int direction)
	{
		checkArgument(firstIndex >= 0 && lastIndex < getRowCount() && lastIndex >= firstIndex);
		
		// move down
		if (direction == 1)
		{
			checkArgument(lastIndex < getRowCount() - 1);
			for (int i = lastIndex; i >= firstIndex; i--)
			{
				Collections.swap(_rows, i, i + 1);
			}
			fireRowsUpdated(firstIndex, lastIndex + 1);
		}
		// move up
		else if (direction == -1)
		{
			checkArgument(firstIndex > 0);
			for (int i = firstIndex; i <= lastIndex; i++)
			{
				Collections.swap(_rows, i - 1, i);
			}
			fireRowsUpdated(firstIndex - 1, lastIndex);
		}
		else
		{
			throw new IllegalArgumentException("direction == -1 or 1");
		}
	}
}