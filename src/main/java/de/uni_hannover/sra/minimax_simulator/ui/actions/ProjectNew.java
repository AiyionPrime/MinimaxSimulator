package de.uni_hannover.sra.minimax_simulator.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.ui.UIUtil;

public class ProjectNew extends AbstractAction
{
	private final static TextResource	res	= Application.getTextResource("application");

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (!UIUtil.confirmCloseProject())
			return;

		UIUtil.executeWorker(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Application.getWorkspace().newProject();

					UI.invokeNow(new Runnable()
					{
						@Override
						public void run()
						{
							Application.getMainWindow().getWorkspacePanel().openDefaultTabs();
						}
					});
				}
				catch (RuntimeException e)
				{
					Application.getWorkspace().closeProject();
					throw e;
				}
			}
		}, res.get("wait.title"), res.get("wait.project.new"));
	}
}