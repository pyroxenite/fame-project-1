public class Sprite {
    private Vector pos;

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