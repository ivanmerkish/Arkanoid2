import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * GamePanel : Game Render with game Loop
 */
public class GamePanel extends JPanel implements Runnable {

    private static final int PERIOD = 20;
    private static final int PANELWIDTH = 950;
    private static final int PANELHEIGHT = 600;
    private static final int INFOPANELWIDHT = 153;
    private static final String FONTNAME = "Terminal";

    boolean running, paused, isGameOver;
    GameField gameField;
    private BufferedImage dbImage = null;
    private ArrayList<BufferedImage> biteImages;
    private BufferedImage bulletImage, fireBallImage, backgroundImage, weaponPUImage, fireballPUImage, gluePUImage;
    private KeyEvent keyEvent, spaceKeyEvent;

    public GamePanel() {
        setFocusable(true);
        requestFocusInWindow();
        try {
            weaponPUImage = ImageIO.read(new File(getClass().getResource("/img/weaponPU.png").toURI()));
            fireballPUImage = ImageIO.read(new File(getClass().getResource("/img/fireballPU.png").toURI()));
            gluePUImage = ImageIO.read(new File(getClass().getResource("/img/gluePU.png").toURI()));
            bulletImage = ImageIO.read(new File(getClass().getResource("/img/bullet.png").toURI()));
            biteImages = new ArrayList<>();
            biteImages.add(ImageIO.read(new File(getClass().getResource("/img/Bite.png").toURI())));
            biteImages.add(ImageIO.read(new File(getClass().getResource("/img/BiteGuns.png").toURI())));
            fireBallImage = ImageIO.read(new File(getClass().getResource("/img/Fireball.png").toURI()));
            backgroundImage = ImageIO.read(new File(getClass().getResource("/img/background.png").toURI()));
            //image loading;
        } catch (Exception e) {
            System.out.println("Image Load Error");
        }
        gameField = new GameField(biteImages, bulletImage, fireBallImage, PANELWIDTH - INFOPANELWIDHT, PANELHEIGHT);


        addKeyListener(new KeyAdapter() {
            @Override
            public synchronized void keyTyped(KeyEvent e) {

            }

            @Override
            public synchronized void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_SPACE) {
                    keyEvent = e;
                }
            }

            @Override
            public synchronized void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    spaceKeyEvent = e;
                } else {
                    keyEvent = null;
                }
            }
        });
    }


    public void run() {
        long before, diff, sleepTime;
        before = System.currentTimeMillis();
        running = true;

        while (running) {
            isGameOver = gameField.isGameOver();
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
        Graphics2D g2 = (Graphics2D) dbg;
        g2.drawImage(backgroundImage, 0, 0, null);
        scoreRender(dbg);
        effectRender(dbg);
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
        gameField.updateGameField(keyEvent, spaceKeyEvent);
        if (spaceKeyEvent != null && spaceKeyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceKeyEvent = null;
        }

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

    private void scoreRender(Graphics graphics) {
        Font scoreFont = new Font(FONTNAME, Font.BOLD, 30);
        FontMetrics fm = graphics.getFontMetrics(scoreFont);
        String scoreStr = String.valueOf(gameField.getScore());
        String scoretitl = "Score: ";
        String lifeTitl = "Lives: ";
        graphics.setFont(scoreFont);

        graphics.drawString(scoretitl, PANELWIDTH - INFOPANELWIDHT + 20, fm.getHeight());

        graphics.drawString(scoreStr, PANELWIDTH - INFOPANELWIDHT + 20, fm.getHeight() * 2);
        graphics.drawString(lifeTitl, PANELWIDTH - INFOPANELWIDHT + 20, fm.getHeight() * 2 + 53);
        int line = 0;
        int limit = gameField.getLifeCount();
        for (int i = 0; i < limit; i++) {
            if (PANELWIDTH - INFOPANELWIDHT + 20 + i * 25 > PANELWIDTH - 10) {
                line++;
                limit = limit - i;
                i = 0;
            }
            graphics.drawImage(gameField.bite.image, PANELWIDTH - INFOPANELWIDHT + 20 + i * 25, (int) (fm.getHeight() * 1.5d) + 80 + (gameField.bite.height / 7 + 2) * line, gameField.bite.width / 7, gameField.bite.height / 7, this);
        }
    }

    private void effectRender(Graphics graphics) {
        Font scoreFont = new Font(FONTNAME, Font.BOLD, 30);
        FontMetrics fm = graphics.getFontMetrics(scoreFont);
        String effectString = "Effects:";
        int userPanelX = PANELWIDTH - INFOPANELWIDHT + 20;
        graphics.drawString(effectString, userPanelX, fm.getHeight() + 180);
        int i = 0;
        int imgW = weaponPUImage.getWidth() / 5;
        int imgH = weaponPUImage.getHeight() / 5;
        graphics.setColor(new Color(0x3edc00));
        if (gameField.bite.isWeapon) {
            graphics.drawImage(weaponPUImage, userPanelX, (int) (fm.getHeight() * 1.5d) + 180 + i * imgH, imgW, imgH, this);
            String bullets = "X " + gameField.bite.getBulletCount();
            graphics.drawString(bullets, userPanelX + imgW + 10, fm.getHeight() * 2 + 180 + i * imgH);
            i++;
        }
        if (gameField.bite.isSticky()) {
            if (!gameField.isGameLaunch()) {
                String glue = "X " + gameField.bite.getGlueCounter();
                graphics.drawImage(gluePUImage, userPanelX, (int) (fm.getHeight() * 1.5d) + 180 + i * imgH, imgW, imgH, this);
                graphics.drawString(glue, userPanelX + imgW + 10, fm.getHeight() * 2 + 180 + i * imgH);
                i++;
            }
        }

    }


}
