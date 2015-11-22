import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

/**
 * PowerUP Class extends Sprite Class: PowerUP Item Class
 */
public class PowerUP extends Sprite {


    public PowerUP(double x, double y, int width, int height) {
        super(x, y, null, false);
        this.width = width;
        this.height = height;
        spdy = 3;
        setPowerUpEffect(PowerUpEffect.getRandom());
        try {
            image = ImageIO.read(new File(getClass().getResource("/img/" + getPowerUpEffect().toString().toLowerCase() + "PU.png").toURI()));
        } catch (Exception e) {
            System.out.println("PowerUP: " + getPowerUpEffect().toString() + " image load error ");
            e.printStackTrace();
        }
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
        y += spdy;
    }
}
