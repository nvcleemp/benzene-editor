
package benzene.editor.gui;

import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;

public class NoneFocusScrollPane extends ScrollPane {

    public NoneFocusScrollPane(Pane content) {
        super(content);
    }

    public NoneFocusScrollPane() {
        super();
    }

    @Override
    public void requestFocus() {}

}
