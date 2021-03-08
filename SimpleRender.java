import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.*;

/**
 * Class to run the Swing application.
 */
public class SimpleRender implements Runnable {
    private static final int FPS = 60;
    private JFrame frame = new JFrame();
    private JLabel score = new JLabel("Score: 0");
    private JLabel lives = new JLabel(" Lives: ❤❤❤");
    private JCheckBox aiCheckBox = new JCheckBox("Auto Play");
    private Playground playground = new Playground(score, lives);

    public SimpleRender() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.setLayout(null);

        playground.setBounds(0, 25, 400, 400);
        lives.setBounds(0, 0, 100, 25);
        score.setBounds(200-35, 0, 100, 25);
        aiCheckBox.setBounds(300, 0, 100, 25);
        frame.add(playground);
        frame.add(lives);
        frame.add(score);
        frame.add(aiCheckBox);

        aiCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(aiCheckBox.isSelected()) {
                    playground.enableAI();
                } else {
                    playground.disableAI();
                }
            }
        });
        
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