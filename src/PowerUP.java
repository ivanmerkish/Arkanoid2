import java.awt.*;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class PowerUP extends Sprite {




    public PowerUP(double x, double y) {
        super(x, y, null, false);
    }

    public PowerUP.PowerUpEffect getPowerUpEffect() {
        return powerUpEffect;
    }

    public void setPowerUpEffect(PowerUP.PowerUpEffect powerUpEffect) {
        this.powerUpEffect = powerUpEffect;

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
