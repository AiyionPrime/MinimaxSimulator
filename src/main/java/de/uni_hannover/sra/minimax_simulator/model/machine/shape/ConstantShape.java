package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

import de.uni_hannover.sra.minimax_simulator.layout.Component;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.display.FontMetricsProvider;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Constant;

public class ConstantShape extends TextRenderShape
{
	public ConstantShape(FontMetricsProvider fontProvider)
	{
		super(fontProvider);
	}

	@Override
	public void updateShape(Component component)
	{
		Constant constant = (Constant) component;

		// Likely to be cached
		component.setDimension(getStringDimension(constant.getConstantStr()));
	}

	@Override
	public void layout(Component component)
	{
	}
}