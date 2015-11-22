import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

/**
 * Ball Class extend Sprite Class: game Ball
 */
public class Ball extends Sprite {

    private double angle;
    private AffineTransform af;
    private PowerUpEffect currPowerUpEffect;
    private PowerUpEffect lastPowerUpEffect;
    private int speedDef;

    private BufferedImage fireBall;
    private boolean isGlued;

    public Ball(double x, double y) {
        super(x, y, null, false);
        this.angle = 10;
        this.currPowerUpEffect = null;
        af = new AffineTransform();
        fireBall = null;
        width = 25;
        height = 25;
        isGlued = true;
        speedDef = 3;


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
        if (currPowerUpEffect != null) {
            switch (currPowerUpEffect) {
                case NORMAL:
                    if (lastPowerUpEffect == PowerUpEffect.SMALL) {
                        width *= 2;
                        height *= 2;
                        lastPowerUpEffect = PowerUpEffect.NORMAL;
                        //af.scale(2,2);
                    }
                    if (lastPowerUpEffect == PowerUpEffect.LARGE) {
                        width *= 0.5;
                        height *= 0.5;
                        lastPowerUpEffect = PowerUpEffect.LARGE;

                        //af.scale(0.5,0.5);
                    }
                    if (lastPowerUpEffect == PowerUpEffect.FAST || lastPowerUpEffect == PowerUpEffect.SLOW) {
                        speedDef = 3;
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
                    if (lastPowerUpEffect != PowerUpEffect.LARGE) {
                        width *= 2;
                        height *= 2;
                        lastPowerUpEffect = PowerUpEffect.LARGE;
                        //af.scale(2,2);
                    }
                    break;
                case SMALL:
                    if (lastPowerUpEffect != PowerUpEffect.SMALL) {
                        width *= 0.5;
                        height *= 0.5;
                        lastPowerUpEffect = PowerUpEffect.SMALL;
                        //af.scale(0.5, 0.5);
                    }
                    break;
                case FIREBALL:
                    image = fireBall;
                    break;

            }
            currPowerUpEffect = null;
        }
        if (!isGlued) {//
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
//    public boolean isFireBall(){
//
//
//    }

    public void setSpeedDef(int speedDef) {
        this.speedDef = speedDef;
    }
}
