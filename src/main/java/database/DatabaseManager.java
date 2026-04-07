package database;

import backend.Player;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DatabaseManager {

    // Initalize db connection
    private static final String URL = "jdbc:sqlite:blackjack.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Add new user in db
    public static boolean newUser(int id, String username, String plainPassword) {
        String sql = "INSERT INTO users (id, username, password_hash, balance) VALUES (?, ?, ?, ?)"; // Insert query

        try (Connection conn = DatabaseManager.getConnection(); // open db connection
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) { //

            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt()); // encrypt password so no one can see it

            // Modify sql script. 1st parameter = ? index, 2nd is value
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setInt(4, 1000); // default balance = $1000

            int rows = preparedStatement.executeUpdate(); // Execute code and return number of rows modified
            return rows > 0; // true if successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }  /** To Do: Set up username from client **/

    // Login existing user and return Player object if password matches
    /** To Do: set up from client**/
    public static boolean loginUser(String username, String plainPassword) {
        String sql = "SELECT id, username, password_hash, balance FROM users WHERE username = ?"; // search query

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                if (BCrypt.checkpw(plainPassword, storedHash)) {
                    return true;
                }
                else
                {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

//    // Find a.java user by username without checking password
//    public Player getUserByUsername(String username) {
//        String sql = "SELECT id, username, balance FROM users WHERE username = ?";
//
//        try (Connection conn = DatabaseManager.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, username);
//            ResultSet rs = stmt.executeQuery();
//
//            if (rs.next()) {
//                int id = rs.getInt("id");
//                int balance = rs.getInt("balance");
//
//                Player player = new Player(username);
//                player.setBalance(balance);
//
//                return player;
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }

    // Get balance
    public static double getBalance(Player p)
    {
        String sql = "SELECT balance FROM users WHERE username = ?";
        double balance = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement sqlScript = conn.prepareStatement(sql)) {

            sqlScript.setString(1, p.getUsername());
            ResultSet sqlReturn = sqlScript.executeQuery();
            balance = sqlReturn.getInt("balance");

            return balance;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    // Update a.java user's balance
    public static boolean setBalance(Player p, double newBalance) {
        String sql = "UPDATE users SET balance = ? WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement sqlScript = conn.prepareStatement(sql)) {

            sqlScript.setDouble(1, newBalance);
            sqlScript.setString(2, p.getUsername());

            int rows = sqlScript.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add amount to current balance
    public static boolean deposit(Player p, int amount) {
        String sql = "UPDATE users SET balance = balance + ? WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, amount);
            stmt.setString(2, p.getUsername());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Subtract amount from current balance
    public static boolean withdraw(Player p, int amount) {
        String sql = "UPDATE users SET balance = balance - ? WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, amount);
            stmt.setString(2, p.getUsername());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}