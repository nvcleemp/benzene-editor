package benzene.editor.utils;

import benzene.editor.Benzene;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;

/**
 * Objects of this class can be used to check whether the associated benzenoid
 * is valid.
 * 
 * @author nvcleemp
 */
public class BenzeneValidator {
    
    private final Benzene benzene;
    
    private final BooleanProperty catacondensed = new BooleanPropertyBase(false){
        
        @Override
        public Object getBean() {
            return BenzeneValidator.this;
        }

        @Override
        public String getName() {
            return "catacondensed";
        }
    };

    public BenzeneValidator(Benzene benzene) {
        this.benzene = benzene;
    }
    
    public boolean isValid(){
        if(!isConnected()){
            return false;
        }
        if(containsHoles()){
            return false;
        }
        if(catacondensed.getValue() && !isCatacondensed()){
            return false;
        }
        return true;
    }
    
    private boolean isConnected(){
        Location startHex = benzene.locations().findAny().orElse(null);
        if(startHex==null){
            return true;
        }
        
        Set<Location> hexes = benzene.locations().collect(Collectors.toSet());
        Set<Location> visited = new HashSet<>();
        RecursiveConsumer<Location> recursiveVisit = new RecursiveConsumer<>();
        Consumer<Location> visit = l -> {
            if(!visited.contains(l)){
                visited.add(l);
                for(Orientation orientation: Orientation.values()) {
                    Location neighbour = orientation.applyOffset(l);
                    if(hexes.contains(neighbour)){
                        recursiveVisit.consumer.accept(neighbour);
                    }
                }
            }
        };
        recursiveVisit.consumer = visit;
        visit.accept(startHex);
        
        return visited.size() == hexes.size();
    }
    
    private boolean containsHoles(){
        Set<Location> hexes = benzene.locations().collect(Collectors.toSet());
        Set<Location> neighbouringHexes = benzene.locations()
                .flatMap(l -> 
                        Stream.of(Orientation.values()).map(o -> o.applyOffset(l)))
                .filter(l -> !hexes.contains(l))
                .collect(Collectors.toSet());
        
        Location startHex = neighbouringHexes.stream().findAny().orElse(null);
        if(startHex==null){
            return false;
        }
        Set<Location> visited = new HashSet<>();
        RecursiveConsumer<Location> recursiveVisit = new RecursiveConsumer<>();
        Consumer<Location> visit = l -> {
            if(!visited.contains(l)){
                visited.add(l);
                for(Orientation orientation: Orientation.values()) {
                    Location neighbour = orientation.applyOffset(l);
                    if(neighbouringHexes.contains(neighbour)){
                        recursiveVisit.consumer.accept(neighbour);
                    }
                }
            }
        };
        recursiveVisit.consumer = visit;
        visit.accept(startHex);
        
        return visited.size() != neighbouringHexes.size();
    }
    
    /*
     * A valid benzenoid is catacondensed if no hexagon has a neighbour on two
     * consecutive edges
     */
    private boolean isCatacondensed(){
        Set<Location> hexes = benzene.locations().collect(Collectors.toSet());
        Predicate<Location> hexagonValidator = l -> {
            List<Boolean> isNeighbour = 
                    Stream.of(Orientation.values())
                            .map(o -> hexes.contains(o.applyOffset(l)))
                            .collect(Collectors.toList());
            
            return !IntStream.range(0, 6).anyMatch(i -> isNeighbour.get(i) && isNeighbour.get((i+1)%6));
        };
        
        return benzene.locations().allMatch(hexagonValidator);
    }
    
    public BooleanProperty getCatacondensed(){
        return catacondensed;
    }
    
    private static class RecursiveConsumer<T> {
        public Consumer<T> consumer;
    }
}
