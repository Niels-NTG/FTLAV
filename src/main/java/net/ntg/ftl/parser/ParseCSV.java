package net.ntg.ftl.parser;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ParseCSV {

	private static final Logger log = LogManager.getLogger(ParseCSV.class);

	private static final char DELIMITER = ';';

	public static void importCSV(String fileName) {



	}


	public static void writeCSV(String fileName) {

		StringWriter sw = new StringWriter();
		CSVWriter writer = new CSVWriter(sw, DELIMITER);


	}

}