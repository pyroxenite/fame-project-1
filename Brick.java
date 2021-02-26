import java.awt.*;

public class Brick extends Rectangle {
    private int powerupType = Powerup.NONE;
    private double speed = 0.05;

    public Brick(Vector pos) {
        super(pos, 40, 20);
    }

    public int getPowerupType() { return powerupType; }
    public void setPowerupType(int type) { powerupType = type; }

    public void setSpeed(double speed) { this.speed = speed; }

    @Override
    public void update() {
        this.getPos().add(new Vector(0, speed));
    }
}