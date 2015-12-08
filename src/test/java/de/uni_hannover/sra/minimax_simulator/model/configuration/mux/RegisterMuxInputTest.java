package de.uni_hannover.sra.minimax_simulator.model.configuration.mux;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests the implementation of a register multiplexer input.
 *
 * @see RegisterMuxInput
 *
 * @author Philipp Rohde
 */
public class RegisterMuxInputTest {

    private final static String PC = "PC";
    private final static String IR = "IR";
    private final static String AT = "AT";

    /**
     * Tests the register multiplexer input.
     */
    @Test
    public void testRegisterMuxInput() {
        RegisterMuxInput rmi = new RegisterMuxInput(PC);
        assertEquals("register name", PC, rmi.getRegisterName());
        assertEquals("display name", PC, rmi.getName());

        RegisterMuxInput ir = new RegisterMuxInput(IR, AT);
        assertEquals("register name", IR, ir.getRegisterName());
        assertEquals("display name", AT, ir.getName());

        // test equals
        assertEquals("equals: self comparison", true, rmi.equals(rmi));
        RegisterMuxInput equal = new RegisterMuxInput(PC);
        assertEquals("equals: same register", true, rmi.equals(equal));
        assertEquals("equals: different registers", false, rmi.equals(ir));
        assertEquals("equals: null comparison", false, rmi.equals(null));
        assertEquals("equals: different classes", false, rmi.equals(NullMuxInput.INSTANCE));
    }
}
