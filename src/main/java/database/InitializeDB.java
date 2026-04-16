package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InitializeDB {
    public static void initialize() {
        /** Creates database file if it does not already exit
         * Primary key identifier is id (integer) - could have used username as the rest of the classes reference player name.
         * Username is unique
         * Password cannot be null
         * Balance cannot be null and is 1000 by default (fail safe)
         **/
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                balance INTEGER NOT NULL DEFAULT 1000
            );
        """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement sqlScript = conn.createStatement())
        {
            sqlScript.execute(sql); // Execute script
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}