package de.uni_hannover.sra.minimax_simulator.model.machine.shape;

/**
 * The shape of the ALU.
 *
 * @author Martin L&uuml;ck
 */
public class AluShape extends FixedShape {

	private final static int	ALU_WIDTH	= 69;
	private final static int	ALU_HEIGHT	= 88;

	// private final static int ALU_UPPER_SPACING = 30;

	/**
	 * Initializes the {@code AluShape}.
	 */
	public AluShape() {
		super(ALU_WIDTH, ALU_HEIGHT);
	}
}