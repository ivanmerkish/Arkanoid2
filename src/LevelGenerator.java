import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Level Generator: Generates Level from level text file
 */
public class LevelGenerator {

    private CopyOnWriteArrayList<Brick> bricks;
    private File levelFile;
    private int panelWidth, panelHeight;

    public LevelGenerator(String levelName) {
        try {
            this.levelFile = new File(getClass().getResource("/levels/" + levelName).toURI());
        } catch (URISyntaxException e) {
            System.out.println("Open Level File Error");
        }
        this.bricks = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<Brick> buildLevel() throws IOException {

        FileInputStream fis = new FileInputStream(levelFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        ArrayList<String> lvllist = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lvllist.add(line);
        }

        for (int i = 0; i < lvllist.size(); i++) {
            String[] lvlbr = lvllist.get(i).split(";");
            int brickW, brickH;
            brickH = panelHeight * 3 / 4;
            brickW = panelWidth / lvlbr.length;
            int bi=0;
            for (String txtBrick :
                    lvlbr) {
                String[] params = txtBrick.split(",");
                if (!params[0].equals("0")) {
                    Color aColor = Color.BLACK;

                    try {
                        aColor = (Color) Color.class.getField(params[1]).get(null);
                    } catch (Exception e) {
                        System.out.println("Color Assignment Error");
                    }
                    if (params[0].equals("1")) {
                        bricks.add(new Brick(brickW * bi, brickH / lvllist.size() * i, null, aColor, 1, brickW, brickH));
                        bi++;
                        continue;
                    }
                    if (params[0].equals("2")) {
                        bricks.add(new Brick(brickW * bi, brickH / lvllist.size() * i, new PowerUP(brickW * bi, brickH / lvllist.size() * i), aColor, 1, brickW, brickH));
                        bi++;
                        continue;
                    }
                    if (params[0].equals("3")) {
                        bricks.add(new Brick(brickW * bi, brickH / lvllist.size() * i, null, aColor, 2, brickW, brickH));
                        bi++;
                        continue;
                    }
                    if (params[0].equals("4")) {
                        bricks.add(new Brick(brickW * bi, brickH / lvllist.size() * i, null, aColor, -1, brickW, brickH));
                        bi++;
                    }
                }

            }
        }

        br.close();
        fis.close();
        return bricks;
    }

    public void setPanelWidth(int panelWidth) {
        this.panelWidth = panelWidth;
    }

    public void setPanelHeight(int panelHeight) {
        this.panelHeight = panelHeight;
    }
}
