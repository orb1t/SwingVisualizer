package mlos.sgl.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Vec2d;

public class PointView implements ObjectView {
    
    private final CanvasPoint point;

    public PointView(CanvasPoint point) {
        this.point = point;
    }
    
    public Color getColor() {
        if (point.isSelected()) {
            return point.getSelectedColor();
        } else if (point.isHover()) {
            return point.getHoverColor();
        } else {
            return point.getColor();
        }
    }
    
    public Color getBorderColor() {
        return point.getBorderColor();
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        Vec2d p = point.getPoint();
        int size = point.getSize();
        Color color = getColor();

        Vec2d s = panel.toScreen(p);
        int hsize = size / 2;
        ctx.setColor(color);
        int left = (int) s.x - hsize;
        int top = (int) s.y - hsize;
        ctx.fillOval(left, top, size, size);

        ctx.setColor(getBorderColor());
        Stroke stroke = new BasicStroke(point.getBorderSize());
        ctx.setStroke(stroke);
        ctx.drawOval(left, top, size, size);
    }

    @Override
    public CanvasPoint getObject() {
        return point;
    }

}
