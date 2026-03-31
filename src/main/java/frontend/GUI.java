package frontend;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public  GUI(){
        setTitle("OTech Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createStartMenu(), "start");

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "start");

        setVisible(true);
    }

    private JPanel createStartMenu() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("table_start.jpeg");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(350, 250));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 0, 0, 170));

        JLabel titleLabel = new JLabel("OTech Blackjack");
        titleLabel.setFont(loadCustomFont(40f));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newGameButton = new JButton("Start New Game");
        JButton joinGameButton = new JButton("Join Game");

        Font buttonFont = loadCustomFont(25f);
        newGameButton.setFont(buttonFont);
        joinGameButton.setFont(buttonFont);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.setMaximumSize(new Dimension(220, 45));
        joinGameButton.setMaximumSize(new Dimension(220, 45));

        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(35));
        menuPanel.add(newGameButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(joinGameButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(menuPanel);

        return backgroundPanel;
    }

    private Font loadCustomFont(float size) {
        try (java.io.InputStream is = getClass().getClassLoader()
                .getResourceAsStream("fonts/Harlow Solid Regular.ttf")) {

            System.out.println(is);

            if (is == null) {
                throw new RuntimeException("Font file not found.");
            }

            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is);
            return customFont.deriveFont(size);

        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.BOLD, (int) size);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
