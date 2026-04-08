package frontend;

import frontend.GUIClasses.Pages.StartPage;
import frontend.GUIClasses.Pages.GamePage;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final Client client;
    private GamePage gamePage;

    public GUI(Client client){
        this.client = client; // connect gui to client

        setTitle("OTech Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        StartPage startPage = new StartPage(client, cardLayout, mainPanel);
        gamePage = new GamePage(client);
        mainPanel.add(startPage.createStartMenu(), "start");
        mainPanel.add(gamePage.createGamePanel(), "game");

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "start");

        setVisible(true);
    }

    // Processes and routes server responses
    public void processServerResponse(String message) {
        if(message.equals("DRAW"))
        {
            gamePage.setResultLabelText("Draw!", Color.LIGHT_GRAY);
            resetTimer();
        }
        else if(message.equals("WIN"))
        {
            gamePage.setResultLabelText("You Won!", Color.GREEN);
            resetTimer();
        }
        else if(message.equals("LOSE"))
        {
            gamePage.setResultLabelText("You Lost!", Color.RED);
            resetTimer();
        }
        else if (message.startsWith("UPDATE:"))
        {
            gamePage.updateGameDisplay(message.substring(7));
        }
        else if (message.startsWith("BALANCE:"))
        {
            gamePage.setBalanceLabel(message.substring(8).trim());
        }
    }

    // Helper method to keep processServerResponse clean
    private void resetTimer() {
        Timer timer = new Timer(2000, e -> {
            gamePage.resetTable();
            client.sendMessage("START");
        });
        timer.setRepeats(false);
        timer.start();
    }
}
