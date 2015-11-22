import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Bullet Class extends Sprite Class: Bullet Item Class
 */
public class Bullet extends Sprite {

    public Bullet(double x, double y, BufferedImage image) {
        super(x, y, image, false);
        spdy = 3;
    }

    @Override
    protected void updateSprite() {
        y -= spdy;
    }

    public boolean isCollision(Brick brick) {
        Rectangle rect = new Rectangle((int) x, (int) y, width, height);
        return rect.intersectsLine(brick.x, brick.y + brick.height, brick.x + brick.width, brick.y + brick.height);

    }
}
