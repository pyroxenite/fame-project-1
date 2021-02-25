import java.awt.*;

public class Circle implements Sprite {
    private Vector pos;
    private Vector vel;
    private double radius;
    private Color color;

    public Circle(Vector pos, Vector vel, double radius) {
        this.pos = pos;
        this.vel = vel;
        this.radius = radius;
    }

    public Vector getPos() { return pos; }
    public void setPos(Vector pos) { this.pos = pos; }

    public Vector getVel() { return vel; }
    public void setVel(Vector vel) { this.vel = vel; }

    public double getRadius() { return radius; }
    public void setRadius(double radius) { this.radius = radius; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public void update() {
        pos.add(vel);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(
            (int) (pos.getX() - radius), 
            (int) (pos.getY() - radius), 
            (int) (2 * radius),
            (int) (2 * radius)
        );
    }
}
