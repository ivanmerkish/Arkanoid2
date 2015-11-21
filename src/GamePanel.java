import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * GamePanel : Game Render with game Loop
 */
public class GamePanel extends JPanel implements Runnable {

    private static final int PERIOD = 20;
    private static final int PANELWIDTH = 800;
    private static final int PANELHEIGHT = 600;
    boolean running, paused, isGameOver;
    GameField gameField;
    private BufferedImage dbImage = null;
    private ArrayList<BufferedImage> powerUPSImages, biteImages;
    private BufferedImage bulletImage, fireBallImage;
    private KeyEvent keyEvent;
    private MouseEvent mouseEvent;


    public GamePanel() {
        setFocusable(true);
        requestFocusInWindow();
        try {

            bulletImage = ImageIO.read(new File(getClass().getResource("/img/bullet.png").toURI()));
            biteImages = new ArrayList<>();
            biteImages.add(ImageIO.read(new File(getClass().getResource("/img/Bite.png").toURI())));
            biteImages.add(ImageIO.read(new File(getClass().getResource("/img/BiteGuns.png").toURI())));
            powerUPSImages = new ArrayList<>();
            fireBallImage = ImageIO.read(new File(getClass().getResource("/img/Fireball.png").toURI()));
            //image loading;
        } catch (Exception e) {
            System.out.println("Image Load Error");
        }
        gameField = new GameField(powerUPSImages, biteImages, bulletImage, fireBallImage, PANELWIDTH, PANELHEIGHT);


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public synchronized void keyPressed(KeyEvent e) {
                keyEvent = e;
            }

            @Override
            public synchronized void keyReleased(KeyEvent e) {
                keyEvent = null;
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public synchronized void mouseClicked(MouseEvent e) {
                mouseEvent = e;
            }

            @Override
            public synchronized void mouseMoved(MouseEvent e) {
                mouseEvent = e;
            }
        });
    }


    public void run() {
        long before, diff, sleepTime;
        before = System.currentTimeMillis();
        running = true;

        while (running) {
            if (!paused && !isGameOver) {
                gameUpdate();
            }
            gameRender();
            paintScreen();

            diff = System.currentTimeMillis() - before;
            sleepTime = PERIOD - diff;
            if (sleepTime <= 0)
                sleepTime = 5;

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
            }

            before = System.currentTimeMillis();

        }


    }

    public void addNotify() {
        super.addNotify();   // creates the peer
        (new Thread(this)).start();
    }

    private void gameRender() {
        Graphics dbg;

        dbImage = new BufferedImage(PANELWIDTH, PANELHEIGHT, BufferedImage.OPAQUE);
        dbg = dbImage.createGraphics();
        gameField.bite.drawSprite(dbg);
        for (Ball ball : gameField.gameBalls) {
            ball.drawSprite(dbg);
        }
        for (PowerUP powerUP : gameField.powerUPs) {
            powerUP.drawSprite(dbg);
        }
        for (Brick brick : gameField.bricks) {
            brick.drawSprite(dbg);
        }
        for (Bullet bullet : gameField.bullets) {
            bullet.drawSprite(dbg);
        }

    }

    private void gameUpdate() {
        gameField.updateGameField(keyEvent, mouseEvent);

    }

    private void paintScreen() {
        Graphics g;
        try {
            g = getGraphics();
            if (g != null && dbImage != null) {
                g.drawImage(dbImage, 0, 0, null);
            }
        } catch (Exception e) {
            System.out.println("Graphics error");
            e.printStackTrace();
        }
    }


}
