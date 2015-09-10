package de.uni_hannover.sra.minimax_simulator.gui.common;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;

import java.util.function.UnaryOperator;

/**
 * The NullAwareIntFormatter is a {@link TextFormatter} using the {@link NullAwareIntStringConverter}.<br>
 * This class is used for the multiplexer constant input {@link javafx.scene.control.Spinner}s of the {@link de.uni_hannover.sra.minimax_simulator.gui.MuxView}.
 *
 * @author Philipp Rohde
 */
public class NullAwareIntFormatter extends TextFormatter {

	public enum Mode {
		DEC {
			@Override
			public StringConverter getConverter() {
				// signed decimal
				return new NullAwareIntStringConverter(10, true);
			}

			@Override
			public UnaryOperator<TextFormatter.Change> getFilter() {
				return new UnaryOperator<Change>() {
					@Override
					public Change apply(Change change) {
						if (change.isContentChange()) {
							//TODO: improve
							try {
								Integer.parseInt(change.getControlNewText());
							} catch (NumberFormatException e) {
								return null;
							}
						}
						return change;
					}
				};
			}
		},
		HEX {
			@Override
			public StringConverter getConverter() {
				// unsigned hexadecimal
				return new NullAwareIntStringConverter(16, false);
			}

			@Override
			public UnaryOperator<TextFormatter.Change> getFilter() {
				return new UnaryOperator<Change>() {
					@Override
					public Change apply(Change change) {
						if (change.isContentChange()) {
							String newValue = change.getControlNewText();
							if (!newValue.matches("-?[0-9a-fA-F]+") || newValue.length() > 8) {
								return null;
							}
						}
						return change;
					}
				};
			}
		};

		public StringConverter getConverter() {
			return null;
		}
		public UnaryOperator<TextFormatter.Change> getFilter() {
			return null;
		}
	}

	public NullAwareIntFormatter(Mode mode) {
		super(mode.getConverter(), 0, mode.getFilter());
	}


}