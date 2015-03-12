package de.uni_hannover.sra.minimax_simulator.model.machine.minimax;

import java.util.ArrayList;
import java.util.List;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.model.configuration.MachineConfiguration;
import de.uni_hannover.sra.minimax_simulator.model.configuration.alu.AluOperation;
import de.uni_hannover.sra.minimax_simulator.model.configuration.mux.MuxType;
import de.uni_hannover.sra.minimax_simulator.model.configuration.register.RegisterExtension;
import de.uni_hannover.sra.minimax_simulator.model.signal.DescriptionFactory;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalRow;
import de.uni_hannover.sra.minimax_simulator.model.signal.SignalValue;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;

public class MinimaxSignalDescription implements DescriptionFactory
{
	private final MachineConfiguration	_configuration;
	private final TextResource			_aluRes;

	public MinimaxSignalDescription(MachineConfiguration machineConfiguration)
	{
		_configuration = machineConfiguration;
		_aluRes = Application.getTextResource("alu");
	}

	private List<String> getWritingRegisterNames(SignalRow row)
	{
		List<String> registersWrittenByAlu = new ArrayList<String>();
		int mdrSelect = row.getSignalValue(BaseControlPort.MDR_SEL.name());
		for (RegisterExtension register : _configuration.getBaseRegisters())
		{
			int value = row.getSignalValue(register.getName() + ".W");
			if (value == 0)
				continue;

			if (register.getName().equals("MDR") && mdrSelect != 0)
			{
				// MDR is only written if MDR select is 0
				continue;
			}

			registersWrittenByAlu.add(register.getName());
		}
		for (RegisterExtension register : _configuration.getRegisterExtensions())
		{
			Integer value = row.getSignalValue(register.getName() + ".W");
			if (value == 1)
				registersWrittenByAlu.add(register.getName());
		}
		return registersWrittenByAlu;
	}

	private String getAluOperation(SignalRow row)
	{
		SignalValue paramA = row.getSignal(BaseControlPort.ALU_SELECT_A.name(),
			SignalValue.DONT_CARE);
		SignalValue paramB = row.getSignal(BaseControlPort.ALU_SELECT_B.name(),
			SignalValue.DONT_CARE);
		SignalValue aluCtrl = row.getSignal(BaseControlPort.ALU_CTRL.name(),
			SignalValue.DONT_CARE);

		String muxNameA;
		if (!paramA.isDontCare()
			&& paramA.intValue() < _configuration.getMuxSources(MuxType.A).size())
		{
			muxNameA = _configuration.getMuxSources(MuxType.A).get(paramA.intValue()).getName();
		}
		else
		{
			muxNameA = "?";
		}

		String muxNameB;
		if (!paramB.isDontCare()
			&& paramB.intValue() < _configuration.getMuxSources(MuxType.B).size())
		{
			muxNameB = _configuration.getMuxSources(MuxType.B).get(paramB.intValue()).getName();
		}
		else
		{
			muxNameB = "?";
		}

		if (!aluCtrl.isDontCare()
			&& aluCtrl.intValue() < _configuration.getAluOperations().size())
		{
			AluOperation op = _configuration.getAluOperation(aluCtrl.intValue());
			return op.getRtOperation(_aluRes, muxNameA, muxNameB);
		}
		else
		{
			return muxNameA + " ? " + muxNameB;
		}
	}

	@Override
	public String createDescription(int rowIndex, SignalRow row)
	{
		StringBuilder sb = new StringBuilder(/* "<html>" */);

		List<String> registersWrittenByAlu = getWritingRegisterNames(row);

		// Check if the ALU result is written into registers.
		if (registersWrittenByAlu.size() > 0)
		{
			// There is an ALU operation, fetch the parameters
			String aluOp = " \u2190 " + getAluOperation(row);

			for (String registerName : registersWrittenByAlu)
			{
				if (sb.length() > 0)
					sb.append('\n');
				sb.append(registerName).append(aluOp);
			}
		}
		else
		{
			// result is not used for registers. do we have a conditional jump?
			if (row.getJump().getTargetRow(rowIndex, 0) != row.getJump().getTargetRow(
				rowIndex, 1))
			{
				sb.append(getAluOperation(row) + " == 0?");
			}
		}

		// Check for memory access
		if (row.getSignalValue(BaseControlPort.MEM_CS.name()) == 1)
		{
			if (row.getSignalValue(BaseControlPort.MEM_RW.name()) == 0)
			{
				// write
				if (sb.length() > 0)
					sb.append('\n');

				sb.append("M[MAR] \u2190 MDR");
			}
			else if (row.getSignalValue(BaseControlPort.MDR_SEL.name()) == 1
				&& row.getSignalValue("MDR.W") == 1)
			{
				// read
				if (sb.length() > 0)
					sb.append('\n');

				sb.append("MDR \u2190 M[MAR]");
			}
			else
			{
				// CS is 1 and RW is 1, but MDR is not write enabled
				// no appended line since no write is executed
			}
		}

		// sb.append("</html>");

		return sb.toString();
	}
}