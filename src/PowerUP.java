import java.awt.*;

/**
 *  PowerUP Class extends Sprite Class: PowerUP Item Class
 */
public class PowerUP extends Sprite {




    public PowerUP(double x, double y) {
        super(x, y, null, false);
        setPowerUpEffect(PowerUpEffect.getRandom());
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
