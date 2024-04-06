package net.blerf.ftl.model.state;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A store, which contains supplies and item shelves of various types.
 * <p>
 * FTL 1.01-1.03.3 always had two StoreShelf objects.
 * If more are added, only the first two will be saved.
 * If fewer, writeBeacon() will add dummy shelves with no items.
 * <p>
 * FTL 1.5.4 can have a variable number of shelves, but zero
 * crashes the game. So writeBeacon() will add a dummy shelf.
 * <p>
 * TODO: Find out what happens if more than four shelves are added.
 */
@Getter
@Setter
@NoArgsConstructor
public class StoreState {
    private int fuel = 0;
    private int missiles = 0;
    private int droneParts = 0;
    private List<StoreShelf> shelfList = new ArrayList<>();

    /**
     * Copy constructor.
     * <p>
     * Each StoreShelf will be copy-constructed as well.
     */
    public StoreState(StoreState srcStore) {
        fuel = srcStore.getFuel();
        missiles = srcStore.getMissiles();
        droneParts = srcStore.getDroneParts();

        for (StoreShelf srcShelf : srcStore.getShelfList()) {
            addShelf(new StoreShelf(srcShelf));
        }
    }

    public void addShelf(StoreShelf shelf) {
        shelfList.add(shelf);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Fuel:        %2d%n", fuel));
        result.append(String.format("Missiles:    %2d%n", missiles));
        result.append(String.format("Drone Parts: %2d%n", droneParts));

        for (int i = 0; i < shelfList.size(); i++) {
            result.append(String.format("%nShelf %d...%n", i));
            result.append(shelfList.get(i).toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
