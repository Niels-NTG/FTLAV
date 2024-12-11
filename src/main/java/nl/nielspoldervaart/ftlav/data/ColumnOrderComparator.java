package nl.nielspoldervaart.ftlav.data;

import java.util.ArrayList;
import java.util.Comparator;

public class ColumnOrderComparator implements Comparator<String> {

	private static final ArrayList<String> TABLE_COLUMNS = TableRow.getTableWriterHeaders();

	@Override
	public int compare(String o1, String o2) {
		return TABLE_COLUMNS.indexOf(o1) - TABLE_COLUMNS.indexOf(o2);
	}

}
