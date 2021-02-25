import java.awt.*;

public class Powerup extends Circle {
    private int type;

    static final int NONE = 0;
    static final int EXTRA_BALL = 1;
    static final int FREEZE_BRICKS = 2; // bricks stop coming down
    static final int BLAZING_BALL = 3; // makes balls go through bricks without colliding?

    public Powerup(Vector pos, int type) {
        super(pos, new Vector(0, 1), 5);
        this.type = type;
    }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }

    @Override
    public void draw(Graphics g) {
        if (type == 0) return;

        switch (type) {
            case 1: // extra ball powerup
                g.setColor(new Color(230, 230, 0)); break;
            case 2: // freeze powerup
                g.setColor(new Color(0, 200, 255)); break;
            case 3: // blaze powerup
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
        System.out.println("Powerup type: " + type);
        switch (type) {
            case 1: // extra ball powerup
                // Ball newBall = new Ball(
                //     p.getBalls().get(0).getPos().copy(),
                //     p.getBalls().get(0).getVel().copy()
                // ); 
                Ball newBall = new Ball(
                    new Vector(200, 200),
                    new Vector(1, -1)
                ); 
                //newBall.getVel().rotate(Math.PI / 6);
                p.getBalls().add(newBall);
                break;
            case 2: // freeze powerup
                break;
            case 3: // blaze powerup
                break;
            default:
                return;
        }
    }
}
