import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Ball extends Sprite {
    private double radius = 10;
    private Color color = Color.red;

    public Ball(Vector pos, Vector vel) {
        super(pos, vel);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() { return radius; }

    public void setColor(Color color) {
        this.color = color;
    }

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
}
