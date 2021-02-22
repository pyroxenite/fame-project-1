import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Rectangle extends Sprite {
    private double width = 100;
    private double height = 100;
    private Color color = Color.black;

    public Rectangle(Vector pos) {
        super(pos, new Vector(0, 0));
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWidth() {return width;}

    public double getHeight() {return height;}

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(Graphics g) {
        Vector pos = this.getPos();
        g.setColor(color);
        g.fillRect(
            (int)(pos.getX() - width/2),
            (int)(pos.getY() - height/2),
            (int)width,
            (int)height
        );
    }
}

