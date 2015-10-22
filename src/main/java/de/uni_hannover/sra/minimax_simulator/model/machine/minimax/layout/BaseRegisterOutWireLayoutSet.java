package de.uni_hannover.sra.minimax_simulator.model.machine.minimax.layout;

import de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.ConstraintBuilder;
import de.uni_hannover.sra.minimax_simulator.model.machine.minimax.Parts;

/**
 * The container for the {@link de.uni_hannover.sra.minimax_simulator.ui.layout.constraint.Layout}s
 * of the base register's output wires.
 *
 * @author Martin L&uuml;ck
 */
public class BaseRegisterOutWireLayoutSet extends DefaultLayoutSet {

	/**
	 * Initializes the {@code BaseRegisterOutWireLayoutSet}.
	 */
	public BaseRegisterOutWireLayoutSet() {
		layoutMdr();
		layoutIr();
		layoutPc();
		layoutAccu();
	}

	/**
	 * Initializes the {@code Layout}s of the MDR's output wires.
	 */
	private void layoutMdr() {
		ConstraintBuilder cb = new ConstraintBuilder();

		String mdrMemJunction = Parts.MEMORY + Parts._JUNCTION;
		String mdrOutJunction = Parts.MDR + Parts._OUT_JUNCTION;

		String mdrToMemJunctionWire = Parts.MDR + Parts._WIRE_DATA_OUT;
		String j2jWire = Parts.MDR + Parts._OUT_JUNCTION + Parts._WIRE_DATA_IN;

		addLayout(mdrMemJunction, cb.alignVertically(Parts.MDR).left(Parts.MDR, 80));
		addLayout(mdrOutJunction, cb.alignVertically(Parts.MDR).left(Parts.IR + Parts._OUT_JUNCTION, 18));

		addLayout(mdrToMemJunctionWire + ".0", cb.left(Parts.MDR).alignVertically(Parts.MDR));
		addLayout(mdrToMemJunctionWire + ".1", cb.align(mdrMemJunction));

		addLayout(j2jWire + ".0", cb.align(mdrMemJunction));
		addLayout(j2jWire + ".1", cb.align(mdrOutJunction));

		String memDiWire = Parts.MEMORY + Parts._WIRE_DATA_IN;

		addLayout(memDiWire + ".0", cb.align(mdrMemJunction));
		addLayout(memDiWire + ".1", cb.alignHorizontally(mdrMemJunction).alignVertically(Parts.MEMORY_DI));
		addLayout(memDiWire + ".2", cb.align(Parts.MEMORY_DI));
	}

	/**
	 * Initializes the {@code Layout}s of the IR's output wires.
	 */
	private void layoutIr() {
		ConstraintBuilder cb = new ConstraintBuilder();

		String irOutJunction = Parts.IR + Parts._OUT_JUNCTION;
		String irOutWire = Parts.IR + Parts._WIRE_DATA_OUT;

		addLayout(irOutJunction, cb.above(Parts.SIGN_EXTENSION, 15).left(Parts.PC + Parts._OUT_JUNCTION, 10));
		addLayout(irOutWire + ".0", cb.above(Parts.SIGN_EXTENSION).alignHorizontally(Parts.SIGN_EXTENSION));
		addLayout(irOutWire + ".1", cb.alignVertically(irOutJunction).alignHorizontally(Parts.SIGN_EXTENSION));
		addLayout(irOutWire + ".2", cb.align(irOutJunction));
	}

	/**
	 * Initializes the {@code Layout}s of the PC's output wires.
	 */
	private void layoutPc() {
		ConstraintBuilder cb = new ConstraintBuilder();

		String pcOutJunction = Parts.PC + Parts._OUT_JUNCTION;
		String pcOutWire = Parts.PC + Parts._WIRE_DATA_OUT;

		addLayout(pcOutJunction, cb.alignVertically(Parts.PC).left(Parts.GROUP_MUX_EXT_REGISTERS, 10));
		addLayout(pcOutWire + ".0", cb.left(Parts.PC).alignVertically(Parts.PC));
		addLayout(pcOutWire + ".1", cb.align(pcOutJunction));
	}

	/**
	 * Initializes the {@code Layout}s of the ACCU's output wires.
	 */
	private void layoutAccu() {
		ConstraintBuilder cb = new ConstraintBuilder();

		String accuOutJunction = Parts.ACCU + Parts._OUT_JUNCTION;
		String accuOutWire = Parts.ACCU + Parts._WIRE_DATA_OUT;

		addLayout(accuOutJunction, cb.alignVertically(Parts.ACCU).left(Parts.GROUP_MUX_EXT_REGISTERS, 10));
		addLayout(accuOutWire + ".0", cb.left(Parts.ACCU).alignVertically(Parts.ACCU));
		addLayout(accuOutWire + ".1", cb.align(accuOutJunction));
	}
}