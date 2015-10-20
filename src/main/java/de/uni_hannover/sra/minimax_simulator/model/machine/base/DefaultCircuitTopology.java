package de.uni_hannover.sra.minimax_simulator.model.machine.base;

import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.Circuit;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.topology.MachineTopology;

import java.util.*;

/**
 * Default implementation of the {@link MachineTopology}.
 *
 * @author Martin L&uuml;ck
 */
public class DefaultCircuitTopology implements MachineTopology {

	/**
	 * Data structure for holding a {@link Circuit} in a {@code HashMap}.
	 */
	private static class CircuitEntry {

		Class<? extends Circuit>	clazz;
		Circuit						circuit;

		/**
		 * Constructs a new {@code CircuitEntry} with the specified class and {@link Circuit}.
		 *
		 * @param clazz
		 *          the class of the {@code Circuit} the entry represents
		 * @param circuit
		 *          the {@code Circuit} the entry represents
		 */
		CircuitEntry(Class<? extends Circuit> clazz, Circuit circuit) {
			this.clazz = clazz;
			this.circuit = circuit;
		}
	}

	private final Set<Circuit>									_allCircuits;
	private final Map<String, CircuitEntry>						_circuits;

	/**
	 * Initializes an empty {@code DefaultCircuitTopology} instance.
	 */
	public DefaultCircuitTopology() {
		_allCircuits = new HashSet<Circuit>();
		_circuits = new HashMap<String, CircuitEntry>();
	}

	@Override
	public Set<Circuit> getAllCircuits() {
		return Collections.unmodifiableSet(_allCircuits);
	}

	@Override
	public <T extends Circuit> T getCircuit(Class<T> clazz, String id) {
		CircuitEntry entry = _circuits.get(id);
		if (entry == null) {
			throw new NullPointerException("No circuit for the id " + id);
		}

		if (clazz.isAssignableFrom(entry.clazz)) {
			return clazz.cast(entry.circuit);
		}
		else {
			throw new ClassCastException(id + " is a " + entry.clazz.getName() + ", not a " + clazz.getName());
		}
	}

	@Override
	public <T extends Circuit> void addCircuit(Class<T> clazz, String id, T circuit) {
		CircuitEntry entry = _circuits.get(id);
		if (entry != null) {
			throw new IllegalStateException("Circuit already defined as: " + entry.circuit);
		}

		_circuits.put(id, new CircuitEntry(clazz, circuit));
		_allCircuits.add(circuit);
		//fireCircuitAdded(circuit);
	}

	@Override
	public void addCircuit(String id, Circuit circuit) {
		CircuitEntry entry = _circuits.get(id);
		if (entry != null) {
			throw new IllegalStateException("Circuit " + id + " already defined as: " + entry.circuit);
		}

		_circuits.put(id, new CircuitEntry(circuit.getClass(), circuit));
		_allCircuits.add(circuit);
		//fireCircuitAdded(circuit);
	}

	@Override
	public void removeCircuit(String id) {
		CircuitEntry entry = _circuits.remove(id);
		if (entry != null) {
			_allCircuits.remove(entry.circuit);
			//fireCircuitRemoved(entry.circuit);
		}
	}
}