package net.blerf.ftl.parser.sectormap;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;


/**
 * A generator to create a GeneratedSectorMap object.
 * <p>
 * This class simply creates a rectangular grid of beacons.
 * Each slot's x/y location will be centered in equal-sized cells.
 * All other values will be untouched defaults.
 */
public class GridSectorMapGenerator {

    public GeneratedSectorMap generateSectorMap(int columns, int rows, int columnWidth, int rowHeight) {

        GeneratedSectorMap genMap = new GeneratedSectorMap();
        genMap.setPreferredSize(new Dimension(640, 488));  // TODO: Magic numbers.
        // These numbers were copied from RandomSectorMapGenerator's size for FTL 1.5.4.

        List<GeneratedBeacon> genBeaconList = new ArrayList<GeneratedBeacon>(columns * rows);

        for (int c = 0; c < columns; c++) {

            for (int r = 0; r < rows; r++) {
                int newX = c * columnWidth + columnWidth / 2;
                int newY = r * rowHeight + rowHeight / 2;

                GeneratedBeacon genBeacon = new GeneratedBeacon();
                genBeacon.setLocation(newX, newY);

                genBeaconList.add(genBeacon);
            }
        }

        genMap.setGeneratedBeaconList(genBeaconList);

        return genMap;
    }
}
