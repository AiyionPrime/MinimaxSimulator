package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.ConstantMuxInput;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.MuxInputManager.InputEntry;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.Group;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group.MuxConstantGroup;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.GroupLayout;

class ConstantInputGroupManager implements MuxInputGroupManager
{
	private final Set<String>						_defaultGroupSet;

	private final String							_layoutGroupName;
	private final GroupManager						_groupManager;
	private final MinimaxLayout						_layout;

	private final Set<String>						_namesOfConstants;

	private final Map<MuxType, List<InputEntry>>	_inputEntries;

	public ConstantInputGroupManager(String layoutGroupName, Collection<String> defaultGroupSet,
			MinimaxMachine machine)
	{
		_defaultGroupSet = new HashSet<String>(defaultGroupSet);
		_layoutGroupName = layoutGroupName;
		_groupManager = machine.getGroupManager();
		_layout = machine.getLayout();

		_namesOfConstants = new HashSet<String>();

		_inputEntries = new HashMap<MuxType, List<InputEntry>>();
		for (MuxType type : MuxType.values())
			_inputEntries.put(type, new ArrayList<InputEntry>());

		updateLayout();
	}

	@Override
	public void update(MuxInputManager muxInputs)
	{
		List<InputEntry> entries = _inputEntries.get(muxInputs.getMuxType());

		destroyGroups(entries);
		entries.clear();
		entries.addAll(muxInputs.getMuxInputs());
		createGroups(entries);

		updateLayout();
	}

	private void createGroups(List<InputEntry> entries)
	{
		for (InputEntry entry : entries)
		{
			if (entry.input instanceof ConstantMuxInput)
			{
				int value = ((ConstantMuxInput) entry.input).getConstant();
				Group group = new MuxConstantGroup(entry.pinId, entry.pin, value);

				_groupManager.initializeGroup(entry.pinId + Parts._CONSTANT, group);
				_namesOfConstants.add(entry.pinId +  Parts._CONSTANT);	
			}
		}
	}

	private void destroyGroups(List<InputEntry> entries)
	{
		for (InputEntry entry : entries)
		{
			if (entry.input instanceof ConstantMuxInput)
			{
				_groupManager.removeGroup(entry.pinId + Parts._CONSTANT);
				_namesOfConstants.remove(entry.pinId +  Parts._CONSTANT);	
			}
		}
	}

	private void updateLayout()
	{
		Set<String> constants;
		if (_namesOfConstants.isEmpty())
			constants = _defaultGroupSet;
		else
			constants = _namesOfConstants;

		_layout.putLayout(_layoutGroupName, new GroupLayout(constants));
	}
}