public class Vector {
    private double x;
    private double y;

    public Vector() {
        this.x = 0;
        this.y = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "{ x: " + this.x + ", y: " + this.y + " }";
    }

    public Vector copy() {
        return new Vector(this.x, this.y);
    }

    ////// Reading //////

    public double getX() {
        return this.x;
    }
 
    public double getY() {
        return this.y;
    }

    public double mag() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double heading() {
        return Math.atan2(this.y, this.x);
    }

    ////// Writing //////

    public void setX(double value) {
        x = value;
    }

    public void setY(double value) {
        y = value;
    }
 
    public void add(Vector other) {
        x += other.x;
        y += other.y;
    }

    public void sub(Vector other) {
        x -= other.x;
        y -= other.y;
    }

    public void scale(double factor) {
        x *= factor;
        y *= factor;
    }

    public void scaleX(double factor) {
        x *= factor;
    }

    public void scaleY(double factor) {
        y *= factor;
    }

    public void rotate(double angle) {
        double oldX = x;
        x =    x * Math.cos(angle) - y * Math.sin(angle);
        y = oldX * Math.sin(angle) - y * Math.cos(angle);
    }

    ////// Tests //////

    public static void main(String [] args) {
        Vector v = new Vector(1, 0);
        v.rotate(Math.PI/2);
        Vector u = v.copy();
        System.out.println(u.mag());
    }
}