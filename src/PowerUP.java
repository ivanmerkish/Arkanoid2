import java.awt.*;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class PowerUP extends Sprite {


    private powerUpEffect powerUpEffect;

    public PowerUP(double x, double y) {
        super(x, y, null, false);
    }

    public PowerUP.powerUpEffect getPowerUpEffect() {
        return powerUpEffect;
    }

    public void setPowerUpEffect(PowerUP.powerUpEffect powerUpEffect) {
        this.powerUpEffect = powerUpEffect;
        switch (powerUpEffect){

            case NORMAL:
                break;
            case FAST:
                break;
            case SLOW:
                break;
            case FIREBALL:
                break;
            case SMALL:
                break;
            case LARGE:
                break;
            case GROW:
                break;
            case SHRINK:
                break;
            case WEAPON:
                break;
            case TRIPLE:
                break;
            case LIFE:
                break;
        }
    }

    @Override
    protected void drawSprite(Graphics graphics) {
        super.drawSprite(graphics);
    }

    @Override
    protected void updateSprite() {
        y+=spdy;
    }
}
