package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import static com.google.common.base.Preconditions.*;

import java.awt.Graphics2D;

import de.uni_hannover.sra.minimax_simulator.layout.Point;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Junction;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Wire;
import de.uni_hannover.sra.minimax_simulator.ui.render.RenderEnvironment;
import javafx.scene.canvas.GraphicsContext;

public class WireSprite extends CircuitSprite
{
	private final Wire				_wire;

	public WireSprite(Wire wire)
	{
		_wire = checkNotNull(wire);
	}

	@Override
	public void paint(Graphics2D g, RenderEnvironment env)
	{
		Point[] points = _wire.getPoints();

		int i = 1;
		for (; i < points.length; i++)
		{
			g.drawLine(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y);
		}

		if (!(_wire.getDrain() instanceof Junction))
		{
			drawArrow(g, points[i - 2], points[i - 1]);
		}
	}

	@Override
	public void paint(GraphicsContext gc, RenderEnvironment env) {
		Point[] points = _wire.getPoints();

		double lineWidth = gc.getLineWidth();
		gc.setLineWidth(1.0);
		int i = 1;
		for (; i < points.length; i++) {
			gc.strokeLine(points[i-1].x, points[i-1].y, points[i].x, points[i].y);
		}
		gc.setLineWidth(lineWidth);

		if (!(_wire.getDrain() instanceof  Junction)) {
			drawArrow(gc, points[i-2], points[i-1]);
		}
	}
}