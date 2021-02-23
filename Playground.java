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
            - rand.nextDouble() * 4 
        );
        balls.add(new Ball(pos, vel));

        Vector pos2 = new Vector(
            rand.nextDouble() * 400,
            rand.nextDouble() * 200
        );
        bricks.add(new Brick(pos2));

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
        }

        for (Brick b : bricks) {
            b.update();
        }
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
            }
            // } else if (pos.getY() > 400 - r) {
            //     pos.setY(400.0 - r);
            //     vel.scaleY(-1);
            // }
        }
    }
}