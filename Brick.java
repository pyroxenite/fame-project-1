import java.awt.*;

public class Brick extends Rectangle {
    private int powerupType = Playground.PowerupType.NONE;

    public Brick(Vector pos) {
        super(pos, 40, 30);
    }

    public int getPowerupType() { return powerupType; }

    public void setPowerupType(int type) { powerupType = type; }
}