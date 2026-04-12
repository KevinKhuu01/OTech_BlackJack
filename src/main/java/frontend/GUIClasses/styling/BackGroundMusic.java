package frontend.GUIClasses.styling;

import javax.sound.sampled.*;
import java.io.File;

public class BackGroundMusic {
    private Clip clip;
    /** Method to handle background music during gameplay, using a WAV file
     * @param filePath: The pathway directory of the .WAV file containing the music for the game.
     **/
    public void playMusic(String filePath){
        try {
            File file = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        if (clip != null){
            clip.stop();
        }
    }
}
