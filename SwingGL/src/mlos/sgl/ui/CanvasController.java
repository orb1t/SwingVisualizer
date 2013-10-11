package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.neg;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasView;

public class CanvasController implements CanvasListener {

    public static final int DEFAULT_TRESHOLD = 5;

    private final class MotionListener implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            update(e);
            onMouseHover(getScreenPos(e));
            prevPos = getScreenPos(e);
        }

        private void update(MouseEvent e) {
            properties.put("cursor", getScreenPos(e));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            update(e);
            Vec2d screenPos = getScreenPos(e);
            if (captured == null) {
                Vec2d prevPlanePos = view.planeToScreen().invert(prevPos);
                Vec2d planePos = getPlanePos(e);
                Vec2d d = diff(planePos, prevPlanePos);
                view.prepend(Transforms.t(d));
            }
            prevPos = screenPos;
        }
    }
    
    private final class ButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            if (e.getButton() == MouseEvent.BUTTON3) {
                CanvasObject hit = findHit(screenPos);
                if (hit != null) {
                    hit.setSelected(true);
                    view.refresh();
                }
            }
            prevPos = screenPos;
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            properties.remove("cursor");
        }
        
    }
    
    private final class WheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Vec2d planePos = getPlanePos(e);
            
            double f = e.getPreciseWheelRotation();
            double scale = Math.pow(0.95, f);
            
            Transform scaling = new Transform.Builder()
                .t(neg(planePos))
                .s(scale)
                .t(planePos)
                .create();
            view.prepend(scaling);
        }
        
    }
    
    private Vec2d getScreenPos(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }
    
    private Vec2d getPlanePos(MouseEvent e) {
        Vec2d screenPos = getScreenPos(e);
        return view.planeToScreen().invert(screenPos);
    }
    
    private final CanvasView view;
    
    
    private final PropertyMap properties;

    private ObjectControllerFactory geometryFactory;

    private final Map<CanvasObject, ObjectController> geometryMap = new HashMap<>();
    
    
    private Vec2d prevPos;
    
    private CanvasObject captured;

    public CanvasController(CanvasView view, PropertyMap properties, 
            ObjectControllerFactory geometryFactory) {
        this.view = checkNotNull(view);
        this.properties = checkNotNull(properties);
        this.geometryFactory = checkNotNull(geometryFactory);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectController geometry = geometryFactory.createController(object);
        geometryMap.put(object, geometry);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        geometryMap.remove(object);
    }
    
    
    private void onMouseHover(Vec2d p) {
        for (CanvasObject object : geometryMap.keySet()) {
            object.setHover(false);
        }
        CanvasObject hit = findHit(p);
        hit.setHover(true);
    }
    
    public CanvasObject findHit(Vec2d p) {
        Transform normToScreen = view.normToScreen();
        Transform planeToNorm = view.planeToNorm();
        Transform planeToScreen = Transforms.compose(planeToNorm, normToScreen);
        
        CanvasObject closest = null;
        double minDist = DEFAULT_TRESHOLD;
        for (ObjectController geometry : geometryMap.values()) {
            double d = geometry.distance(p, planeToScreen);
            if (d < minDist) {
                closest = geometry.getObject();
                minDist = d;
            }
        }
        return closest;
    }
    
    
    
    public MouseMotionListener getMouseMotionListener() {
        return new MotionListener();
    }
    
    public MouseListener getMouseListener() {
        return new ButtonListener();
    }
    
    public MouseWheelListener getMouseWheelListener() {
        return new WheelListener();
    }
    
    public KeyListener getKeyListener() {
        return null;
    }

}
