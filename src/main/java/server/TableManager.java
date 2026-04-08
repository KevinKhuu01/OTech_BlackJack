package server;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all active tables.
 * Assigns incoming players to an existing table with room, or creates a new one.
 */
public class TableManager {

    private static final List<Table> tables = new ArrayList<>();
    private static int nextTableId = 1;

    /**
     * Assigns the given handler to a table.
     * Reuses the first table that has room; otherwise creates a new table.
     *
     * @return The Table the player was assigned to.
     */
    public static synchronized Table assignToTable(ClientConnectionHandler handler) {
        // Find an existing table with room
        for (Table table : tables) {
            if (table.hasRoom()) {
                return table;
            }
        }
        // No room anywhere — open a new table
        Table newTable = new Table(nextTableId++);
        tables.add(newTable);
        System.out.println("New table created: Table #" + newTable.getTableId());
        return newTable;
    }

    /**
     * Removes a client from their table. If the table becomes empty, it is
     * dissolved so its ID can be reused in the future.
     */
    public static synchronized void removeFromTable(Table table, ClientConnectionHandler handler) {
        if (table == null) return;
        table.removeClient(handler);
        if (table.isEmpty()) {
            tables.remove(table);
            System.out.println("Table #" + table.getTableId() + " dissolved (no players left).");
        }
    }

    public static List<Table> getTables() {
        return tables;
    }
}