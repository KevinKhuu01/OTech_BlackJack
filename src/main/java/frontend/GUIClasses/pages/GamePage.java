package frontend.GUIClasses.pages;

import frontend.Client;
import frontend.GUIClasses.styling.BackgroundMusic;
import frontend.GUIClasses.styling.BackgroundPanel;
import frontend.GUIClasses.styling.CustomFont;

import javax.swing.*;
import java.awt.*;

import static java.lang.Thread.sleep;

/**
 * Creates, maintains, and updates the game page during the start of the game and interactions during
 */

public class GamePage {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    // Utilities
    private final Client client;
    private final CustomFont customFont = new CustomFont();

    // Main player (this client) and dealer
    private JPanel playerCardsPanel;
    private JLabel playerTotalLabel;
    private JPanel dealerCardsPanel;
    private JLabel dealerTotalLabel;

    // Multiplayer functionality
    private JPanel player2Panel;
    private JPanel player2Cards;
    private JLabel player2TotalLabel;
    private JLabel player2NameLabel;

    private JPanel player3Panel;
    private JPanel player3Cards;
    private JLabel player3TotalLabel;
    private JLabel player3NameLabel;

    // Client display
    private BackgroundPanel backgroundPanel;
    private JLabel balanceLabel;
    private JLabel resultLabel;
    private JButton hitButton;
    private JButton standButton;
    private JButton leaveGameButton;
    private BackgroundMusic backgroundMusic;
    private JButton muteMusicButton;
    private JTextField betAmountTextField;
    private String betInput = Integer.toString(100);
    private int actualBalance = -1;
    private Timer balanceTimer;

    /** CONSTRUCTOR -------------------------------------------------------------------------------------------------**/

    /** Constructs a GamePage instance.
     * @param client: The client used to communicate with the server
     * @param backGroundMusic: The background music controller
     */
    public GamePage(Client client, BackgroundMusic backGroundMusic)
    {
        this.client = client;
        this.backgroundMusic = backGroundMusic;
    }

    /** HELPER METHODS --------------------------------------------------------------------------------------------- **/

    /**
     * Updates the result label to display a message such as win, loss, or draw.
     *
     * @param text  the message to display
     * @param color the color of the message text
     */
    public void setResultLabelText(String text, Color color)
    {
        this.resultLabel.setText(text);
        this.resultLabel.setForeground(color);
        resultLabel.setFont(customFont.bold(20));
        resultLabel.setOpaque(true);
        resultLabel.setBackground(new Color(0, 0, 0, 200));
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
    }

    // Displays the player's account balance
    public void setBalanceLabel(String text)
    {
        int newBalance = Integer.parseInt(text.trim());
        String currentText = balanceLabel.getText().replaceAll("[^0-9]", "");
        int currentBalance = currentText.isEmpty() ? 0 : Integer.parseInt(currentText);

        // If this is the very first time the balance is loading, just set it and skip the animation
        if (actualBalance == -1) {
            actualBalance = newBalance;
            balanceLabel.setText("Balance: $" + actualBalance);
            return;
        }
        if (balanceTimer != null && balanceTimer.isRunning()) {
            balanceTimer.stop();
        }

        // Draw
        if(newBalance - currentBalance == Integer.parseInt(betAmountTextField.getText()))
        {
            this.balanceLabel.setText("Balance: $ " + "+" + (newBalance-currentBalance));
            this.balanceLabel.setForeground(Color.GRAY);
        }
        // Bet
        else if(currentBalance > newBalance)
        {
            this.balanceLabel.setText("Bet: -$" + (currentBalance-newBalance));
            this.balanceLabel.setForeground(Color.RED);
        }
        // Win
        else if(currentBalance < newBalance)
        {
            this.balanceLabel.setText("Balance: $ " + "+" + (newBalance-currentBalance));
            this.balanceLabel.setForeground(Color.GREEN);
        }

        actualBalance = newBalance;

        // Start a 2 second timer to reset the visual text
        balanceTimer = new Timer(1000, e -> {
            balanceLabel.setForeground(Color.WHITE);
            balanceLabel.setText("Balance: $" + actualBalance);
            balanceLabel.revalidate();
        });
        balanceTimer.setRepeats(false);
        balanceTimer.start();
    }

    /**Disables player action buttons and updates the result label based on game outcome. **/
    public void disableButtons()
    {
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        setResultLabelText("Waiting for others...", Color.LIGHT_GRAY);

        if(playerTotalLabel.getText().split(" ")[1].equals("21"))
        {
            setResultLabelText("BlackJack!", Color.GREEN);
        }
        else if(Integer.parseInt(playerTotalLabel.getText().split(" ")[1]) > 21)
        {
            setResultLabelText("You Lose!", Color.RED);
        }
    }

    // Resets cards and card totals on the table for next round
    public void resetTable()
    {
        checkSufficientFunds();
        resultLabel.setOpaque(false);
        resultLabel.setBorder(null);

        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();

        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();

        hitButton.setEnabled(true);
        standButton.setEnabled(true);

        setResultLabelText("Your Turn!", Color.WHITE);
        resultLabel.revalidate();
        resultLabel.repaint();
    }

    /** Creates a visual card label using an image file corresponding to the card name.
     * @param card   the card identifier (e.g., "AS", "10H")
     * @param width  the desired width of the card image
     * @param height the desired height of the card image
     * @return a JLabel containing the card image or fallback text if not found
     */
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

    /** BUILD PAGE METHODS ----------------------------------------------------------------------------------------- **/
    public JPanel createGamePanel() {
        backgroundPanel = new BackgroundPanel("table.png");
        backgroundPanel.setLayout(new BorderLayout());

        // MAIN PANEL (CLIENT AND DEALER) -----------------------------------------------------------------------------
        JPanel gamePanel = new JPanel();
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        resultLabel = new JLabel("");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        setResultLabelText("Your Turn!", Color.WHITE);

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
        gamePanel.add(dealerCardsPanel);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(dealerTotalLabel);
        gamePanel.add(Box.createVerticalStrut(80));
        gamePanel.add(playerCardsPanel);
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(playerTotalLabel);
        gamePanel.add(Box.createVerticalStrut(10));
        gamePanel.add(resultLabel);
        gamePanel.add(Box.createVerticalStrut(0));
        gamePanel.add(Box.createVerticalGlue());

        // WEST PANEL (PLAYER 2 IF CONNECTED) ------------------------------------------------------------------------
        player2Panel = new JPanel();
        player2Panel.setOpaque(false);
        player2Panel.setPreferredSize(new Dimension(280, 100));
        player2Panel.setLayout(new BoxLayout(player2Panel, BoxLayout.Y_AXIS));

        player2Cards = new JPanel(new FlowLayout(FlowLayout.CENTER, -100, 0));
        player2Cards.setOpaque(false);
        player2Cards.setAlignmentX(Component.CENTER_ALIGNMENT);

        player2TotalLabel = createStyledLabel("");
        player2NameLabel = createStyledLabel("");
        player2NameLabel.setFont(customFont.regular(22));
        player2NameLabel.setForeground(Color.WHITE);
        player2NameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2TotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        player2Panel.add(Box.createVerticalGlue());
        player2Panel.add(Box.createVerticalStrut(60));
        player2Panel.add(player2Cards);
        player2Panel.add(Box.createVerticalStrut(-100));
        player2Panel.add(player2NameLabel);
        player2Panel.add(Box.createVerticalStrut(5));
        player2Panel.add(player2TotalLabel);
        player2Panel.add(Box.createVerticalStrut(60));
        player2Panel.add(Box.createVerticalGlue());

        // EAST PANEL (PLAYER 3 IF CONNECTED) -------------------------------------------------------------------------
        player3Panel = new JPanel();
        player3Panel.setOpaque(false);
        player3Panel.setPreferredSize(new Dimension(280, 300));
        player3Panel.setLayout(new BoxLayout(player3Panel, BoxLayout.Y_AXIS));

        player3Cards = new JPanel(new FlowLayout(FlowLayout.CENTER, -100, 0));
        player3Cards.setOpaque(false);
        player3Cards.setAlignmentX(Component.CENTER_ALIGNMENT);

        player3TotalLabel = createStyledLabel("");
        player3NameLabel = createStyledLabel("");
        player3NameLabel.setFont(customFont.regular(22));
        player3NameLabel.setForeground(Color.WHITE);
        player3NameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player3TotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        player3Panel.add(Box.createVerticalGlue());
        player3Panel.add(Box.createVerticalStrut(60));
        player3Panel.add(player3Cards);
        player3Panel.add(Box.createVerticalStrut(-100));
        player3Panel.add(player3NameLabel);
        player3Panel.add(Box.createVerticalStrut(5));
        player3Panel.add(player3TotalLabel);
        player3Panel.add(Box.createVerticalStrut(60));
        player3Panel.add(Box.createVerticalGlue());

        // Hide player 2 and 3 components until they have connected
        player2NameLabel.setVisible(false);
        player2TotalLabel.setVisible(false);
        player3NameLabel.setVisible(false);
        player3TotalLabel.setVisible(false);

        // SOUTH PANEL (BUTTONS & UTILITIES) ----------------------------------------------------------------------------------------------
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(true);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 20));
        buttonPanel.setBackground(new Color(0, 0, 0, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(4, 14, 8, 14));

        balanceLabel = new JLabel("Balance: $0");
        balanceLabel.setFont(customFont.bold(24));
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setOpaque(false);
        balanceLabel.setBackground(new Color(0, 0, 0, 140));

        JPanel balanceWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        balanceWrapper.setOpaque(false);
        balanceWrapper.add(balanceLabel);

        hitButton = new JButton("Hit");
        hitButton.setFont(customFont.bold(25f));
        hitButton.setPreferredSize(new Dimension(150, 40));
        hitButton.addActionListener(e -> client.sendMessage("hit"));

        standButton = new JButton("Stand");
        standButton.setFont(customFont.bold(25f));
        standButton.setPreferredSize(new Dimension(150, 40));
        standButton.addActionListener(e -> {
            client.sendMessage("stay");
            disableButtons();
        });

        JPanel betWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        betWrapper.setOpaque(false);

        JLabel betAmountLabel = new JLabel("Bet Amount:");
        betAmountLabel.setFont(customFont.bold(24));
        betAmountLabel.setForeground(Color.WHITE);
        betAmountLabel.setOpaque(false);
        betAmountLabel.setBackground(new Color(0, 0, 0, 140));

        betAmountTextField = new JTextField(10);
        betAmountTextField.setText("100");
        betAmountTextField.setPreferredSize(new Dimension(200, 40));
        betAmountTextField.setMaximumSize(new Dimension(200, 40));
        betAmountTextField.setMinimumSize(new Dimension(200, 40));

        JButton sendBet = new JButton("Send");
        sendBet.setFont(customFont.bold(20f));
        sendBet.setPreferredSize(new Dimension(100, 40));
        betAmountTextField.addActionListener(e -> sendBet.doClick());
        sendBet.addActionListener(e -> {
            betInput = betAmountTextField.getText().trim();
            if(!betInput.matches("[0-9]+"))
            {
                JOptionPane.showMessageDialog(backgroundPanel, "Invalid bet. Please place an integer bet with only numbers","Invalid bet", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                client.sendMessage("BET " + betInput);
                JOptionPane.showMessageDialog(backgroundPanel, "Your betting amount will be $" + betInput + " next round");
            }

        });

        betWrapper.add(betAmountLabel);
        betWrapper.add(betAmountTextField);
        betWrapper.add(sendBet);

        buttonPanel.add(balanceWrapper);
        buttonPanel.add(betWrapper);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        // NORTH PANEL (LEAVE GAME BUTTON) ----------------------------------------------------------------------------------------------
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));

        leaveGameButton = new JButton("Leave Table");
        leaveGameButton.setFont(customFont.bold(20));
        leaveGameButton.setPreferredSize(new Dimension(150, 40));
        leaveGameButton.addActionListener(e -> {
            client.sendMessage("LEAVE_TABLE");
        });

        muteMusicButton = new JButton("Music Off");
        backgroundMusic.registerButton(muteMusicButton);
        muteMusicButton.setFont(customFont.bold(20f));
        muteMusicButton.setPreferredSize(new Dimension(150, 40));

        muteMusicButton.addActionListener(e -> {
            boolean isMuted = backgroundMusic.toggleMute();
            if (isMuted)
            {
                backgroundMusic.stopMusic();
            } else
            {
                backgroundMusic.playMusic("src/main/resources/music/BlackJackBGM.wav");
            }
        });

        topPanel.add(leaveGameButton);
        topPanel.add(muteMusicButton);

        // Putting components together --------------------------------------------------------------------------------
        backgroundPanel.add(topPanel, BorderLayout.NORTH);
        backgroundPanel.add(player2Panel, BorderLayout.WEST);
        backgroundPanel.add(gamePanel, BorderLayout.CENTER);
        backgroundPanel.add(player3Panel, BorderLayout.EAST);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        return backgroundPanel;
    }

    // styling
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(customFont.regular(24));
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
                    dealerCardsPanel.add(createCardLabel(cardStr, 150, 180));
                }
            }

            String[] playerData = serverData[1].split(":");
            playerTotalLabel.setText("Total: " + playerData[0]);
            if(playerData[0].equals("21"))
            {
                disableButtons();
            }
            else if(Integer.parseInt(playerData[0]) > 21)
            {
                disableButtons();
            }
            String balanceData = serverData[2];
            setBalanceLabel(balanceData);
            playerCardsPanel.removeAll();
            if (playerData.length > 1 && !playerData[1].isEmpty()) {
                for (String cardStr : playerData[1].split(",")) {
                    playerCardsPanel.add(createCardLabel(cardStr, 150, 180));
                }
            }

            // Player 2 - Left side
            player2Cards.removeAll();
            if (serverData.length >= 6)
            {
                player2NameLabel.setVisible(true);
                player2TotalLabel.setVisible(true);
                player2Panel.setVisible(true);
                player2NameLabel.setText(serverData[3]);
                String[] player2Data = serverData[4].split(":");
                player2TotalLabel.setText("Total: " + player2Data[0]);
                if(player2Data[0].equals("21"))
                {
                    player2TotalLabel.setText("BlackJack!");
                    System.out.println("BLACKJACK");
                }
                else if(Integer.parseInt(player2Data[0]) > 21)
                {
                    player2TotalLabel.setText("Lost!");
                }
                if (player2Data.length > 1 && !player2Data[1].isEmpty())
                {
                    for (String cardStr : player2Data[1].split(","))
                    {
                        player2Cards.add(createCardLabel(cardStr, 120, 150));
                    }
                }
            }
            else
            {
                player2NameLabel.setVisible(false);
                player2TotalLabel.setVisible(false);
            }

            // Player 3 - Right side
            player3Cards.removeAll();
            if (serverData.length >= 9) {
                player3NameLabel.setVisible(true);
                player3TotalLabel.setVisible(true);
                player3Panel.setVisible(true);
                player3NameLabel.setText((serverData[6]));
                String[] player3Data = serverData[7].split(":");
                player3TotalLabel.setText("Total: " + player3Data[0]);
                if(player3Data[0].equals("21"))
                {
                    player3TotalLabel.setText("BlackJack!");
                    System.out.println("BLACKJACK");
                }
                else if(Integer.parseInt(player3Data[0]) > 21)
                {
                    player3TotalLabel.setText("Lost!");
                }
                if (player3Data.length > 1 && !player3Data[1].isEmpty()) {
                    for (String cardStr : player3Data[1].split(",")) {
                        player3Cards.add(createCardLabel(cardStr, 120, 150));
                    }
                }
            } else {
                player3NameLabel.setVisible(false);
                player3TotalLabel.setVisible(false);
            }

            dealerCardsPanel.revalidate();
            dealerCardsPanel.repaint();
            playerCardsPanel.revalidate();
            playerCardsPanel.repaint();
            player2Cards.revalidate();
            player2Cards.repaint();
            player3Cards.revalidate();
            player3Cards.repaint();
            resultLabel.revalidate();
            resultLabel.repaint();
        }
        catch (Exception e)
        {
            System.err.println("Error parsing update string from server: " + data);
        }
    }

    public void checkSufficientFunds()
    {
        Timer timer = new Timer(1200, e -> {
            int balance = Integer.parseInt(balanceLabel.getText().split("\\$")[1]);
            int bet = Integer.parseInt(betInput);
            if(balance < bet)
            {
                disableButtons();
                JOptionPane.showMessageDialog(backgroundPanel, "Insufficient funds! Add funds in the main menu.", "Error placing bet", JOptionPane.ERROR_MESSAGE);
                client.sendMessage("LEAVE_TABLE");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
