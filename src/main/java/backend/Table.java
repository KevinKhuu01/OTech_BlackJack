package backend;

import server.ClientConnectionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single Blackjack table
 * Each table owns its own GameLogic instance so game state is fully isolated.
 */
public class Table {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    public static final int MAX_PLAYERS = 3;
    private final int tableId;
    private final Game game;
    private final List<ClientConnectionHandler> clients = new ArrayList<>();

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/

    /** Initializes a table with a unique ID
     * Creates a new Game instance for isolated gameplay
     **/
    public Table(int tableId) {
        this.tableId = tableId;
        this.game = new Game(); // isolated game state per table
    }

    /** GETTER AND SETTER METHODS --------------------------------------------------------------------------------------------------- **/

    /** Getter for table ID
     * Returns the unique identifier of the table
     **/
    public int getTableId() {
        return tableId;
    }

    /** Getter for game logic
     * Returns the Game instance associated with this table
     * Provides access to game state and logic
     **/
    public Game getGameLogic() {
        return game;
    }

    /** Checks if the table can accept more players
     * Compares current client count with MAX_PLAYERS
     * Returns true if space is available
     **/
    public boolean hasRoom() {
        return clients.size() < MAX_PLAYERS;
    }

    /** Checks if the table has no connected clients
     * Returns true if client list is empty
     **/
    public boolean isEmpty() {
        return clients.isEmpty();
    }

    /** Method to get the number of clients active
     * @return number of clients
     **/
    public int numClients() {return clients.size();}

    /** Adds a client connection to the table
     * Associates the client with a player
     * Registers the player in the game logic
     **/
    public void addClient(ClientConnectionHandler handler, Player player) {
        clients.add(handler);
        game.addPlayer(player);
    }

    /** Removes a client connection from the table
     * Does not remove the player from game logic
     **/
    public void removeClient(ClientConnectionHandler handler) {
        clients.remove(handler);
    }

    /** Getter for List of clients
     * @return the list of connected clients
     **/
    public List<ClientConnectionHandler> getClients() {
        return clients;
    }

    /** Getter for the nymber of Players at a table
     * @return the number of players currently at the table
     **/
    public int getPlayerCount() {
        return clients.size();
    }

    /** TABLE ACTION METHODS --------------------------------------------------------------------------------------------------- **/

    /** Method to send messages to the clients
     * Used for general updates or notifications
     **/
    public void broadcast(String message) {
        for (ClientConnectionHandler c : clients) {
            c.sendMessage(message);
        }
    }

    /** Sends updated game state to each client
     * Retrieves personalized game state per player
     * Skips clients without an assigned player name
     **/
    public void updateAllClients() {
        for (ClientConnectionHandler c : clients) {
            if (c.getPlayerName() == null) continue;
            c.sendMessage(game.getGameState(c.getPlayerName()));
        }
    }

    /** Sends round results to all clients
     * Only sends results if the round is finished
     * Skips players still in PLAYING state
     **/
    public void sendResultsToAll() {
        for (ClientConnectionHandler client : clients) { // Assuming you have a list of clients
            String result = getGameLogic().getRoundResult(client.getPlayerName());
            if (!result.equals("PLAYING")) {
                client.sendMessage(result);
            }
        }
    }
}