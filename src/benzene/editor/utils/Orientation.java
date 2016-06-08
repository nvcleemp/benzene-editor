
package benzene.editor.utils;

import java.util.regex.Pattern;
import java.util.function.Predicate;

public enum Orientation {
    // Should be in clockwise order!
    UpperRight  (0, ".+/",    -1,  1),
    MiddleRight (1, ".+-",     0,  1),
    LowerRight  (2, ".+\\\\",  1,  0),
    LowerLeft   (3, "/.+",     1, -1),
    MiddleLeft  (4, "-.+",     0, -1),
    UpperLeft   (5, "\\\\.+", -1,  0);

    private final int index;
    private final Predicate<String> predicate;
    private final int rowOffset;
    private final int colOffset;

    private Orientation(int index, String pattern, int rowOffset, int colOffset) {
        this.index = index;
        this.predicate = Pattern.compile(pattern).asPredicate();
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    public Location getOffset() {
        return new Location(rowOffset, colOffset);
    }

    public Location applyOffset(Location location) {
        return new Location(location.row + rowOffset, location.col + colOffset);
    }

    public Predicate<String> getMatcher() { return predicate; }
    public int getRowOffset() { return rowOffset; }
    public int getColOffset() { return colOffset; }

    public static Orientation fromString(String reference) {
        for(Orientation orientation : Orientation.values()) {
            if(orientation.getMatcher().test(reference)) {
                return orientation;
            }
        }
        return null;
    }

    public Orientation nextClockwise() {
        return Orientation.values()[(index + 1) % Orientation.values().length];
    }

    public Orientation nextCounterClockwise() {
        return Orientation.values()[(index - 1 + Orientation.values().length) % Orientation.values().length];
    }

    public Orientation invers() {
        return Orientation.values()[(index + 3) % Orientation.values().length];
    }

}

