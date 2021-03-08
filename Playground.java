import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main class to handle the game.
 */
public class Playground extends JPanel {
    private static Random rand = new Random();

    private JLabel score;
    private JLabel lives;
    
    private int frameCount = 0;
    private int scoreVal = 0;
    private int livesVal = 3;
    private double brickSpeed = 0.1;
    private int freezeFramesLeft = 0;
    
    private transient ArrayList<Ball> balls = new ArrayList<>();
    private transient ArrayList<Brick> bricks = new ArrayList<>();
    private transient ArrayList<Powerup> powerups = new ArrayList<>();
    private transient ArrayList<Announcement> announcements = new ArrayList<>();
    private transient Paddle playerPaddle;

    /**
     * Contructor for Playground class.
     * @param superScore Parent JLabel instance for score.
     * @param superLives Parent JLabel instance for remaining lives.
     */
    public Playground(JLabel superScore, JLabel superLives) {
        super();

        score = superScore;
        lives = superLives;

        // Display game name
        announcements.add(new Announcement("GAMENAME", 240)); //  <--- insert game name

        // Setup game
        resetGameState();

        // Setup mouse events
        this.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                playerPaddle.getPos().setX(e.getX());
            }
            
            public void mouseDragged(MouseEvent e) {
                playerPaddle.getPos().setX(e.getX());
            }
        });
    }

    /**
     * Resets game. To be used after all balls have been droped. Does not reset score,
     * lives, brick speed or ball speed.
     */
    private void resetGameState() {
        // New ball
        Vector pos = new Vector(200, 350);
        Vector vel = new Vector(rand.nextDouble() * 4, -5);
        balls.add(new Ball(pos, vel));
        
        // Initial bricks
        bricks.clear(); 
        addRandomBrickRow(15);
        addRandomBrickRow(40);
        addRandomBrickRow(65);
        addRandomBrickRow(90);

        // New paddle
        playerPaddle = new Paddle(new Vector(200, 440));
        playerPaddle.setColor(new Color(150, 150, 150));

        // Clear powerups
        powerups.clear();

        // Count down
        announcements.add(new Announcement("3", 40));
        announcements.add(new Announcement("2", 40));
        announcements.add(new Announcement("1", 40));
        announcements.add(new Announcement("Go!", 40, false));
    }

    public ArrayList<Ball> getBalls() { return balls; }
    public Paddle getPaddle() { return playerPaddle; }

    /**
     * Sets the number of frames that the bricks will be freezed for.
     * @param freezeFramesLeft Number of frames.
     */
    public void setFreezeFramesLeft(int freezeFramesLeft) { this.freezeFramesLeft = freezeFramesLeft; }

    public void addAnnouncement(Announcement a) { announcements.add(a); }

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

        // draw various sprites
        for (int i = 0; i < bricks.size(); i++)
            bricks.get(i).draw(g);

        for (int i = 0; i < powerups.size(); i++)
            powerups.get(i).draw(g);

        for (int i = 0; i < balls.size(); i++)
            balls.get(i).draw(g);

        playerPaddle.draw(g);

        drawAnnouncements(g);

        this.repaint();
    }
    
    /**
     * Moves all sprites according to their speed. Executes game logic and 
     * most game operations.
     */
    public void animate() {
        if (playerPaddle.getPos().getY() > 375) {
            playerPaddle.getPos().add(new Vector(0, -2));
        }

        if (!announcements.isEmpty()) {
            if (announcements.get(0).shouldPauseGame()) {
                updateAnnouncements();
                return; // wait
            } else
                updateAnnouncements();
        }

        executeGameLogic();

        autoPlay();

        double ballSpeed = Math.min(5.0 + scoreVal/100.0, 10.0);
        for (Ball b : balls) {
            b.update();
            b.getVel().setMag(ballSpeed);
        }
        removeFallenBalls();

        for (Powerup p : powerups) 
            p.update();
        removeFallenPowerups();

        brickSpeed = 0.1 + 0.05*(scoreVal/100);
        for (Brick b : bricks) {
            if (freezeFramesLeft > 0)
                b.setSpeed(0);
            else
                b.setSpeed(brickSpeed);
            
            b.update();
        }
        generateBricks();

        if (freezeFramesLeft > 0)
            freezeFramesLeft--;
        else
            frameCount++;

        playerPaddle.update();
    }

    /**
     * Handles game logic. Checks whether at least one ball remains and
     * whether no brick touches the ground.
     */
    private void executeGameLogic() {
        if (balls.isEmpty()) { // if it's last ball
            if (--livesVal < 1) { // if it's last life
                announcements.add(new Announcement("Game Over!", 120));
                announcements.add(new Announcement("Your score: "+scoreVal, 120));
                scoreVal = 0;
                livesVal = 3;
                score.setText("Score: 0");
            } else {
                announcements.add(new Announcement("Lives left: "+livesVal, 60));
            }
            resetGameState();
            String livesText = " Lives: ";
            for (int i=0; i<livesVal; i++) 
                livesText += "❤";
            lives.setText(livesText);
        }
    }
        
    /**
     * Checks for collisions and bounces balls.
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

    /**
     * Checks for collisions between ball provided in arguments and walls.
     * Bounces if necessairy.
     * @param b A ball to check collisions for.
     */
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

    /**
     * Checks for collisions between ball provided in arguments and bricks.
     * Bounces if necessairy.
     * @param b A ball to check collisions for.
     */
    private void collideWithBricks(Ball b) {
        Vector pos = b.getPos();
        Vector vel = b.getVel();

        ArrayList<Brick> garbage = new ArrayList<>();

        Boolean hBounce = false;
        Boolean vBounce = false;

        for (Brick brick : bricks) {
            if (intersects(b, brick)) {
                Vector bPos = brick.getPos();
                double bX = bPos.getX();
                double bWidth = brick.getWidth();

                if (pos.getX() > (bX - bWidth / 2) && pos.getX() < (bX + bWidth / 2)) //hit on top or bottom of brick
                    vBounce = true;
                else //hit on side of brick
                    hBounce = true;
                
                scoreVal++;
                score.setText(" Score: " + scoreVal);

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

    /**
     * Checks for collisions between ball provided in arguments and paddle.
     * Bounces if necessairy.
     * @param b A ball to check collisions for.
     */
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

    /**
     * Checks for intersection between a Circle and a Rectangle.
     * @param circ The circle.
     * @param rect The rectangle.
     * @return Returns true if the shapes intersect.
     */
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
     * Generates bricks only if needed.
     */
    private void generateBricks() {
        if (frameCount % (int)(25 / brickSpeed) == 0) {
            if (frameCount % (int)(200 / brickSpeed) == 0)
                addSpecialBrickRow(-10);
            else
                addRandomBrickRow(-10);
            
        }
    }

    /**
     * Generates a row of bricks with random powerups.
     * @param y Height at which the bricks should be added. 
     */
    private void addRandomBrickRow(double y) {
        for (int i = -4; i <= 4; i++) {
            if (rand.nextDouble() > 0.3) {
                Brick b = new Brick(new Vector(200 + i * 45, y));
                if (rand.nextDouble() > 0.95)
                    b.addRandomPowerup();
                bricks.add(b);
            }
        }
    }

    /**
     * Generates a row of bricks than isn't alligned with usual rows.
     * @param y Height at which the bricks should be added. 
     */
    private void addSpecialBrickRow(double y) {
        double[][] pairs = {{10, 20}, {40, 30}, {80, 40}, {130, 50}};
        for (double[] pair : pairs) {
            bricks.add(new Brick(new Vector(pair[0], y), pair[1]));
            bricks.add(new Brick(new Vector(400-pair[0], y), pair[1]));
        }
        bricks.add(new Brick(new Vector(200, y), 80));
    }

    /**
     * Removes balls that are no longer above the ground.
     */
    private void removeFallenBalls() {
        ArrayList<Ball> garbage = new ArrayList<>(); 

        for (Ball b : balls) {
            if (b.getPos().getY() > 400 + b.getRadius()) {
                garbage.add(b);        
            }
        }

        balls.removeAll(garbage);
    }

    /**
     * Removes powerups that are no longer above the ground.
     */
    private void removeFallenPowerups() {
        ArrayList<Powerup> garbage = new ArrayList<>(); 

        for (Powerup p : powerups) {
            if (p.getPos().getY() > 400 + p.getRadius()) {
                garbage.add(p);
            }
        }
        powerups.removeAll(garbage);
    }

    /**
     * Automatically positions the paddle to aim for the lowest ball or powerup.
     */
    private void autoPlay() {
        double xPos = 200;
        if (!balls.isEmpty()) {
            xPos = 200;
            double coefTotal = 1;
            for (int i=0; i<balls.size(); i++) {
                double coef = Math.exp(balls.get(i).getPos().getY()/400*20);
                coefTotal += coef;
                xPos += coef*balls.get(i).getPos().getX();
            }
            for (int i=0; i<powerups.size(); i++) {
                double coef = Math.exp(powerups.get(i).getPos().getY()/400*19.5);
                coefTotal += coef;
                xPos += coef*powerups.get(i).getPos().getX();
            }
            xPos /= coefTotal;
        }
        Vector pPos = playerPaddle.getPos();
        pPos.setX(xPos*0.3 + pPos.getX()*0.7);
    }

    /**
     * Displays the first announcement from available announcements, if any.
     * @param g Graphics drawing context.
     */
    private void drawAnnouncements(Graphics g) {
        if (announcements.isEmpty()) return;

        Announcement currentAnnouncement = announcements.get(0);
        double progress = currentAnnouncement.getProgress();
        String text = currentAnnouncement.getText();

        Font gameFont;
        int fontSize = 40;
        if (text.length() <= 3)
            fontSize = (int) (progress * 150);
        gameFont = new Font("Arial", Font.PLAIN, fontSize);
        g.setFont(gameFont);

        FontMetrics metrics = g.getFontMetrics(gameFont);
        int xOffset = metrics.stringWidth(text) / 2;
        
        g.setColor(new Color(240, 100, 0, (int) (255-255*progress)));
        g.drawString(text, 200-xOffset, 200+fontSize/3);
    }

    /**
     * Dercements the number of frames left for the first avaiable announcement.
     * @param g Graphics drawing context.
     */
    private void updateAnnouncements() {
        if (announcements.isEmpty()) return;

        Announcement currentAnnouncement = announcements.get(0);

        currentAnnouncement.decrementFramesLeft();

        if (currentAnnouncement.isExpired())
            announcements.remove(currentAnnouncement);
    }
}