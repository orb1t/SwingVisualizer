package mlos.sgl.view;

import java.awt.Graphics2D;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPolygon;
import mlos.sgl.core.Transform;

public class PolygonPainter implements ObjectPainter {

    private final CanvasPolygon polygon;

    public PolygonPainter(CanvasPolygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public void paint(Transform toScreen, Graphics2D ctx) {
        Drawer d = new Drawer(ctx, toScreen).solid(polygon.getThickness());
        if (polygon.isOpaque()) {
            d.color(polygon.getFillColor())
                .fillPolygon(polygon.getPoints());
        }
        d.color(polygon.getBorderColor()).polygon(polygon.getPoints());
        d.restore();
    }

    @Override
    public CanvasObject getObject() {
        return polygon;
    }

}
