import javax.sound.sampled.*;
import java.util.HashMap;

/**
 * SoundEffectManger Class: uses to manage all sound events.
 */
public class SoundEffectManager implements Runnable {

    private AudioInputStream inputStream;
    private HashMap<String, String> effects;

    public SoundEffectManager() {

        effects.put("transform", "board_transform.wav");
        effects.put("ball2biteCollision", "collision_ball_board.wav");
        effects.put("ball2brickCollision", "collision_ball_brick_3.wav");
        effects.put("ballCollision", "collision_ball_event.wav");
        effects.put("bulletCollision", "collision_shot_brick.wav");
        effects.put("lostBallEvent", "event_ball_lost.wav");
        effects.put("lifeLooseEvent", "event_lose_life.wav");
        effects.put("lvlCompleteEvent", "event_win_level_planets.wav");
        effects.put("brickExplosionEvent", "normal_star_explosion.wav");
        effects.put("fireBallEvent", "transformer_fire_ball.wav");


    }

    void loadEffectToStream(String effectFile) {
        try {
            inputStream = AudioSystem.getAudioInputStream(
                    getClass().getResourceAsStream("/mp3/" + effectFile));
        } catch (Exception e) {
            System.out.println("effect file error");
        }
    }

    @Override
    public void run() {
        try {
            AudioFormat format = inputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

