package frontend;

import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private backend.GameLogic gameLogic;
    private backend.Player player;
    private backend.Player dealer;

    private JPanel playerCardsPanel;
    private JPanel dealerCardsPanel;
    private JLabel playerTotalLabel;
    private JLabel dealerTotalLabel;


    public GUI(){
        setTitle("OTech Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        gameLogic = new backend.GameLogic();
        backend.GameLogic.addPlayer("player");
        backend.GameLogic.startGame();

        player = backend.GameLogic.getPlayer("player");
        dealer = backend.GameLogic.getPlayer("dealer");

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        mainPanel.add(createStartMenu(), "start");
        mainPanel.add(createGamePanel(), "game");
        mainPanel.add(createWinPanel(), "won");
        mainPanel.add(createLosePanel(), "lost");

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

        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "game"));

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

    private JPanel createGamePanel() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("Table.jpeg");
        backgroundPanel.setLayout(new BorderLayout());

        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 120));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        gamePanel.setOpaque(false);
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));


        dealerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -300, 0));
        dealerCardsPanel.setOpaque(false);

        dealerTotalLabel = new JLabel();
        dealerTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        dealerTotalLabel.setForeground(Color.WHITE);
        dealerTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);


        playerCardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, -300, 0));
        playerCardsPanel.setOpaque(false);

        playerTotalLabel = new JLabel();
        playerTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        playerTotalLabel.setForeground(Color.WHITE);
        playerTotalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(dealerCardsPanel);
        gamePanel.add(dealerTotalLabel);
        gamePanel.add(Box.createVerticalStrut(20));
        gamePanel.add(playerCardsPanel);
        gamePanel.add(playerTotalLabel);
        gamePanel.add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");

        Font buttonFont = loadCustomFont(25f);
        hitButton.setFont(buttonFont);
        standButton.setFont(buttonFont);

        hitButton.setMaximumSize(new Dimension(220, 45));
        standButton.setMaximumSize(new Dimension(220, 45));

        hitButton.addActionListener(e -> {
            backend.GameLogic.hit(player);
            updateGameDisplay();

            if (player.getStatus() == backend.Player.STATUS.WIN) {
                cardLayout.show(mainPanel, "won");
            } else if (player.getStatus() == backend.Player.STATUS.LOST) {
                cardLayout.show(mainPanel, "lost");
            }
        });

        standButton.addActionListener(e -> {
            backend.GameLogic.stay(player);
            updateGameDisplay();
        });

        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);

        JScrollPane scrollPane = new JScrollPane(gamePanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        backgroundPanel.add(scrollPane, BorderLayout.CENTER);
        backgroundPanel.add(buttonPanel, BorderLayout.SOUTH);

        updateGameDisplay();

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

    private JPanel createWinPanel() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("Table.jpeg");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(350, 350));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 0, 0, 170));

        JLabel titleLabel = new JLabel("You Won!");
        titleLabel.setFont(loadCustomFont(60f));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newGameButton = new JButton("Play Again");
        JButton joinGameButton = new JButton("Join Game");
        JButton endGameButton = new JButton("Leave Game");

        Font buttonFont = loadCustomFont(25f);
        newGameButton.setFont(buttonFont);
        joinGameButton.setFont(buttonFont);
        endGameButton.setFont(buttonFont);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.setMaximumSize(new Dimension(220, 45));
        joinGameButton.setMaximumSize(new Dimension(220, 45));
        endGameButton.setMaximumSize(new Dimension(220, 45));


        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "game"));
        endGameButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(35));
        menuPanel.add(newGameButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(joinGameButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(endGameButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(menuPanel);

        return backgroundPanel;
    }

    private JPanel createLosePanel() {
        BackgroundPanel backgroundPanel = new BackgroundPanel("Table.jpeg");
        backgroundPanel.setLayout(new GridBagLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(350, 350));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 0, 0, 170));

        JLabel titleLabel = new JLabel("You Lost :(");
        titleLabel.setFont(loadCustomFont(60f));
        titleLabel.setForeground(new Color(139, 38, 53));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton newGameButton = new JButton("Play Again");
        JButton joinGameButton = new JButton("Join Game");
        JButton endGameButton = new JButton("Leave Game");

        Font buttonFont = loadCustomFont(25f);
        newGameButton.setFont(buttonFont);
        joinGameButton.setFont(buttonFont);
        endGameButton.setFont(buttonFont);

        newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        joinGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        endGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        newGameButton.setMaximumSize(new Dimension(220, 45));
        joinGameButton.setMaximumSize(new Dimension(220, 45));
        endGameButton.setMaximumSize(new Dimension(220, 45));

        newGameButton.addActionListener(e -> cardLayout.show(mainPanel, "game"));
        endGameButton.addActionListener(e -> System.exit(0));

        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createVerticalStrut(35));
        menuPanel.add(newGameButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(joinGameButton);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(endGameButton);
        menuPanel.add(Box.createVerticalGlue());

        backgroundPanel.add(menuPanel);

        return backgroundPanel;
    }

    private void updateGameDisplay() {
        dealerCardsPanel.removeAll();
        playerCardsPanel.removeAll();

        for (backend.Card card : dealer.getHand()) {
            dealerCardsPanel.add(createCardLabel(card));
        }

        for (backend.Card card : player.getHand()) {
            playerCardsPanel.add(createCardLabel(card));
        }

        dealerTotalLabel.setText("Total: " + dealer.getHandTotal());
        playerTotalLabel.setText("Total: " + player.getHandTotal());

        dealerCardsPanel.revalidate();
        dealerCardsPanel.repaint();
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }

    private JLabel createCardLabel(backend.Card card) {
        String fileName = getCardFileName(card);
        java.net.URL imageUrl = getClass().getClassLoader().getResource(fileName);

        if (imageUrl == null) {
            JLabel fallback = new JLabel(fileName);
            fallback.setForeground(Color.WHITE);
            return fallback;
        }

        ImageIcon icon = new ImageIcon(imageUrl);
        Image scaled = icon.getImage().getScaledInstance(460, 260, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(scaled));
    }

    private String getCardFileName(backend.Card card) {
        String face = "";
        String suit = "";

        switch (card.getFace()) {
            case ACE: face = "A"; break;
            case KING: face = "K"; break;
            case QUEEN: face = "Q"; break;
            case JACK: face = "J"; break;
            case TEN: face = "10"; break;
            case NINE: face = "9"; break;
            case EIGHT: face = "8"; break;
            case SEVEN: face = "7"; break;
            case SIX: face = "6"; break;
            case FIVE: face = "5"; break;
            case FOUR: face = "4"; break;
            case THREE: face = "3"; break;
            case TWO: face = "2"; break;
        }

        switch (card.getSuit()) {
            case HEARTS: suit = "H"; break;
            case SPADES: suit = "S"; break;
            case DIAMONDS: suit = "D"; break;
            case CLUBS: suit = "C"; break;
        }

        return face + suit + ".png";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
