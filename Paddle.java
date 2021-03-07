public class Paddle extends Rectangle {
    public Paddle(Vector pos) {
        super(pos, 100, 20);
    }

    public String toString() {
        return "Paddle: {\n    pos: " + this.getPos() + ",\n    width: " + 
        this.getWidth() + ",\n    height: " + this.getHeight() + "\n}";
    }
    
    @Override
    public void update() {
        if (getWidth() > 100) {
            setWidth(getWidth() * 0.9995);
        }
    }

    public static void main(String[] args) {
        System.out.println(new Paddle(new Vector(10, 20)));
    }
}