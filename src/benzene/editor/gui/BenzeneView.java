
package benzene.editor.gui;


import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import benzene.editor.Benzene;
import benzene.editor.utils.IntegerModel;
import benzene.editor.utils.Location;

public class BenzeneView extends Pane implements InvalidationListener {

    private static final int RADIUS = 50;
    private static final double PADDING = 1.05;
    private final Benzene benzene;
    private final Region parent;
    private boolean showNeighbouringHexagons;
    private IntegerModel scaleModel;

    public BenzeneView(Benzene benzene) {
        this(null, benzene, false);
    }

    public BenzeneView(Region parent, Benzene benzene) {
        this(parent, benzene, true);
    }

    public BenzeneView(Region parent, Benzene benzene, IntegerModel scaleModel) {
        this(parent, benzene, true, scaleModel);
    }
    
    public BenzeneView(Region parent, Benzene benzene, boolean showNeighbouringHexagons) {
        this(parent, benzene, showNeighbouringHexagons, new IntegerModel(0));
    }
    
    public BenzeneView(Region parent, Benzene benzene, boolean showNeighbouringHexagons, IntegerModel scaleModel) {
        super();
        this.benzene = benzene;
        this.parent = parent;
        this.showNeighbouringHexagons = showNeighbouringHexagons;
        this.scaleModel = scaleModel;
        benzene.addListener(this);
        scaleModel.addListener(this);
        if(parent!=null){
            parent.widthProperty().addListener(this);
            parent.heightProperty().addListener(this);
        }
    }

    @Override
    public void invalidated(Observable observable) {
        draw();
    }

    @Override
    public void requestFocus() {}

    private void draw() {
        getChildren().clear();
        double radius = RADIUS * Math.pow(2, scaleModel.getValue());
        double verticalCenter = parent == null ? 0 : parent.getHeight() / 2,
               horizontalCenter = parent == null ? 0 : parent.getWidth() / 2,
               verticalOffset = verticalCenter - Math.sqrt(0.75) * radius,
               horizontalOffset = horizontalCenter - radius;
        if(benzene.isEmpty()) {
            if(showNeighbouringHexagons){
                Tile empty = new Tile(radius, new Location(0, 0), benzene);
                empty.setLayoutX(horizontalOffset);
                empty.setLayoutY(verticalOffset);
                getChildren().add(empty);
            }
        } else {
            int startrow = benzene.verticalLowerBound(),
                endrow   = benzene.verticalUpperBound(),
                startcol = benzene.horizontalLowerBound(),
                endcol   = benzene.horizontalUpperBound();
            for(int r = startrow - 1; r <= endrow + 1; r++) {
                for(int c = startcol - 1; c <= endcol + 1; c++) {
                    Location location = new Location(r, c);
                    if(benzene.isOccupied(location) || (showNeighbouringHexagons && benzene.hasNeighbour(location))) {
                        Tile tile = new Tile(radius, location, benzene);
                        tile.setLayoutX(horizontalOffset + (2 * c + r) * Math.sqrt(0.75) * PADDING * radius);
                        tile.setLayoutY(verticalOffset + 1.5 * r * PADDING * radius);
                        getChildren().add(tile);
                    }
                }
            }
        }
    }

}
