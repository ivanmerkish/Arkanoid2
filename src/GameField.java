import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameField Class: Game engine
 */
public class GameField {

    private static final int TOOLBARHEIGHT = 10;
    private static final String[] LEVELS = {"level1", "level2", "level3", "level4", "level5"};
    protected CopyOnWriteArrayList<Ball> gameBalls;
    protected CopyOnWriteArrayList<Brick> bricks;
    protected CopyOnWriteArrayList<PowerUP> powerUPs;
    protected CopyOnWriteArrayList<Bullet> bullets;
    protected Bite bite;
    private int panelWidth, panelHeight;
    private LevelGenerator lg;
    private int lifeCount, levelCounter;
    private ArrayList<BufferedImage> powerUPSImages, biteImages;
    private BufferedImage bulletImage, fireBallImage;
    private double stickyPoint;
    private boolean gameLaunch;
    private boolean isGameover;

    public GameField(ArrayList<BufferedImage> powerUPSImages, ArrayList<BufferedImage> biteImages, BufferedImage bulletImage, BufferedImage fireBallImage, int panelWidth, int panelHeight) {
        this.gameBalls = new CopyOnWriteArrayList<>();
        this.bricks = new CopyOnWriteArrayList<>();
        this.powerUPs = new CopyOnWriteArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.bite = null;
        this.lg = null;
        this.powerUPSImages = powerUPSImages;
        this.biteImages = biteImages;
        this.bulletImage = bulletImage;
        this.fireBallImage = fireBallImage;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;
        gameLaunch = true;
        isGameover = false;

        levelCounter = 1;
        lifeCount = 3;
        init();

    }

    public void init() {
        MusicPlayer m = new MusicPlayer();

        Thread t = new Thread(m);
        t.run();
        lg = new LevelGenerator(LEVELS[levelCounter]);
        lg.setPanelHeight(panelHeight);
        lg.setPanelWidth(panelWidth);
        try {
            bricks = lg.buildLevel();
        } catch (IOException e) {
            System.out.println("Level Build Error");
        }
        int biteWidth, biteHeight;
        biteWidth = bricks.get(0).width * 3;
        biteHeight = bricks.get(0).height;
        bite = new Bite(panelWidth / 2 - biteWidth / 2, panelHeight - biteHeight - TOOLBARHEIGHT, biteImages.get(0), biteWidth, biteHeight);
        Ball ball = new Ball(panelWidth / 2 - 12.5, bite.y - 26);
        ball.setFireBall(fireBallImage);
        gameBalls.add(ball);
        stickyPoint = bite.x;

    }

    public void updateGameField(KeyEvent keyEvent, MouseEvent mouseEvent) {
        bite.keyEvent = keyEvent;
        bite.mouseEvent = mouseEvent;
        updateAllItems();
        collisionCheck();

    }

    private void updateAllItems() {
        for (Ball ball : gameBalls) {
            if (ball.isGlued()) {
                if (bite.keyEvent != null && bite.keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (gameLaunch) {
                        bite.setSticky(false);
                        gameLaunch = false;
                    }

                    ball.setGlued(false);
                    if (ball.getAngle() > 180) {
                        ball.setAngle(ball.getAngle() - 180);
                    }
                } else {
                    double biteXDef = stickyPoint - bite.x;
                    if ((biteXDef) != 0) {
                        ball.x = ball.x - biteXDef;
                    }
                    ball.spdy = 0;
                    stickyPoint = bite.x;
                }
            }
            if (bite.getBallPowerUpEffect() != null) {
                ball.setCurrPowerUpEffect(bite.getBallPowerUpEffect());
                bite.setBallPowerUpEffect(null);
            }
            ball.updateSprite();
        }
        for (Bullet bullet : bullets) {
            bullet.updateSprite();
        }
        if (bite.isWeapon) {
            bite.image = biteImages.get(1);
        } else {
            bite.image = biteImages.get(0);
        }
        if (bite.isWeapon) {
            bite.image = biteImages.get(1);
        }
        bite.updateSprite();
        for (PowerUP powerUP : powerUPs) {
            powerUP.updateSprite();
        }
    }

    private void collisionCheck(){
        //ball collisions;
        for (Ball b:gameBalls) {
            for (Brick brick:bricks) {
                if (b.isCollision(brick)) {
                    if (brick.getHardness() - 1 == 0) {
                        if (brick.getPowerUP() != null) {
                            powerUPs.add(brick.getPowerUP());
                        }
                        bricks.remove(brick);
                        break;
                    } else if (brick.getHardness() - 1 > 0) {
                        brick.setHardness(brick.getHardness() - 1);
                    } else {
                        brick.setHardness(-1);
                    }
                }
            }
            if (b.isCollision(bite)) {
                b.setGlued(bite.isSticky());
            }
            isBorder(b);
        }
        //Bite collision;
        isBorder(bite);
        //powerUP collision;
        for (PowerUP powerUP : powerUPs) {
            if (bite.isCollision(powerUP) || isBorder(powerUP)) {
                powerUPs.remove(powerUP);
            }
        }
    }

    private boolean isBorder(Sprite sprite) {
        Line2D upperBorder = new Line2D.Double(15, 0, panelWidth, 0);
        Line2D leftBorder = new Line2D.Double(15, 0, 15, panelHeight);
        Line2D rightBorder = new Line2D.Double(panelWidth, 15, panelWidth, panelHeight);
        Line2D bottomBorder = new Line2D.Double(15, panelHeight, panelWidth, panelHeight);
        Rectangle2D rectangle = new Rectangle.Double(sprite.x, sprite.y, sprite.width, sprite.height);
        if (sprite instanceof Ball) {
            if (rectangle.intersectsLine(upperBorder)) {
                ((Ball) sprite).setAngle(180 - ((Ball) sprite).getAngle());
                return true;
            }
            if (rectangle.intersectsLine(bottomBorder)) {
                lifeCount--;
                restart();
            }
            if (rectangle.intersectsLine(leftBorder) || rectangle.intersectsLine(rightBorder)) {
                ((Ball) sprite).setAngle(360 - ((Ball) sprite).getAngle());
                return true;
            }
        }
        if (sprite instanceof PowerUP) {

            if (rectangle.intersectsLine(bottomBorder)) {
                return true;
            }
        }
        if (sprite instanceof Bite) {
            if (rectangle.intersectsLine(leftBorder)) {
                ((Bite) sprite).isLeftBorder = true;
            }
            if (rectangle.intersectsLine(rightBorder)) {
                ((Bite) sprite).isRightBorder = true;
            }
        }
        return false;
    }

    private void restart() {
        if (lifeCount - 1 > -1) {
            gameBalls.clear();
            int biteWidth, biteHeight;
            biteWidth = bricks.get(0).width * 3;
            biteHeight = bricks.get(0).height / 2;
            bite = new Bite(panelWidth / 2 - biteWidth / 2, panelHeight - biteHeight - TOOLBARHEIGHT, biteImages.get(0), biteWidth, biteHeight);
            Ball ball = new Ball(panelWidth / 2 - 12.5, bite.y - 26);
            ball.setFireBall(fireBallImage);
            gameBalls.add(ball);
            stickyPoint = bite.x;
        } else isGameover = true;
    }

    public boolean isGameover() {
        return isGameover;
    }
}


