
package benzene.editor.gui;


import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import benzene.editor.Benzene;
import benzene.editor.utils.Location;

public class BenzeneView extends Pane implements InvalidationListener {

    private static final int RADIUS = 50;
    private static final double PADDING = 1.05;
    private final Benzene benzene;
    private final Region parent;

    public BenzeneView(Region parent, Benzene benzene) {
        super();
        this.benzene = benzene;
        this.parent = parent;
        benzene.addListener(this);
        parent.widthProperty().addListener(this);
        parent.heightProperty().addListener(this);
    }

    @Override
    public void invalidated(Observable observable) {
        draw();
    }

    @Override
    public void requestFocus() {}

    private void draw() {
        getChildren().clear();
        double verticalCenter = parent.getHeight() / 2,
               horizontalCenter = parent.getWidth() / 2,
               verticalOffset = verticalCenter - Math.sqrt(0.75) * RADIUS,
               horizontalOffset = horizontalCenter - RADIUS;
        if(benzene.isEmpty()) {
            Tile empty = new Tile(RADIUS, new Location(0, 0), benzene);
            empty.setLayoutX(horizontalOffset);
            empty.setLayoutY(verticalOffset);
            getChildren().add(empty);
        } else {
            int startrow = benzene.verticalLowerBound(),
                endrow   = benzene.verticalUpperBound(),
                startcol = benzene.horizontalLowerBound(),
                endcol   = benzene.horizontalUpperBound();
            for(int r = startrow - 1; r <= endrow + 1; r++) {
                for(int c = startcol - 1; c <= endcol + 1; c++) {
                    Location location = new Location(r, c);
                    if(benzene.isOccupied(location) || benzene.hasNeighbour(location)) {
                        Tile tile = new Tile(RADIUS, location, benzene);
                        tile.setLayoutX(horizontalOffset + (2 * c + r) * Math.sqrt(0.75) * PADDING * RADIUS);
                        tile.setLayoutY(verticalOffset + 1.5 * r * PADDING * RADIUS);
                        getChildren().add(tile);
                    }
                }
            }
        }
    }

}
