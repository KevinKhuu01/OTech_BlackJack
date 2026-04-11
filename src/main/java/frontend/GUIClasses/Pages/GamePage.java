package frontend.GUIClasses.Pages;

import frontend.Client;
import frontend.GUIClasses.Styling.BackgroundPanel;
import frontend.GUIClasses.Styling.CustomFont;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

public class GamePage {
    private final Client client;

    // Main player (this client) and dealer
    private JPanel playerCardsPanel;
    private JLabel playerTotalLabel;
    private JPanel dealerCardsPanel;
    private JLabel dealerTotalLabel;

    // Multiplayer additions
    private JPanel player2Panel;
    private JPanel player2Cards;
    private JLabel player2TotalLabel;
    private JLabel player2NameLabel;

    private JPanel player3Panel;
    private JPanel player3Cards;
    private JLabel player3TotalLabel;
    private JLabel player3NameLabel;

    // client display
    private JLabel balanceLabel;
    private JLabel resultLabel;
    private JButton hitButton;
    private JButton standButton;

    // Win, Loss, or Draw notification
    public void setResultLabelText(String text, Color color)
    {
        this.resultLabel.setText(text);
        this.resultLabel.setForeground(color);
        resultLabel.setFont(new Font ("Lucida Handwriting", Font.BOLD, 70));
        resultLabel.setOpaque(true);
        resultLabel.setBackground(new Color(0, 0, 0, 180));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        try
        {
            sleep(10);
        }
        catch(InterruptedException e)
        {
            System.out.println(e);
        }
    }

    // Displays the player's account balance
    public void setBalanceLabel(String text)
    {
        this.balanceLabel.setText("Balance: $" + text);
        balanceLabel.revalidate();

//        SwingUtilities.invokeLater(() -> {
//            Component ghost = ((BorderLayout)balanceLabel.getParent().getParent().getLayout())
//                    .getLayoutComponent(BorderLayout.WEST);
//            if (ghost != null) {
//                ghost.setPreferredSize(new Dimension(balanceLabel.getParent().getPreferredSize().width, 0));
//                ghost.getParent().revalidate();
//            }
//        });
    }

    public GamePage(Client client) {
        this.client = client;
    }

    // Resets cards and card totals on the table for next round
    public void resetTable()
    {
        resultLabel.setText(" ");
        resultLabel.setOpaque(false);
        resultLabel.setBorder(null);

        player2TotalLabel.setText("");
        player2NameLabel.setText("");

        player3TotalLabel.setText("");
        player3NameLabel.setText("");

        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();

        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();

        hitButton.setEnabled(true);
        standButton.setEnabled(true);
    }

    public JPanel createGamePanel() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("Table.png");
        backgroundPanel.setLayout(new BorderLayout());

        // ------------------------- Dealer and Main Player -------------------------
        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        resultLabel = new JLabel(" ");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -120, 0));
        dealerCardsPanel.setOpaque(false);
        dealerCardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dealerTotalLabel = createStyledLabel("Total: ");

        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -120, 0));
        playerCardsPanel.setOpaque(false);
        playerCardsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerTotalLabel = createStyledLabel("Total: ");

        // Vertical adjustments
        gamePanel.add(Box.createVerticalGlue());
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(dealerCardsPanel);
        gamePanel.add(Box.createVerticalStrut(0));
        gamePanel.add(dealerTotalLabel);
        gamePanel.add(Box.createVerticalStrut(40));
        gamePanel.add(resultLabel);
        gamePanel.add(Box.createVerticalStrut(40));
        gamePanel.add(playerCardsPanel);
        gamePanel.add(Box.createVerticalStrut(0));
        gamePanel.add(playerTotalLabel);
        gamePanel.add(Box.createVerticalStrut(40));
        gamePanel.add(Box.createVerticalGlue());

        // Left panel - player 2
        player2Panel = new JPanel();
        player2Panel.setOpaque(false);
        player2Panel.setPreferredSize(new Dimension(280, 100));
        player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));

        player2Cards = new JPanel(new FlowLayout(FlowLayout.CENTER, -70, 0));
        player2Cards.setOpaque(false);
        player2Cards.setAlignmentX(Component.CENTER_ALIGNMENT);

        player2TotalLabel = createStyledLabel("");
        player2NameLabel = createStyledLabel("");
        player2NameLabel.setFont(CustomFont.newFont(22));
        player2NameLabel.setForeground(Color.WHITE);
        player2NameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2TotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        player2Panel.add(Box.createVerticalGlue());
        player2Panel.add(Box.createVerticalStrut(60));
        player2Panel.add(player2Cards);
        player2Panel.add(Box.createVerticalStrut(-160));
        player2Panel.add(player2NameLabel);
        player2Panel.add(Box.createVerticalStrut(5));
        player2Panel.add(player2TotalLabel);
        player2Panel.add(Box.createVerticalStrut(60));
        player2Panel.add(Box.createVerticalGlue());

        // Right panel - Player 3
        player3Panel = new JPanel();
        player3Panel.setOpaque(false);
        player3Panel.setPreferredSize(new Dimension(280, 300));
        player3Panel.setLayout(new BoxLayout(player3Panel, BoxLayout.Y_AXIS));

        player3Cards = new JPanel(new FlowLayout(FlowLayout.CENTER, -70, 0));
        player3Cards.setOpaque(false);
        player3Cards.setAlignmentX(Component.CENTER_ALIGNMENT);

        player3TotalLabel = createStyledLabel("");
        player3NameLabel = createStyledLabel("");
        player3NameLabel.setFont(CustomFont.newFont(22));
        player3NameLabel.setForeground(Color.WHITE);
        player3NameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player3TotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        player3Panel.add(Box.createVerticalGlue());
        player3Panel.add(Box.createVerticalStrut(60));
        player3Panel.add(player3Cards);
        player3Panel.add(Box.createVerticalStrut(-160));
        player3Panel.add(player3NameLabel);
        player3Panel.add(Box.createVerticalStrut(5));
        player3Panel.add(player3TotalLabel);
        player3Panel.add(Box.createVerticalStrut(60));
        player3Panel.add(Box.createVerticalGlue());

        // Add balance to bottom of Right Panel
        balanceLabel = new JLabel("Balance: $0");
        balanceLabel.setFont(CustomFont.newFont(24));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setOpaque(true);
        balanceLabel.setBackground(new Color(0, 0, 0, 140));
        balanceLabel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        JPanel balanceWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        balanceWrapper.setOpaque(false);
        balanceWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 25));
        balanceWrapper.add(balanceLabel);
        player3Panel.add(balanceWrapper, BorderLayout.SOUTH);

        // --- SOUTH PANEL (Buttons) ---
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        hitButton = new JButton("Hit");
        hitButton.setFont(CustomFont.newFont(25f));
        hitButton.setPreferredSize(new Dimension(150, 45));
        hitButton.addActionListener(e -> client.sendMessage("hit"));

        standButton = new JButton("Stand");
        standButton.setFont(CustomFont.newFont(25f));
        standButton.setPreferredSize(new Dimension(150, 45));
        standButton.addActionListener(e -> {
            client.sendMessage("stay");
            disableButtons();
        });

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        backgroundPanel.add(player2Panel, BorderLayout.WEST);
        backgroundPanel.add(gamePanel, BorderLayout.CENTER);
        backgroundPanel.add(player3Panel, BorderLayout.EAST);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        return backgroundPanel;
    }

//    public JPanel createGamePanel() {
//        // Table.png image generated by Gemini
//        BackgroundPanel backgroundPanel = new BackgroundPanel("Table.png");
//        backgroundPanel.setLayout(new BorderLayout());
//
//        JPanel gamePanel = new JPanel();
//        gamePanel.setOpaque(false);
//        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
//
//        resultLabel = new JLabel(" ");
//        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -120, 0));
//        dealerCardsPanel.setOpaque(false);
//
//        dealerTotalLabel = new JLabel();
//        dealerTotalLabel.setFont(CustomFont.newFont(28));
//        dealerTotalLabel.setForeground(Color.WHITE);
//        dealerTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        dealerTotalLabel.setOpaque(true);
//        dealerTotalLabel.setBackground(new Color(0, 0, 0, 140));
//        dealerTotalLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
//
//        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -120, 0));
//        playerCardsPanel.setOpaque(false);
//        playerCardsPanel.add(new JLabel("Player"));
//
//        playerTotalLabel = new JLabel();
//        playerTotalLabel.setFont(CustomFont.newFont(28));
//        playerTotalLabel.setForeground(Color.WHITE);
//        playerTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//        playerTotalLabel.setOpaque(true);
//        playerTotalLabel.setBackground(new Color(0, 0, 0, 140));
//        playerTotalLabel.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
//
//        balanceLabel = new JLabel("Balance: $0");
//        balanceLabel.setFont(CustomFont.newFont(28));
//        balanceLabel.setForeground(Color.WHITE);
//        balanceLabel.setOpaque(true);
//        balanceLabel.setBackground(new Color(0, 0, 0, 140));
//        balanceLabel.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
//
//        JPanel balanceWrapper = new JPanel(new BorderLayout());
//        balanceWrapper.setOpaque(false);
//        balanceWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 25));
//        balanceWrapper.setPreferredSize(null);
//        balanceWrapper.add(balanceLabel, BorderLayout.SOUTH);
//
//        // acts as counterweight to balancelabel, centering the cards
//        ghostPanel = new JPanel();
//        ghostPanel.setOpaque(false);
//        SwingUtilities.invokeLater(() -> { // adjust counterweight panel based on balancelabel size
//            int w = balanceWrapper.getPreferredSize().width;
//            ghostPanel.setPreferredSize(new Dimension(w, 0));
//            ghostPanel.revalidate();
//        });
//
//        gamePanel.add(Box.createVerticalGlue());
//        gamePanel.add(Box.createVerticalStrut(20));
//        gamePanel.add(dealerCardsPanel);
//        gamePanel.add(Box.createVerticalStrut(4));
//        gamePanel.add(dealerTotalLabel);
//
//        gamePanel.add(Box.createVerticalStrut(40));
//        gamePanel.add(resultLabel);
//        gamePanel.add(Box.createVerticalStrut(40));
//
//        gamePanel.add(playerCardsPanel);
//        gamePanel.add(Box.createVerticalStrut(0));
//        gamePanel.add(playerTotalLabel);
//        gamePanel.add(Box.createVerticalStrut(60));
//        gamePanel.add(Box.createVerticalGlue());
//
//        backgroundPanel.add(balanceWrapper, BorderLayout.EAST);
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.setOpaque(false);
//
//        hitButton = new JButton("Hit");
//        standButton = new JButton("Stand");
//
//        hitButton.setFont(CustomFont.newFont(25f));
//        standButton.setFont(CustomFont.newFont(25f));
//
//        hitButton.setMaximumSize(new Dimension(220, 45));
//        standButton.setMaximumSize(new Dimension(220, 45));
//
//        hitButton.addActionListener(e -> {
//            client.sendMessage("hit");
//        });
//
//        standButton.addActionListener(e -> {
//            client.sendMessage("stay");
//            disableButtons();
//        });
//
//        buttonPanel.add(hitButton);
//        buttonPanel.add(standButton);
//
//        JScrollPane scrollPane = new JScrollPane(gamePanel);
//        scrollPane.setBorder(null);
//        scrollPane.setOpaque(false);
//        scrollPane.getViewport().setOpaque(false);
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//
//        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
//        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);
//        backgroundPanel.add(balanceWrapper, BorderLayout.EAST);
//        backgroundPanel.add(ghostPanel, BorderLayout.WEST);
//
//        return backgroundPanel;
//    }

    // styling
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(CustomFont.newFont(24));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setOpaque(true);
        label.setBackground(new Color(0, 0, 0, 140));
        label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return label;
    }

    // handles server response for game status
    public void updateGameDisplay(String data) {

        try {
            // Expected format: "DealerTotal:DCard1,DCard2|PlayerTotal:PCard1,PCard2"
            String[] serverData = data.split("\\|");


            String[] dealerData = serverData[0].split(":");
            dealerTotalLabel.setText("Total: " + dealerData[0]);
            dealerCardsPanel.removeAll();
            if (dealerData.length > 1 && !dealerData[1].isEmpty()) {
                for (String cardStr : dealerData[1].split(",")) {
                    dealerCardsPanel.add(createCardLabel(cardStr, 160, 140));
                }
            }


            String[] playerData = serverData[1].split(":");
            playerTotalLabel.setText("Total: " + playerData[0]);
            String balanceData = serverData[2];
            balanceLabel.setText("Balance: $" + balanceData);
            playerCardsPanel.removeAll();
            if (playerData.length > 1 && !playerData[1].isEmpty()) {
                for (String cardStr : playerData[1].split(",")) {
                    playerCardsPanel.add(createCardLabel(cardStr, 160, 140));
                }
            }

            // Player 2 - Left side
            player2Cards.removeAll();
            if (serverData.length >= 6)
            {
                player2Panel.setVisible(true);
                player2NameLabel.setText(serverData[3]);
                String[] player2Data = serverData[4].split(":");
                player2TotalLabel.setText("Total: " + player2Data[0]);
                if (player2Data.length > 1 && !player2Data[1].isEmpty())
                {
                    for (String cardStr : player2Data[1].split(","))
                    {
                        player2Cards.add(createCardLabel(cardStr, 160, 140));
                    }
                }
            }
            else
            {
                player2NameLabel.setText(" ");
                player2TotalLabel.setText(" ");
            }

            // Player 3 - Right side
            player3Cards.removeAll();
            if (serverData.length >= 9) {
                player3Panel.setVisible(true);
                player3NameLabel.setText((serverData[6]));
                String[] player3Data = serverData[7].split(":");
                player3TotalLabel.setText("Total: " + player3Data[0]);
                if (player3Data.length > 1 && !player3Data[1].isEmpty()) {
                    for (String cardStr : player3Data[1].split(",")) {
                        player3Cards.add(createCardLabel(cardStr, 160, 140));
                    }
                }
            } else {
                player3NameLabel.setText(" ");
                player3TotalLabel.setText(" ");
            }

            dealerCardsPanel.revalidate();
            dealerCardsPanel.repaint();
            playerCardsPanel.revalidate();
            playerCardsPanel.repaint();
            player2Cards.revalidate();
            player2Cards.repaint();
            player3Cards.revalidate();
            player3Cards.repaint();

        } catch (Exception e) {
            System.err.println("Error parsing update string from server: " + data);
        }
    }

    // Creates card visual through getting appropriate card file.
    private JLabel createCardLabel(String card, int width, int height)
    {
        card = card.trim();
        String fileName = "CardImages/" + card + ".png";
        java.net.URL imageUrl = getClass().getClassLoader().getResource(fileName);

        if (imageUrl == null)
        {
            JLabel fallback = new JLabel(fileName);
            fallback.setForeground(Color.WHITE);
            return fallback;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    public void disableButtons()
    {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
    }
}
