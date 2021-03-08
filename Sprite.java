import java.awt.*;

/**
 * General structure of a Sprite.
 */
public interface Sprite {
    public Vector getPos();
    public void setPos(Vector pos);
    
    public void draw(Graphics g);
    public void update();
}