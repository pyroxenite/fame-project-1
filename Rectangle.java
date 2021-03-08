import java.awt.*;

/**
 * A Sprite implementation that represents a rectangle.
 */
public class Rectangle implements Sprite {
    private Vector pos;
    private double width = 100;
    private double height = 100;
    private Color color = Color.black;

    public Rectangle(Vector pos, double width, double height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
    }

    public Vector getPos() { return pos; }
    public void setPos(Vector pos) { this.pos = pos; }

    public double getWidth() { return width; }
    public void setWidth(double width) { this.width = width; }

    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }

    public Color getColor() { return this.color; }
    public void setColor(Color color) { this.color = color; }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(
            (int) (pos.getX() - width/2),
            (int) (pos.getY() - height/2),
            (int) width,
            (int) height
        );
    }

    public void update() {
        // override in subclasses if needed
    }
}