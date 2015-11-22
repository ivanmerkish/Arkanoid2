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
    protected boolean isLeftBorder, isRightBorder;
    KeyEvent keyEvent;
    MouseEvent mouseEvent;
    private int normalWidth = WIDTH * 2;
    private boolean isSticky;
    private int bulletCount, glueCounter;
    private boolean isNewLife;
    private int newWidth;
    private boolean isFirstLaunch;
    private boolean isNewPowerUP;
    private PowerUpEffect ballPowerUpEffect;


    public Bite(double x, double y, BufferedImage image, int width, int height) {
        super(x, y, image, false);
        isSticky = true;
        isWeapon = false;
        isNewLife = false;
        isFirstLaunch = true;
        normalWidth = width;
        this.width = normalWidth;
        this.height = height;
        spdx = 0;
        isLeftBorder = false;
        isRightBorder = false;
        ballPowerUpEffect = null;


    }

    @Override
    protected void drawSprite(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.drawImage(image, (int) x, (int) y, width, height, null);

    }

    @Override
    protected void updateSprite() {
        if (keyEvent != null) {
            spdx = 10;
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (!isLeftBorder) {
                        x -= spdx;
                        isRightBorder = false;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!isRightBorder) {
                        x += spdx;
                        isLeftBorder = false;
                    }
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
                    if (!(scale < 0.25)) scale *= 0.5;
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
                    break;
                default:
                    ballPowerUpEffect = powerUpEffect;
                    break;

            }
            powerUpEffect = null;
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

    public void setSticky(boolean sticky) {
        isSticky = sticky;
    }

    public PowerUpEffect getBallPowerUpEffect() {
        return ballPowerUpEffect;
    }

    public void setBallPowerUpEffect(PowerUpEffect ballPowerUpEffect) {
        this.ballPowerUpEffect = ballPowerUpEffect;
    }

    public int getGlueCounter() {
        return glueCounter;
    }

    public void setGlueCounter(int glueCounter) {
        this.glueCounter = glueCounter;
    }
}
