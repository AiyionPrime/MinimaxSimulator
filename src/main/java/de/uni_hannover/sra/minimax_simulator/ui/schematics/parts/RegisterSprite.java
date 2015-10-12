package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.layout.Bounds;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Register;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite for a {@link Register}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class RegisterSprite extends CircuitSprite {

	public final static Color EXTENDED_REGISTER_FX = new javafx.scene.paint.Color(0.95f, 0.95f, 0.95f, 1f);

	private final Register _register;

	/**
	 * Initializes the {@code RegisterSprite}.
	 *
	 * @param reg
	 *          the {@code Register} this sprite will represent
	 */
	public RegisterSprite(Register reg) {
		_register = checkNotNull(reg);
	}

	@Override
	public void paint(GraphicsContext gc) {
		final Bounds b = _register.getBounds();
		debugBounds(gc, b);

		String name = _register.getLabel();
		com.sun.javafx.tk.FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());
		double textWidth = fm.computeStringWidth(name);
		double textHeight = fm.getLineHeight();

		if (_register.isExtended()) {
			gc.setFill(EXTENDED_REGISTER_FX);
			gc.fillRect(b.x + 0.5, b.y + 0.5, b.w, b.h);
			gc.setFill(javafx.scene.paint.Color.BLACK);
		}

		double xCenter = b.x + b.w / 2;
		double yCenter = b.y + b.h / 2;

		gc.strokeRect(b.x + 0.5, b.y + 0.5, b.w, b.h);
		gc.fillText(name, xCenter - textWidth / 2, yCenter + textHeight / 4 + 1);

		debugPin(gc, _register.getDataIn());
		debugPin(gc, _register.getDataOut());
		debugPin(gc, _register.getWriteEnabled());
	}
}