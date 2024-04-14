package nl.nielspoldervaart.ftlav.data;

import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TableReader {

	public static final CellProcessor[] CELL_PROCESSORS = TableRow.getCellProcessors();

	public static void read(File targetFile) throws IOException {
		ArrayList<TableRow> newTableRows = new ArrayList<>();
		try (CsvBeanReader tableReader = new CsvBeanReader(
			new FileReader(targetFile),
			CsvPreference.TAB_PREFERENCE
		)) {
			final String[] header = tableReader.getHeader(true);
			TableRow row;
			while ((row = tableReader.read(TableRow.class, header, CELL_PROCESSORS)) != null) {
				newTableRows.add(row);
			}
		} finally {
			if (!newTableRows.isEmpty()) {
				FTLAdventureVisualiser.recording = newTableRows;
			}
		}
	}

}
