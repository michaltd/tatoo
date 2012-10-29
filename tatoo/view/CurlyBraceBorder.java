package tatoo.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;

import javax.swing.border.AbstractBorder;

public class CurlyBraceBorder extends AbstractBorder {

    /**
     * 
     */
    private static final long serialVersionUID = 4722014839583908976L;
    protected int             thickness;
    protected Color           lineColor;
    protected Position        position;
    protected Position        direction;

    public enum Position {
        LEFT,
        RIGHT
    }

    /**
     * Erzeugt eine geschweifte Klammer mit der übergebenen Farbe. Die Klammer
     * hat eine Breite von 10 Pixeln, wird auf der rechten Seite gezeichnet und
     * zeigt nach Rechts.
     * 
     * @param color
     * die Farbe für die Klammer
     */
    public CurlyBraceBorder (Color color) {
        this (color, 10);
    }

    /**
     * Erzeugt eine geschweifte Klammer mit der übergebenen Farbe und Breite.
     * Die Klammer wird auf der rechten Seite gezeichnet und zeigt nach Rechts.
     * 
     * @param color
     * die Farbe für die Klammer
     * @param thickness
     * die Breite der Klammer
     */
    public CurlyBraceBorder (Color color, int thickness) {
        this (color, thickness, Position.RIGHT);
    }

    /**
     * Erzeugt eine geschweifte Klammer mit der übergebenen Farbe, Breite und
     * Position. Die Klammer zeigt nach Rechts.
     * 
     * @param color
     * die Farbe für die Klammer
     * @param thickness
     * die Breite der Klammer
     * @param position
     * Die Position der Klammer
     */
    public CurlyBraceBorder (Color color, int thickness, Position position) {
        this (color, thickness, position, Position.RIGHT);
    }

    /**
     * Erzeugt eine geschweifte Klammer mit der übergebenen Farbe, Breite,
     * Position und Zeigerichtung.
     * 
     * @param color
     * die Farbe für die Klammer
     * @param thickness
     * die Breite der Klammer
     * @param position
     * Die Position der Klammer
     * @param die
     * Richtung in die die Klammer zeigt.
     */
    public CurlyBraceBorder (Color color, int thickness, Position position, Position direction) {
        lineColor = color;
        this.thickness = thickness;
        this.position = position;
        this.direction = direction;
    }

    /**
     * Malt die Klammer für die übergebene Komponente.
     * 
     * @param c
     * the component for which this border is being painted
     * @param g
     * the paint graphics
     * @param x
     * the x position of the painted border
     * @param y
     * the y position of the painted border
     * @param width
     * the width of the painted border
     * @param height
     * the height of the painted border
     */
    public void paintBorder (Component c, Graphics g, int x, int y, int width, int height) {
        if ((this.thickness > 0) && (g instanceof Graphics2D)) {
            Graphics2D g2d = (Graphics2D) g;

            Color oldColor = g2d.getColor ();
            g2d.setColor (this.lineColor);

            int startx;
            int starty;
            int startCPx;
            int startCPy;
            int spearheadx;
            int spearheady;
            int spearheadCPx;
            int spearheadCPy;
            int endx;
            int endy;
            int endCPx;
            int endCPy;

            int offset = thickness * 2;
            int spearheadOffset = height / 2;

            if (direction == Position.RIGHT) {
                startx = endx = x;
                starty = startCPy = y - 1;
                startCPx = endCPx = startx + offset;
                spearheadx = x + thickness;
                spearheady = spearheadCPy = y + spearheadOffset;
                endy = endCPy = y + height - 2;
                spearheadCPx = spearheadx - offset;
            }
            else if (direction == Position.LEFT) {
                startx = endx = x + thickness;
                starty = startCPy = y - 1;
                startCPx = endCPx = startx - offset;
                spearheadx = x;
                spearheady = spearheadCPy = spearheadOffset;
                endy = endCPy = y + height - 2;
                spearheadCPx = spearheadx + offset;
            }
            else {
                startx = startCPx = spearheadx = spearheadCPx = endx = endCPx = 0;
                starty = startCPy = spearheady = spearheadCPy = endy = endCPy = 0;
            }

            if (position == Position.RIGHT) {
                startx += width - thickness;
                startCPx += width - thickness;
                spearheadx += width - thickness;
                spearheadCPx += width - thickness;
                endx += width - thickness;
                endCPx += width - thickness;
            }

            CubicCurve2D curveTop = new CubicCurve2D.Double ();
            curveTop.setCurve (startx, starty, startCPx, startCPy, spearheadCPx, spearheadCPy, spearheadx, spearheady);

            CubicCurve2D curveBottom = new CubicCurve2D.Double ();
            curveBottom.setCurve (spearheadx, spearheady, spearheadCPx, spearheadCPy, endCPx, endCPy, endx, endy);

            Path2D path = new Path2D.Float (Path2D.WIND_EVEN_ODD);
            path.append (curveTop, false);
            path.append (curveBottom, false);
            g2d.draw (path);
            g2d.setColor (oldColor);
        }
    }

    /**
     * Returns the insets of the border.
     * 
     * @param c
     * the component for which this border insets value applies
     */
    public Insets getBorderInsets (Component c) {

        return new Insets (0, position == Position.LEFT ? thickness : 0, 0, position == Position.RIGHT ? thickness : 0);
    }

    /**
     * Reinitialize the insets parameter with this Border's current Insets.
     * 
     * @param c
     * the component for which this border insets value applies
     * @param insets
     * the object to be reinitialized
     */
    public Insets getBorderInsets (Component c, Insets insets) {
        insets.top = 0;
        insets.bottom = 0;
        insets.left = position == Position.LEFT ? thickness : 0;
        insets.right = position == Position.RIGHT ? thickness : 0;
        return insets;
    }

    /**
     * Returns the color of the border.
     */
    public Color getLineColor () {
        return lineColor;
    }

    /**
     * Returns the thickness of the border.
     */
    public int getThickness () {
        return thickness;
    }

}
