package nl.nielspoldervaart.ftlav.data;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import nl.nielspoldervaart.ftlav.ui.FTLFrame;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
public class TableReader {

	public static void read(File targetFile, FTLFrame parentFrame) {
		try {
			log.info("Reading TSV file at {}", targetFile.getAbsolutePath());
			List<TableRow> newTableRows = new CsvToBeanBuilder<TableRow>(new FileReader(targetFile)).withSeparator('\t').withType(TableRow.class).build().parse();
			if (newTableRows.isEmpty()) {
				log.warn("Input TSVG file has no valid data rows!");
				return;
			}

			if (isChosenFileOfSameShip(newTableRows)) {
				log.info("Input TSV file appears to be from the same save game");
				FTLAdventureVisualiser.recording.addAll(newTableRows);
				RowOrderComparator comparator = new RowOrderComparator();
				FTLAdventureVisualiser.recording.sort(comparator);
				// TODO remove rows with duplicate dates

			} else {
				int overwriteExistingRowsDialogChoice = JOptionPane.showConfirmDialog(
					parentFrame,
					String.format("The selected file %s appears to be for a different ship than the one in the current save game.\nAre you sure you want to overide the existing recording?", targetFile.getName()),
					"Before you open this file",
					JOptionPane.YES_NO_OPTION
				);
				if (overwriteExistingRowsDialogChoice == JOptionPane.YES_OPTION) {
					FTLAdventureVisualiser.recording.clear();
					FTLAdventureVisualiser.recording.addAll(newTableRows);
				}
			}
			log.info("Imported recording with {} rows", newTableRows.size());
			log.info("Total recording size is {} rows", FTLAdventureVisualiser.recording.size());
			parentFrame.updateStatusBar(String.format("Imported recording with %s rows", newTableRows.size()));
			FTLAdventureVisualiser.recordsExportFile = targetFile;
			FTLAdventureVisualiser.makeGameStateTable();
			parentFrame.onGameStateUpdate();
			parentFrame.toggleGraphButton.setSelected(true);
		} catch (IOException e) {
			log.error("Unable to read TSV file at {}", targetFile.getAbsolutePath(), e);
			parentFrame.showErrorDialog(String.format("Unable to read TSV file: %s", e.getMessage()));
		}
	}

	private static boolean isChosenFileOfSameShip(List<TableRow> importedRecording) {
		if (!FTLAdventureVisualiser.hasRecords() || importedRecording.isEmpty()) {
			return true;
		}
		TableRow lastImportedRecordingRow = importedRecording.get(importedRecording.size() - 1);
		TableRow lastExistingRow = DataUtil.getLastRecord();

		@SuppressWarnings("DataFlowIssue")
		boolean isSameShipNameAndType = lastExistingRow.getShipName().equals(lastImportedRecordingRow.getShipName()) &&
			lastExistingRow.getShipType().equals(lastImportedRecordingRow.getShipType());
		if (!isSameShipNameAndType) {
			return false;
		}
		return lastImportedRecordingRow.getTime().compareTo(lastExistingRow.getTime()) < 0;
	}

}
