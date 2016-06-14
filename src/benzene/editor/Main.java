package benzene.editor;

import benzene.editor.gui.BenzeneView;
import benzene.editor.gui.NoneFocusScrollPane;
import benzene.editor.utils.BenzeneExporter;
import benzene.editor.utils.IntegerModel;

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
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Benzene benzene = new Benzene();
        Parent root = layout(benzene, new IntegerModel(0));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("gui/style.css").toExternalForm());
        
        primaryStage.setTitle("Benzene editor");
        primaryStage.setScene(scene);
        primaryStage.show();

        benzene.invalidate();
    }

    private Parent layout(final Benzene benzene, final IntegerModel scaleModel) {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);
        root.setId("root");
        
        MenuBar menuBar = new MenuBar();

        Menu menuExport = new Menu("Export");
        
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
        
        MenuItem exportPng = new MenuItem("Export PNG");
        exportPng.setOnAction((ActionEvent t) -> {
            BenzeneExporter.exportPng(benzene);
        });
        
        menuExport.getItems().addAll(exportSage, exportTikz, exportLaTeX, exportPng);
        
        Menu menuEdit = new Menu("Edit");
        
        MenuItem clear = new MenuItem("Clear");
        clear.setOnAction((ActionEvent t) -> {
            benzene.clear();
        });
        
        MenuItem rotateClockwise = new MenuItem("Rotate Clockwise");
        rotateClockwise.setOnAction((ActionEvent t) -> {
            benzene.rotateClockwise();
        });
        
        MenuItem rotateCounterclockwise = new MenuItem("Rotate Counterclockwise");
        rotateCounterclockwise.setOnAction((ActionEvent t) -> {
            benzene.rotateCounterclockwise();
        });
        
        menuEdit.getItems().addAll(clear, new SeparatorMenuItem(), rotateClockwise, rotateCounterclockwise);
 
        Menu menuView = new Menu("View");
        
        MenuItem zoomIn = new MenuItem("Zoom in");
        zoomIn.setOnAction((ActionEvent t) -> {
            scaleModel.increase();
        });
        
        MenuItem zoomOut = new MenuItem("Zoom out");
        zoomOut.setOnAction((ActionEvent t) -> {
            scaleModel.decrease();
        });
        
        MenuItem zoom100 = new MenuItem("Reset Zoom");
        zoom100.setOnAction((ActionEvent t) -> {
            scaleModel.setValue(0);
        });
        
        menuView.getItems().addAll(zoomIn, zoomOut, new SeparatorMenuItem(), zoom100);
        
        menuBar.getMenus().addAll(menuExport, menuEdit, menuView);
        
        root.setTop(menuBar);

        NoneFocusScrollPane gamePane = new NoneFocusScrollPane();
        gamePane.setContent(new BenzeneView(gamePane, benzene, scaleModel));
        gamePane.setId("editor");
        root.setCenter(gamePane);

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
