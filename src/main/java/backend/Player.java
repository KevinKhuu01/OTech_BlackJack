package backend;

import database.DatabaseManager;

public class Player {
    private final String username;
    public enum STATUS { WIN, LOST, STAY, PLAYING };
    private STATUS status;
    private double balance;

    // Constructor
    public Player(String name)
    {
        this.username = name;
        this.status = STATUS.PLAYING;
    }

    // Getters and setters
    public String getUsername() {
        return this.username;
    }

    public double getBalance()
    {
        return DatabaseManager.getBalance(this);
    }

    public void setBalance(double balance)
    {
        if (!DatabaseManager.setBalance(this, balance))
        {
            System.out.println("Database error with setting balance");
        }

    }

    public void withdrawBalance(int amount)
    {
        if(!DatabaseManager.withdraw(this, amount))
        {
            System.out.println("Database Error with withdrawing amount");
        }
    }

    public void depositBalance(int amount)  /** To Do **/
    {
        if(!DatabaseManager.deposit(this, amount))
        {
            System.out.println("Database error with depositing amount");
        }

    }

    public STATUS getStatus()
    {
        return this.status;
    }

    public void setStatus(STATUS status)
    {
        this.status = status;
    }

}
