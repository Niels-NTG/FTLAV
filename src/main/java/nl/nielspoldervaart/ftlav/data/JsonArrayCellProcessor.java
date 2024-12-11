package nl.nielspoldervaart.ftlav.data;

import com.opencsv.bean.AbstractBeanField;
import processing.data.JSONArray;

public class JsonArrayCellProcessor extends AbstractBeanField<TableRow, Integer> {

	@Override
	protected JSONArray convert(String value) {
		return JSONArray.parse(String.valueOf(value));
	}

}
