package frontend.GUIClasses.styling;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BackgroundMusic {
    private Clip clip;
    private boolean isMuted;
    private List<JButton> registeredButtons = new ArrayList<>();

    /** Handles behavior of music and retreival of music file
     * @param filePath The directory where the music file is found
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

    /** Adds a button to the list and sets its initial text
     * @param button The register button
     **/
    public void registerButton(JButton button) {
        registeredButtons.add(button);
        button.setText(isMuted ? "Music On" : "Music Off");
    }

    /** Boolean method that flips the mute state and updates every single button at the same time
     * @return isMuted; can be true or false
     **/
    public boolean toggleMute() {
        isMuted = !isMuted;
        for (JButton btn : registeredButtons) {
            btn.setText(isMuted ? "Music On" : "Music Off");
        }
        return isMuted;
    }

    public void stopMusic(){
        if (clip != null){
            clip.stop();
        }
    }
}
