package net.blerf.ftl.parser.sectortree;

import java.util.ArrayList;
import java.util.List;

import net.blerf.ftl.model.sectortree.SectorDot;


/**
 * A generator of SectorDots to populate a SectorTree object.
 *
 * This class simply creates a list of one-dot columns.
 * Each dot has null type, null id, and "Unknown" title.
 */
public class LinearSectorTreeGenerator {

	public List<List<SectorDot>> generateSectorTree(int columnCount) {
		List<List<SectorDot>> result = new ArrayList<>(columnCount);

		for (int c=0; c < columnCount; c++) {
			List<SectorDot> columnDots = new ArrayList<>(1);

			SectorDot dot = new SectorDot(null, null, "Unknown");
			columnDots.add(dot);

			result.add(columnDots);
		}

		return result;
	}

	/**
	 * Generates an invalid tree approximating a blind visitation route.
	 *
	 * The first column will have 1 dot.
	 * Subsequent columns will have 0-or-more unvisited dots, then 1 visited.
	 * When no further visited dots remain, columns will have 1 unvisited dot.
	 * The final column will hold any excess dots.
	 */
	public List<List<SectorDot>> generateSectorTree(List<Boolean> route, int columnCount) {
		List<List<SectorDot>> result = new ArrayList<>(columnCount);

		int lastVisitedR = route.lastIndexOf(Boolean.TRUE);
		List<SectorDot> columnDots = new ArrayList<>(1);
		result.add(columnDots);

		for (int r=0; r < route.size(); r++) {
			SectorDot dot = new SectorDot(null, null, "Unknown");
			dot.setVisited(route.get(r));
			columnDots.add(dot);

			if ((r == 0 || dot.isVisited() || r > lastVisitedR) && result.size() < columnCount) {
				columnDots = new ArrayList<>();
				result.add(columnDots);
			}
		}

		return result;
	}
}
