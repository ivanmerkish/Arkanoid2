import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Ivan Merkish on 11/2/2015.
 */
public class LevelGenerator {

    private String levelName;
    private CopyOnWriteArrayList<Brick> bricks;
    private File levelFile;

    public LevelGenerator(String levelName) {
        this.levelName = levelName;
        try {
            this.levelFile = new File(getClass().getResource("/levels/" + levelName).toURI());
        }
        catch (URISyntaxException e){}
        this.bricks = new CopyOnWriteArrayList<>();
    }
    public CopyOnWriteArrayList buildLevel() throws IOException{

        FileInputStream fis = new FileInputStream(levelFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line = null;
        int li = 0;
        while ((line = br.readLine()) != null) {
            String[] lvlbr=line.split(";");
            int bi=0;
            for (String txtbrick :
                    lvlbr) {
                String[] params = txtbrick.split(",");
                if(params[0].equals("1")){
                    Color aColor = Color.BLACK;
                    try {
                        aColor = (Color) Color.class.getField(params[1]).get(null);
                    }
                    catch (Exception e){}
                    bricks.add(new Brick(30*bi,20*li,null,aColor,1));
                }
            }
            li++;
        }

        br.close();
        return bricks;
    }


}
