package server;

import backend.GameLogic;
import backend.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single Blackjack table
 * Each table owns its own GameLogic instance so game state is fully isolated.
 */
public class Table {

    public static final int MAX_PLAYERS = 4;

    private final int tableId;
    private final GameLogic gameLogic;
    private final List<ClientConnectionHandler> clients = new ArrayList<>();

    public Table(int tableId) {
        this.tableId = tableId;
        this.gameLogic = new GameLogic(); // isolated game state per table
    }

    public int getTableId() {
        return tableId;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    /** Returns true if this table can accept another player. */
    public boolean hasRoom() {
        return clients.size() < MAX_PLAYERS;
    }

    public boolean isEmpty() {
        return clients.isEmpty();
    }

    /** Add a client/player to this table and register the Player in GameLogic. */
    public void addClient(ClientConnectionHandler handler, Player player) {
        clients.add(handler);
        gameLogic.addPlayer(player);
    }

    /** Remove a client from the table (on disconnect). */
    public void removeClient(ClientConnectionHandler handler) {
        clients.remove(handler);
        // Note: GameLogic does not need cleanup unless you want to award wins on disconnect.
    }

    public List<ClientConnectionHandler> getClients() {
        return clients;
    }

    public int getPlayerCount() {
        return clients.size();
    }

    /** Broadcast a message to every client sitting at this table. */
    public void broadcast(String message) {
        for (ClientConnectionHandler c : clients) {
            c.sendMessage(message);
        }
    }

    /**
     * Push the current game state (hand + balance) to every client at the table,
     * and also send WIN/LOSE/DRAW results to clients whose round is over.
     */
    public void updateAllClients() {
        for (ClientConnectionHandler c : clients) {
            if (c.getPlayerName() == null) continue;
            c.sendMessage(gameLogic.getGameState(c.getPlayerName()));
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