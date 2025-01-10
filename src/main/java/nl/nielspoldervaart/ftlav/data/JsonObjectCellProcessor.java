package nl.nielspoldervaart.ftlav.data;

import com.opencsv.bean.AbstractBeanField;
import processing.data.JSONObject;

public class JsonObjectCellProcessor extends AbstractBeanField<TableRow, Integer> {

	@Override
	protected JSONObject convert(String value) {
		return JSONObject.parse(String.valueOf(value));
	}

}
