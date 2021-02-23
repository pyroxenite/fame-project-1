import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.*;

public class SimpleRender implements Runnable {
    private static final int FPS = 60;
    private JFrame frame = new JFrame();
    private JButton button = new JButton("Animate");
    private Playground playground = new Playground();

    public SimpleRender() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 428);
        frame.setLayout(null);

        playground.setBounds(0, 0, 400, 400);
        frame.add(playground);

        //playground.repaint();

        frame.setVisible(true);
    }

    /**
     * This method is used to implement the `Runnable` interface. It gets called at a fixed 
     * rate by a `ScheduledExecutorService` as soon as `startGame` is called.
     */
    public void run()  {
        playground.doPhysics();
        playground.animate();
    }

    /**
     * Begins render loop.
     */
    private void startGame() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this, 0, 1000 / FPS, TimeUnit.MILLISECONDS);
    }

    public static void main(String[] args) {
        new SimpleRender().startGame();
    }
}