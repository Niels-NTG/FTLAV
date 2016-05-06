package net.blerf.ftl.model.sectortree;

import java.util.ArrayList;
import java.util.List;


public class SectorTree {

	private final List<SectorTreeListener> treeListenerList = new ArrayList<>();

	private List<List<SectorDot>> dotColumns;


	public SectorTree() {
		dotColumns = new ArrayList<>(8);
	}

	public SectorTree(SectorTree srcTree) {
		setSectorDots(srcTree);
	}


	/**
	 * Replaces all SectorDots.
	 *
	 * Old dots' visitation will be forgotten.
	 */
	public void setSectorDots(List<List<SectorDot>> newColumns) {
		dotColumns = newColumns;
	}

	/**
	 * Replaces all SectorDots by copy-constructing them from another tree.
	 *
	 * Old dots' visitation will be forgotten.
	 */
	public void setSectorDots(SectorTree srcTree) {
		List<List<SectorDot>> srcColumns = srcTree.getSectorDots();
		List<List<SectorDot>> newColumns = new ArrayList<>();

		for (List<SectorDot> srcColumn : srcColumns) {
			List<SectorDot> newColumn = new ArrayList<>(srcColumn.size());
			for (SectorDot srcDot : srcColumn) {
				newColumn.add(new SectorDot(srcDot));
			}
			newColumns.add(newColumn);
		}

		dotColumns = newColumns;
	}

	public List<List<SectorDot>> getSectorDots() {
		return dotColumns;
	}

	/**
	 * Sets the visitation of all dots.
	 *
	 * @param route a list of visited values for each column, top-to-bottom
	 */
	public void setSectorVisitation(List<Boolean> route) {
		int dotsSoFar = 0;

		for (List<SectorDot> columnDots : dotColumns) {
			for (int r = 0; r < columnDots.size(); r++) {
				boolean visited = route.get(dotsSoFar + r);
				columnDots.get(r).setVisited(visited);
			}

			dotsSoFar += columnDots.size();
		}
	}

	/**
	 * Returns the visitation of all dots.
	 *
	 * Each call creates a new List.
	 */
	public List<Boolean> getSectorVisitation() {
		List<Boolean> result = new ArrayList<>();

		for (List<SectorDot> columnDots : dotColumns) {
			for (SectorDot columnDot : columnDots) {
				boolean visited = columnDot.isVisited();
				result.add(visited);
			}
		}

		return result;
	}

	/**
	 * Sets visitation to false for all dots beyond a given column.
	 */
	public void truncateSectorVisitation(int lastVisitedColumn) {
		for (int c=lastVisitedColumn+1; c < dotColumns.size(); c++) {
			List<SectorDot> columnDots = dotColumns.get(c);

			for (SectorDot dot : columnDots) {
				dot.setVisited(false);
			}
		}
	}

	/**
	 * Removes all SectorDots.
	 */
	public void clear() {
		dotColumns.clear();
	}

	/**
	 * Returns true if this tree contains no dots.
	 */
	public boolean isEmpty() {
		return (SectorTree.countDots(dotColumns) == 0);
	}


	public void addSectorTreeListener(SectorTreeListener l) {
		if (!treeListenerList.contains(l)) treeListenerList.add(l);
	}

	public void removeSectorTreeListener(SectorTreeListener l) {
		treeListenerList.remove(l);
	}

	public void fireTreeChanged(SectorTreeEvent e) {
		for (SectorTreeListener l : treeListenerList) {
			l.sectorTreeChanged(new SectorTreeEvent(e));
		}
	}

	public void fireColumnsChanged() {
		fireTreeChanged(new SectorTreeEvent(this, SectorTreeEvent.COLUMNS_CHANGED));
	}

	public void fireVisitationChanged() {
		fireTreeChanged(new SectorTreeEvent(this, SectorTreeEvent.VISITATION_CHANGED));
	}


	/**
	 * Returns the total number of columns.
	 */
	public int getColumnsCount() {
		return dotColumns.size();
	}

	/**
	 * Returns a list of dots in a given column.
	 */
	public List<SectorDot> getColumn(int column) {
		return dotColumns.get(column);
	}

	/**
	 * Returns the dot at a given row of a column.
	 */
	public SectorDot getDot(int column, int row) {
		return dotColumns.get(column).get(row);
	}

	/**
	 * Returns the row of a dot in its column, or -1.
	 */
	public int getDotRow(SectorDot dot) {
		for (int c=dotColumns.size()-1; c >= 0; c--) {
			if (dotColumns.get(c).contains(dot)) {
				return dotColumns.get(c).indexOf(dot);
			}
		}
		return -1;
	}

	/**
	 * Returns the column containing a given dot.
	 */
	public int getDotColumn(SectorDot dot) {
		for (int c=dotColumns.size()-1; c >= 0; c--) {
			if (dotColumns.get(c).contains(dot)) {
				return c;
			}
		}
		return -1;
	}

	/**
	 * Returns the dot that was visited in a given column, or null.
	 */
	public SectorDot getVisitedDot(int column) {
		for (SectorDot dot : dotColumns.get(column)) {
			if (dot.isVisited()) return dot;
		}
		return null;
	}

	/**
	 * Returns true if any dot in a given column has been visited.
	 */
	public boolean isColumnVisited(int column) {
		return (getVisitedDot(column) != null);
	}

	/**
	 * Returns the last column that was visited, or -1.
	 */
	public int getLastVisitedColumn() {
		for (int c=dotColumns.size()-1; c >= 0; c--) {
			if (isColumnVisited(c)) return c;
		}
		return -1;
	}

	/**
	 * Returns the visited dot's row in the last visited column, or -1.
	 */
	public int getLastVisitedRow() {
		for (int c=dotColumns.size()-1; c >= 0; c--) {
			SectorDot dot = getVisitedDot(c);
			if (dot != null) return dotColumns.get(c).indexOf(dot);
		}
		return -1;
	}

	/**
	 * Sets visitation to true for a dot in the next unvisited column.
	 *
	 * @param row the dot's row in the next unvisited column
	 */
	public void setNextVisitedRow(int row) {
		int nextColumn = getLastVisitedColumn()+1;
		if (!isDotAccessible(nextColumn, row)) return;

		SectorDot dot = getDot(nextColumn, row);
		dot.setVisited(true);
	}

	/**
	 * Returns true if a dot can potentially be visited.
	 *
	 * A dot can be visited if it is:
	 *   - already visited
	 *   - in an unvisited column, connected to a prior accessible dot
	 */
	public boolean isDotAccessible(int column, int row) {
		if (column < 0 || column >= dotColumns.size()) return false;
		if (row < 0 || row >= dotColumns.get(column).size()) return false;

		if (column == 0) return true;

		if (getDot(column, row).isVisited()) {
			return true;
		}
		else if (isColumnVisited(column)) {
			return false;  // The column was visited, but not this particular dot.
		}
		else {
			// The column was not visited, recurse through prior dots.

			List<SectorDot> nearList = new ArrayList<>(4);

			getConnectedDots(column, row, false, nearList);
			List<SectorDot> nearColumn = dotColumns.get(column-1);

			for (SectorDot nearDot : nearList) {
				if (isDotAccessible(column-1, nearColumn.indexOf(nearDot))) {
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * Fills a list with SectorDots that are connected to an origin dot.
	 *
	 * @param originColumn the origin dot's column
	 * @param originRow the origin dot's row
	 * @param forward true to find dots to the right, false to the left
	 * @param resultList an existing list to hold results (It will be cleared)
	 */
	public void getConnectedDots(int originColumn, int originRow, boolean forward, List<SectorDot> resultList) {
		resultList.clear();
		boolean fromAtoB = true;
		int aCol = originColumn;
		int bCol = originColumn + (forward ? 1 : -1);

		if (bCol < 0 || bCol >= dotColumns.size()) {
			return;  // No more columns in that direction.
		}

		// Ensure aCol is the smaller one.
		if (dotColumns.get(bCol).size() < dotColumns.get(aCol).size()) {
			fromAtoB = false;
			int zCol = aCol;  // Swap the columns.
			aCol = bCol;
			bCol = zCol;
		}
		int aSize = dotColumns.get(aCol).size();
		int bSize = dotColumns.get(bCol).size();

		if (aSize == 1) {
			// Connect A:r to all B dots.
			// 1 vs 2.
			// 1 vs 3.
			// 1 vs 4.
			if (fromAtoB) {
				resultList.addAll(dotColumns.get(bCol));
			} else {
				resultList.addAll(dotColumns.get(aCol));
			}
		}
		else if (aSize == bSize-1) {
			// Connect each A:r to B:r, and to B:r+1.
			// 2 vs 3.
			// 3 vs 4.

			int aStart = 0; int aEnd = aSize;
			if (fromAtoB) {
				aStart = originRow; aEnd = originRow+1;  // Only a single aRow matters.
			}

			for (int aRow=aStart; aRow < aEnd; aRow++) {
				for (int bRow=aRow; bRow <= aRow+1; bRow++) {
					if (fromAtoB && aRow == originRow) {
						resultList.add(getDot(bCol, bRow));
					}
					else if (!fromAtoB && bRow == originRow) {
						resultList.add(getDot(aCol, aRow));
						break;
					}
				}
			}
		}
		else if (aSize*aSize == bSize) {
			// Connect each A:r to fractional swaths of B, with a dot count equalling aSize.
			// 2 vs 4.

			int aStart = 0; int aEnd = aSize;
			if (fromAtoB) {
				aStart = originRow; aEnd = originRow+1;  // Only a single aRow matters.
			}

			for (int aRow=0; aRow < aSize; aRow++) {
				for (int bRow=aSize*(aRow); bRow < aSize*(aRow+1); bRow++) {
					if (fromAtoB && aRow == originRow) {
						resultList.add(getDot(bCol, bRow));
					}
					else if (!fromAtoB && bRow == originRow) {
						resultList.add(getDot(aCol, aRow));
						break;
					}
				}
			}
		}
	}


	/**
	 * Sort of tests for dots' equality. (Visitation is ignored.)
	 *
	 * This was part of an experiment: use a different RNG to generate a
	 * trees at random until a similar one is found. That would have made
	 * migrating saved games partially feasible, but trial and error is slow.
	 *
	 * @see net.blerf.ftl.model.sectortree.SectorDot#isSimilarTo(SectorDot)
	 */
	public static boolean isSimilar(List<List<SectorDot>> aColumns, List<List<SectorDot>> bColumns) {
		int aColsSize = aColumns.size();
		if (aColsSize != bColumns.size()) return false;

		for (int c=0; c < aColsSize; c++) {
			List<SectorDot> aCol = aColumns.get(c);
			List<SectorDot> bCol = bColumns.get(c);

			int aColSize = aCol.size();
			if (aColSize != bCol.size()) return false;

			for (int r=0; r < aColSize; r++) {
				SectorDot aDot = aCol.get(r);
				SectorDot bDot = bCol.get(r);

				if (!aDot.isSimilarTo(bDot)) return false;
			}
		}

		return true;
	}



	/**
	 * Returns the total number of dots in a potential tree.
	 *
	 * The result can be compared with a visitation list's size for sanity
	 * checks.
	 */
	public static int countDots(List<List<SectorDot>> newColumns) {
		int dotsSoFar = 0;
		for (List<SectorDot> columnDots : newColumns) {
			dotsSoFar += columnDots.size();
		}
		return dotsSoFar;
	}

	/**
	 * Checks a potential tree for obvious problems and throws exceptions.
	 */
	public static void validate(List<List<SectorDot>> newColumns) throws SectorTreeException {
		if (newColumns.isEmpty()) {
			throw new SectorTreeException("Columns count is zero.");
		}

		int prevColumnSize = 0;
		boolean prevColumnVisited = true;

		for (int c=0; c < newColumns.size(); c++) {
			List<SectorDot> columnDots = newColumns.get(c);
			if (columnDots.isEmpty()) {
				SectorTreeException ex = new SectorTreeException(String.format("Empty column: %d", c));
				ex.setColumn(c);
				throw ex;
			}

			if (columnDots.size() == prevColumnSize) {
				SectorTreeException ex = new SectorTreeException(String.format("Column size is not different from preceeding column: %d", c));
				ex.setColumn(c);
				throw ex;
			}

			int visitedCount = 0;
			for (SectorDot dot : columnDots) {
				if (dot.isVisited()) visitedCount++;
			}

			if (visitedCount > 1) {
				SectorTreeException ex = new SectorTreeException(String.format("Multiple dots in the same column were visited: %d", c));
				ex.setColumn(c);
				throw ex;
			}
			else if (visitedCount > 0 && !prevColumnVisited) {
				SectorTreeException ex = new SectorTreeException(String.format("Inaccessible dot was marked visited.", c));
				ex.setColumn(c);
				for (int r=0; r < columnDots.size(); r++) {
					if (columnDots.get(r).isVisited()) {
						ex.setRow(r);
					}
				}
				throw ex;
			}

			prevColumnSize = columnDots.size();
			prevColumnVisited = (visitedCount > 0);
		}

		// TODO: Check sector IDs/Names?
	}
}
