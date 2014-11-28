package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.group;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Alu;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Memory;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.model.machine.shape.RegisterShape;

public class BasePartGroup extends AbstractGroup
{
	private final MachineMemory _memory;

	public BasePartGroup(MachineMemory memory)
	{
		_memory = memory;
	}

	@Override
	public void initialize(MachineTopology cr, FontMetricsProvider fontProvider)
	{
		Memory memory = new Memory(_memory);
		memory.setName(Parts.MEMORY);

		Alu alu = new Alu();
		alu.setName(Parts.ALU);

		Multiplexer muxA = new Multiplexer();
		muxA.setName(Parts.MUX_A);

		Multiplexer muxB = new Multiplexer();
		muxB.setName(Parts.MUX_B);

		Multiplexer mdrSelect = new Multiplexer(2);
		mdrSelect.setName(Parts.MDR_SELECT);

		Register mar = new Register(Parts.MAR);
		mar.setName(Parts.MAR);
		Register mdr = new Register(Parts.MDR);
		mdr.setName(Parts.MDR);
		Register ir = new Register(Parts.IR);
		ir.setName(Parts.IR);
		Register pc = new Register(Parts.PC);
		pc.setName(Parts.PC);

		mar.setShape(new RegisterShape(fontProvider));
		mdr.setShape(new RegisterShape(fontProvider));
		ir.setShape(new RegisterShape(fontProvider));
		pc.setShape(new RegisterShape(fontProvider));

		Wire muxOutA = new Wire(2, muxA.getDataOut(), alu.getInA());
		Wire muxOutB = new Wire(2, muxB.getDataOut(), alu.getInB());

		add(memory, Parts.MEMORY);
		add(alu, Parts.ALU);
		add(muxA, Parts.MUX_A);
		add(muxB, Parts.MUX_B);
		add(mar, Parts.MAR);
		add(mdr, Parts.MDR);
		add(ir, Parts.IR);
		add(pc, Parts.PC);
		add(mdrSelect, Parts.MDR_SELECT);

		add(muxOutA, Parts.MUX_A + "_WIRE_OUT");
		add(muxOutB, Parts.MUX_B + "_WIRE_OUT");

		addVirtual(Parts.GROUP_BASE_REGISTERS);
		addVirtual(Parts.GROUP_EXTENDED_REGISTERS);
		addVirtual(Parts.GROUP_ALL_REGISTERS);
		addVirtual(Parts.GROUP_MUX_LABEL);
		addVirtual(Parts.GROUP_MUX_CONSTANTS);
		addVirtual(Parts.GROUP_MUX_LINE);
		addVirtual(Parts.GROUP_MUX_BASE_REGISTERS);
		addVirtual(Parts.GROUP_MUX_EXT_REGISTERS);
	}
}