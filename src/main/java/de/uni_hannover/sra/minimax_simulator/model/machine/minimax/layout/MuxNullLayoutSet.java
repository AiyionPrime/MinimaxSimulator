package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class MuxNullLayoutSet extends DefaultLayoutSet
{
	public MuxNullLayoutSet(String pinName)
	{
		String endName = pinName + "_END";
		String wireName = pinName + Parts._WIRE;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(endName, cb.left(pinName, 15).alignVertically(pinName));
		addLayout(wireName + ".0", cb.align(endName));
		addLayout(wireName + ".1", cb.align(pinName));
	}
}