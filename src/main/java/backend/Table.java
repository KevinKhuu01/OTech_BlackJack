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
    public Table(int tableId) {
        this.tableId = tableId;
        this.game = new Game(); // isolated game state per table
    }

    /** GETTER AND SETTER METHODS --------------------------------------------------------------------------------------------------- **/
    public int getTableId() {
        return tableId;
    }

    public Game getGameLogic() {
        return game;
    }

    public boolean hasRoom() {
        return clients.size() < MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return clients.isEmpty();
    }

    public int numClients() {return clients.size();}

    public void addClient(ClientConnectionHandler handler, Player player) {
        clients.add(handler);
        game.addPlayer(player);
    }

    public void removeClient(ClientConnectionHandler handler) {
        clients.remove(handler);
    }

    public List<ClientConnectionHandler> getClients() {
        return clients;
    }

    public int getPlayerCount() {
        return clients.size();
    }

    /** TABLE ACTION METHODS --------------------------------------------------------------------------------------------------- **/
    public void broadcast(String message) {
        for (ClientConnectionHandler c : clients) {
            c.sendMessage(message);
        }
    }

    public void updateAllClients() {
        for (ClientConnectionHandler c : clients) {
            if (c.getPlayerName() == null) continue;
            c.sendMessage(game.getGameState(c.getPlayerName()));
        }
    }

    public void sendResultsToAll() {
        for (ClientConnectionHandler client : clients) { // Assuming you have a list of clients
            String result = getGameLogic().getRoundResult(client.getPlayerName());
            if (!result.equals("PLAYING")) {
                client.sendMessage(result);
            }
        }
    }
}