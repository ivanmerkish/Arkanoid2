import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Ivan Merkish on 11/13/2015.
 */
public class GameField {

    private int panelWidth;
    private int panelHeight;

    private CopyOnWriteArrayList<Ball> gameBalls;
    private CopyOnWriteArrayList<Brick> bricks;
    private CopyOnWriteArrayList<PowerUP> powerUPs;
    private Bite bite;
    private LevelGenerator lg;

    public GameField() {
        this.gameBalls = new CopyOnWriteArrayList<>();
        this.bricks = new CopyOnWriteArrayList<>();
        this.powerUPs = new CopyOnWriteArrayList<>();
        this.bite = null;
        this.lg = null;

    }

    public void init() {

    }

    public void updateGameField(KeyEvent event){



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
        Line2D bottomBorder = new Line2D.Double(0, panelWidth, panelWidth, panelHeight);
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


