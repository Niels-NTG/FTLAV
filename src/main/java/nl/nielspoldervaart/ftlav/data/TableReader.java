package nl.nielspoldervaart.ftlav.data;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
public class TableReader {

	public static void read(File targetFile) throws IOException {
		List<TableRow> newTableRows = new CsvToBeanBuilder<TableRow>(new FileReader(targetFile)).withSeparator('\t').withType(TableRow.class).build().parse();
		if (!newTableRows.isEmpty()) {
			FTLAdventureVisualiser.recording.clear();
			FTLAdventureVisualiser.recording.addAll(newTableRows);
		} else {
			log.warn("Input TSVG file has no valid data rows!");
		}
	}

}
