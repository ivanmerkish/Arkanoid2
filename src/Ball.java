import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Ball Class extend Sprite Class: game Ball
 */
public class Ball extends Sprite {

    private static final int DIF_SIZE = 25;
    private static final int DIF_SPEED = 5;
    private double angle;
    private PowerUpEffect currPowerUpEffect;
    private int speedDef;
    private BufferedImage fireBall;
    private boolean isGlued, isFireBall, isFirstLaunch;

    public Ball(double x, double y, boolean isFirstLaunch) {
        super(x, y, null, false);
        this.angle = 10;
        this.currPowerUpEffect = null;
        fireBall = null;
        width = DIF_SIZE;
        height = DIF_SIZE;
        this.isFirstLaunch = isFirstLaunch;
        if (this.isFirstLaunch) {
            isGlued = true;

        }
        speedDef = DIF_SPEED;
        isFireBall = false;


    }

    @Override
    protected void drawSprite(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        setQuality(graphics2D);

        if (!isFireBall) {
            graphics2D.setColor(new Color(0xABABAB));
            graphics2D.fillOval((int) x, (int) y, width, height);
        } else {
            graphics2D.setColor(new Color(0xFF9436));
            graphics2D.fillOval((int) x, (int) y, width, height);
        }
    }

    @Override
    protected void updateSprite() {
        if (currPowerUpEffect != null) {
            switch (currPowerUpEffect) {
                case NORMALBALL:
                    if (DIF_SIZE != width) {
                        width = DIF_SIZE;
                        height = DIF_SIZE;
                    }
                    if (speedDef != DIF_SPEED) {
                        speedDef = DIF_SPEED;
                    }
                    if (isFireBall) {
                        isFireBall = false;
                    }
                    break;
                case FAST:
                    if (speedDef < 10) {
                        speedDef *= 2;
                    }
                    break;

                case SLOW:
                    if (speedDef > 2) {

                        speedDef *= 0.5;
                    }
                    break;
                case LARGE:
                    if (width <= DIF_SIZE) {
                        y = y - height - 2;
                        width *= 2;
                        height *= 2;

                    }
                    break;
                case SMALL:
                    if (width >= DIF_SIZE) {
                        width *= 0.5;
                        height *= 0.5;
                    }
                    break;
                case FIREBALL:
                    Thread t = new Thread(new SoundEffectManager("fireBallEvent"));
                    t.run();
                    image = fireBall;
                    isFireBall = true;
                    break;

            }
            currPowerUpEffect = null;
        }
        if (!isGlued) {
            spdx = Math.sin(Math.toRadians(angle)) * speedDef;
            spdy = -Math.cos(Math.toRadians(angle)) * speedDef;

        }
        x += spdx;
        y += spdy;
    }

    public boolean isCollision(Sprite sprite) {
        Rectangle rect = new Rectangle((int) x, (int) y, width, height);
        if (rect.intersectsLine(sprite.x, sprite.y, sprite.x + sprite.width, sprite.y) ||
                rect.intersectsLine(sprite.x, sprite.y + sprite.height, sprite.x + sprite.width, sprite.y + sprite.height)) {
            if (sprite instanceof Bite) {
                while (y + height > sprite.y) {
                    y--;
                }
                if ((rect.getX() + rect.getWidth() / 2) < (sprite.x + sprite.width / 3)) {
                    angleChanges(true);
                }
                if ((rect.getX() + rect.getWidth() / 2) > (sprite.x + sprite.width / 3 * 2)) {
                    angleChanges(false);
                }
                if (!isGlued) {
                    Thread t = new Thread(new SoundEffectManager("ball2biteCollision"));
                    t.run();
                }
            }
            if (!isFireBall) {
                angle = 180 - angle;
            } else if (sprite instanceof Bite) {
                angle = 180 - angle;
            }
            return true;
        }
        if (rect.intersectsLine(sprite.x, sprite.y, sprite.x, sprite.y + sprite.height)
                || rect.intersectsLine(sprite.x + sprite.width, sprite.y, sprite.x + sprite.width, sprite.y + sprite.height)) {
            if (!isFireBall) {
                angle = 360 - angle;
            } else if (sprite instanceof Bite) {
                angle = 360 - angle;
            }
            return true;
        }
        return false;
    }


    public PowerUpEffect getCurrPowerUpEffect() {
        return currPowerUpEffect;
    }

    public void setCurrPowerUpEffect(PowerUpEffect currPowerUpEffect) {
        this.currPowerUpEffect = currPowerUpEffect;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isGlued() {
        return isGlued;
    }

    public void setGlued(boolean glued) {
        isGlued = glued;
    }

    public boolean isFireBall() {
        return isFireBall;
    }
//    public boolean isFireBall(){
//
//
//    }

    public void setFireBall(BufferedImage fireBall) {
        this.fireBall = fireBall;
    }

    private void angleChanges(boolean left) {
        if (left) {
            if (angle < 0) {
                angle -= 10;
            } else
                angle += 10;
        } else {
            if (angle < 0) {
                angle += 10;
            } else
                angle -= 10;
        }
        if (angle > 360) {
            angle -= 360;
        }
        if (angle < 360) {
            angle += 360;
        }
        if (angle == 90) {
            if (left) {
                angle += 10;
            } else {
                angle -= 10;
            }
        } else if (angle == -90) {
            if (left) {
                angle -= 10;
            } else {
                angle += 10;
            }
        }
    }
}