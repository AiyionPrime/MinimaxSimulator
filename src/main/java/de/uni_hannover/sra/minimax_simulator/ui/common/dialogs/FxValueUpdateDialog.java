package de.uni_hannover.sra.minimax_simulator.ui.common.dialogs;

import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public abstract class FxValueUpdateDialog extends FxDialog
{
	protected enum Mode
	{
		HEX
		{
			@Override
			public String toString(FxValueUpdateDialog instance, Integer value)
			{
				return String.format(instance._hexFormat, value);
			}

			@Override
			public Integer decode(String value)
			{
				try
				{
					long lon = Long.parseLong(value, 16);
					if (lon > 0xFFFFFFFFL)
						return null;
					return (int) lon;
				}
				catch (NumberFormatException e)
				{
					return null;
				}
			}
		},
		DEC
		{
			@Override
			public String toString(FxValueUpdateDialog instance, Integer value)
			{
				return Integer.toString(value);
			}

			@Override
			public Integer decode(String value)
			{
				try
				{
					return Integer.valueOf(value, 10);
				}
				catch (NumberFormatException e)
				{
					return null;
				}
			}
		};

		public abstract String toString(FxValueUpdateDialog instance, Integer value);

		public abstract Integer decode(String value);
	}

	protected abstract void setValue(int value);

	protected final Label			_messageLabel;
	protected final Label			_modeLabel;
	protected final Button 			_swapMode;
	protected final Button			_okButton;
	protected final ButtonType		_okButtonType;
	protected final String			_hexFormat;
	protected final TextField 		_field;

	protected final TextResource	_res;

	private Mode					_mode;

	// TODO: show current mode
	public FxValueUpdateDialog(int currentValue)
	{
		super(AlertType.NONE, null, null);

		_res = Main.getTextResource("project").using("memory.update");

		_hexFormat = Util.createHexFormatString(32, false);
		_mode = Mode.DEC;

		_field = new TextField();

		_field.setText(_mode.toString(this, currentValue));

		_swapMode = new Button();
		_swapMode.setTooltip(new Tooltip(_res.get("swapmode.tooltip")));

		_messageLabel = new Label();

		_okButtonType = new ButtonType(_res.get("ok"), ButtonBar.ButtonData.OK_DONE);

		_modeLabel = new Label();
		updateLabelMode(_mode);
		//UIUtil.closeOnEscapePressed(this);

		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.add(_messageLabel, 0, 0, 2, 1);
		pane.add(_field, 0, 2);
		pane.add(_swapMode, 1, 2);
		pane.add(_modeLabel, 0, 1, 2, 1);

		this.getDialogPane().setContent(pane);
		this.getDialogPane().getButtonTypes().addAll(_okButtonType, ButtonType.CANCEL);

		_swapMode.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
					Mode newMode = _mode == Mode.DEC ? Mode.HEX : Mode.DEC;
					updateTextFieldMode(newMode);
					updateLabelMode(newMode);
				}
			}
		});

		_okButton = (Button) this.getDialogPane().lookupButton(_okButtonType);

		_field.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boolean shouldEnable = _mode.decode(_field.getText()) != null;

				if (_okButton.isDisabled() != !shouldEnable) {
					_okButton.setDisable(!shouldEnable);
				}
			}
		});

		this.setResultConverter(dialogButton -> {
			if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
				Integer value = _mode.decode(_field.getText());
				if (value != null) {
					setValue(value.intValue());
				}
			}
			return null;
		});

	}

	private void updateTextFieldMode(Mode mode)
	{
		if (_mode == mode)
			return;

		Integer value = _mode.decode(_field.getText().trim());
		if (value == null)
			value = Integer.valueOf(0);
		_mode = mode;
		_field.setText(_mode.toString(this, value));
	}

	private void updateLabelMode(Mode mode) {
		String currentMode = _res.get("mode.label") + " ";
		if (_mode == Mode.DEC) {
			currentMode += _res.get("mode.dec");
		}
		else if (_mode == Mode.HEX) {
			currentMode += _res.get("mode.hex");
		}
		_modeLabel.setText(currentMode);
	}
}