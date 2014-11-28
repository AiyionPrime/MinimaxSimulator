package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.ExtensionList;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.RegisterManager.RegisterType;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.FlowLeftLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.StackLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

class RegisterExtensionList implements ExtensionList<RegisterExtension>
{
	private final MinimaxMachine			_machine;
	private final MinimaxLayout				_layout;

	private final List<String>				_registerNames;
	private final List<RegisterExtension>	_registerExtensions;

	private LayoutSet						_stackLayout;
	private LayoutSet						_flowLayout;

	private RegisterManager					_registerManager;

	public RegisterExtensionList(MinimaxMachine machine, RegisterManager registerManager)
	{
		_machine = machine;
		_layout = machine.getLayout();

		_registerNames = new ArrayList<String>();
		_registerExtensions = new ArrayList<RegisterExtension>();

		_registerManager = registerManager;

		setStackLayout();
	}

	@Override
	public void add(RegisterExtension element)
	{
		removeStackLayout();

		addInternal(element);

		// Align the array of registers and display
		setStackLayout();
		_machine.updateLayout();
	}

	@Override
	public void addAll(Collection<? extends RegisterExtension> elements)
	{
		removeStackLayout();

		for (RegisterExtension element : elements)
			addInternal(element);

		// Align the array of registers and display
		setStackLayout();
		_machine.updateLayout();
	}

	@Override
	public void remove(int index)
	{
		removeStackLayout();

		removeInternal(index);

		// Align the array of registers and display
		setStackLayout();
		_machine.updateLayout();
	}

	@Override
	public void swap(int index1, int index2)
	{
		removeStackLayout();

		Collections.swap(_registerNames, index1, index2);
		Collections.swap(_registerExtensions, index1, index2);

		// Align the array of registers and display
		setStackLayout();
		_machine.updateLayout();
	}

	@Override
	public void set(int index, RegisterExtension element)
	{
		removeStackLayout();

		removeInternal(index);
		addInternal(index, element);

		// Align the array of registers and display
		setStackLayout();
		_machine.updateLayout();
	}

	private void addInternal(RegisterExtension element)
	{
		addInternal(_registerNames.size(), element);
	}

	private void addInternal(int index, RegisterExtension element)
	{
		String registerId = _registerManager.addRegister(RegisterType.EXTENDED, element.getName());
		_registerNames.add(index, registerId);
		_registerExtensions.add(index, element);
	}

	private void removeInternal(int index)
	{
		RegisterExtension ext = _registerExtensions.remove(index);
		_registerNames.remove(index);
		_registerManager.removeRegister(ext.getName());
	}

	private void setStackLayout()
	{
		List<String> outJunctionNames = new ArrayList<String>(_registerNames.size());
		for (String name : _registerNames)
			outJunctionNames.add(name + Parts._OUT_JUNCTION + Parts._ANCHOR);

		List<String> names = new ArrayList<String>(_registerNames);
		Collections.reverse(names);

		_stackLayout = new StackLayoutSet(Parts.GROUP_BASE_REGISTERS, names, 40,
				Parts.GROUP_EXTENDED_REGISTERS);
		_layout.putLayouts(_stackLayout);

		_flowLayout = new FlowLeftLayoutSet(Parts.GROUP_MUX_LINE, outJunctionNames, 8,
				Parts.GROUP_MUX_EXT_REGISTERS);
		_layout.putLayouts(_flowLayout);
	}

	private void removeStackLayout()
	{
		if (_stackLayout != null)
		{
			_layout.removeLayouts(_stackLayout);
			_stackLayout = null;
		}
		if (_flowLayout != null)
		{
			_layout.removeLayouts(_flowLayout);
			_flowLayout = null;
		}
	}
}