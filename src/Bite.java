import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class Bite extends Sprite{

    KeyEvent event;




    public Bite(double x, double y, BufferedImage image) {
        super(x, y, image, false);
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
    }

    public boolean isCollision(PowerUP powerUP) {
        Rectangle2D powerUPBound = new Rectangle2D.Double(powerUP.x, powerUP.y, powerUP.width, powerUP.height);
        Rectangle2D biteBound = new Rectangle2D.Double(x, y, width, height);
        if (biteBound.intersects(powerUPBound)) {

        }

        return false;
    }
}
