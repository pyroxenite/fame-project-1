import javax.swing.*;
import java.lang.Math.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class Playground extends JPanel {
    private static Random rand = new Random();

    private transient ArrayList<Ball> balls = new ArrayList<>();
    private transient ArrayList<Brick> bricks = new ArrayList<>();
    private transient Paddle playerPaddle;

    class PowerupType {
        static final int NONE = 0;
        static final int EXTRA_BALL = 1;
        static final int FREEZE_BRICKS = 2; // bricks stop coming down
        static final int BLAZING_BALL = 3; // makes balls go through bricks without colliding?
    }

    public Playground() {
        super();

        Vector pos = new Vector(200, 365);
        Vector vel = new Vector(
            (rand.nextDouble() - 0.5) * 4,
            -5
        );
        balls.add(new Ball(pos, vel));

        for (int i = 0; i < 10; i++) {
            bricks.add(new Brick(new Vector(i * 40, 50)));    
        }

        playerPaddle = new Paddle(new Vector(0, 375));
        playerPaddle.setColor(new Color(150, 150, 150));

        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                playerPaddle.getPos().setX(e.getX());
            }
            
            public void mouseDragged(MouseEvent e) {
                // do something else? could be a different mechanic
            }
        });
    }

    /**
     * Draws the Playground object on screen.
     * @param g current drawing context
     */
    @Override
    public void paintComponent(Graphics g) {
        // turn on antialising
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        ));

        // clear panel
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, 400, 400);

        for (Brick b : bricks) {
            b.draw(g);
        }

        for (Ball b : balls) {
            b.draw(g);
        }

        playerPaddle.draw(g);

        this.repaint();
    }
    
    /**
     * Moves all sprites according to their speed.
     */
    public void animate() {
        for (Ball b : balls) {
            b.update();
            b.getVel().setMag(5);
        }

        for (Brick b : bricks) {
            b.update();
        }
    }

    private boolean intersects(Ball ball, Rectangle rect) {
        Vector cDist = new Vector();
        Vector cPos = ball.getPos();
        Vector rPos = rect.getPos();

        cDist.setX(Math.abs(cPos.getX() - rPos.getX()));
        cDist.setY(Math.abs(cPos.getY() - rPos.getY()));
    
        if (cDist.getX() > (rect.getWidth()/2 + ball.getRadius())) { return false; }
        if (cDist.getY() > (rect.getHeight()/2 + ball.getRadius())) { return false; }
    
        if (cDist.getX() <= (rect.getWidth()/2)) { return true; } 
        if (cDist.getY() <= (rect.getHeight()/2)) { return true; }
    
        double cornerDistance_sq = (int)(cDist.getX() - rect.getWidth()/2)^2 +
                                   (int)(cDist.getY() - rect.getHeight()/2)^2;
    
        return (cornerDistance_sq <= ((int)ball.getRadius() ^ 2));
    }
        
    /**
     * Checks for collisions with walls and bounces balls.
     */
    public void doPhysics() {
        for (Ball b : balls) {
            Vector pos = b.getPos();
            Vector vel = b.getVel();
            double r = b.getRadius();

            if (pos.getX() < r) {
                pos.setX(r);
                vel.scaleX(-1);
            } else if (pos.getX() > 400 - r) {
                pos.setX(400.0 - r);
                vel.scaleX(-1);
            }
            if (pos.getY() < r) {
                pos.setY(r);
                vel.scaleY(-1);
            } else if (pos.getY() > 400 + r) {
                // end game (or decrement lives)
            }

            ArrayList<Brick> garbage = new ArrayList<>();
            for (Brick brick : bricks) {
                if (intersects(b, brick)) {
                    Vector bPos = brick.getPos();
                    double bX = pos.getX(), bY = pos.getY();
                    double bWidth = brick.getWidth(), bHeight = brick.getHeight();

                    //hit on top or bottom of brick
                    if (pos.getX() > (bX - bWidth / 2) && pos.getX() < (bX + bWidth / 2)) 
                        vel.scaleY(-1);
                    else if (pos.getY() > (bY - bHeight / 2) && pos.getY() < (bY + bHeight / 2)) //hit on side of brick
                        vel.scaleX(-1);

                    garbage.add(brick);
                }
            }

            for (Brick brick : garbage) {
                bricks.remove(brick);
            }

            if (intersects(b, playerPaddle)) {
                vel.scaleY(-1);
                double horiDist = b.getPos().getX() - playerPaddle.getPos().getX();
                double hitLocation = horiDist/playerPaddle.getWidth()*2; // -1 -> leftmost, 1 -> rightmost
                vel.add(new Vector(hitLocation*1.5, 0));
            }
        }
    }
}