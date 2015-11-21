import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Ball Class extend Sprite Class: game Ball
 */
public class Ball extends Sprite {
    private static final double baseSpdx = 1;
    private static final double baseSpdy = 1;

    private double angle;
    private AffineTransform af;
    private PowerUpEffect currPowerUpEffect;
    private PowerUpEffect lastPowerUpEffect;

    private BufferedImage fireBall;
    private boolean isGlued;

    public Ball(double x, double y) {
        super(x, y, null, false);
        this.angle = 10;
        this.currPowerUpEffect = PowerUpEffect.NORMALBALL;
        af = new AffineTransform();
        fireBall = null;
        width = 25;
        height = 25;
        isGlued = true;


    }

    @Override
    protected void drawSprite(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        setQuality(graphics2D);
        graphics2D.setTransform(af);
        if (currPowerUpEffect != PowerUpEffect.FIREBALL) {
            graphics2D.setColor(new Color(0xABABAB));
            graphics2D.fillOval((int) x, (int) y, width, height);
        } else {
            graphics2D.drawImage(fireBall, (int) x, (int) y, width, height, null);
        }
    }

    @Override
    protected void updateSprite() {
        switch (currPowerUpEffect){
            case NORMAL:
                if (lastPowerUpEffect == PowerUpEffect.SMALL) {
                    af.scale(2,2);
                }
                if (lastPowerUpEffect == PowerUpEffect.LARGE) {
                    af.scale(0.5,0.5);
                }
                if (lastPowerUpEffect == PowerUpEffect.FAST || lastPowerUpEffect == PowerUpEffect.SLOW) {
                    spdx = baseSpdx;
                    spdy = baseSpdy;
                }
                break;
            case FAST: spdx *=2;
                break;

            case SLOW: spdx *=0.5;
                break;
            case LARGE:
                if (lastPowerUpEffect != PowerUpEffect.LARGE) {
                    af.scale(2,2);
                }
                break;
            case SMALL:
                if (lastPowerUpEffect != PowerUpEffect.SMALL)
                    af.scale(0.5,0.5);
        }
        if (!isGlued) {//
            spdx = Math.sin(Math.toRadians(angle)) * 3;
            spdy = -Math.cos(Math.toRadians(angle)) * 3;

        }
        x += spdx;
        y += spdy;
    }

    public boolean isCollision(Sprite sprite) {
        Rectangle rect = new Rectangle((int) x, (int) y, width, height);
        if (rect.intersectsLine(sprite.x, sprite.y, sprite.x + sprite.width, sprite.y) ||
                rect.intersectsLine(sprite.x, sprite.y + sprite.height, sprite.x + sprite.width, sprite.y + sprite.height)) {
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

    public boolean isCollision(Line2D line) {
        Rectangle rect = new Rectangle((int) x, (int) y, width, height);

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

    public void setFireBall(BufferedImage fireBall) {
        this.fireBall = fireBall;
    }

    public boolean isGlued() {
        return isGlued;
    }

    public void setGlued(boolean glued) {
        isGlued = glued;
    }
}
