package net.blerf.ftl.seedsearch;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.constants.Difficulty;
import net.blerf.ftl.parser.random.NativeRandom;
import net.blerf.ftl.parser.random.RandRNG;
import net.blerf.ftl.parser.sectormap.GeneratedBeacon;
import net.blerf.ftl.parser.sectormap.GeneratedSectorMap;
import net.blerf.ftl.parser.sectormap.RandomSectorMapGenerator;
import net.blerf.ftl.parser.sectortree.RandomSectorTreeGenerator;
import net.blerf.ftl.parser.shiplayout.RandomShipLayout;
import net.blerf.ftl.xml.Choice;
import net.blerf.ftl.xml.FTLEvent;
import net.blerf.ftl.xml.ship.ShipEvent;

/**
 * Finding good seeds
 */
@Slf4j
public class SeedSearch {

    protected RandRNG rng;
    boolean dlcEnabled = true;
    Difficulty difficulty = Difficulty.HARD;

    private static final Set<Integer> uniqueCrewNames = new HashSet<>();

    RandomShipLayout ship = new RandomShipLayout("kestral", uniqueCrewNames); // kestral is not a typo
    @Getter
    private boolean found;

    /* Generate a whole seed, and look at the sector map for a valid path.
     * Returns if one was found.
     */
    public boolean generateAll(RandRNG rng) {

        uniqueCrewNames.clear();

        /* Game startup */
        for (int i = 0; i < 101; i++) {
            rng.rand();
        }

        /* New Game */
        for (int i = 0; i < 68; i++) {
            rng.rand();
        }

        /* Random ship generation */
        int seed = rng.rand();
        log.debug("Ship generation, seed: {}", seed);

        ship.generateShipLayout(rng, seed);

        /* Sector tree generation */
        rng.rand();
        rng.rand();
        rng.rand();

        RandomSectorTreeGenerator expandedTreeGen = new RandomSectorTreeGenerator(rng);
        seed = rng.rand();
        log.info("Sector tree generation, seed: {}", seed);
        expandedTreeGen.generateSectorTree(seed, dlcEnabled);

        /* Sector map generation */
        RandomSectorMapGenerator sectorMapGen = new RandomSectorMapGenerator();
        sectorMapGen.sectorId = "STANDARD_SPACE";
        sectorMapGen.sectorNumber = 0;
        sectorMapGen.difficulty = difficulty;
        sectorMapGen.dlcEnabled = dlcEnabled;
        sectorMapGen.setUniqueNames(uniqueCrewNames);

        seed = rng.rand();
        rng.srand(seed);
        log.debug("Sector map generation, seed: {}", seed);

        GeneratedSectorMap map = sectorMapGen.generateSectorMap(rng, 9);

        /* Check if generation finished early */
        if (map == null)
            return false;

        List<GeneratedBeacon> beaconList = map.getGeneratedBeaconList();

        return bfsStart(map);
    }

    /* Iterate for each seed value and look at a valid path */
    public void search() {

        rng = new NativeRandom("Native");

        /* 20677891
         * 40823384

         *
         * 250410660
         * 273050382
         * 340087472
         * 416218906
         * 492539968
         */

        for (int seed = 250410660; seed < 250410661; seed++) {
//			if (0 == (seed & 0xfff))
//				log.info( String.format( "Seed %d", seed ) );

            rng.srand(seed);

            boolean res = generateAll(rng);
            if (res) {
                log.info("Seed {}", seed);
                // break;
            }
        }



        /* Sector map generation */
        RandomSectorMapGenerator sectorMapGen = new RandomSectorMapGenerator();
        // sectorMapGen.sectorId = "PIRATE_SECTOR";
        sectorMapGen.sectorId = "MANTIS_SECTOR";
        sectorMapGen.sectorNumber = 1;
        sectorMapGen.difficulty = difficulty;
        sectorMapGen.dlcEnabled = dlcEnabled;
        sectorMapGen.setUniqueNames(uniqueCrewNames);

        rng.srand(1351785981);

        for (int k = 0; k < 694 + 24; k++) {
            rng.rand();
        }

        int seed2 = rng.rand();
        rng.srand(seed2);
        log.info(String.format("Sector 2 map generation, seed: %d", seed2));

        sectorMapGen.sectorId = "MANTIS_SECTOR";
        sectorMapGen.sectorNumber = 1;
        sectorMapGen.generateSectorMap(rng, 9);

        for (int k = 0; k < 1256 - 16; k++) {
            rng.rand();
        }

        int seed3 = rng.rand();

        rng.srand(seed3);
        log.info("Sector 3 map generation, seed: {}", seed3);

        sectorMapGen.sectorId = "NEBULA_SECTOR";
        sectorMapGen.sectorNumber = 2;
        sectorMapGen.generateSectorMap(rng, 9);


        rng.srand(1798517121);
        log.info("Sector 4 map generation, seed: 1798517121");

        sectorMapGen.sectorId = "ENGI_HOME";
        sectorMapGen.sectorNumber = 3;
        sectorMapGen.generateSectorMap(rng, 9);

        rng.srand(1090748583);
        log.info("Sector 5 map generation, seed: 1090748583");

        sectorMapGen.sectorId = "ENGI_SECTOR";
        sectorMapGen.sectorNumber = 4;
        sectorMapGen.generateSectorMap(rng, 9);

        rng.srand(1472587140);
        log.info("Sector 6 map generation, seed: 1472587140");

        sectorMapGen.sectorId = "MANTIS_SECTOR";
        sectorMapGen.sectorNumber = 5;
        sectorMapGen.generateSectorMap(rng, 9);

        // rng.srand( 1866532180 );
        // log.info( String.format( "Sector 7 map generation, seed: 1866532180" ) );
        //
        // sectorMapGen.sectorId = "REBEL_SECTOR";
        // sectorMapGen.sectorNumber = 6;
        // sectorMapGen.generateSectorMap(rng, 9);

        Set<Integer> backCrewNames = new HashSet<>(uniqueCrewNames);

        for (int l = 200; l < 600; l++) {

            uniqueCrewNames.clear();
            uniqueCrewNames.addAll(backCrewNames);

            rng.srand(1866532180);
            // log.info( String.format( "Sector 7 map generation, seed: 1866532180" ) );

            sectorMapGen.sectorId = "REBEL_SECTOR";
            sectorMapGen.sectorNumber = 6;
            sectorMapGen.generateSectorMap(rng, 9);

            for (int k = 0; k < 1000 + l; k++) {
                rng.rand();
            }

            int seed8 = rng.rand();

            // if (seed8 != 579979646)
            // 	continue;

            rng.srand(seed8);
            log.debug(String.format("Sector 8 map generation for l %d, seed: %d", l, seed8));

            sectorMapGen.sectorId = "FINAL";
            sectorMapGen.sectorNumber = 7;
            GeneratedSectorMap map = sectorMapGen.generateSectorMap(rng, 9);

            if (map.flagshipBeacon == -1)
                continue;

            List<GeneratedBeacon> beaconList = map.getGeneratedBeaconList();
            GeneratedBeacon endBeacon = beaconList.get(map.endBeacon);

            int begB = map.startBeacon;
            int endB = map.endBeacon;
            int flagB = map.flagshipBeacon;

            /* Only keep sectors when flagship is at distance 4 */
            map.endBeacon = map.flagshipBeacon;
            sectorMapGen.minDistanceMap(map, 4);

            if (beaconList.get(flagB).distance != 4)
                continue;

            /* There should be one path without fights for the first two beacons */
            if (!bfsStart(map))
                continue;

            /* We must check if there is a beacon:
             * - at distance 3 from start
             * - at distance 1 from flagship
             * - in a shortest path from flagship to base
             */

            /* Find all beacons at distance 3 from start to flagship */
            Set<Integer> d3s = new HashSet<Integer>();
            for (int m = 0; m < beaconList.size(); m++) {
                GeneratedBeacon gg = beaconList.get(m);
                if (gg.distance == 3)
                    d3s.add(m);
            }

            /* Find all beacons at distance 1 from flagship to base */
            map.startBeacon = flagB;
            map.endBeacon = endB;
            sectorMapGen.minDistanceMap(map, 20);
            int ss = beaconList.get(endB).distance;

            Set<Integer> d1f = new HashSet<Integer>();
            for (int m = 0; m < beaconList.size(); m++) {
                GeneratedBeacon gg = beaconList.get(m);
                if (gg.distance == 1) {
                    d1f.add(m);
                    log.info(String.format("Beacon at dist 1 from flagship: %d", m));
                }
            }

            /* Find all beacons at distance n-1 from base to flagship */
            map.startBeacon = endB;
            map.endBeacon = flagB;
            sectorMapGen.minDistanceMap(map, 20);

            Set<Integer> dnb = new HashSet<Integer>();
            for (int m = 0; m < beaconList.size(); m++) {
                GeneratedBeacon gg = beaconList.get(m);
                if (gg.distance == (ss - 1)) {
                    dnb.add(m);
                    log.info(String.format("Beacon at dist %d from end: %d", ss - 1, m));
                }
            }

            /* Intersect the two sets to find all 1+dist beacons */
            d1f.retainAll(dnb);

            /* Check that all beacons are at distance 3 from start */
            if (!d3s.containsAll(d1f))
                continue;

            /* Check that there's a path without fights */
            d3s.retainAll(d1f);
            if (!d3s.isEmpty()) {
                for (Integer ii : d3s) {
                    map.startBeacon = begB;
                    map.endBeacon = ii;
                    sectorMapGen.minDistanceMap(map, 20);
                    if (bfsStart(map))
                        log.info("Found!");
                    found = true;

                }
            }

            break;
        }

    }

    public static final double ISOLATION_THRESHOLD = 165d;

    private double distance(GeneratedBeacon b1, GeneratedBeacon b2) {
        Point p1 = b1.getLocation();
        Point p2 = b2.getLocation();
        return Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }

    private boolean bfsStart(GeneratedSectorMap map) {
        List<GeneratedBeacon> beaconList = map.getGeneratedBeaconList();

        List<Integer> beaconPath = new ArrayList<Integer>(5);

        return bfs(map, map.startBeacon, beaconPath);
    }

    private boolean bfs(GeneratedSectorMap map, int currentBeacon, List<Integer> beaconPath) {
        List<GeneratedBeacon> beaconList = map.getGeneratedBeaconList();

        GeneratedBeacon endBeacon = beaconList.get(map.endBeacon);

        /* Examine current beacon event */
        GeneratedBeacon gb = beaconList.get(currentBeacon);
        FTLEvent event = gb.getEvent();

        /* Check if finish beacon */
        if (currentBeacon == map.endBeacon) {
            return validatePath(map, beaconPath);
        }

        /* Is ship hostile */
        if (eventHostile(event, false))
            return false;

        /* Register the beacon in the list */
        int currentDist = gb.distance;
        if (beaconPath.size() == currentDist)
            beaconPath.add(currentBeacon);
        else
            beaconPath.set(currentDist, currentBeacon);

        int curRow = gb.row;
        int curCol = gb.col;

        boolean res = false;
        for (int bd = 0; bd < beaconList.size(); bd++) {
            GeneratedBeacon otherBec = beaconList.get(bd);

            if (otherBec.distance != (currentDist + 1))
                continue;

            /* Check if the two beacons are connected */
            if (Math.abs(otherBec.row - curRow) > 1)
                continue;

            if (Math.abs(otherBec.col - curCol) > 1)
                continue;

            if (distance(gb, otherBec) >= ISOLATION_THRESHOLD)
                continue;

            res = res || bfs(map, bd, beaconPath);
        }
        return res;
    }

    private boolean validatePath(GeneratedSectorMap map, List<Integer> beaconPath) {
        List<GeneratedBeacon> beaconList = map.getGeneratedBeaconList();

        // boolean ret = false;
        // int w = -1;
        // for (int b : beaconPath) {
        // 	GeneratedBeacon bec = beaconList.get(b);
        // 	FTLEvent event = bec.getEvent();
        // 	if (eventItem(event, "BEAM_HULL")) {
        // 		ret = true;
        // 		w = b;
        // 		break;
        // 	}
        // }
        //
        // if (! ret)
        // 	return false;

        // ret = false;
        // for (int b : beaconPath) {
        // 	if (b == w)
        // 		continue;
        // 	GeneratedBeacon bec = beaconList.get(b);
        // 	FTLEvent event = bec.getEvent();
        // 	if (eventItem(event, "SHOTGUN_2")) {
        // 		ret = true;
        // 		break;
        // 	}
        // }
        //
        // if (! ret)
        // 	return false;

        for (int b : beaconPath) {
            GeneratedBeacon bec = beaconList.get(b);
            FTLEvent event = bec.getEvent();
            log.debug("Got beacon {}", b);
            log.debug(event.toDescription(0));
        }

        return true;
    }

    private boolean eventHostile(FTLEvent event, boolean hostile) {
        if (event.getBoarders() != null)
            return true;

        ShipEvent se = event.getShip();
        if (se != null)
            hostile = se.isHostile();

        /* Browse each choice, and load the corresponding event */
        List<Choice> choiceList = event.getChoiceList();

        if (choiceList == null)
            return hostile;

        boolean childHostile = true;
        for (int i = 0; i < choiceList.size(); i++) {
            Choice choice = choiceList.get(i);
            /* We skip if any requirement, we probably don't meet any */
            if (choice.getReq() != null)
                continue;

            FTLEvent choiceEvent = choice.getEvent();
            childHostile = childHostile && eventHostile(choiceEvent, hostile);
        }

        return childHostile;
    }

    private boolean eventItem(FTLEvent event, String item) {
        boolean gotItem = false;

        FTLEvent.Item weapon = event.getWeapon();
        if (weapon != null && weapon.name.equals(item))
            gotItem = true;

        FTLEvent.Item augment = event.getAugment();
        if (augment != null && augment.name.equals(item))
            gotItem = true;

        FTLEvent.Item drone = event.getDrone();
        if (drone != null && drone.name.equals(item))
            gotItem = true;

        FTLEvent.AutoReward autoReward = event.getAutoReward();
        if (autoReward != null) {
            if (autoReward.weapon != null && autoReward.weapon.equals(item))
                gotItem = true;

            if (autoReward.augment != null && autoReward.augment.equals(item))
                gotItem = true;

            if (autoReward.drone != null && autoReward.drone.equals(item))
                gotItem = true;
        }

        if (gotItem) {
            /* Check if loosing crew */
            FTLEvent.CrewMember cm = event.getCrewMember();
            if ((cm != null) && (cm.amount < 0))
                gotItem = false;

            /* Check if loosing stuff */
            FTLEvent.ItemList il = event.getItemList();
            if (il != null) {
                for (FTLEvent.Reward r : il.items) {
                    if (r.value < 0)
                        gotItem = false;
                }
            }
        }

        if (gotItem)
            return true;

        /* Browse each choice, and load the corresponding event */
        List<Choice> choiceList = event.getChoiceList();

        if (choiceList == null)
            return false;

        for (int i = 0; i < choiceList.size(); i++) {
            Choice choice = choiceList.get(i);
            /* We skip if any requirement, we probably don't meet any */
            if (choice.getReq() != null)
                continue;

            FTLEvent choiceEvent = choice.getEvent();
            if (eventItem(choiceEvent, item))
                return true;
        }

        return false;
    }
}
