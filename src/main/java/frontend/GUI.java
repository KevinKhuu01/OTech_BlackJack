package frontend;

import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;

    private backend.GameLogic gameLogic;
    private backend.Player player;
    private backend.Player dealer;

//    private JLabel playerCardsLabel;
//    private JLabel playerTotalLabel;
//    private JLabel dealerCardsLabel;
//    private JLabel dealerTotalLabel;


    public GUI(){
        setTitle("OTech Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

//        gameLogic = new backend.GameLogic();
//        backend.GameLogic.addPlayer("player");
//        backend.GameLogic.startGame();
//
//        player = backend.GameLogic.getPlayer("player");
//        dealer = backend.GameLogic.getPlayer("dealer");

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

    private JPanel  createGamePanel() {
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

        JButton hitButton = new JButton("Hit");
        JButton standButton = new JButton("Stand");

        Font buttonFont = loadCustomFont(25f);
        hitButton.setFont(buttonFont);
        standButton.setFont(buttonFont);

        hitButton.setMaximumSize(new Dimension(220, 45));
        standButton.setMaximumSize(new Dimension(220, 45));
        gamePanel.add(hitButton);
        gamePanel.add(standButton);
        gamePanel.add(Box.createVerticalGlue());
        backgroundPanel.add(gamePanel, BorderLayout.SOUTH);



        return   backgroundPanel;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GUI::new);
    }
}
