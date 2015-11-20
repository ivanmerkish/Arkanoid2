import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class Sprite {

    protected static final int WIDTH = 30;
    protected static final int HEIGHT = 10;

    protected int width;
    protected int height;
    protected double spdx;
    protected double spdy;
    protected double x;
    protected double y;
    protected double scale;
    protected BufferedImage image;
    protected boolean isStatic;
    protected PowerUpEffect powerUpEffect;

    public Sprite(double x, double y, BufferedImage image, boolean isStatic) {
        if (image == null){
            this.width = WIDTH;
            this.height = HEIGHT;
        }
        else {
            this.width = image.getWidth();
            this.height = image.getHeight();
            this.image = image;
        }
        this.x = x;
        this.y = y;
        this.isStatic = isStatic;
    }

    protected void drawSprite(Graphics graphics){
        Graphics2D graphics2D = (Graphics2D)graphics;
        setQuality(graphics2D);
        graphics2D.drawImage(image,(int)x,(int)y,null);

    }

    protected void updateSprite(){
        if(!isStatic){
            x+=spdx;
        }
        colorAnimation();

    }

    protected void colorAnimation(){

    }

    protected void setQuality(Graphics2D graphics2D){
        RenderingHints aarh = new RenderingHints(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        RenderingHints crqrh = new RenderingHints(RenderingHints.KEY_COLOR_RENDERING,RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        RenderingHints rqrh = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHints(aarh);
        graphics2D.setRenderingHints(crqrh);
        graphics2D.setRenderingHints(rqrh);
    }

    protected enum PowerUpEffect {
        NORMAL, FAST, SLOW, FIREBALL, SMALL, LARGE, GROW, SHRINK, WEAPON, TRIPLE, LIFE, NORMALBALL, GLUE;

        public static PowerUpEffect getRandom() {
            return values()[(int) (Math.random() * values().length)];
        }
    }

}
