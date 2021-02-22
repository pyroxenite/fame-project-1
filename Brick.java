import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Brick extends Rectangle {
    private double width = 100;
    private Color color = Color.black;
    // private int powerupId = -1;

    public Brick(Vector pos) {
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