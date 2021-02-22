import java.util.ArrayList;
import java.util.Random;
import java.awt.*;

public class Sprite {
    private Vector pos;
    private Vector vel;

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
                             (int)(cDist.getY() - rect.getHeight()/2) ^ 2;
    
        return (cornerDistance_sq <= ((int)ball.getRadius() ^ 2));
    }

    /**
     * Sprite constructor.
     * @param pos A Vector object that represents the position of the Sprite.
     * @param vel A Vector object that represents the velocity of the Sprite.
     * @param type A string to store the type of Sprite. For example: "ball", "paddle", etc...
     */
    public Sprite(Vector pos, Vector vel) {
        this.pos = pos;
        this.vel = vel;
    }

    public String toString() {
        return "{ pos: " + this.pos + ", vel: " + this.vel + " }";
    }

    public Vector getPos() {
        return pos;
    }

    public Vector getVel() {
        return vel;
    }

    public void setPos(Vector newPos) { this.pos = newPos; }

    public void update(ArrayList<Rectangle> collisionList) { 
        for (Rectange r : collisionList) {
            
        }
    }

    // public static void main(String[] args) {
    //     Vector pos = new Vector(30, 30);
    //     Vector vel = new Vector(2, 2);
    //     Sprite p1 = new Sprite(pos, vel);
    //     System.out.println(p1);
    //     p1.move();
    //     System.out.println(p1);
    // }
}