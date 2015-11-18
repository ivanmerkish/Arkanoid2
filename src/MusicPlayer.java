import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;

/**
 * Created by Ivan Merkish on 11/13/2015.
 */

public class MusicPlayer implements Runnable {
    private HashMap<String,URL> musicFiles;
    private Media media;
    private MediaPlayer mp;
    private String track;

    public MusicPlayer() {
        musicFiles = new HashMap();
        musicFiles.put("theme",getClass().getResource("/mp3/Ricochet_Xtreme_Theme.mp3"));
        musicFiles.put("normal",getClass().getResource("/mp3/Riocchet_Xtreme_Space_Normal.mp3"));
        musicFiles.put("slow",getClass().getResource("/mp3/Ricochet_Xtreme_Space_Slow.mp3"));
        musicFiles.put("fast",getClass().getResource("/mp3/Riocchet_Xtreme_Space_Speed.mp3"));
        media = null;
        mp = null;
        track = "";
    }

    @Override
    public void run() {
        if (!track.equals("")){
            media = new Media(musicFiles.get(track).toString());
            if(mp!=null){
                mp.stop();

            }
            mp = new MediaPlayer(media);
            mp.play();
            mp.setOnEndOfMedia(new Runnable() {
                @Override
                public void run() {
                    mp.seek(Duration.ZERO);
                }
            });
        }
    }

    public void setTrack(String track) {
        this.track = track;
    }
}
