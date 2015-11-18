import java.awt.*;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class Brick extends Sprite {
    private int cycle;
    private PowerUP powerUP;
    private Color color;
    private int hardness;

    public Brick(double x, double y,PowerUP powerUP,Color color, int hardness) {
        super(x, y, null, true);
        cycle = 0;
        this.powerUP = powerUP;
        this.color = color;
        this.hardness = hardness;

    }


    @Override
    protected void drawSprite(Graphics graphics) {
        super.drawSprite(graphics);
        Graphics2D graphics2D = (Graphics2D)graphics;
        setQuality(graphics2D);
        GradientPaint gr;
        if (cycle==0) {
            gr = new GradientPaint((float) x, (float) y, Color.WHITE, (float) x + width, (float) y + height, color, true);
            cycle++;
        }
        else{
            gr = new GradientPaint((float) x, (float) y, color, (float) x + width, (float) y + height, Color.WHITE, true);
            cycle--;
        }
        graphics2D.setPaint(gr);
        graphics2D.fillRoundRect((int)x,(int)y,width,height,2,2);
    }

    public PowerUP getPowerUP() {
        return powerUP;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }
}
