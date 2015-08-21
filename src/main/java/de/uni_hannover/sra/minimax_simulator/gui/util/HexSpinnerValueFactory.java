package de.uni_hannover.sra.minimax_simulator.gui.util;

import de.uni_hannover.sra.minimax_simulator.gui.common.HexStringConverter;
import javafx.scene.control.SpinnerValueFactory;

/**
 * A {@link SpinnerValueFactory} for hexadecimal values.
 * The range is the same as of {@link Integer}.
 *
 * @author Philipp Rohde
 */
public class HexSpinnerValueFactory extends SpinnerValueFactory.IntegerSpinnerValueFactory {

    public HexSpinnerValueFactory() {
        // set range in parent class
        super(Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.setWrapAround(true);

        // render values as hexadecimal String
        this.setConverter(new HexStringConverter(Integer.MIN_VALUE, Integer.MAX_VALUE));
    }

}
