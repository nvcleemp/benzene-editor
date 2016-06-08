
package benzene.editor.gui;

import benzene.editor.Benzene;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

import benzene.editor.utils.Location;

public class Tile extends Group {

    public Tile(double radius, Location mLoc, Benzene benzene) {

        getStyleClass().add("tile");

        double halfWidth = Math.sqrt(0.75) * radius;
        double halfHeight = radius;

        // The Hex
        Polygon polygon = new Polygon(
            0.0 * halfWidth, 0.5 * halfHeight, // upperleft
            1.0 * halfWidth, 0.0 * halfHeight, // uppermiddle
            2.0 * halfWidth, 0.5 * halfHeight, // upperright
            2.0 * halfWidth, 1.5 * halfHeight, // lowerright
            1.0 * halfWidth, 2.0 * halfHeight, // lowermiddle
            0.0 * halfWidth, 1.5 * halfHeight  // lowerleft
        );
        polygon.setStroke(Color.BLACK);
        polygon.setFill(benzene.isOccupied(mLoc) ? Color.BLACK : Color.LIGHTGRAY);
        polygon.setOnMouseClicked(ev -> {
            benzene.toggle(mLoc);
        });


        getChildren().add(polygon);
    }

}
