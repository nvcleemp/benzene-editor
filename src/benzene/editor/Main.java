package benzene.editor;

import benzene.editor.gui.BenzeneView;
import benzene.editor.gui.NoneFocusScrollPane;
import benzene.editor.io.BenzeneLoader;
import benzene.editor.io.BenzeneSaver;
import benzene.editor.utils.BenzeneExporter;
import benzene.editor.utils.BenzeneValidator;
import benzene.editor.utils.IntegerModel;
import benzene.editor.utils.Location;

import java.io.File;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.FileChooser;

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
        
        BenzeneValidator validator = new BenzeneValidator(benzene);
        
        MenuBar menuBar = new MenuBar();
        
        Menu menuFile = new Menu("File");
        
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Benzenoid XML (*.xml)", "*.xml"),
                new FileChooser.ExtensionFilter("All files", "*")
        );
        
        MenuItem load = new MenuItem("Load...");
        BenzeneLoader loader = new BenzeneLoader(benzene);
        load.setOnAction((ActionEvent t) -> {
            File f = chooser.showOpenDialog(null);
            if(f!=null)
                if(!loader.load(f))
                    new Alert(Alert.AlertType.ERROR, "Unable to load benzenoid", 
                            ButtonType.OK).show();
        });
        
        MenuItem save = new MenuItem("Save as...");
        BenzeneSaver saver = new BenzeneSaver(benzene);
        save.setOnAction((ActionEvent t) -> {
            File f = chooser.showSaveDialog(null);
            if(f!=null)
                if(!saver.save(f))
                    new Alert(Alert.AlertType.ERROR, "Error while saving benzenoid", 
                            ButtonType.OK).show();
        });

        Menu menuExport = new Menu("Export");
        BenzeneExporter exporter = new BenzeneExporter(benzene);
        
        MenuItem exportSage = new MenuItem("Export Sage");
        exportSage.setOnAction((ActionEvent t) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(exporter.exportSage());
            Clipboard.getSystemClipboard().setContent(content);
            new Alert(Alert.AlertType.INFORMATION, "Code copied to clipboard", ButtonType.OK).show();
        });
        
        MenuItem exportTikz = new MenuItem("Export Tikz");
        exportTikz.setOnAction((ActionEvent t) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(exporter.exportTikZ());
            Clipboard.getSystemClipboard().setContent(content);
            new Alert(Alert.AlertType.INFORMATION, "Code copied to clipboard", ButtonType.OK).show();
        });
        
        MenuItem exportLaTeX = new MenuItem("Export LaTeX");
        exportLaTeX.setOnAction((ActionEvent t) -> {
            ClipboardContent content = new ClipboardContent();
            content.putString(exporter.exportLaTeX());
            Clipboard.getSystemClipboard().setContent(content);
            new Alert(Alert.AlertType.INFORMATION, "Code copied to clipboard", ButtonType.OK).show();
        });
        
        MenuItem exportPng = new MenuItem("Export PNG");
        exportPng.setOnAction((ActionEvent t) -> {
            exporter.exportPng();
        });
        
        menuExport.getItems().addAll(exportSage, exportTikz, exportLaTeX, exportPng);
        
        menuFile.getItems().addAll(load, save, new SeparatorMenuItem(), menuExport);
        
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
        
        MenuItem shiftRight = new MenuItem("Shift Right");
        shiftRight.setOnAction((ActionEvent t) -> {
            benzene.shift(new Location(0, 1));
        });
        
        MenuItem shiftLeft = new MenuItem("Shift Left");
        shiftLeft.setOnAction((ActionEvent t) -> {
            benzene.shift(new Location(0, -1));
        });
        
        MenuItem shiftUp = new MenuItem("Shift Up");
        shiftUp.setOnAction((ActionEvent t) -> {
            benzene.shift(new Location(-2, 1));
        });
        
        MenuItem shiftDown = new MenuItem("Shift Down");
        shiftDown.setOnAction((ActionEvent t) -> {
            benzene.shift(new Location(2, -1));
        });
        
        Menu validationMenu = new Menu("Validation");
        
        CheckMenuItem catacondensed = new CheckMenuItem("Catacondensed");
        catacondensed.selectedProperty().bindBidirectional(validator.getCatacondensed());
        
        validationMenu.getItems().addAll(catacondensed);
        
        menuEdit.getItems().addAll(clear, new SeparatorMenuItem(), rotateClockwise,
                rotateCounterclockwise, new SeparatorMenuItem(), shiftRight, 
                shiftLeft, shiftUp, shiftDown, new SeparatorMenuItem(), validationMenu);
 
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
        
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
        
        root.setTop(menuBar);

        NoneFocusScrollPane gamePane = new NoneFocusScrollPane();
        gamePane.setContent(new BenzeneView(gamePane, benzene, scaleModel));
        gamePane.setId("editor");
        root.setCenter(gamePane);
        
        benzene.addListener(o -> {
            if(validator.isValid()) {
                gamePane.getStyleClass().removeAll("error");
            } else {
                gamePane.getStyleClass().add("error");
            }
        });
        catacondensed.selectedProperty().addListener((obs, o, n) -> {
            if(validator.isValid()) {
                gamePane.getStyleClass().removeAll("error");
            } else {
                gamePane.getStyleClass().add("error");
            }
        });

        return root;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
