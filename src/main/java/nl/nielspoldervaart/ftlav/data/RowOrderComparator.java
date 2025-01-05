package nl.nielspoldervaart.ftlav.data;

import java.util.Comparator;

public class RowOrderComparator implements Comparator<TableRow> {
	@Override
	public int compare(TableRow o1, TableRow o2) {
		if (o1.getTime() == null || o2.getTime() == null) {
			return 0;
		}
		return o1.getTime().compareTo(o2.getTime());
	}
}
