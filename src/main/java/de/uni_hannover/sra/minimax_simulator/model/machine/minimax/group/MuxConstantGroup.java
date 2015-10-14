package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.LayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout.MuxConstantLayoutSet;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Constant;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.ConstantShape;

public class MuxConstantGroup extends AbstractGroup
{
	private final String _pinName;
	private final IngoingPin _muxPin;

	private final int _value;

	public MuxConstantGroup(String pinName, IngoingPin muxPin, int value)
	{
		_pinName = pinName;
		_muxPin = muxPin;

		_value = value;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Constant constant = new Constant(_value);
		constant.setShape(new ConstantShape(fontProvider));

		Wire wire = new Wire(2, constant.getDataOut(), _muxPin);

		add(constant, _pinName + Parts._CONSTANT);
		addWire(wire, _pinName + Parts._WIRE);
	}

	@Override
	public boolean hasLayouts()
	{
		return true;
	}

	@Override
	public LayoutSet createLayouts()
	{
		return new MuxConstantLayoutSet(_pinName);
	}
}