package frontend.GUIClasses.pages;

import frontend.Client;
import frontend.GUIClasses.styling.BackgroundPanel;
import frontend.GUIClasses.styling.CustomFont;

import javax.swing.*;
import java.awt.*;

public class LoginPage {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private final Client client;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final CustomFont customFont = new CustomFont();

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
    public LoginPage(Client client, CardLayout cardLayout, JPanel mainPanel) {
        this.client = client;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    /** PAGE BUILDER --------------------------------------------------------------------------------------------------- **/
    public JPanel createLoginMenu() {
        JPanel backgroundPanel = new JPanel(new BorderLayout());
        JPanel wallpaper = new BackgroundPanel("wallpaper.png");
        wallpaper.setPreferredSize(new Dimension(900,800));

        JPanel menuPanel = new BackgroundPanel("menu_wallpaper.png");
        menuPanel.setPreferredSize(new Dimension(300, 800));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(245, 245, 245, 1));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(customFont.bold(42));
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

        JButton loginButton = new JButton("Login");
        loginButton.setFont(customFont.regular(25));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(buttonSize);
        loginButton.setMaximumSize(buttonSize);
        loginButton.setMinimumSize(buttonSize);

        JButton createAccButton = new JButton("Create Account");
        createAccButton.setFont(customFont.regular(20));
        createAccButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccButton.setPreferredSize(buttonSize);
        createAccButton.setMaximumSize(buttonSize);
        createAccButton.setMinimumSize(buttonSize);

        // Actions
        passField.addActionListener(e -> loginButton.doClick());
        loginButton.addActionListener(e -> {
            String username = userField.getText().trim().toLowerCase();
            String password = new String(passField.getPassword()).trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                // Send login request to server
                client.sendMessage("LOGIN " + username + " " + password);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Please fill in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        createAccButton.addActionListener(e -> cardLayout.show(mainPanel, "create"));

        // Assembly
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(40));
        menuPanel.add(userPanel);
        menuPanel.add(Box.createVerticalStrut(0));
        menuPanel.add(passPanel);
        menuPanel.add(Box.createVerticalStrut(300));
        menuPanel.add(loginButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createAccButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(wallpaper, BorderLayout.WEST);
        backgroundPanel.add(menuPanel, BorderLayout.EAST);
        return backgroundPanel;
    }
}