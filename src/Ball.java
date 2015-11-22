import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Ball Class extend Sprite Class: game Ball
 */
public class Ball extends Sprite {

    private static final int DIF_SIZE = 25;
    private static final int DIF_SPEED = 3;
    private double angle, oldAngle;
    private AffineTransform af;
    private PowerUpEffect currPowerUpEffect;
    private PowerUpEffect lastPowerUpEffect;
    private int speedDef;
    private BufferedImage fireBall;
    private boolean isGlued, isFireBall;

    public Ball(double x, double y) {
        super(x, y, null, false);
        this.angle = 10;
        this.currPowerUpEffect = null;
        af = new AffineTransform();
        fireBall = null;
        width = DIF_SIZE;
        height = DIF_SIZE;
        isGlued = true;
        speedDef = DIF_SPEED;
        isFireBall = false;


    }

    @Override
    protected void drawSprite(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        setQuality(graphics2D);
        if (angle != oldAngle) {
            af.rotate(Math.toRadians(angle), x + width / 2, y + height / 2);
            oldAngle = angle;
        }

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
                case NORMAL:
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
                    lastPowerUpEffect = PowerUpEffect.FAST;
                    break;

                case SLOW:
                    if (speedDef > 2) {

                        speedDef *= 0.5;
                    }
                    lastPowerUpEffect = PowerUpEffect.SLOW;
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
                if ((rect.getX() + rect.getWidth() / 2) < (sprite.x + sprite.width / 3)) {
                    angle = angle - 5;
                }
                if ((rect.getX() + rect.getWidth() / 2) > (sprite.x + sprite.width / 3 * 2)) {
                    angle = angle + 5;
                }
                if (!isGlued) {
                    Thread t = new Thread(new SoundEffectManager("ball2biteCollision"));
                    t.run();
                }
            }

            angle = 180 - angle;
            return true;
        }
        if (rect.intersectsLine(sprite.x, sprite.y, sprite.x, sprite.y + sprite.height)
                || rect.intersectsLine(sprite.x + sprite.width, sprite.y, sprite.x + sprite.width, sprite.y + sprite.height)) {

            angle = 360 - angle;
            return true;
        }
        return false;
    }


    public PowerUpEffect getCurrPowerUpEffect() {
        return currPowerUpEffect;
    }

    public void setCurrPowerUpEffect(PowerUpEffect currPowerUpEffect) {
        this.lastPowerUpEffect = this.currPowerUpEffect;
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

}
