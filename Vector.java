/**
 * A class for storing and manipulating 2D vectors.
 */
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

    /**
     * Returns a new instance of Vector with identical coordinates.
     * @return The copied Vector.
     */
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

    /**
     * Calculates the magnitude of the Vector instance.
     * @return The magnitude.
     */
    public double mag() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    /** 
     * Calculates the direction or heading in radians of the Vector instance. 
     * @return The angle.
    */
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
 
    /** 
     * Adds a Vector.
     * @param other The other vector.
     */
    public void add(Vector other) {
        x += other.x;
        y += other.y;
    }

    /** 
     * Subtracts a Vector.
     * @param other The other vector.
     */
    public void sub(Vector other) {
        x -= other.x;
        y -= other.y;
    }

    /** 
     * Scales a Vector.
     * @param factor Factor by which to scale.
     */
    public void scale(double factor) {
        x *= factor;
        y *= factor;
    }

    /** 
     * Scales a Vector along the X axis.
     * @param factor Factor by which to scale.
     */
    public void scaleX(double factor) {
        x *= factor;
    }

    /** 
     * Scales a Vector along the Y axis.
     * @param factor Factor by which to scale.
     */
    public void scaleY(double factor) {
        y *= factor;
    }

    /** 
     * Rotates a Vector.
     * @param angle Angle of rotation in radians.
     */
    public void rotate(double angle) {
        double oldX = x;
        x =    x * Math.cos(angle) - y * Math.sin(angle);
        y = oldX * Math.sin(angle) - y * Math.cos(angle);
    }

    /** 
     * Sets the magnitude of the vector. If zero, the result will have a heading of 0 radians.
     * @param mag The desired magnitude.
     */
    public void setMag(double mag) {
        double currentMag = this.mag();
        if (currentMag == 0.0) {
            x = 1;
            y = 0;
            currentMag = 1;
        } 
        this.scale(mag/currentMag);
    } 

    ////// Tests //////

    public static void main(String [] args) {
        Vector v = new Vector(1, 0);
        v.rotate(Math.PI/2);
        Vector u = v.copy();
        System.out.println(u.mag());
    }
}