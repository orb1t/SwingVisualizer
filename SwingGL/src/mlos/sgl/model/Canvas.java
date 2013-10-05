package mlos.sgl.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Canvas {
    
    private final Set<CanvasObject> objects = new HashSet<>();

    private final Set<CanvasListener> listeners = new CopyOnWriteArraySet<>();
    
    public void add(CanvasObject object) {
        objects.add(object);
        for (CanvasListener listener : listeners) {
            listener.objectAdded(object);
        }
    }
    
    public void remove(CanvasObject object) {
        objects.remove(object);
        signalRemoval(object);
    }

    private void signalRemoval(CanvasObject object) {
        for (CanvasListener listener : listeners) {
            listener.objectRemoved(object);
        }
    }
    
    public void clear() {
        for (CanvasObject object : objects) {
            signalRemoval(object);
        }
        objects.clear();
    }
    
    public void addAll(Iterable<? extends CanvasObject> iterable) {
        for (CanvasObject object : iterable) {
            add(object);
        }
    }
    
    public Set<CanvasObject> getObjects() {
        return Collections.unmodifiableSet(objects);
    }
    
    public void addListener(CanvasListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(CanvasListener listener) {
        listeners.remove(listener);
    }
    
}
