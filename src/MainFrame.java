import javax.swing.*;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Frame Class
 */
public class MainFrame {
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Arkanoid");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        GamePanel p = new GamePanel();
        frame.add(p);


        frame.setVisible(true);
    }
}
