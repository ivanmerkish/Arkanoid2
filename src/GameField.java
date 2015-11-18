import java.awt.event.KeyEvent;
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
        for (Ball b:gameBalls) {
            for (Brick brick:bricks) {
                if (brick.getHardness() - 1 == 0) {
                    if (brick.getPowerUP()!=null){
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


