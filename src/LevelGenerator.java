import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Level Generator: Generates Level from level text file
 */
public class LevelGenerator {

    private CopyOnWriteArrayList<Brick> bricks;
    private File levelFile;

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
        String line;
        int li = 0;
        while ((line = br.readLine()) != null) {
            String[] lvlbr=line.split(";");
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

                        bricks.add(new Brick(30 * bi, 20 * li, null, aColor, 1));
                    }
                    if (params[0].equals("2")) {

                        bricks.add(new Brick(30 * bi, 20 * li, new PowerUP(30 * bi, 20 * li), aColor, 1));
                    }
                    if (params[0].equals("3")) {
                        bricks.add(new Brick(30 * bi, 20 * li, null, aColor, 2));
                    }
                    if (params[0].equals("4")) {
                        bricks.add(new Brick(30 * bi, 20 * li, null, aColor, -1));
                    }
                }
            }
            li++;
        }

        br.close();
        return bricks;
    }


}
