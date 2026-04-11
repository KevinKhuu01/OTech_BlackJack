package frontend;

import frontend.GUIClasses.Pages.CreateAccountPage;
import frontend.GUIClasses.Pages.LoginPage;
import frontend.GUIClasses.Pages.StartPage;
import frontend.GUIClasses.Pages.GamePage;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final Client client;
    private GamePage gamePage;
    private StartPage startPage;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
    public GUI(Client client){
        this.client = client; // connect gui to client

        setTitle("OTech Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPage loginPage = new LoginPage(client, cardLayout, mainPanel);
        CreateAccountPage createAccountPage = new CreateAccountPage(client, cardLayout, mainPanel);
        startPage = new StartPage(client, cardLayout, mainPanel);
        gamePage = new GamePage(client);

        mainPanel.add(loginPage.createLoginMenu(), "login");
        mainPanel.add(createAccountPage.createAccountMenu(), "create");
        mainPanel.add(startPage.createStartMenu(), "start");
        mainPanel.add(gamePage.createGamePanel(), "game");

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "login");

        setVisible(true);
    }

    /** METHODS --------------------------------------------------------------------------------------------------- **/
    // Processes and routes server responses
    public void processServerResponse(String message) {
        // Login responses
        if(message.equals("LOGIN_OK"))
        {
            cardLayout.show(mainPanel, "start");
        }
        else if(message.equals("LOGIN_FAIL"))
        {
            JOptionPane.showMessageDialog(mainPanel, "Invalid username or password");
        }
        else if(message.equals("REGISTER_OK"))
        {
            JOptionPane.showMessageDialog(mainPanel, "Registration complete. Welcome!");
            cardLayout.show(mainPanel, "login");
        }
        else if(message.equals("REGISTER_FAIL"))
        {
            JOptionPane.showMessageDialog(mainPanel, "Username already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }

        // Table responses
        else if(message.startsWith("TABLELIST |"))
        {
            startPage.tableList(message);
        }
        else if (message.equals("JOINED_TABLE"))
        {
            cardLayout.show(mainPanel, "game");
        }
        else if (message.startsWith("JOIN_FAIL"))
        {
            JOptionPane.showMessageDialog(mainPanel, message.substring(10));
        }

        // Game responses
        else if(message.equals("DRAW"))
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
        else if(message.startsWith("BUST"))
        {
            gamePage.setResultLabelText("You Lose!", Color.RED);
            gamePage.disableButtons();
        }
        else if(message.startsWith("BLACKJACK"))
        {
            gamePage.setResultLabelText("You Win!", Color.GREEN);
            gamePage.disableButtons();
        }
    }

    // Helper method to keep processServerResponse clean
    private void resetTimer() {
        Timer timer = new Timer(1000, e -> {
            gamePage.resetTable();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
