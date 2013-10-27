package mlos.sgl.demo.monotonic;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import mlos.sgl.App;
import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPolygon;
import mlos.sgl.core.Polygon;
import mlos.sgl.ui.InputAdapter;

public class MonotonicPolygons extends Scene {

    private final Executor exec = Executors.newSingleThreadExecutor();

    private final class ClassificationProcess implements Runnable {
        
        private final Polygon poly;

        public ClassificationProcess(Polygon poly) {
            this.poly = poly;
        }

        @Override
        public void run() {
            MonotonicPolygons scene = MonotonicPolygons.this;
            ClassifyVertices classifier = new ClassifyVertices(poly.vs);
            classifier.setListener(new ClassificationVisualizer(scene));
            classifier.classify();
        }
        
    }
    
    private final class TriangulationProcessJ implements Runnable {

        private final Polygon poly;

        public TriangulationProcessJ(Polygon poly) {
            this.poly = poly;
        }

        @Override
        public void run() {
            MonotonicPolygons scene = MonotonicPolygons.this;
            TriangulateJ triangulate = new TriangulateJ(poly);
            triangulate.setListener(new TriangulationVisualizer(scene));
            triangulate.run();
        }
    }
    
    private final class TriangulationProcess implements Runnable {

        private final Polygon poly;

        public TriangulationProcess(Polygon poly) {
            this.poly = poly;
        }

        @Override
        public void run() {
            MonotonicPolygons scene = MonotonicPolygons.this;
            Triangulate triangulate = new Triangulate(poly, 
                    new TriangulationVisualizer(scene));
            triangulate.run();
        }
    }

    public MonotonicPolygons() {
        super("triangulation");

        handlerStack.pushBack(new InputAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_F5) {
                    for (Polygon poly : extractPolys()) {
                        exec.execute(new ClassificationProcess(poly));
                    }
                } else if (c == KeyEvent.VK_F6) {
                    for (Polygon poly : extractPolys()) {
                        exec.execute(new TriangulationProcess(poly));
                    }
                } else if (c == KeyEvent.VK_F7) {
                    for (Polygon poly : extractPolys()) {
                        exec.execute(new TriangulationProcessJ(poly));
                    }
                }
            }
        });
    }

    private List<Polygon> extractPolys() {
        List<Polygon> polys = new ArrayList<>();

        for (CanvasObject object : canvas.getObjects()) {
            if (object instanceof CanvasPolygon) {
                CanvasPolygon poly = (CanvasPolygon) object;
                polys.add(new Polygon(poly.getPoints()));
            }
        }
        return polys;
    }

    public static void main(String[] args) {
        App.create(new MonotonicPolygons());
    }

}
