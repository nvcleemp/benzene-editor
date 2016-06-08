
package benzene.editor.utils;

public class Location {

    public final int row;
    public final int col;

    public Location(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Location)) return false;
        Location that = (Location) other;
        return this.row == that.row && this.col == that.col;
    }

    @Override
    public int hashCode() {
        return ~row ^ col;
    }

    @Override
    public String toString() {
        return String.format("<%d,%d>", row, col);
    }

    public Location add(int dr, int dc) {
        return new Location(row + dr, col + dc);
    }

}
