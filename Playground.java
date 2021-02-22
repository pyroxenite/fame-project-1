import javax.swing.*;
import java.util.*; 
import java.awt.*;

public class Playground extends JPanel {
    private transient ArrayList<Sprite> sprites = new ArrayList<>();
    private static final int BALL_RADIUS = 10;
    private static Random rand = new Random();

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
    }

    /**
     * Draws the Playground object on screen.
     * @param g current drawing context
     */
    @Override
    public void paintComponent(Graphics g) {
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
                s.getPos().setX(400 - BALL_RADIUS);
                s.getVel().scaleX(-1);
            }
            if (s.getPos().getY() < BALL_RADIUS) {
                s.getPos().setY(BALL_RADIUS);
                s.getVel().scaleY(-1);
            } else if (s.getPos().getY() > 400 - BALL_RADIUS) {
                s.getPos().setY(400 - BALL_RADIUS);
                s.getVel().scaleY(-1);
            }
        }
    }

    public static void main(String[] args) {
        Playground p = new Playground(10);
        System.out.println(p.sprites);
    }
}