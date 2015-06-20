package de.uni_hannover.sra.minimax_simulator.gui.util;

import com.google.common.io.ByteStreams;
import de.uni_hannover.sra.minimax_simulator.Application;
import de.uni_hannover.sra.minimax_simulator.Main;
import de.uni_hannover.sra.minimax_simulator.io.IOUtils;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MachineMemory;
import de.uni_hannover.sra.minimax_simulator.model.machine.base.memory.MemoryState;
import de.uni_hannover.sra.minimax_simulator.resources.TextResource;
import de.uni_hannover.sra.minimax_simulator.ui.UI;
import de.uni_hannover.sra.minimax_simulator.util.Util;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

public class MemoryImportWorker implements Runnable
{
	private final static Logger	_log	= Logger.getLogger(MemoryImportWorker.class.getName());

	private final MachineMemory	_memory;
	private final int			_addressStart;

	private final int			_byteCount;
	private final int			_effectiveByteCount;

	private final File			_file;
	//private final TextResource	_res;

	public MemoryImportWorker(MachineMemory memory, int addressStart, int byteCount,
							  File file)
	{
		_memory = memory;
		_addressStart = addressStart;

		_byteCount = byteCount;

		long addressStartInBytes = ((long) addressStart) << 2;
		long endAddressInBytes = ((long) (memory.getMaxAddress() + 1)) << 2;
		_effectiveByteCount = (int) (Math.min(endAddressInBytes, addressStartInBytes + byteCount) - addressStartInBytes);

		checkArgument(_effectiveByteCount >= 0, "Negative number of bytes to import: " + _effectiveByteCount);
		checkArgument(_effectiveByteCount <= Integer.MAX_VALUE, "Too many bytes to import: " + _effectiveByteCount);

		_file = file;
		//_res = res;
	}

	@Override
	public void run()
	{
		FileInputStream fis = null;
		boolean memoryNotifiesListeners = _memory.getNotifiesListeners();

		try
		{
			fis = new FileInputStream(_file);
			_memory.setNotifiesListeners(false);
			doImport(fis);
		}
		catch (IOException ioe)
		{
			UI.invokeInFAT(new Runnable() {
				@Override
				public void run() {
					Alert fne = new Alert(Alert.AlertType.ERROR);
					fne.setTitle("Fehler");
					fne.setHeaderText(null);
					fne.setContentText("Die Datei " + _file.getPath() + " existiert nicht mehr oder kann nicht gelesen werden.");
					// for setting the icon of the application to the dialog
					fne.initOwner(Main.getPrimaryStage());
					// FIXME: delete if issue with long texts in linux is resolved
					fne.setResizable(true);

					fne.showAndWait();
				}
			});
		}
		finally
		{
			_memory.setNotifiesListeners(memoryNotifiesListeners);
			IOUtils.closeQuietly(fis);
		}
	}

	private void doImport(InputStream is) throws IOException
	{
		byte[] bytes = new byte[_effectiveByteCount];
		ByteStreams.readFully(is, bytes, 0, _effectiveByteCount);

		MemoryState state = _memory.getMemoryState();

		// Convert byte array to ints
		// divide length by 4 (rounding up)
		int intCount = ((_effectiveByteCount - 1) >> 2) + 1;

		for (int i = 0, n = intCount, a = _addressStart; i < n; i++, a++)
		{
			// multiply by 4
			int byteNum = i << 2;

//			// treat 4 bytes as big-endian integer
//			int value = bytes[byteNum] << 24;
//			if (byteNum + 1 < _effectiveByteCount)
//				value |= bytes[byteNum + 1] << 16;
//			if (byteNum + 2 < _effectiveByteCount)
//				value |= bytes[byteNum + 2] << 8;
//			if (byteNum + 3 < _effectiveByteCount)
//				value |= bytes[byteNum + 3];

			// treat 4 bytes as little-endian integer
			int value = bytes[byteNum];
			if (byteNum + 1 < _effectiveByteCount)
				value |= bytes[byteNum + 1] << 8;
			if (byteNum + 2 < _effectiveByteCount)
				value |= bytes[byteNum + 2] << 16;
			if (byteNum + 3 < _effectiveByteCount)
				value |= bytes[byteNum + 3] << 24;

			state.setInt(a, value);
		}

		int truncated = _byteCount - _effectiveByteCount;

		if (_log.isLoggable(Level.FINE))
			_log.fine(bytes.length + " bytes / " + intCount + " words imported, " + truncated + " bytes truncated from " + _file.getPath());

		if (truncated > 0)
		{
			int maxAddress = _memory.getMaxAddress();
			int width = _memory.getAddressWidth();
//			final String title = _res.get("warning");
//			final String message = _res.format("bytes-truncated", Util.toHex(maxAddress, width, true), truncated);

			UI.invokeInFAT(new Runnable() {
				@Override
				public void run() {
					Alert trunc = new Alert(Alert.AlertType.WARNING);
					trunc.setTitle("Warnung");
					trunc.setHeaderText(null);
					trunc.setContentText("Die maximale Adresse ist " + Util.toHex(maxAddress, width, true) + ". Es wurde(n) " + truncated + " Byte(s) am Ende der Datei abgeschnitten.");
					// for setting the icon of the application to the dialog
					trunc.initOwner(Main.getPrimaryStage());
					// FIXME: delete if issue with long texts in linux is resolved
					trunc.setResizable(true);

					trunc.showAndWait();
				}
			});
		}
	}
}
