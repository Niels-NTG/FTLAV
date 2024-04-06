package net.blerf.ftl.parser.shiplayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.blerf.ftl.model.shiplayout.ShipLayout;
import net.blerf.ftl.model.shiplayout.ShipLayoutRoom;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.random.RandRNG;

/**
 * A generator of ship layout.
 *
 * @see net.blerf.ftl.model.shiplayout.ShipLayout
 * @see net.blerf.ftl.parser.random.NativeRandom
 */
@Slf4j
public class RandomShipLayout {

    protected RandRNG rng;

    private static Set<Integer> uniqueCrewNames = null;

    List<RoomSquare> roomSquares = new ArrayList<>();
    boolean[] squarePairs;

    public RandomShipLayout(String shipLayoutId, Set<Integer> un) {
        uniqueCrewNames = un;

        ShipLayout shipLayout = DataManager.get().getShipLayout(shipLayoutId);

        /* Build a list of square in each room */

        int roomCount = shipLayout.getRoomCount();
        for (int r = 0; r < roomCount; r++) {
            ShipLayoutRoom layoutRoom = shipLayout.getRoom(r);

            /* Pick a random square in the room */
            RoomSquare square = new RoomSquare();
            square.roomId = r;
            square.roomX = layoutRoom.locationX;
            square.roomY = layoutRoom.locationY;
            square.roomW = layoutRoom.squaresH;
            square.roomH = layoutRoom.squaresV;

            roomSquares.add(square);
        }

        squarePairs = new boolean[roomCount * roomCount];
    }

    public class RoomSquare {
        public int roomId;

        /* Room coordinates */
        public int roomX;
        public int roomY;
        public int roomW;
        public int roomH;

        /* Square coordinates in game units */
        public int x;
        public int y;
    }

    public void generateShipLayout(RandRNG rng, int seed) {

        rng.srand(seed);
        int roomCount = roomSquares.size();

        for (RoomSquare square : roomSquares) {
            int squareId = rng.rand() % (square.roomW * square.roomH);
            square.x = square.roomX + squareId % square.roomW;
            square.y = square.roomY + squareId / square.roomW;

            /* Translate to game coordinates */
            square.x = square.x * 35 + 17;
            square.y = square.y * 35 + 87;

            log.debug("Room {} has coords {} - {}", square.roomId, square.x, square.y);
        }

        Arrays.fill(squarePairs, false);

        for (RoomSquare square1 : roomSquares)
            for (RoomSquare square2 : roomSquares) {
                if (square1.roomId == square2.roomId) continue;

                /* Compute euclidian distance between the two rooms */
                double distance = Math.hypot(square1.x - square2.x, square1.y - square2.y);

                if ((int) distance < 107) {

                    /* Check if there is already a pair between the two squares */
                    boolean isPair = squarePairs[square2.roomId * roomCount + square1.roomId];

                    if (isPair) continue;

                    /* The following code is only useful the first time we
                     * encounter a pair. When we are processing the swapped pair,
                     * we can skip most of it
                     */
                    if (square2.roomId < square1.roomId) {
                        /* Pair is swapped, only call the rng if necessary */
                        if ((square1.x != square2.x) && (square1.y != square2.y)) {
                            rng.rand();
                        }
                        continue;
                    }

                    /* If the two squares are not aligned,
                     * generate a Square from coords of the two squares */
                    if ((square1.x != square2.x) && (square1.y != square2.y)) {
                        RoomSquare extraSquare = new RoomSquare();
                        int rr = rng.rand();
                        log.debug("Rooms {} - {} value is {}", square1.roomId, square2.roomId, rr);

                        if ((rr & 0x1) == 0) {
                            extraSquare.x = square1.x;
                            extraSquare.y = square2.y;
                        } else {
                            extraSquare.x = square2.x;
                            extraSquare.y = square1.y;
                        }

                        /* Detect if there is a third square between the pairs */
                        isPair = false;
                        for (RoomSquare square3 : roomSquares) {
                            if (square3.roomId == square1.roomId) continue;
                            if (square3.roomId == square2.roomId) continue;
                            if (middleSquare(square1, extraSquare, square3)) {
                                isPair = true;
                                break;
                            }
                            if (middleSquare(extraSquare, square2, square3)) {
                                isPair = true;
                                break;
                            }
                        }

                        if (isPair) {
                            log.debug("Found middle room");
                            continue;
                        }

                        /* Insert the square pair */
                        squarePairs[square1.roomId * roomCount + square2.roomId] = true;
                    }
                }
            }

        for (int k = 0; k < 6; k++)
            rng.rand();

        /* Generate crew names */
        if (uniqueCrewNames != null) {
            /* Generate 3 names. TODO: get that from ship layout */
            for (int k = 0; k < 3; k++) {
                int n = rng.rand() % 169; // TODO: Magic number, look at (sorted?) crew names

                while (uniqueCrewNames.contains(n)) {
                    n = rng.rand() % 169;
                }
                uniqueCrewNames.add(n);
            }
        }

        for (int k = 0; k < 3; k++) {
            rng.rand(); // 0x521559 -> 0x51d7e2
            rng.rand(); // 0x5216c3
            rng.rand(); // 0x5216c3
        }

        for (int k = 0; k < 48; k++) {
            rng.rand(); // 0x56e351 -> 0x52db6b
            rng.rand();    // 0x56e351 -> 0x52de31
        }

        for (int k = 0; k < 3; k++) {
            rng.rand(); // 0x511116 -> 0x68a266 -> 0x689eea
            rng.rand(); // 0x511116 -> 0x68a266 -> 0x689f03
            rng.rand(); // 0x511116 -> 0x68a266 -> 0x689f7a
            rng.rand(); // 0x511116 -> 0x68a266 -> 0x689f93
            rng.rand(); // 0x511116 -> 0x68a266 -> 0x68a00c
        }
    }

    /* Returns if square3 is between square1 and square2 on a same axis */
    private boolean middleSquare(RoomSquare square1, RoomSquare square2, RoomSquare square3) {
        if ((square1.x == square3.x) && (square1.y == square3.y))
            return true;
        if ((square2.x == square3.x) && (square2.y == square3.y))
            return true;

        if ((square1.x == square2.x) && (square1.x == square3.x)) {
            if ((square1.y < square3.y) && (square3.y < square2.y))
                return true;
            return (square2.y < square3.y) && (square3.y < square1.y);
        } else if ((square1.y == square2.y) && (square1.y == square3.y)) {
            if ((square1.x < square3.x) && (square3.x < square2.x))
                return true;
            return (square2.x < square3.x) && (square3.x < square1.x);
        }

        return false;
    }
}
