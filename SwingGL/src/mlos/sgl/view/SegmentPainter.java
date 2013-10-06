package mlos.sgl.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.core.Segment;

public class SegmentPainter implements ObjectPainter {

    private final CanvasSegment segment;
    
    public SegmentPainter(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        Segment seg = segment.getSegment();
        Vec2d a = panel.toScreen(seg.a);
        Vec2d b = panel.toScreen(seg.b);
        Color color = getColor();
        int thickness = segment.getThickness();
        
        ctx.setColor(color);
        Stroke stroke = new BasicStroke(thickness);
        ctx.setStroke(stroke);
        ctx.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }
    
    private Color getColor() {
        if (segment.isHover()) {
            return segment.getHoverColor();
        } else {
            return segment.getColor();
        }
    }

    @Override
    public CanvasSegment getObject() {
        return segment;
    }

}