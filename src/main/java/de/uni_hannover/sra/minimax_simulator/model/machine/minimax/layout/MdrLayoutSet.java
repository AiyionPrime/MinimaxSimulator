package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.AttributeType;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

public class MdrLayoutSet extends DefaultLayoutSet
{
	public MdrLayoutSet(String registerName)
	{
		String junctionName = registerName + Parts._JUNCTION;
		String portName = registerName + Parts._PORT;
		String labelName = registerName + Parts._LABEL;

		ConstraintBuilder cb = new ConstraintBuilder();

		addLayout(junctionName, cb.above(Parts.MDR_SELECT, -10).alignHorizontally(Parts.ALU_LINE));
		addLayout(portName, cb.relative(AttributeType.HORIZONTAL_CENTER, registerName, 20).above(registerName, 17));
		addLayout(labelName, cb.alignHorizontally(portName).above(portName, 3));

		String dataInWire = registerName + Parts._WIRE_DATA_IN;
		addLayout(dataInWire + ".0", cb.align(Parts.MDR_SELECT_OUT));
		addLayout(dataInWire + ".1", cb.alignVertically(registerName).right(registerName));

		String enabledWire = registerName + Parts._WIRE_ENABLED;
		addLayout(enabledWire + ".0", cb.align(portName));
		addLayout(enabledWire + ".1", cb.alignHorizontally(portName).above(registerName));

		String aluWire = registerName + Parts._JUNCTION + Parts._WIRE_DATA_IN;
		addLayout(aluWire + ".0", cb.right(Parts.ALU).relative(AttributeType.VERTICAL_CENTER, Parts.ALU, 4));
		addLayout(aluWire + ".1", cb.alignHorizontally(Parts.ALU_LINE).alignVertically(aluWire + ".0"));
		addLayout(aluWire + ".2", cb.align(junctionName));

		String mdrSelectInput0Wire = registerName + "_WIRE_MDR_SELECT_INPUT0";
		addLayout(mdrSelectInput0Wire + ".0", cb.align(junctionName));
		addLayout(mdrSelectInput0Wire + ".1", cb.alignVertically(mdrSelectInput0Wire + ".0").right(Parts.MDR_SELECT));
//
//		String mdrOutJunction = registerName + _WIRE_DATA_OUT + _JUNCTION;
//		String mdrOutWire = registerName + _WIRE_DATA_OUT;
//		addLayout(mdrOutJunction, cb.alignVertically(MDR).left(MDR, 90));
//		addLayout(mdrOutWire + ".0", cb.alignVertically(MDR).left(MDR));
//		addLayout(mdrOutWire + ".1", cb.align(mdrOutJunction));
//
//		String memDiWire = MEMORY + _WIRE_DATA_IN;
//		addLayout(memDiWire + ".0", cb.align(mdrOutJunction));
//		addLayout(memDiWire + ".1", cb.alignHorizontally(mdrOutJunction).alignVertically(MEMORY_DI));
//		addLayout(memDiWire + ".2", cb.align(MEMORY_DI));
	}
}