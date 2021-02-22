import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class SimpleRender implements ActionListener {

    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton button = new JButton("Animate");
    Playground playground = new Playground(20);

    public SimpleRender() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 428);
        frame.setLayout(null);

        panel.setBounds(0, 0, 400, 400);
        panel.setBackground(Color.white);
        frame.add(panel);

        playground.setBounds(0, 0, 400, 400);
        playground.setBackground(Color.white);
        frame.add(playground);

        playground.repaint();

        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) { 
        // String cmd = e.getActionCommand();
        // //System.out.println(cmd);
        // if (cmd == "Animate") {
        //     playground.animate();
        //     playground.doPhysics();
        // }
    }

    /**
     * This function implements a game loop calling the `animate` and `doPhysics` 
     * methods. It never returns so this should be the last function called in `main`.
     */
    public void run()  {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        while (delta != -1) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 0) { // while (true)
                playground.animate();
                playground.doPhysics();
                delta--;
            }
        } 
    }
    
    public static void main(String[] args) {  
        SimpleRender sr = new SimpleRender();
        sr.run();
    }
}