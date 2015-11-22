import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Paddle Class Extends Sprite Class
 */
public class Paddle extends Sprite {

    protected boolean isWeapon;
    protected boolean isLeftBorder, isRightBorder;
    protected int defX;
    KeyEvent keyEvent, spaceKeyEvent;
    private int normalWidth = WIDTH * 2;
    private boolean isSticky;
    private int bulletCount, glueCounter;
    private boolean isNewLife;
    private PowerUpEffect ballPowerUpEffect;
    private Thread effectThread;


    public Paddle(double x, double y, BufferedImage image, int width, int height) {
        super(x, y, image, false);
        isSticky = true;
        isWeapon = false;
        isNewLife = false;
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
        defX = 0;
        if (keyEvent != null) {
            spdx = 10;
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (!isLeftBorder) {
                        defX = (int) x;
                        x -= spdx;
                        defX = (int) x - defX;
                        isRightBorder = false;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!isRightBorder) {
                        defX = (int) x;
                        x += spdx;
                        defX = (int) x - defX;
                        isLeftBorder = false;
                    }
                    break;
            }


        }
        powerUPManagement();
        resize();

    }

    private void resize() {

        if (scale > 1) {
            if (normalWidth * scale > width) {
                if (isLeftBorder) {
                    width += 3;
                }
                if (isRightBorder) {
                    x--;
                    width += 1;
                }
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
                if (!isLeftBorder) {
                    x--;
                }
                width += 2;
                return;
            }
            if (width > normalWidth) {
                x++;
                width -= 2;
            }

        }
    }

    public boolean isCollision(PowerUP powerUP) {
        Rectangle2D powerUPBound = new Rectangle2D.Double(powerUP.x, powerUP.y, powerUP.width, powerUP.height);
        Rectangle2D biteBound = new Rectangle2D.Double(x, y, width, height);
        if (biteBound.intersects(powerUPBound)) {
            powerUpEffect = powerUP.getPowerUpEffect();
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
                    effectThread = new Thread(new SoundEffectManager("transform"));
                    effectThread.run();
                    break;
                case GROW:
                    effectThread = new Thread(new SoundEffectManager("transform"));
                    effectThread.run();
                    if (scale < 1) scale = 1;
                    if (scale < 2) scale += 0.5;
                    break;
                case SHRINK:
                    effectThread = new Thread(new SoundEffectManager("transform"));
                    effectThread.run();
                    if (scale > 1) scale = 1;
                    if (!(scale < 0.25)) scale *= 0.5;
                    break;
                case WEAPON:
                    effectThread = new Thread(new SoundEffectManager("transform"));
                    effectThread.run();
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

    public int getBulletCount() {
        return bulletCount;
    }

    public void setBulletCount(int bulletCount) {
        this.bulletCount = bulletCount;
    }

}
