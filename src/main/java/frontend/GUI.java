package frontend;

import frontend.GUIClasses.pages.CreateAccountPage;
import frontend.GUIClasses.pages.LoginPage;
import frontend.GUIClasses.pages.StartPage;
import frontend.GUIClasses.pages.GamePage;
import frontend.GUIClasses.styling.BackGroundMusic;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private final Client client;
    private GamePage gamePage;
    private StartPage startPage;
    private BackGroundMusic bgm;
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
        // Login responses (LoginPage)
        if(message.equals("LOGIN_OK"))
        {
            startPage.getTableList();
            startPage.requestBalance();
            cardLayout.show(mainPanel, "start");
        }
        else if(message.equals("LOGIN_FAIL"))
        {
            JOptionPane.showMessageDialog(mainPanel, "Invalid username or password. Passwords are case sensitive.");
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

        // Table & Account Balance responses (StartPage)
        else if(message.startsWith("TABLELIST |"))
        {
            startPage.tableList(message);
        }
        else if (message.equals("JOINED_TABLE"))
        {
            cardLayout.show(mainPanel, "game");
            bgm = new BackGroundMusic();
            bgm.playMusic("src/main/resources/music/BlackJackBGM.wav");
        }
        else if (message.equals("LEFT_TABLE"))
        {
            startPage.getTableList();
            startPage.requestBalance();
            cardLayout.show(mainPanel, "start");
            bgm.stopMusic();
        }
        else if (message.startsWith("JOIN_FAIL"))
        {
            JOptionPane.showMessageDialog(mainPanel, message.substring(10));
        }
        else if(message.startsWith("BALANCE "))
        {
            String[] amount = message.split(" ");
            startPage.updateBalanceLabel(amount[1]);
        }
        else if(message.equals("INSUFFICIENT_FUNDS"))
        {
            gamePage.checkSufficientFunds();
        }

        // Game responses (GamePage)
        else if(message.equals("DRAW"))
        {
            gamePage.setResultLabelText("Draw!", Color.LIGHT_GRAY);
        }
        else if(message.equals("WIN"))
        {
            gamePage.setResultLabelText("You Won!", Color.GREEN);
        }
        else if(message.equals("LOSE"))
        {
            gamePage.setResultLabelText("You Lost!", Color.RED);
        }
        else if(message.startsWith("A new round"))
        {
            gamePage.resetTable();
        }
        else if (message.startsWith("UPDATE:"))
        {
            String data = message.substring(7);
            gamePage.updateGameDisplay(data);
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
}
