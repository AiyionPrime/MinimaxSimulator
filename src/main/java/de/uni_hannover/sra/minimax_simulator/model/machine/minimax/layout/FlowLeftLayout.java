package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.*;

import java.util.EnumSet;
import java.util.Set;

/**
 * A {@link Layout} arranging components in a left flow.
 *
 * @author Martin L&uuml;ck
 */
class FlowLeftLayout implements Layout {

	private final static EnumSet<AttributeType>	attributes	= EnumSet.of(AttributeType.RIGHT, AttributeType.VERTICAL_CENTER);

	private final Constraint					_vertical;
	private final Constraint					_horizontal;

	/**
	 * Constructs a new {@code FlowLayout} with the specified anchor and spacing.
	 *
	 * @param anchor
	 *          the anchor
	 * @param spacing
	 *          spacing
	 */
	FlowLeftLayout(String anchor, int spacing) {
		_horizontal = new RelativeConstraint(new Attribute(anchor, AttributeType.LEFT), -spacing);
		_vertical = new RelativeConstraint(new Attribute(anchor, AttributeType.VERTICAL_CENTER));
	}

	@Override
	public Constraint getConstraint(AttributeType attribute) {
		switch (attribute) {
			case VERTICAL_CENTER:
				return _vertical;
			case RIGHT:
				return _horizontal;

			default:
				// never happens
				throw new AssertionError();
		}
	}

	@Override
	public Set<AttributeType> getConstrainedAttributes() {
		return attributes;
	}
}