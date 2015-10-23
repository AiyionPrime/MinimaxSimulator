package de.uni_hannover.sra.minimax_simulator.model.machine.part;

import de.uni_hannover.sra.minimax_simulator.ui.render.Sprite;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.SpriteOwner;
import de.uni_hannover.sra.minimax_simulator.ui.schematics.parts.ConstantSprite;

/**
 * Implementation of a constant as part of a machine.
 *
 * @author Martin L&uuml;ck
 */
public class Constant extends SimplePart implements SpriteOwner {

	private final int		_constant;

	private final String	_constantStr;

	/**
	 * Constructs a new {@code Constant} with the specified value.
	 *
	 * @param constantValue
	 *          the value of the constant
	 */
	public Constant(int constantValue) {
		_constant = constantValue;

		// Later: Let the user decide when he creates the constant in the GUI?
		if ((0xFFFFFFFFL & constantValue) >= 0x0000FFFFL) {
			_constantStr = String.format("0x%X", constantValue);
		}
		else {
			_constantStr = Integer.toString(constantValue);
		}
	}

	/**
	 * Gets the value of the constant.
	 *
	 * @return
	 *          the value of the constant
	 */
	public int getConstant() {
		return _constant;
	}

	/**
	 * Gets the string representation of the constant value.
	 *
	 * @return
	 *          the string representation of the value of the constant
	 */
	public String getConstantStr() {
		return _constantStr;
	}

	@Override
	public void update() {
		getDataOut().write(_constant);
	}

	@Override
	public Sprite createSprite() {
		return new ConstantSprite(this);
	}

	@Override
	public String toString() {
		return "Constant[" + _constantStr + "]";
	}
}
