package mlos.sgl.canvas;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;

import mlos.sgl.core.Segment;

public class CanvasSegment extends CanvasObject {
    
    public static final Color DEFAULT_COLOR = Color.black;
    
    public static final Color DEFAULT_HOVER_COLOR = Color.blue;
    
    public static final Color DEFAULT_SELECTED_COLOR = Color.green;
    
    
    public static final int DEFAULT_THICKNESS = 2;
    
    private Segment segment;
    
    private Color color = DEFAULT_COLOR;
    
    private Color hoverColor = DEFAULT_HOVER_COLOR;
    
    private Color selectedColor = DEFAULT_SELECTED_COLOR;

    private int thickness = DEFAULT_THICKNESS;

    public CanvasSegment(Segment segment) {
        this.segment = checkNotNull(segment);
    }
    
    public Segment getSegment() {
        return segment;
    }
    
    public void setSegment(Segment segment) {
        this.segment = checkNotNull(segment);
        notifyListeners();
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = checkNotNull(color);
        notifyListeners();
    }
    
    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = checkNotNull(hoverColor);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = checkNotNull(selectedColor);
    }

    public int getThickness() {
        return thickness;
    }
    
    public void setThickness(int thickness) {
        checkArgument(thickness > 0, "Line thickness must be positive");
        this.thickness = thickness;
        notifyListeners();
    }

    @Override
    public void accept(CanvasVisitor visitor) {
        visitor.visit(this);
    }

}
