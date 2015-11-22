import javax.sound.sampled.*;

/**
 * MusicPlayer Class: plays background music;
 */

public class MusicPlayer implements Runnable {

    private AudioInputStream inputStream;

    public MusicPlayer() {
        try {
            inputStream = AudioSystem.getAudioInputStream(
                    getClass().getResourceAsStream("/mp3/track1.wav"));
        } catch (Exception e) {
            System.out.println("Music file error");
        }
    }

    @Override
    public void run() {
        try {
            AudioFormat format = inputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
