package backend;

import database.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private int playerId;
    private String username;
    public enum STATUS { WIN, LOST, STAY, PLAYING };
    private STATUS status;
    private double balance;

    // Constructor
    public Player(String name, int id)
    {
        this.username = name;
        this.status = STATUS.PLAYING;
        this.playerId = id;
    }

    // Getters and setters
    public String getUsername() {
        return this.username;
    }

    public double getBalance()
    {
        return DatabaseManager.getBalance(this);
    }

    public void setBalance(double balance) { DatabaseManager.setBalance(this, balance); }

    public void withdrawBalance(int amount)
    {
        this.balance -= amount;
    } /** To Do **/

    public void depositBalance(int amount)  /** To Do **/
    {
        this.balance += amount;
    }

    public STATUS getStatus()
    {
        return this.status;
    }

    public void setStatus(STATUS status)
    {
        this.status = status;
    }

    public int getPlayerId() {return this.playerId;}


}
