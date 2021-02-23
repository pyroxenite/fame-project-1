import java.awt.*;

public class Ball extends Sprite {
    private Vector vel;
    private double radius = 10;
    private Color color = Color.red;

    public Ball(Vector pos, Vector vel) {
        super(pos);
        this.vel = vel;
    }

    public Vector getVel() { return vel; }

    public void setVel(Vector vel) { this.vel = vel; }

    public double getRadius() { return radius; }

    public void setRadius(double radius) { this.radius = radius; }

    public Color getColor() { return color; }

    public void setColor(Color color) { this.color = color; }

    public void update() {
        getPos().add(getVel());
    }

    public void draw(Graphics g) {
        Vector pos = this.getPos();
        g.setColor(color);
        g.fillOval(
            (int) (pos.getX() - radius), 
            (int) (pos.getY() - radius), 
            (int) (2 * radius),
            (int) (2 * radius)
        );
    }

    public static void main(String[] args) {
        Vector pos = new Vector(30, 30);
        Vector vel = new Vector(2, 2);
        Ball b = new Ball(pos, vel);
        System.out.println(b);
        b.update();
        System.out.println(b);
    }
}
