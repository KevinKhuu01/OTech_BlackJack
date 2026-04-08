package frontend.GUIClasses.Pages;

import frontend.Client;
import frontend.GUIClasses.Styling.BackgroundPanel;
import frontend.GUIClasses.Styling.CustomFont;

import javax.swing.*;
import java.awt.*;

public class LoginPage {
    private final Client client;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    public LoginPage(Client client, CardLayout cardLayout, JPanel mainPanel) {
        this.client = client;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel createLoginMenu() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("Table Start.png"); // Reusing your start background
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(400, 350));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 0, 0, 170));

        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(CustomFont.newFont(30));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Field
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("Username: ");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(CustomFont.newFont(20));
        JTextField userField = new JTextField(15);
        userPanel.add(userLabel);
        userPanel.add(userField);

        // Password Field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passPanel.setOpaque(false);
        JLabel passLabel = new JLabel("Password: ");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(CustomFont.newFont(20));
        JPasswordField passField = new JPasswordField(15);
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Buttons
        JButton loginButton = new JButton("Login");
        loginButton.setFont(CustomFont.newFont(20));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(200, 40));

        JButton createAccButton = new JButton("Create Account");
        createAccButton.setFont(CustomFont.newFont(16));
        createAccButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccButton.setMaximumSize(new Dimension(200, 40));

        // Actions
        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
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
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(userPanel);
        menuPanel.add(passPanel);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(loginButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(createAccButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(menuPanel);
        return backgroundPanel;
    }
}