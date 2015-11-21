import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Bite Class Extends Sprite Class
 */
public class Bite extends Sprite{

    protected boolean isWeapon;
    KeyEvent keyEvent;
    MouseEvent mouseEvent;
    private int normalWidth = WIDTH * 2;
    private boolean isSticky;
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
        normalWidth = image.getWidth();
        spdx = 0;

    }

    @Override
    protected void drawSprite(Graphics graphics) {
        super.drawSprite(graphics);

    }

    @Override
    protected void updateSprite() {
        if (keyEvent != null) {
            spdx = 10;
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    x -= spdx;
                    break;
                case KeyEvent.VK_RIGHT:
                    x += spdx;
                    break;
            }

        }
        if (mouseEvent != null) {
            if (x - mouseEvent.getX() > 0) {
                x += spdx;
            }
            if (x - mouseEvent.getX() < 0) {
                x -= spdx;
            }
        }
        spdx = 0;
        powerUPManagement();
        resize();

    }

    private void resize() {
        if (scale > 1) {
            if (normalWidth * scale > width) {
                x--;
                width += 2;
                return;
            }

        }
        if (scale < 1) {
            if (normalWidth * scale < width) {
                x++;
                width -= 2;
                return;
            }

        }
        if (scale == 1) {
            if (width < normalWidth) {
                x--;
                width += 2;
                return;
            }
            if (width > normalWidth) {
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
        if (powerUpEffect != null) {
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
    }

    public boolean isNewLife() {
        return isNewLife;
    }

    public void setNewLife(boolean newLife) {
        isNewLife = newLife;
    }

    public boolean isSticky() {
        return isSticky;
    }
}
