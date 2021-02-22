import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Paddle extends Rectangle {
    private double width = 100;
    private Color color = Color.black;

    public Paddle(Vector pos) {
        super(pos);
        Vector vel = new Vector(0, 0);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void update() {
        // we'll do this later
    }
}

