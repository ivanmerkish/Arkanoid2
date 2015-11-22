import javax.swing.*;

/**
 * Frame Class
 */
public class MainFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Arkanoid");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(966, 639);
        GamePanel p = new GamePanel();
        frame.add(p);


        frame.setVisible(true);
    }
}
