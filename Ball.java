import java.awt.*;

/**
 * A Circle subclass that represents a ball.
 */
public class Ball extends Circle {
    public Ball(Vector pos, Vector vel) {
        super(pos, vel, 10);
        setColor(Color.red);
    }

    public String toString() {
        return "Ball: {\n    pos: " + this.getPos() + ",\n    vel: " + this.getVel() + "\n}";
    }

    @Override
    public void update() {
        getPos().add(getVel());
        if (Math.abs(getVel().getY()) < 3) {
            getVel().scaleY(2);
        }
        if (Math.abs(getVel().getY()) == 0) {
            getVel().setY(-3);
        }
    }

    public static void main(String[] args) {
        Vector pos = new Vector(30, 30);
        Vector vel = new Vector(2, 2);
        Ball b = new Ball(pos, vel);
        System.out.println(b);
        b.update();
        System.out.println(b);
    }
}
