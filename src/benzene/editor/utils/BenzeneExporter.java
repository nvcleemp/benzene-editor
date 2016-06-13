package benzene.editor.utils;

import benzene.editor.Benzene;
import benzene.editor.gui.BenzeneView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 *
 * @author nvcleemp
 */
public class BenzeneExporter {
    
    private static Collection<Location> vertices(Location location){
        int c = 2*location.col + location.row;
        int r = 2*location.row;
        return Arrays.asList(
                new Location(r, c - 1),
                new Location(r + 1, c - 1),
                new Location(r + 2, c),
                new Location(r + 1, c + 1),
                new Location(r, c + 1),
                new Location(r - 1, c)
                );
    }
    
    private static Collection<Pair<Location, Location>> edges(Location location){
        int c = 2*location.col + location.row;
        int r = 2*location.row;
        Location[] ls = {
                new Location(r, c - 1),
                new Location(r + 1, c - 1),
                new Location(r + 2, c),
                new Location(r + 1, c + 1),
                new Location(r, c + 1),
                new Location(r - 1, c)
        };
        return Arrays.asList(
                Pair.of(ls[0], ls[1]),
                Pair.of(ls[1], ls[2]),
                Pair.of(ls[2], ls[3]),
                Pair.of(ls[4], ls[3]),
                Pair.of(ls[5], ls[4]),
                Pair.of(ls[0], ls[5])
        );
    }
    
    public static String exportSage(Benzene benzene){
        Set<Location> vertices = new HashSet<>();
        benzene.locations().forEach(l -> vertices.addAll(vertices(l)));
        List<Location> verticesL = new ArrayList<>(vertices);
        Map<Location, Integer> vertex2index = new HashMap<>();
        for (int i = 0; i < verticesL.size(); i++) {
            vertex2index.put(verticesL.get(i), i);
        }
        Set<Pair<Location, Location>> edges = new HashSet<>();
        benzene.locations().forEach(l -> edges.addAll(edges(l)));
        
        List<String> instructions = new ArrayList<>();
        instructions.add(String.format("g = Graph(%d)", vertices.size()));
        instructions.add(String.format("g.add_edges([%s])",
                String.join(", ", 
                        edges.stream()
                            .map(e -> String.format("(%d,%d)",
                                    vertex2index.get(e.first), vertex2index.get(e.second)))
                            .collect(Collectors.toCollection(ArrayList::new)))));
        
        return String.join("\n", instructions);
    }
    
    public static String exportTikZ(Benzene benzene){
        Set<Location> vertices = new HashSet<>();
        benzene.locations().forEach(l -> vertices.addAll(vertices(l)));
        Set<Pair<Location, Location>> edges = new HashSet<>();
        benzene.locations().forEach(l -> edges.addAll(edges(l)));
        
        List<String> instructions = new ArrayList<>();
        instructions.add("\\begin{tikzpicture}[every node/.style={circle, fill, inner sep=1pt},every edge/.style={draw}]");
        instructions.addAll(vertices.stream()
                .map(v -> String.format("    \\node (%d_%d) at (%f,%f) {};", 
                        v.col, v.row, 
                        v.col*Math.sqrt(3)/2, 
                        v.row % 2 == 0 ? -1.5*v.row/2 : -1.5*(v.row - 1)/2 - 1))
                .collect(Collectors.toCollection(ArrayList::new)));
        instructions.addAll(edges.stream()
                .map(e -> String.format("    \\path (%d_%d) edge (%d_%d);",
                        e.first.col, e.first.row, e.second.col, e.second.row))
                .collect(Collectors.toCollection(ArrayList::new)));
        instructions.add("\\end{tikzpicture}");
        
        return String.join("\n", instructions);
    }
    
    public static String exportLaTeX(Benzene benzene){
        return "\\documentclass[tikz]{standalone}\n\\begin{document}\n\n" + 
                exportTikZ(benzene) + "\n\n\\end{document}";
    }
    
    private static final FileChooser fileChooser = new FileChooser();
    
    public static void exportPng(Benzene benzene){
        BenzeneView view = new BenzeneView(benzene);
        view.invalidated(benzene);
        Image img = view.snapshot(new SnapshotParameters(), null);
        
        fileChooser.setTitle("Save PNG image");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG image", "*.png"));
        File f = fileChooser.showSaveDialog(null);
        
        if(f != null){
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", f);
            } catch (IOException ex) {
                Logger.getLogger(BenzeneExporter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        benzene.removeListener(view);
    }
}
