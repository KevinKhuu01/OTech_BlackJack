package frontend.GUIClasses.Pages;

import frontend.Client;
import frontend.GUIClasses.Styling.BackgroundPanel;
import frontend.GUIClasses.Styling.CustomFont;

import javax.swing.*;
import java.awt.*;

public class CreateAccountPage {
    private final Client client;
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final CustomFont customFont = new CustomFont();

    public CreateAccountPage(Client client, CardLayout cardLayout, JPanel mainPanel) {
        this.client = client;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
    }

    public JPanel createAccountMenu() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("Table Start.png");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(400, 350));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 0, 0, 170));

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(customFont.bold(30));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Username Field
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("New User: ");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(customFont.regular(20));
        JTextField userField = new JTextField(15);
        userPanel.add(userLabel);
        userPanel.add(userField);

        // Password Field
        JPanel passPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        passPanel.setOpaque(false);
        JLabel passLabel = new JLabel("New Pass: ");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(customFont.regular(20));
        JPasswordField passField = new JPasswordField(15);
        passPanel.add(passLabel);
        passPanel.add(passField);

        // Buttons
        JButton registerButton = new JButton("Submit");
        registerButton.setFont(customFont.bold(20));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setMaximumSize(new Dimension(200, 40));

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(customFont.bold(16));
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setMaximumSize(new Dimension(200, 40));

        // Actions
        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (!username.isEmpty() && !password.isEmpty()) {
                // Send register request to server
                client.sendMessage("REGISTER " + username + " " + password);
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Please fill in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // Assembly
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(userPanel);
        menuPanel.add(passPanel);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(registerButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(backButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(menuPanel);
        return backgroundPanel;
    }
}