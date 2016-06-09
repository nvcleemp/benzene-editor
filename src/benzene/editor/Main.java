package benzene.editor;

import benzene.editor.gui.BenzeneView;
import benzene.editor.gui.NoneFocusScrollPane;
import benzene.editor.utils.BenzeneExporter;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Benzene benzene = new Benzene();
        Parent root = layout(benzene);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("gui/style.css").toExternalForm());
        
        primaryStage.setTitle("Benzene editor");
        primaryStage.setScene(scene);
        primaryStage.show();

        benzene.invalidate();
    }

    private Parent layout(final Benzene benzene) {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        root.setId("root");
        
        MenuBar menuBar = new MenuBar();

        Menu menuBenzene = new Menu("Benzene");
        
        MenuItem exportSage = new MenuItem("Export Sage");
        exportSage.setOnAction((ActionEvent t) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(BenzeneExporter.exportSage(benzene));
            Clipboard.getSystemClipboard().setContent(content);
            new Alert(Alert.AlertType.INFORMATION, "Code copied to clipboard", ButtonType.OK).show();
        });
        
        MenuItem exportTikz = new MenuItem("Export Tikz");
        exportTikz.setOnAction((ActionEvent t) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(BenzeneExporter.exportTikZ(benzene));
            Clipboard.getSystemClipboard().setContent(content);
            new Alert(Alert.AlertType.INFORMATION, "Code copied to clipboard", ButtonType.OK).show();
        });
        
        MenuItem exportLaTeX = new MenuItem("Export LaTeX");
        exportLaTeX.setOnAction((ActionEvent t) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(BenzeneExporter.exportLaTeX(benzene));
            Clipboard.getSystemClipboard().setContent(content);
            new Alert(Alert.AlertType.INFORMATION, "Code copied to clipboard", ButtonType.OK).show();
        });
        
        menuBenzene.getItems().addAll(exportSage, exportTikz, exportLaTeX);
 
        menuBar.getMenus().add(menuBenzene);
        
        root.setTop(menuBar);

        NoneFocusScrollPane gamePane = new NoneFocusScrollPane();
        gamePane.setContent(new BenzeneView(gamePane, benzene));
        root.setCenter(gamePane);
        root.getCenter().setId("game-panel");

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
