package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import mlos.sgl.core.Point;

public class CanvasPanel {

    /** Virtual width of the canvas */
    private double width;

    /** Virtual height of the canvas */
    private double height;

    /** Painter drawing content */
    private Painter painter;

    private final class SwingPanelAdapter extends JPanel {
        @Override
        protected final void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            painter.paint(CanvasPanel.this, graphics);
        }
    }

    private final JPanel swingPanel = new SwingPanelAdapter();

    /**
     * Creates new {@code 1 x 1} canvas.
     * 
     * @param painter
     *            Painter used to draw canvas content
     */
    public CanvasPanel(Painter painter) {
        this(painter, 1, 1);
    }

    /**
     * Creates new {@code width x height} canvas.
     * 
     * @param painter
     *            Painter used to draw canvas content
     * @param width
     *            Width of the canvas
     * @param height
     *            Height of the canvas
     */
    public CanvasPanel(Painter painter, double width, double height) {
        this.painter = painter;
        this.width = width;
        this.height = height;
        
        swingPanel.setBackground(Color.white);
    }

    public final JPanel swingPanel() {
        return swingPanel;
    }

    /**
     * Causes canvas repaint.
     */
    public void refresh() {
        swingPanel.repaint();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        doSetWidth(width);
        refresh();
    }

    private void doSetWidth(double width) {
        checkArgument(width > 0, "Width %s must be positive", width);
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        doSetHeight(height);
        refresh();
    }

    private void doSetHeight(double height) {
        checkArgument(height > 0, "Height %s must be positive", height);
        this.height = height;
    }

    public void setSize(double width, double height) {
        doSetWidth(width);
        doSetHeight(height);
        refresh();
    }

    protected int getScreenWidth() {
        return swingPanel.getWidth();
    }

    protected int getScreenHeight() {
        return swingPanel.getHeight();
    }

    /**
     * Transforms the virtual coordinates to the screen coordinates.
     * 
     * @param p
     *            ScreenPoint in virtual coordinates
     * @return ScreenPoint in screen coordinates
     */
    public ScreenPoint toScreen(Point p) {
        return toScreen(p.x, p.y);
    }

    /**
     * Transforms the virtual coordinates to screen coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return ScreenPoint in screen coordinates
     */
    public ScreenPoint toScreen(double x, double y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        double invy = height - y;
        int screenx = (int) (x * sw / width);
        int screeny = (int) (invy * sh / height);
        return new ScreenPoint(screenx, screeny);
    }

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return ScreenPoint in virtual coordinates
     */
    public Point toVirtual(int x, int y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        int invy = sh - y;
        double vx = width * x / sw;
        double vy = height * invy / sh;
        return new Point(vx, vy);
    }

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param p
     *            ScreenPoint in screen coordinates
     * @return ScreenPoint in virtual coordinates
     */
    public Point toVirtual(ScreenPoint p) {
        return toVirtual(p.x, p.y);
    }

}