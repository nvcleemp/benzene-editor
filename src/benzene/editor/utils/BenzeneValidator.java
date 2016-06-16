package benzene.editor.utils;

import benzene.editor.Benzene;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Objects of this class can be used to check whether the associated benzenoid
 * is valid.
 * 
 * @author nvcleemp
 */
public class BenzeneValidator {
    
    private final Benzene benzene;

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
        return false;
    }
    
    private static class RecursiveConsumer<T> {
        public Consumer<T> consumer;
    }
}
