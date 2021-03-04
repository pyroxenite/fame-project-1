import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;


public class Playground extends JPanel {
    private static Random rand = new Random();

    private transient ArrayList<Ball> balls = new ArrayList<>();
    private transient ArrayList<Brick> bricks = new ArrayList<>();
    private transient ArrayList<Powerup> powerups = new ArrayList<>();
    private transient Paddle playerPaddle;

    private int frameCount = 0;
    private JLabel score;
    private JLabel lives;
    private int scoreVal = 0;
    private int livesVal = 3;
    private double brickSpeed = 0.1;
    private int freezeFramesLeft = 0;
    private int waitFramesLeft = 0; // to pause the game for a few frames between resets

    private void resetGameState() {
        Vector pos = new Vector(200, 350);
        Vector vel = new Vector(rand.nextDouble() * 4, -5);
        balls.add(new Ball(pos, vel));
        bricks.clear();

        addRandomBrickRow(15);
        addRandomBrickRow(40);
        addRandomBrickRow(65);
        addRandomBrickRow(90);

        playerPaddle = new Paddle(new Vector(0, 375));
        playerPaddle.setColor(new Color(150, 150, 150));

        waitFramesLeft += 120; // wait two seconds
    }
   
    public Playground(JLabel sScore, JLabel sLives) {
        super();

        score = sScore;
        lives = sLives;

        resetGameState();

        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                playerPaddle.getPos().setX(e.getX());
            }
            
            public void mouseDragged(MouseEvent e) {
                // do something else? could be a different mechanic
            }
        });
    }

    public ArrayList<Ball> getBalls() { return balls; }
    public void setFreezeFramesLeft(int freezeFramesLeft) { this.freezeFramesLeft = freezeFramesLeft; }

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

        for (int i = 0; i < powerups.size(); i++) {
            powerups.get(i).draw(g);
        }

        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).draw(g);
        }

        playerPaddle.draw(g);

        this.repaint();
    }
    
    /**
     * Moves all sprites according to their speed.
     */
    public void animate() {
        if (waitFramesLeft > 0) {
            waitFramesLeft--;
            return;
        }

        autoPlay();

        for (Ball b : balls) {
            b.update();
            b.getVel().setMag(5); // keeps the ball from going too fast or too slow
        }

        removeFallenBalls();

        for (Powerup p : powerups) 
            p.update();

        removeFallenPowerups();

        for (Brick b : bricks) {
            if (freezeFramesLeft > 0)
                b.setSpeed(0);
            else
                b.setSpeed(brickSpeed);
            
            b.update();
        }

        if (freezeFramesLeft > 0)
            freezeFramesLeft--;

        generateBricks();

        brickSpeed = 0.1 + 0.05*(scoreVal/100);

        if (freezeFramesLeft == 0)
            frameCount++;
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

        ArrayList<Powerup> garbage = new ArrayList<>();

        for (Powerup p : powerups) {
            if (intersects(p, playerPaddle)) {
                p.activate(this);
                garbage.add(p);
            }
        }

        for (Powerup p : garbage)
            powerups.remove(p);
    }

    private void collideWithWalls(Ball b) {
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

    private void collideWithBricks(Ball b) {
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

        if (vBounce) vel.scaleY(-1);
        if (hBounce) vel.scaleX(-1);

        for (Brick brick : garbage) {
            brick.releasePowerup(powerups);
            bricks.remove(brick);
        }
    }

    private void collideWithPaddle(Ball b) {
        Vector pos = b.getPos();
        Vector vel = b.getVel();

        if (intersects(b, playerPaddle)) {
            vel.scaleY(-1);
            double horiDist = pos.getX() - playerPaddle.getPos().getX();
            double hitLocation = horiDist/playerPaddle.getWidth()*2; // -1 -> leftmost, 1 -> rightmost
            vel.add(new Vector(hitLocation*1.5, 0)); // alters ball direction
            pos.setY(355);
        }
    }

    private void generateBricks() {
        if (frameCount % (int)(25 / brickSpeed) == 0) {
            if (frameCount % (int)(200 / brickSpeed) == 0)
                addSpecialBrickRow(-10);
            else
                addRandomBrickRow(-10);
            
        }
    }

    private void addRandomBrickRow(double y) {
        for (int i = -4; i <= 4; i++) {
            if (rand.nextBoolean()) {
                Brick b = new Brick(new Vector(200 + i * 45, y));
                if (rand.nextDouble() > 0.9)
                    b.addRandomPowerup();
                bricks.add(b);
            }
        }
    }

    private void addSpecialBrickRow(double y) {
        double[][] pairs = {{10, 20}, {40, 30}, {80, 40}, {130, 50}};
        for (double[] pair : pairs) {
            bricks.add(new Brick(new Vector(pair[0], y), pair[1]));
            bricks.add(new Brick(new Vector(400-pair[0], y), pair[1]));
        }
        bricks.add(new Brick(new Vector(200, y), 80));
    }

    private void removeFallenBalls() {
        ArrayList<Ball> garbage = new ArrayList<>(); 

        for (Ball b : balls) {
            if (b.getPos().getY() > 400 + b.getRadius()) {
                garbage.add(b);        
            }
        }

        balls.removeAll(garbage);
        if (balls.size() == 0) { // if it's last ball
            if (--livesVal < 1) {
                scoreVal = 0;
                livesVal = 3;
                score.setText("Score: 0");
                waitFramesLeft += 120;
            }
            resetGameState();
            lives.setText("Lives: " + livesVal);
        }
    }

    private void removeFallenPowerups() {
        ArrayList<Powerup> garbage = new ArrayList<>(); 

        for (Powerup p : powerups) {
            if (p.getPos().getY() > 400 + p.getRadius()) {
                garbage.add(p);
            }
        }
        powerups.removeAll(garbage);
    }

    private void autoPlay() {
        double xPos = 200;
        if (!balls.isEmpty()) {
            xPos = 0;
            double coefTotal = 0;
            for (int i=0; i<balls.size(); i++) {
                double coef = Math.exp(balls.get(i).getPos().getY()/400*20);
                coefTotal += coef;
                xPos += coef*balls.get(i).getPos().getX();
            }
            for (int i=0; i<powerups.size(); i++) {
                double coef = Math.exp(powerups.get(i).getPos().getY()/400*20);
                coefTotal += coef;
                xPos += coef*powerups.get(i).getPos().getX();
            }
            xPos /= coefTotal;
        }
        Vector pPos = playerPaddle.getPos();
        pPos.setX(xPos*0.2 + pPos.getX()*0.8);
    }
}