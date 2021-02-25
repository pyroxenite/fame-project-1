/**
 * Subclassing at its best.
 */
public class Paddle extends Rectangle {
    public Paddle(Vector pos) {
        super(pos, 100, 20);
    }

    public String toString() {
        return "Paddle: {\n    pos: " + this.getPos() + ",\n    width: " + 
        this.getWidth() + ",\n    height: " + this.getHeight() + "\n}";
    }

    public static void main(String[] args) {
        System.out.println(new Paddle(new Vector(10, 20)));
    }
}