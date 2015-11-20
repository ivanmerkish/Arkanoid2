import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Bite Class Extends Sprite Class
 */
public class Bite extends Sprite{

    private static final int NORMALWIDTH = WIDTH * 2;

    KeyEvent event;
    private boolean isSticky;
    private boolean isWeapon;
    private int bulletCount, glueCounter;
    private boolean isNewLife;
    private int newWidth;
    private boolean isFirstLaunch;
    private boolean isNewPowerUP;


    public Bite(double x, double y, BufferedImage image) {
        super(x, y, image, false);
        isSticky = true;
        isWeapon = false;
        isNewLife = false;
        isFirstLaunch = true;

    }

    @Override
    protected void drawSprite(Graphics graphics) {
        super.drawSprite(graphics);

    }

    @Override
    protected void updateSprite() {
        switch (event.getKeyCode()){
            case KeyEvent.VK_LEFT : x-=spdx;
                break;
            case KeyEvent.VK_RIGHT: x+=spdx;
                break;
        }
        powerUPManagement();
        resize();

    }

    private void resize() {
        if (scale > 1) {
            if (NORMALWIDTH * scale > width) {
                x--;
                width += 2;
                return;
            }

        }
        if (scale < 1) {
            if (NORMALWIDTH * scale < width) {
                x++;
                width -= 2;
                return;
            }

        }
        if (scale == 1) {
            if (width < NORMALWIDTH) {
                x--;
                width += 2;
                return;
            }
            if (width > NORMALWIDTH) {
                x++;
                width -= 2;
                return;
            }

        }
        isNewPowerUP = false;
    }

    public boolean isCollision(PowerUP powerUP) {
        Rectangle2D powerUPBound = new Rectangle2D.Double(powerUP.x, powerUP.y, powerUP.width, powerUP.height);
        Rectangle2D biteBound = new Rectangle2D.Double(x, y, width, height);
        if (biteBound.intersects(powerUPBound)) {
            powerUpEffect = powerUP.getPowerUpEffect();
            if (powerUpEffect != PowerUpEffect.LIFE)
                isNewPowerUP = true;
            return true;
        }

        return false;
    }

    private void powerUPManagement() {
        switch (powerUpEffect) {
            case NORMAL:
                scale = 1;
                isSticky = false;
                isWeapon = false;
                break;
            case GROW:
                if (scale < 1) scale = 1;
                if (scale < 2) scale += 0.5;
                break;
            case SHRINK:
                if (scale > 1) scale = 1;
                if (scale < 0.25) scale *= 0.5;
                break;
            case WEAPON:
                bulletCount += 10;
                isWeapon = true;
                break;
            case LIFE:
                isNewLife = true;
                break;
            case GLUE:
                isSticky = true;
                glueCounter = 5;

        }
    }

    public boolean isNewLife() {
        return isNewLife;
    }

    public void setNewLife(boolean newLife) {
        isNewLife = newLife;
    }
}
