import java.awt.*;
import java.util.Random;

public class Powerup extends Circle {
    private int type;

    static final int EXTRA_BALL = 1;
    static final int EXTRA_BALLS = 2;
    static final int FREEZE_BRICKS = 3; // bricks stop coming down

    static Random rand = new Random();

    public Powerup(Vector pos, int type) {
        super(pos, new Vector(0, 1), 5);
        this.type = type;
    }

    public Powerup(Vector pos) {
        super(pos, new Vector(0, 1), 5);
        this.type = 1 + rand.nextInt(3);
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    @Override
    public void draw(Graphics g) {
        switch (type) {
            case EXTRA_BALL, EXTRA_BALLS:
                g.setColor(new Color(230, 230, 0)); break;
            case FREEZE_BRICKS:
                g.setColor(new Color(0, 200, 255)); break;
            case 4: // ??
                g.setColor(new Color(230, 100, 0)); break;
            default:
                return;
        }
        
        double radius = getRadius();        
        g.fillOval(
            (int) (getPos().getX() - radius), 
            (int) (getPos().getY() - radius), 
            (int) (2 * radius),
            (int) (2 * radius)
        );
    }

    public void activate(Playground p) {
        Ball newBall;
        switch (type) {
            case 1: // extra ball powerup
                newBall = new Ball(
                    p.getBalls().get(0).getPos().copy(),
                    p.getBalls().get(0).getVel().copy()
                ); 
                newBall.getVel().rotate(Math.PI / 6);
                p.getBalls().add(newBall);
                break;
            case 2: // extra balls powerup
                for (int i=0; i<3; i++) {
                    newBall = new Ball(
                        p.getBalls().get(0).getPos().copy(),
                        p.getBalls().get(0).getVel().copy()
                    ); 
                    newBall.getVel().rotate(Math.PI / 20 * (i+1));
                    p.getBalls().add(newBall);
                }
                break;
            case 3: // freeze powerup
                p.setFreezeFramesLeft(60 * 5); // 5 seconds
                break;
            default:
                return;
        }
    }
}
