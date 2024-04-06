package net.ntg.ftl.parser;

import lombok.extern.slf4j.Slf4j;
import net.ntg.ftl.FTLAdventureVisualiser;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class TableMaker {

	public TableMaker(File tableFile) throws IOException {
		CsvBeanWriter tableWriter = new CsvBeanWriter(
			new FileWriter(tableFile),
			CsvPreference.TAB_PREFERENCE
		);
		String[] headers = DataUtil.getTableHeaders().toArray(new String[0]);

		try {
			tableWriter.writeHeader(headers);
			for (TableRow row : FTLAdventureVisualiser.recording) {
				tableWriter.write(row, headers);
			}
			log.info("Records are written to {}", tableFile.getAbsolutePath());
		} finally {
			tableWriter.close();
		}
	}

}
