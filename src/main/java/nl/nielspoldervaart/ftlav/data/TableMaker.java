package nl.nielspoldervaart.ftlav.data;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import nl.nielspoldervaart.ftlav.FTLAdventureVisualiser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

@Slf4j
public class TableMaker {

	public TableMaker(File tableFile) throws IOException {
		Writer writer = new FileWriter(tableFile);

		HeaderColumnNameMappingStrategy<TableRow> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
		mappingStrategy.setColumnOrderOnWrite(new ColumnOrderComparator());
		mappingStrategy.setType(TableRow.class);

		StatefulBeanToCsv<TableRow> tableWriter = new StatefulBeanToCsvBuilder<TableRow>(writer).withSeparator('\t').withMappingStrategy(mappingStrategy).build();
		try {
			tableWriter.write(FTLAdventureVisualiser.recording);
			log.info("Records are written to {}", tableFile.getAbsolutePath());
		} catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			log.error(e.getMessage());
		} finally {
			writer.close();
		}
	}

}
