
package benzene.editor.utils;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {

    @XmlAttribute
    public final int row;
    @XmlAttribute
    public final int col;
    
    private Location(){
        row = col = 0;
    }

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
