package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.SignExtension;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import javafx.scene.canvas.GraphicsContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The sprite for a {@link SignExtension}.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class SignExtSprite extends CircuitSprite {

    private final SignExtension signExt;

    /**
     * Initializes the {@code SignExtSprite}.
     *
     * @param signExt
     *          the {@code SignExtension} this sprite will represent
     */
    public SignExtSprite(SignExtension signExt) {
        this.signExt = checkNotNull(signExt);
    }

    @Override
    public void paint(GraphicsContext gc) {
        Bounds b = signExt.getBounds();
        debugBounds(gc, b);

        gc.strokeOval(b.x + 0.5, b.y + 0.5, b.w, b.h);

        String name = signExt.getLabel();
        FontMetrics fm = Toolkit.getToolkit().getFontLoader().getFontMetrics(gc.getFont());
        double textWidth = fm.computeStringWidth(name);
        double textHeight = fm.getLineHeight();

        double textX = b.x + b.w / 2 - textWidth / 2;
        double textY = b.y + b.h / 2 + textHeight / 4 + 1;

        gc.fillText(name, textX, textY);
    }
}
