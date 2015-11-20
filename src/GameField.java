import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameField Class: Game engine
 */
public class GameField {

    private static final String[] LEVELS = {"level1", "level2", "level3", "level4", "level5"};

    private int panelWidth;
    private int panelHeight;

    private CopyOnWriteArrayList<Ball> gameBalls;
    private CopyOnWriteArrayList<Brick> bricks;
    private CopyOnWriteArrayList<PowerUP> powerUPs;
    private CopyOnWriteArrayList<Bullet> bullets;
    private Bite bite;
    private LevelGenerator lg;
    private int lifeCount;
    private int levelCounter;

    public GameField() {
        this.gameBalls = new CopyOnWriteArrayList<>();
        this.bricks = new CopyOnWriteArrayList<>();
        this.powerUPs = new CopyOnWriteArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.bite = null;
        this.lg = null;
        levelCounter = 0;
        lifeCount = 3;

    }

    public void init() {
        lg = new LevelGenerator(LEVELS[levelCounter]);
        try {
            bricks = lg.buildLevel();
        } catch (IOException e) {
            System.out.println("Level Build Error");
        }

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




    /**
     * Class Getters and Setters
     **/
    public CopyOnWriteArrayList<Ball> getGameBalls() {
        return gameBalls;
    }

    public void setGameBalls(CopyOnWriteArrayList<Ball> gameBalls) {
        this.gameBalls = gameBalls;
    }

    public CopyOnWriteArrayList<Brick> getBreaks() {
        return bricks;
    }

    public void setBreaks(CopyOnWriteArrayList<Brick> breaks) {
        this.bricks = breaks;
    }

    public CopyOnWriteArrayList<PowerUP> getPowerUPs() {
        return powerUPs;
    }

    public void setPowerUPs(CopyOnWriteArrayList<PowerUP> powerUPs) {
        this.powerUPs = powerUPs;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }

    public Bite getBite() {
        return bite;
    }

    public void setBite(Bite bite) {
        this.bite = bite;
    }

    public LevelGenerator getLg() {
        return lg;
    }

    public void setLg(LevelGenerator lg) {
        this.lg = lg;
    }
}


