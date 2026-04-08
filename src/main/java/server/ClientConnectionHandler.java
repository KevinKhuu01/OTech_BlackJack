package server;

import backend.GameLogic;
import backend.LoginHandler;
import backend.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientConnectionHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private String playerName;
    private Table table;

    public ClientConnectionHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    @Override
    public void run() {
        try
        {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            output.println("Connected to the server.");
            String message;

            while ((message = input.readLine()) != null) {
                System.out.println("Client [" + playerName + "] says: " + message);
                handleMessage(message);
            }

        }
        catch (IOException e)
        {
            System.out.println("Error handling client connection.");
        }
        finally
        {
            closeEverything();
        }
    }

    private void handleMessage(String message)
    {
        if(message.startsWith("LOGIN"))
        {
            handleLogin(message);
        }
        else if(message.startsWith("REGISTER "))
        {
            handleRegister(message);
        }
        else if (message.equalsIgnoreCase("START"))
        {
            handleStart(message);
        }
        else if (message.equalsIgnoreCase("HIT"))
        {
            handleHit(message);
        }
        else if (message.equalsIgnoreCase("STAY"))
        {
            handleStay(message);
        }
        else if (message.equalsIgnoreCase("STATE"))
        {
            if(table != null && playerName != null)
            {
                output.println(table.getGameLogic().getGameState(playerName));
            }
        }
        else if (message.equalsIgnoreCase("BYE"))
        {
            output.println("Goodbye from server.");
            closeEverything();
        }
        else
        {
            output.println("Unknown command.");
        }
    }


    // Server message Handlers ---------------------------------------------
    private void handleLogin(String message) {
        String[] loginMessage = message.split(" ");

        if(loginMessage.length < 3)
        {
            output.println("LOGIN_FAIL");
            return;
        }

        String username = loginMessage[1];
        String password = loginMessage[2];

        if(!LoginHandler.checkLogin(username, password))
        {
            output.println("LOGIN_FAIL");
        }

        playerName = username;

        table = TableManager.assignToTable(this);
        GameLogic g = table.getGameLogic();

        Player p = new Player(playerName);
        if(p == null)
        {
            p = new Player(playerName);
        }

        table.addClient(this, p);

        output.println("LOGIN_OK");

        table.broadcast("Table #" + table.getTableId() + ": " + playerName + " joined. (" + table.getPlayerCount() + "/" + Table.MAX_PLAYERS + " players)");

        if(table.getPlayerCount() == 1)
        {
            g.startGame();
        }
        else
        {
            g.dealNewPlayer(g.getPlayer(playerName));
        }

        table.updateAllClients();
    }

    private void handleRegister(String message)
    {
        String[] registerMessage = message.split(" ");

        if(registerMessage.length < 3)
        {
            output.println("REGISTER_FAIL");
        }

        if(LoginHandler.newUser(registerMessage[1], registerMessage[2]))
        {
            output.println("REGISTER_OK");
        }
        else
        {
            output.println("REGISTER_FAIL");
        }
    }

    private void handleStart(String message)
    {
        if(playerName == null || table == null)
        {
            return;
        }

        GameLogic g = table.getGameLogic();
        String status = g.getRoundResult(playerName);

        if (!status.equals("PLAYING")) {
            g.startGame();
            table.broadcast("A new round has started at Table #" + table.getTableId() + "!");
            table.updateAllClients();
        } else {
            output.println("Round still in progress!");
        }
    }

    private void handleHit(String message)
    {
        if (playerName == null || table == null)
        {
            output.println("You must LOGIN first.");
            return;
        }

        GameLogic g = table.getGameLogic();
        Player player = g.getPlayer(playerName);
        g.hit(player);

        table.broadcast(playerName + " chose HIT.");
        table.updateAllClients();

        String result = g.getRoundResult(playerName);
        if (!result.equals("PLAYING"))
        {
            output.println(result);
        }
    }

    private void handleStay(String message)
    {
        if (playerName == null || table == null)
        {
            output.println("You must LOGIN first.");
            return;
        }

        GameLogic g = table.getGameLogic();
        Player player = g.getPlayer(playerName);
        g.stay(player);

        table.broadcast(playerName + " chose STAY.");
        table.updateAllClients();

        String result = g.getRoundResult(playerName);
        if (!result.equals("PLAYING"))
        {
            output.println(result);
        }
    }


    public void sendMessage(String message) {
        if(output != null)
        {
            output.println(message);
        }
    }

    public void closeEverything() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Error closing connection for " + playerName);
            e.printStackTrace();
        }
    }
}
