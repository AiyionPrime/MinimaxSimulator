package de.uni_hannover.sra.minimax_simulator.ui.layout.constraint;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Constraint} for arranging several things relative to each other.
 *
 * @author Martin L&uuml;ck
 */
public abstract class RelativeMultiConstraint implements Constraint {

	protected final int _offset;

	protected final Set<Attribute> _anchors;

	/**
	 * Constructs a new {@code RelativeMultiConstraint} with the specified anchors.
	 *
	 * @param anchors
	 *          the anchor {@code Attribute}s
	 */
	public RelativeMultiConstraint(Set<Attribute> anchors) {
		this(anchors, 0);
	}

	/**
	 * Constructs a new {@code RelativeMultiConstraint} with the specified names and {@link AttributeType}
	 * of the anchor {@link Attribute}s and the specified offset
	 *
	 * @param anchors
	 *          the names of the anchor {@code Attribute}s
	 * @param type
	 *          the {@link AttributeType} of the anchor {@code Attribute}s
	 * @param offset
	 *          the offset
	 */
	public RelativeMultiConstraint(Set<String> anchors, AttributeType type, int offset) {
		this(toAttributeSet(anchors, type), offset);
	}

	/**
	 * Constructs a new {@code RelativeMultiConstraint} with the specified anchors and offset.
	 *
	 * @param anchors
	 *          the anchor {@code Attribute}s
	 * @param offset
	 *          the offset
	 */
	public RelativeMultiConstraint(Set<Attribute> anchors, int offset) {
		if (anchors.isEmpty()) {
			throw new IllegalArgumentException("Empty anchor set is invalid for group");
		}

		_offset = offset;
		_anchors = new HashSet<Attribute>(anchors);
	}

	@Override
	public String toString() {
		return "rel(" + _anchors + " + " + _offset + ")";
	}

	@Override
	public Set<Attribute> getDependencies() {
		return _anchors;
	}

	/**
	 * Creates a set of {@link Attribute}s with the specified names of anchors and {@link AttributeType}.
	 *
	 * @param anchors
	 *          the names of the anchor {@code Attribute}s
	 * @param type
	 *          the {@code AttributeType} of the {@code Attribute}s
	 * @return
	 *          a set of the {@code Attribute}s with the specified names and {@code AttributeType}
	 */
	private static Set<Attribute> toAttributeSet(Set<String> anchors, AttributeType type) {
		Set<Attribute> attrs = new HashSet<Attribute>(anchors.size());
		for (String anchor : anchors) {
			attrs.add(new Attribute(anchor, type));
		}
		return attrs;
	}

}