
package benzene.editor.utils;

import java.util.Set;
import java.util.HashSet;

import javafx.beans.Observable;
import javafx.beans.InvalidationListener;

public class Model implements Observable {

    private final Set<InvalidationListener> listeners = new HashSet<>();
    private boolean frozen = false;
    private boolean invalidated = false;

    @Override
    public void addListener(InvalidationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listeners.remove(listener);
    }

    public void freeze() {
        frozen = true;
    }

    public void invalidate() {
        invalidated = true;
        if(!frozen) {
            listeners.forEach(l -> l.invalidated(this));
            invalidated = false;
        }
    }

    public void unfreeze() {
        frozen = false;
        if(invalidated) invalidate();
    }

}

