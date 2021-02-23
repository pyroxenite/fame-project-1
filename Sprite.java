public class Sprite {
    private Vector pos;

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
     * Sprite constructor.
     * @param pos A Vector object that represents the position of the Sprite.
     * @param type A string to store the type of Sprite. For example: "ball", "paddle", etc...
     */
    public Sprite(Vector pos) {
        this.pos = pos;
    }

    public String toString() {
        return "{ pos: " + this.pos + " }";
    }

    public Vector getPos() { return pos; }

    public void setPos(Vector pos) { this.pos = pos; }

}