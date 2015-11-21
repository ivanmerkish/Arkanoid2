import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameField Class: Game engine
 */
public class GameField {

    private static final String[] LEVELS = {"level1", "level2", "level3", "level4", "level5"};
    protected CopyOnWriteArrayList<Ball> gameBalls;
    protected CopyOnWriteArrayList<Brick> bricks;
    protected CopyOnWriteArrayList<PowerUP> powerUPs;
    protected CopyOnWriteArrayList<Bullet> bullets;
    protected Bite bite;
    private int panelWidth;
    private int panelHeight;
    private LevelGenerator lg;
    private int lifeCount;
    private int levelCounter;
    private BufferedImage[] powerUPSImages, biteImages;
    private BufferedImage bulletImage, fireBallImage;

    public GameField(BufferedImage[] powerUPSImages, BufferedImage[] biteImages, BufferedImage bulletImage, BufferedImage fireBallImage) {
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

        levelCounter = 0;
        lifeCount = 3;
        init();

    }

    public void init() {
        lg = new LevelGenerator(LEVELS[levelCounter]);
        try {
            bricks = lg.buildLevel();
        } catch (IOException e) {
            System.out.println("Level Build Error");
        }
        bite = new Bite(panelWidth / 2 - 30 * 2, panelHeight - 40, biteImages[0]);
        Ball ball = new Ball(panelWidth / 2 - 30, bite.y - 30);
        ball.setFireBall(fireBallImage);
        gameBalls.add(ball);

    }

    public void updateGameField(KeyEvent event){
        updateAllItems();
        collisionCheck();

    }

    private void updateAllItems() {
        for (Ball ball : gameBalls) {
            ball.updateSprite();
        }
        for (Bullet bullet : bullets) {
            bullet.updateSprite();
        }
        if (bite.isWeapon) {
            bite.image = biteImages[1];
        } else {
            bite.image = biteImages[0];
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
                    } else if (brick.getHardness() - 1 > 0) {
                        brick.setHardness(brick.getHardness() - 1);
                    } else {
                        brick.setHardness(-1);
                    }
                }
            }
            b.isCollision(bite);
            isBorder(b);
        }
        //powerUP collision;
        for (PowerUP powerUP : powerUPs) {
            if (bite.isCollision(powerUP) || isBorder(powerUP)) {
                powerUPs.remove(powerUP);
            }
        }
    }

    private boolean isBorder(Sprite sprite) {
        Line2D upperBorder = new Line2D.Double(0, 0, panelWidth, panelHeight);
        Line2D leftBorder = new Line2D.Double(0, 0, 0, panelHeight);
        Line2D rightBorder = new Line2D.Double(panelWidth, 0, panelWidth, panelHeight);
        Line2D bottomBorder = new Line2D.Double(0, panelHeight, panelWidth, panelHeight);
        Rectangle2D rectangle = new Rectangle.Double(sprite.x, sprite.y, sprite.width, sprite.height);
        if (sprite instanceof Ball) {
            if (((Ball) sprite).isCollision(upperBorder) || ((Ball) sprite).isCollision(leftBorder) ||
                    ((Ball) sprite).isCollision(rightBorder) || ((Ball) sprite).isCollision(bottomBorder)) {
                return true;
            }
        }
        if (sprite instanceof PowerUP) {

            if (rectangle.intersectsLine(bottomBorder)) {
                return true;
            }
        }
        return false;
    }


}


