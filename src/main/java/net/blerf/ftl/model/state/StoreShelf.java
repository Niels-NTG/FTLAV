package net.blerf.ftl.model.state;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.blerf.ftl.model.type.StoreItemType;

/**
 * Up to 3 StoreItems may to be added (Set the StoreItemType, too.)
 * Fewer StoreItems mean empty space on the shelf.
 */
@Getter
@Setter
@NoArgsConstructor
public class StoreShelf {
    private StoreItemType itemType = StoreItemType.WEAPON;
    private final List<StoreItem> items = new ArrayList<>();

    /**
     * Copy constructor.
     * <p>
     * Each StoreItem will be copy-constructed as well.
     */
    public StoreShelf(StoreShelf srcShelf) {
        itemType = srcShelf.getItemType();

        for (StoreItem tmpItem : srcShelf.getItems()) {
            addItem(new StoreItem(tmpItem));
        }
    }

    public void addItem(StoreItem item) {
        items.add(item);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        result.append(String.format("Item Type: %s%n", itemType));
        for (StoreItem item : items) {
            if (first) {
                first = false;
            } else {
                result.append(",\n");
            }
            result.append(item.toString().replaceAll("(^|\n)(.+)", "$1  $2"));
        }

        return result.toString();
    }
}
