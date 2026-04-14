package database;

import backend.Player;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.sql.*;

public class DatabaseManager {

    /** INITIALIZE DB CONNECTION --------------------------------------------------------------------------------------------------- **/
    private static final String URL = "jdbc:sqlite:blackjack.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Add new user in db
     * @param id: The integer ID of the new user
     * @param username: The username the new user enters at registration
     * @param plainPassword: The passsword the new user enters at registration
     **/
    public static boolean newUser(int id, String username, String plainPassword) {
        String sql = "INSERT INTO users (id, username, password_hash, balance) VALUES (?, ?, ?, ?)"; // Insert query

        try (Connection conn = DatabaseManager.getConnection(); // open db connection
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) { //

            String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt()); // encrypt password so no one can see it

            // Modify sql script. 1st parameter = ? index, 2nd is value
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setInt(4, 10000); // default balance = $1000

            int rows = preparedStatement.executeUpdate(); // Execute code and return number of rows modified
            return rows > 0; // true if successful

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Login existing user and return Player object if password matches
     * @param username
     * @param plainPassword: Unhidden password of the user logging in
     **/
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

    /** Get the balance of a Player from the database
     * @param p The Player whos balance is being retrieved from the database
     * @return Balance as an integer number
     **/
    public static int getBalance(String p)
    {
        String sql = "SELECT balance FROM users WHERE username = ?";
        double balance = 0;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement sqlScript = conn.prepareStatement(sql)) {

            sqlScript.setString(1, p);
            ResultSet sqlReturn = sqlScript.executeQuery();

            if (sqlReturn.next()) {
                balance = sqlReturn.getDouble("balance");
            }

            return (int)balance;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (int)balance;
    }

    /** Boolean method to update a.java user's balance
     * @param p The Player whose balance is being updated
     * @param newBalance The updated balance of the Player
     **/
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

    /** Add amount to current balance
     * @param p The player who's doing the deposit
     * @param amount the amount being deposited into the balance
     **/
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

    /** Subtract amount from current balance
     * @param p The player who's doing the withdrawal
     * @param amount the amount being withdrawn from the balance
     * **/
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