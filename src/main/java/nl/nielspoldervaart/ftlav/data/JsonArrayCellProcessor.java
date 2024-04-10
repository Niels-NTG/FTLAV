package nl.nielspoldervaart.ftlav.data;

import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.util.CsvContext;
import processing.data.JSONArray;

public class JsonArrayCellProcessor extends CellProcessorAdaptor {

	public JsonArrayCellProcessor() {
		super();
	}

	@Override
	public Object execute(Object value, CsvContext context) {
		validateInputNotNull(value, context);

		return JSONArray.parse(String.valueOf(value));
	}

}
