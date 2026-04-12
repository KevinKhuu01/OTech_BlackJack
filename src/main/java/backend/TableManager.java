package backend;

import server.ClientConnectionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all active tables.
 * Assigns incoming players to an existing table with room, or creates a new one.
 */
public class TableManager {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private static final List<Table> tables = new ArrayList<>();
    private static int nextTableId = 1;


    /** GETTER --------------------------------------------------------------------------------------------------- **/
    public static List<Table> getTables() {
        return tables;
    }

    /** ACTION METHODS --------------------------------------------------------------------------------------------------- **/
    /**
     * Assigns the given handler to a table.
     * Reuses the first table that has room; otherwise creates a new table.
     *
     * @return The Table the player was assigned to.
     */
    public static synchronized Table assignToTable(ClientConnectionHandler handler, String message, String playerName) {
        if(message.equals("NEWTABLE"))
        {
            Table newTable = new Table(nextTableId++);
            tables.add(newTable);
            System.out.println("New table created: Table #" + newTable.getTableId());
            return newTable;
        }
        else
        {
            try
            {
                int tableId;
                for(Table t : tables)
                {
                    tableId = Integer.parseInt(message);
                    String[] table = message.split(" ");
                    if(t.getTableId() == tableId && t.hasRoom())
                    {
                        return t;
                    }
                }
            }
            catch (NumberFormatException e)
            {
                System.out.println("Invalid table ID.");
            }

        }
        return null;
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

}