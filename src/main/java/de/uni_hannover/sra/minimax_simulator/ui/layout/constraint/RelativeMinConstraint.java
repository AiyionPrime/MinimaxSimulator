package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.Set;

/**
 * A {@link RelativeMultiConstraint} that minimizes the value returned by {@link #getValue(AttributeSource)}.
 *
 * @author Martin L&uuml;ck
 */
public class RelativeMinConstraint extends RelativeMultiConstraint {

    /**
     * Constructs a new {@code RelativeMinConstraint} with the specified anchors.
     *
     * @param anchors
     *          the anchor {@code Attribute}s
     */
    public RelativeMinConstraint(Set<Attribute> anchors) {
        super(anchors);
    }

    /**
     * Constructs a new {@code RelativeMinConstraint} with the specified names and {@link AttributeType}
     * of the anchor {@link Attribute}s and the specified offset.
     *
     * @param anchors
     *          the names of the anchor {@code Attribute}s
     * @param type
     *          the {@code AttributeType} of the anchor {@code Attribute}s
     * @param offset
     *          the offset
     */
    public RelativeMinConstraint(Set<String> anchors, AttributeType type, int offset) {
        super(anchors, type, offset);
    }

    /**
     * Constructs a new {@code RelativeMinConstraint} with the specified anchors and offset.
     *
     * @param anchors
     *          the anchor {@code Attribute}s
     * @param offset
     *          the offset
     */
    public RelativeMinConstraint(Set<Attribute> anchors, int offset) {
        super(anchors, offset);
    }

    @Override
    public int getValue(AttributeSource attributes) {
        int minimum = Integer.MAX_VALUE;
        for (Attribute attr : anchors) {
            int val = attributes.getValue(attr);
            if (val < minimum) {
                minimum = val;
            }
        }
        return minimum + offset;
    }
}