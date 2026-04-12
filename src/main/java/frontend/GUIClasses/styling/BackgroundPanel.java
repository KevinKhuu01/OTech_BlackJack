package frontend.GUIClasses.styling;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image backgroundImage;

    /** Handles the background of the pages as user navigates throught the application
     * @param imagePath: The directory pathway of the needed image file
     **/
    public BackgroundPanel(String imagePath) {
        java.net.URL imageURL = getClass().getClassLoader().getResource(imagePath);

        if (imageURL != null) {
            backgroundImage = new ImageIcon(imageURL).getImage();
        } else {
            System.out.println("Could not find image: " + imagePath);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clears page

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}