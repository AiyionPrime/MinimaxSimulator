package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;
import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;

/**
 * Container for the {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}s of
 * an extended register.
 *
 * @author Martin L&uuml;ck
 */
public class ExtendedRegisterLayoutSet extends DefaultRegisterLayoutSet {

    /**
     * Constructs a new {@code ExtendedRegisterLayoutSet} for the specified register.
     *
     * @param registerId
     *          the ID of the register
     */
    public ExtendedRegisterLayoutSet(String registerId) {
        super(registerId);

        String outJunctionId = registerId + Parts._OUT_JUNCTION;
        String anchorId = registerId + Parts._OUT_JUNCTION + Parts._ANCHOR;

        ConstraintBuilder cb = new ConstraintBuilder();

        addLayout(outJunctionId, cb.alignHorizontally(anchorId).alignVertically(registerId));

        String dataOutWire = registerId + Parts._WIRE_DATA_OUT;
        addLayout(dataOutWire + ".0", cb.left(registerId).alignVertically(registerId));
        addLayout(dataOutWire + ".1", cb.align(outJunctionId));
    }
}