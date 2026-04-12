package server;

import backend.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import static backend.TableManager.getTables;
import static database.DatabaseManager.getBalance;

public class ClientConnectionHandler implements Runnable {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private String playerName;
    private Table table;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/

    /** Constructs a ClientConnectionHandler for a given client socket.
     * @param clientSocket the socket representing the client connection
     **/
    public ClientConnectionHandler(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    /** METHODS --------------------------------------------------------------------------------------------------- **/

    /** Returns the username of the connected player.
     * @return the player's username
     **/
    public String getPlayerName()
    {
        return playerName;
    }

    /** RUNNABLE OVERRIDE (BUILD CONNECTION TO CLIENT) --------------------------------------------------------------------------------------------------- **/

    /** Entry point for the client handler thread.
     * Establishes input/output streams and continuously listens for messages
     * from the client until the connection is closed.
     **/
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

    /** MESSAGE HANDLING --------------------------------------------------------------------------------------------------- **/

    /** Parses and routes incoming client messages to the appropriate handler.
     * Supported commands include:
     * LOGIN, REGISTER
     * BET, HIT, STAY, START
     * TABLELIST, NEWTABLE, JOINTABLE
     * STATE, GET_BALANCE, ADDFUNDS
     * LEAVETABLE, BYE
     * @param message the raw message received from the client
     **/
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
        else if(message.startsWith("BET "))
        {
            handleBet(message);
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
        else if (message.equalsIgnoreCase("TABLELIST"))
        {
            List<Table> tables = getTables();
            for(Table t : tables)
            {
                output.println("TABLELIST |" + t.getTableId() + "|" + t.numClients() + "|" + (t.hasRoom() ? "Y" : "N"));
            }
        }
        else if(message.equalsIgnoreCase("NEWTABLE"))
        {
            handleNewTable();
        }
        else if(message.startsWith("JOINTABLE "))
        {
            handleJoinTable(message);
        }
        else if (message.equalsIgnoreCase("STATE"))
        {
            if(table != null && playerName != null)
            {
                output.println(table.getGameLogic().getGameState(playerName));
            }
        }
        else if(message.equalsIgnoreCase("GET_BALANCE"))
        {
            output.println("BALANCE " + getBalance(playerName));
        }
        else if (message.startsWith("ADDFUNDS "))
        {
            try {
                int amount = Integer.parseInt(message.substring(9).trim());
                Player p = new Player(playerName);

                if (database.DatabaseManager.deposit(p, amount)) {
                    output.println("BALANCE " + getBalance(playerName));
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid fund amount received.");
            }
        }
        else if(message.equalsIgnoreCase("LEAVETABLE"))
        {
            if (table != null) {
                TableManager.removeFromTable(table, this);

                if (!table.isEmpty()) {
                    table.broadcast(playerName + " has left the table.");
                    table.getGameLogic().removePlayer(playerName);
                    table.removeClient(this);
                    table.updateAllClients();
                }

                this.table = null;
            }

            output.println("LEFT_TABLE");
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


    /** MESSAGE HANDLER HELPER METHODS --------------------------------------------------------------------------------------------------- **/

    /** Handles login requests from the client.
     * Expected format: LOGIN username password
     * @param message the login message
     **/
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

            Player p = new Player(playerName);
            if(p == null)
            {
                p = new Player(playerName);
            }

            output.println("LOGIN_OK");
        }

    }

    /** Handles user registration requests.
     * Expected format: REGISTER username password
     * @param message the registration message
     */
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

            if (g.isRoundOver())
            {
                table.sendResultsToAll();
                restartRound();
            }
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

    /** Processes a "stay" action from the player.
     * Ends the player's turn and updates the game state.
     *
     * @param message the stay command
     */
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

    /** UTILITY METHODS --------------------------------------------------------------------------------------------------- **/

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
                if (g.isRoundOver()) {
                    table.sendResultsToAll();
                    restartRound();
                }
            }
        }).start();
    }

    /** Creates a new table and assigns the current player to it.
     Also initializes the game if sufficient funds are available.
     **/
    private void handleNewTable() {
        this.table = TableManager.assignToTable(this, "NEWTABLE", playerName);
        Player p = new Player(playerName);
        this.table.addClient(this, p);

        if(table.getGameLogic().getPlayer(playerName).getBalance() < 100)
        {
            output.println("INSUFFICIENT_FUNDS");
            return;
        }

        this.table.broadcast("Table #" + this.table.getTableId() + ": " + playerName + " created the table.");
        this.table.getGameLogic().startGame();
        this.table.updateAllClients();

        output.println("JOINED_TABLE");

        if (this.table.getGameLogic().isRoundOver()) {
            this.table.sendResultsToAll();
            restartRound();
        }
    }

    /**
     * Handles requests to join an existing table.
     * Expected format: JOINTABLE tableId
     * @param message the join table command
     **/
    private void handleJoinTable(String message) {
        String tableIdStr = message.split(" ")[1];
        Table foundTable = TableManager.assignToTable(this, tableIdStr, playerName);

        if (foundTable != null)
        {
            this.table = foundTable;
            Player p = new Player(playerName);
            this.table.addClient(this, p);

            this.table.broadcast("Table #" + this.table.getTableId() + ": " + playerName + " joined. (" + this.table.getPlayerCount() + "/" + Table.MAX_PLAYERS + " players)");
            this.table.getGameLogic().dealNewPlayer(p);
            this.table.updateAllClients();

            output.println("JOINED_TABLE");
        }
        else
        {
            output.println("JOIN_FAIL Table is full or does not exist.");
        }
    }

    /**
     * Sets the player's bet for the next round.
     * Expected format: BET amount
     * @param message the bet command
     **/
    private void handleBet(String message)
    {
        String betAmount = message.split(" ")[1];
        table.getGameLogic().setNextBet(table.getGameLogic().getPlayer(playerName), Integer.parseInt(betAmount));
    }

    /** CONNECTION ACTION METHODS --------------------------------------------------------------------------------------------------- **/

    /**
     * Sends a message to the connected client.
     * @param message the message to send
     */
    public void sendMessage(String message) {
        if(output != null)
        {
            output.println(message);
        }
    }

    /** Closes all resources associated with this client connection.
     * This includes Removing the player from their table,
     * Closing input/output streams,
     * Closing the socket
     **/
    public void closeEverything() {
        try {
            if (table != null) {
                TableManager.removeFromTable(table, this);

                // If the table didn't dissolve (other players are still there), update their screens
                if (!table.isEmpty()) {
                    table.broadcast(playerName + " has left the table.");
                    // Optional: You may also need to remove the player from the GameLogic's player list here
                    table.getGameLogic().removePlayer(playerName);
                    table.removeClient(this);
                    table.updateAllClients();
                }
            }
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
