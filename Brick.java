import java.util.*;
import java.awt.*;

/**
 * A Rectangle subclass that represents a breakable brick.
 */
public class Brick extends Rectangle {
    private Powerup powerup = null;
    private double speed = 0.05;
    private static Random rand = new Random();

    public Brick(Vector pos) {
        super(pos, 40, 20);
        int val = rand.nextInt(100);
        setColor(new Color(val, val, val));
    }

    public Brick(Vector pos, double width) {
        super(pos, width, 20);
        int val = rand.nextInt(100);
        setColor(new Color(val, val, val));
    }

    public void addRandomPowerup() {
        powerup = new Powerup(new Vector());
    }

    public void releasePowerup(ArrayList<Powerup> powerups) {
        if (powerup != null) {
            powerup.setPos(new Vector(
                this.getPos().getX(), 
                this.getPos().getY()
            ));
            powerups.add(powerup);
        }
    }

    public void setSpeed(double speed) { this.speed = speed; }

    @Override
    public void update() {
        this.getPos().add(new Vector(0, speed));
    }
}