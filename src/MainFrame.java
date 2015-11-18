import javax.swing.*;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class MainFrame {
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Asteroids");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        GamePanel p = new GamePanel();
        frame.add(p);


        frame.setVisible(true);
    }
}
