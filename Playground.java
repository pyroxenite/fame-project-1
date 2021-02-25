import javax.swing.*;
import java.lang.Math.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class Playground extends JPanel {
    private static Random rand = new Random();

    private transient ArrayList<Ball> balls = new ArrayList<>();
    private transient ArrayList<Brick> bricks = new ArrayList<>();
    private transient ArrayList<Powerup> powerups = new ArrayList<>();
    private transient Paddle playerPaddle;
    private JLabel score;
    private int scoreVal = 0;

    public Playground(JLabel sScore) {
        super();

        score = sScore;

        Vector pos = new Vector(200, 365);
        Vector vel = new Vector(
            (rand.nextDouble() - 0.5) * 4,
            -5
        );
        balls.add(new Ball(pos, vel));

        playerPaddle = new Paddle(new Vector(0, 375));
        playerPaddle.setColor(new Color(150, 150, 150));

        // powerups.add(new Powerup(new Vector(200, 100), 1));

        addBrickRow(30);
        addBrickRow(55);
        addBrickRow(80);

        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                playerPaddle.getPos().setX(e.getX());
            }
            
            public void mouseDragged(MouseEvent e) {
                // do something else? could be a different mechanic
            }
        });
    }

    public ArrayList<Ball> getBalls() {
        return balls;
    }

    /**
     * Draws the Playground object on screen.
     * @param g current drawing context
     */
    @Override
    public void paintComponent(Graphics g) {
        // turn on antialising
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHints(new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        ));

        // clear panel
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, 400, 400);

        for (int i = 0; i < bricks.size(); i++) {
            bricks.get(i).draw(g);
        }

        for (Powerup p : powerups)
            p.draw(g);

        for (Ball b : balls)
            b.draw(g);

        playerPaddle.draw(g);

        this.repaint();
    }
    
    /**
     * Moves all sprites according to their speed.
     */
    public void animate() {
        for (Ball b : balls) {
            b.update();
            b.getVel().setMag(5); // keeps the ball from going too fast or too slow
        }

        for (Brick b : bricks) {
            b.update();
        }

        for (Powerup p : powerups) {
            p.update();
        }
    }

    private boolean intersects(Circle circ, Rectangle rect) {
        Vector cDist = new Vector();
        Vector cPos = circ.getPos();
        Vector rPos = rect.getPos();

        cDist.setX(Math.abs(cPos.getX() - rPos.getX()));
        cDist.setY(Math.abs(cPos.getY() - rPos.getY()));
    
        if (cDist.getX() > (rect.getWidth()/2 + circ.getRadius())) { return false; }
        if (cDist.getY() > (rect.getHeight()/2 + circ.getRadius())) { return false; }
    
        if (cDist.getX() <= (rect.getWidth()/2)) { return true; } 
        if (cDist.getY() <= (rect.getHeight()/2)) { return true; }
    
        double cornerDistance_sq = (int)(cDist.getX() - rect.getWidth()/2)^2 +
                                   (int)(cDist.getY() - rect.getHeight()/2)^2;
    
        return (cornerDistance_sq <= ((int)circ.getRadius() ^ 2));
    }
        
    /**
     * Checks for collisions bounces balls.
     */
    public void doPhysics() {
        for (Ball b : balls) {            
            collideWithWalls(b);
            collideWithBricks(b);
            collideWithPaddle(b);
        }

        for (Powerup p : powerups) {
            collideWithPaddle(p);
        }
    }

    public void collideWithWalls(Ball b) {
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
    }

    public void collideWithBricks(Ball b) {
        Vector pos = b.getPos();
        Vector vel = b.getVel();

        ArrayList<Brick> garbage = new ArrayList<>();

        Boolean hBounce = false;
        Boolean vBounce = false;

        for (Brick brick : bricks) {
            if (intersects(b, brick)) {
                Vector bPos = brick.getPos();
                double bX = bPos.getX(), bY = bPos.getY();
                double bWidth = brick.getWidth(), bHeight = brick.getHeight();

                if (pos.getX() > (bX - bWidth / 2) && pos.getX() < (bX + bWidth / 2)) //hit on top or bottom of brick
                    vBounce = true;
                else if (pos.getY() > (bY - bHeight / 2) && pos.getY() < (bY + bHeight / 2)) //hit on side of brick
                    hBounce = true; 
                else {
                    //check for corners by adding and subtracting width & height in 4 directions
                    for (int ix = -1; ix < 1; ix++) {
                        for (int iy = -1; iy < 1; iy++) {
                            if (ix != 0 && iy != 0) {
                                //get distance of ball center from brick corner point
                                double dist = Math.sqrt(
                                    Math.pow(pos.getX() - (bX + ix * bWidth / 2), 2) +
                                    Math.pow(pos.getY() - (bY + iy * bHeight / 2), 2)
                                );
                                //if ball is overlapping, reflect velocity
                                if (dist <= b.getRadius()) {
                                    hBounce = true; 
                                    vBounce = true; 
                                }
                            }
                        }
                    }
                }
                
                scoreVal++;
                score.setText("Score: " + scoreVal);

                garbage.add(brick);
            }
        }

        for (Brick brick : garbage)
            bricks.remove(brick);

        if (vBounce) vel.scaleY(-1);
        if (hBounce) vel.scaleX(-1);
    }

    public void collideWithPaddle(Ball b) {
        Vector pos = b.getPos();
        Vector vel = b.getVel();

        if (intersects(b, playerPaddle)) {
            vel.scaleY(-1);
            double horiDist = pos.getX() - playerPaddle.getPos().getX();
            double hitLocation = horiDist/playerPaddle.getWidth()*2; // -1 -> leftmost, 1 -> rightmost
            vel.add(new Vector(hitLocation*1.5, 0)); // alters ball direction
        }
    }

    public void collideWithPaddle(Powerup p) {
        if (intersects(p, playerPaddle)) {
            p.activate(this);
            powerups.remove(p);
        }
    }

    public void addBrickRow(double height) {
        for (int i = -4; i <= 4; i++) {
            bricks.add(new Brick(new Vector(200 + i * 45, height)));    
        }
    }
}