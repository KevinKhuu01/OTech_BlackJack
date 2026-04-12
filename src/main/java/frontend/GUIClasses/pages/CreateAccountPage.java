package frontend.GUIClasses.pages;

import frontend.Client;
import frontend.GUIClasses.styling.BackgroundPanel;
import frontend.GUIClasses.styling.CustomFont;

import javax.swing.*;
import java.awt.*;

public class CreateAccountPage {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private final Client client;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final CustomFont customFont = new CustomFont();

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/

    /** Constructs a CreateAccountPage instance.
     * @param client      the client used to send messages to the server
     * @param cardLayout  the layout manager used for switching views
     * @param mainPanel   the main panel containing all UI pages
     */
    public CreateAccountPage(Client client, CardLayout cardLayout, JPanel mainPanel) {
        this.client = client;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }
    /** PAGE BUILDER --------------------------------------------------------------------------------------------------- **/
    public JPanel createAccountMenu() {
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        JPanel wallpaper = new BackgroundPanel("wallpaper.png");
        wallpaper.setPreferredSize(new Dimension(900,800));

        JPanel menuPanel = new BackgroundPanel("menu_wallpaper.png");
        menuPanel.setPreferredSize(new Dimension(300, 800));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(245, 245, 245, 1));

        JLabel titleLabel = new JLabel("Registration");
        titleLabel.setFont(customFont.bold(50));
        titleLabel.setForeground(new Color(0, 60, 113));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Field
        Dimension fieldSize = new Dimension(250, 40);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username: ");
        userLabel.setFont(customFont.regular(25));
        userLabel.setForeground(new Color(0, 60, 113));

        JTextField userField = new JTextField(16);
        userField.setPreferredSize(fieldSize);
        userField.setMaximumSize(fieldSize);
        userField.setMinimumSize(fieldSize);
        userField.setFont(customFont.regular(18));
        userPanel.add(userLabel);
        userPanel.add(userField);

        // Password Field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passPanel.setOpaque(false);

        JLabel passLabel = new JLabel("Password: ");
        passLabel.setFont(customFont.regular(25));
        passLabel.setForeground(new Color(0, 60, 113));

        JPasswordField passField = new JPasswordField(16);
        passField.setPreferredSize(fieldSize);
        passField.setMaximumSize(fieldSize);
        passField.setMinimumSize(fieldSize);
        passField.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Buttons
        Dimension buttonSize = new Dimension(250, 40);

        JButton registerButton = new JButton("Submit");
        registerButton.setFont(customFont.bold(25));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setPreferredSize(buttonSize);
        registerButton.setMaximumSize(buttonSize);
        registerButton.setMinimumSize(buttonSize);

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(customFont.regular(20));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(buttonSize);
        backButton.setMaximumSize(buttonSize);
        backButton.setMinimumSize(buttonSize);

        // Actions
        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                if(username.length() <= 4 || !username.matches(".*[A-Za-z].*"))
                {
                    JOptionPane.showMessageDialog(backgroundPanel, "Invalid username! Username must be greater than 3 characters and contain letters", "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(password.length() < 5 || !password.matches(".*[A-Za-z].*"))
                {
                    JOptionPane.showMessageDialog(backgroundPanel, "Invalid password! Password must be greater than 5 characters and contain letters", "Registration Error", JOptionPane.ERROR_MESSAGE);
                }
                else
                {
                    client.sendMessage("REGISTER " + username.toLowerCase() + " " + password);
                }
            }
            else {
                JOptionPane.showMessageDialog(mainPanel, "Please fill in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // Assembly
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(40));
        menuPanel.add(userPanel);
        menuPanel.add(Box.createVerticalStrut(0));
        menuPanel.add(passPanel);
        menuPanel.add(Box.createVerticalStrut(300));
        menuPanel.add(registerButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(backButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(wallpaper, BorderLayout.WEST);
        backgroundPanel.add(menuPanel, BorderLayout.EAST);
        return backgroundPanel;
    }
}