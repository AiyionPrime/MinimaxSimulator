package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

public class ProjectClose extends ProjectAction
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (!UIUtil.confirmCloseProject())
			return;

		Application.getWorkspace().closeProject();
	}
}