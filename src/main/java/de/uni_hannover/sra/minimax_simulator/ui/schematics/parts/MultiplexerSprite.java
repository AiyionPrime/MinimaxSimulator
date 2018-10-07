package de.uni_hannover.sra.minimax_simulator.ui.schematics.parts;

import de.uni_hannover.sra.minimax_simulator.model.machine.part.IngoingPin;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Multiplexer;
import de.uni_hannover.sra.minimax_simulator.model.machine.part.Pin;
import de.uni_hannover.sra.minimax_simulator.ui.gui.util.FontMetrics;
import de.uni_hannover.sra.minimax_simulator.ui.layout.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The multiplexer sprite.
 *
 * @author Martin L&uuml;ck
 * @author Philipp Rohde
 */
public class MultiplexerSprite extends CircuitSprite {

    private final Multiplexer mux;

    private static final ArrayList<String> ADDRESS_NUMBER_CACHE = new ArrayList<>();

    /**
     * Initializes the {@code MultiplexerSprite}.
     *
     * @param mux
     *          the {@code Multiplexer} this sprite will represent
     */
    public MultiplexerSprite(Multiplexer mux) {
        this.mux = checkNotNull(mux);
    }

    /**
     * Converts the specified integer to string using a list for caching already done conversions.
     *
     * @param i
     *          the integer to convert
     * @return
     *          the integer as string
     */
    private static String intToStr(int i) {
        if (ADDRESS_NUMBER_CACHE.size() <= i) {
            for (int c = ADDRESS_NUMBER_CACHE.size(); c <= i; c++) {
                ADDRESS_NUMBER_CACHE.add(Integer.toString(c));
            }
        }
        return ADDRESS_NUMBER_CACHE.get(i);
    }

    @Override
    public void paint(GraphicsContext gc) {
        Bounds b = mux.getBounds();
        debugBounds(gc, b);

        FontMetrics fm = new FontMetrics(gc.getFont());
        double textHeight = fm.getAscent() - 3;

        // upper / lower arc
        gc.strokeArc(b.x + 0.5, b.y + 0.5, b.w, b.w, 0, 180, ArcType.OPEN);
        gc.strokeArc(b.x + 0.5, b.y + b.h - b.w + 0.5, b.w, b.w, 180, 180, ArcType.OPEN);

        // left / right line
        gc.strokeLine(b.x + 0.5, b.y + b.w / 2 + 0.5, b.x + 0.5, b.y + b.h - b.w / 2 + 0.5);
        gc.strokeLine(b.x + b.w + 0.5, b.y + b.w / 2 + 0.5, b.x + b.w + 0.5, b.y + b.h - b.w / 2 + 0.5);

        // pin addresses
        int labelX = b.x + b.w / 2;
        int labelY = 0;

        List<IngoingPin> pins = mux.getDataInputs();

        if (!pins.isEmpty()) {
            int addr = 0;
            for (Pin pin : pins) {
                String nrStr = intToStr(addr);
                double textWidth = fm.computeStringWidth(nrStr);

                labelY = pin.getY();

                gc.fillText(nrStr, labelX - textWidth / 2, labelY + textHeight / 2);
                addr++;
            }
        }

        for (Pin pin : pins) {
            debugPin(gc, pin);
        }
        debugPin(gc, mux.getDataOut());
        debugPin(gc, mux.getSelectPin());
    }
}