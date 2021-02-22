import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class Playground extends JPanel {
    private transient ArrayList<Sprite> sprites = new ArrayList<>();
    private static final int BALL_RADIUS = 10;
    private static Random rand = new Random();

    private Sprite playerPaddle = new Sprite(
        new Vector(0, 375), 
        new Vector(0, 0), 
        "Paddle"
    );

    public Playground(int n) {
        super();
        for (int i=0; i<n; i++) {
            Vector pos = new Vector(
                rand.nextDouble() * 400,
                rand.nextDouble() * 400
            );
            Vector vel = new Vector(
                rand.nextDouble() * 2,
                rand.nextDouble() * 2 
            );
            sprites.add(new Sprite(pos, vel, "ball"));
        }

        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                playerPaddle.setPos(new Vector(e.getX(), 375));
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

        // turns on antialising
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        ));

        //clear panel
        g.clearRect(0, 0, 400, 400);

        for (Sprite s : sprites) {
            Vector pos = s.getPos();
            g.setColor(Color.red);
            g.fillOval(
                (int) pos.getX() - BALL_RADIUS, 
                (int) pos.getY() - BALL_RADIUS, 
                2 * BALL_RADIUS,
                2 * BALL_RADIUS
            );
        }

        Vector ppPos = playerPaddle.getPos();
        g.setColor(Color.BLACK);
        g.fillRect((int)ppPos.getX(), (int)ppPos.getY(), 50, 10);

        this.repaint();
    }
    
    /**
     * Moves all sprites according to their speed.
     */
    public void animate() {
        for (Sprite s : sprites) {
            s.move();
        }
    }
        
    /**
     * Checks for collisions with walls and bounces sprites.
     */
    public void doPhysics() {
        for (Sprite s : sprites) {
            if (s.getPos().getX() < BALL_RADIUS) {
                s.getPos().setX(BALL_RADIUS);
                s.getVel().scaleX(-1);
            } else if (s.getPos().getX() > 400 - BALL_RADIUS) {
                s.getPos().setX(400.0 - BALL_RADIUS);
                s.getVel().scaleX(-1);
            }
            if (s.getPos().getY() < BALL_RADIUS) {
                s.getPos().setY(BALL_RADIUS);
                s.getVel().scaleY(-1);
            } else if (s.getPos().getY() > 400 - BALL_RADIUS) {
                s.getPos().setY(400.0 - BALL_RADIUS);
                s.getVel().scaleY(-1);
            }
        }
    }

    public static void main(String[] args) {
        Playground p = new Playground(10);
        System.out.println(p.sprites);
    }
}