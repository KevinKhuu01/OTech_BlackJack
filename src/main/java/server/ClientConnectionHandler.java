package server;

import backend.Game;
import backend.LoginHandler;
import backend.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
        else
        {
            playerName = username;

            table = TableManager.assignToTable(this);
            Game g = table.getGameLogic();

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

        Game g = table.getGameLogic();
        boolean roundOver = g.isRoundOver();

        if (roundOver) {
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

        Game g = table.getGameLogic();
        Player player = g.getPlayer(playerName);
        g.hit(player);

        table.broadcast(playerName + " chose HIT.");
        table.updateAllClients();

        if (player.getStatus() == Player.STATUS.LOST)
        {
            output.println("BUST");
        }
        else if (player.getStatus() == Player.STATUS.WIN)
        {
            output.println("BLACKJACK");
        }

        if(g.isRoundOver())
        {
            table.sendResultsToAll();
            restartRound();
        }
    }

    private void handleStay(String message)
    {
        if (playerName == null || table == null)
        {
            output.println("You must LOGIN first.");
            return;
        }

        Game g = table.getGameLogic();
        Player player = g.getPlayer(playerName);
        g.stay(player);

        table.broadcast(playerName + " chose STAY.");
        table.updateAllClients();

        if(g.isRoundOver())
        {
            table.sendResultsToAll();
            restartRound();
        }
    }

    private void restartRound() {
        new Thread(() ->
        {
            try
            {
                Thread.sleep(1500);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
                return;
            }

            if (table == null) {
                return;
            }

            Game g = table.getGameLogic();
            if (g.isRoundOver()) {
                g.startGame();
                table.broadcast("A new round has started at Table #" + table.getTableId() + "!");
                table.updateAllClients();
            }
        }).start();
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
