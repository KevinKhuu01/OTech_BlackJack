package frontend.GUIClasses.Styling;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {

    private Image backgroundImage;

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
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}