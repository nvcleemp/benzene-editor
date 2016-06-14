
package benzene.editor;

import benzene.editor.utils.Model;
import benzene.editor.utils.Location;
import benzene.editor.utils.Orientation;

import java.util.stream.Stream;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Benzene as a grid of hexagons.
 *
 *   (0, 0)    (0, 1)    (0, 2)    (0, 3)
 *
 *        (1, 0)    (1, 1)    (1, 2)    (1, 3)
 *
 *             (2, 0)    (2, 1)    (2, 2)    (2, 3)
 *
 *                  (3, 0)    (3, 1)    (3, 2)    (3, 3)
 *
 */
public class Benzene extends Model {

    private final Set<Location> hexes;

    public Benzene() {
        hexes = new HashSet<>();
    }

    /* Queries (no invalidation) ================================== */

    public boolean isOccupied(Location location) {
        return hexes.contains(location);
    }

    public boolean hasNeighbour(Location location) {
        for(Orientation orientation: Orientation.values()) {
            Location neighbour = orientation.applyOffset(location);
            if(isOccupied(neighbour)) return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return hexes.isEmpty();
    }

    /* Private (partial) operations (no invalidation) ============= */

    private void privateToggle(Location location) {
        if(hexes.contains(location)){
            hexes.remove(location);
        } else {
            hexes.add(location);
        }
    }

    private void privateClear() {
        hexes.clear();
    }
    
    private void privateRotateClockwise() {
        Set<Location> newHexes = hexes.stream()
                .map(l -> new Location(l.col+l.row, -l.row))
                .collect(Collectors.toCollection(HashSet::new));
        hexes.clear();
        hexes.addAll(newHexes);
    }
    
    private void privateRotateCounterclockwise() {
        Set<Location> newHexes = hexes.stream()
                .map(l -> new Location(-l.col, l.col+l.row))
                .collect(Collectors.toCollection(HashSet::new));
        hexes.clear();
        hexes.addAll(newHexes);
    }

    /* Public (complete) operations (invalidating) ================ */

    public void toggle(Location location) {
        privateToggle(location);
        invalidate();
    }

    public void clear() {
        privateClear();
        invalidate();
    }

    public void rotateClockwise() {
        privateRotateClockwise();
        invalidate();
    }

    public void rotateCounterclockwise() {
        privateRotateCounterclockwise();
        invalidate();
    }

    /* Grid queries =============================================== */

    public Stream<Location> locations() {
        return hexes.stream();
    }

    public int horizontalLowerBound() { return locations().mapToInt(p -> p.col).min().orElse(0); }
    public int horizontalUpperBound() { return locations().mapToInt(p -> p.col).max().orElse(0); }
    public int verticalLowerBound() { return locations().mapToInt(p -> p.row).min().orElse(0); }
    public int verticalUpperBound() { return locations().mapToInt(p -> p.row).max().orElse(0); }

}

