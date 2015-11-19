import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class Ball extends Sprite {
    private static final double baseSpdx = 1;
    private static final double baseSpdy = 1;

    private double angle;
    private AffineTransform af;
    private powerUpEffect currPowerUpEffect, lastPowerUpEffect;
    public Ball(double x, double y, BufferedImage image) {
        super(x, y, image, false);
        this.angle=0;
        this.currPowerUpEffect = powerUpEffect.NORMAL;
        af = new AffineTransform();
    }

    @Override
    protected void drawSprite(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        setQuality(graphics2D);
        graphics2D.setTransform(af);
        if (currPowerUpEffect != powerUpEffect.FIREBALL) {
            graphics2D.setColor(new Color(0xABABAB));
            graphics2D.fillOval((int) x, (int) y, width, height);
        } else {

        }
    }

    @Override
    protected void updateSprite() {
        switch (currPowerUpEffect){
            case NORMAL:
                if(lastPowerUpEffect == powerUpEffect.SMALL){
                    af.scale(2,2);
                }
                if(lastPowerUpEffect == powerUpEffect.LARGE){
                    af.scale(0.5,0.5);
                }
                if(lastPowerUpEffect == powerUpEffect.FAST || lastPowerUpEffect == powerUpEffect.SLOW){
                    spdx = baseSpdx;
                    spdy = baseSpdy;
                }
                break;
            case FAST: spdx *=2;
                break;

            case SLOW: spdx *=0.5;
                break;
            case LARGE:
                if(lastPowerUpEffect != powerUpEffect.LARGE){
                    af.scale(2,2);
                }
                break;
            case SMALL:
                if(lastPowerUpEffect != powerUpEffect.SMALL)
                    af.scale(0.5,0.5);
        }
        spdx = Math.sin(Math.toRadians(angle));
        spdy = -Math.cos(Math.toRadians(angle));
        x += spdx;
        y += spdy;
    }

    public boolean isCollision(Sprite sprite) {
        Rectangle rect = new Rectangle((int) x, (int) y, width, height);
        if (rect.intersectsLine(sprite.x, sprite.y, sprite.x + width, sprite.y)) {
            angle = -angle;
            return true;
        }
        if (rect.intersectsLine(sprite.x, sprite.y + height, sprite.x + width, sprite.y + height)) {
            angle = -angle;
            return true;
        }
        if (rect.intersectsLine(sprite.x, sprite.y, sprite.x, sprite.y + height)) {
            angle = 90 - angle;
            return true;
        }
        if (rect.intersectsLine(sprite.x + width, sprite.y, sprite.x + width, sprite.y + height)) {
            angle = 90 - angle;
            return true;
        }
        return false;
    }

    public powerUpEffect getCurrPowerUpEffect() {
        return currPowerUpEffect;
    }

    public void setCurrPowerUpEffect(powerUpEffect currPowerUpEffect) {
        this.lastPowerUpEffect = this.currPowerUpEffect;
        this.currPowerUpEffect = currPowerUpEffect;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    private enum powerUpEffect {
        NORMAL, FAST, SLOW, FIREBALL, SMALL, LARGE
    }
}
