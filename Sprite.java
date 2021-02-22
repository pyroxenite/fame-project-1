import java.util.Random;

public class Sprite {
    private Vector pos;
    private Vector vel;
    private String type;

    /**
     * Sprite constructor.
     * @param pos A Vector object that represents the position of the Sprite.
     * @param vel A Vector object that represents the velocity of the Sprite.
     * @param type A string to store the type of Sprite. For example: "ball", "paddle", etc...
     */
    public Sprite(Vector pos, Vector vel, String type) {
        this.pos = pos;
        this.vel = vel;
        this.type = type;
    }

    public String toString() {
        return "{ type: \"" + type + "\", pos: " + this.pos + ", vel: " + this.vel + " }";
    }

    public Vector getPos() {
        return pos;
    }

    public Vector getVel() {
        return vel;
    }

    public void move() {
        this.pos.add(this.vel);
    }

    public static void main(String[] args) {
        Vector pos = new Vector(30, 30);
        Vector vel = new Vector(2, 2);
        Sprite p1 = new Sprite(pos, vel, "ball");
        System.out.println(p1);
        p1.move();
        System.out.println(p1);
    }
}
