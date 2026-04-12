package backend;

import database.DatabaseManager;

public class Player {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private final String username;
    public enum STATUS { WIN, DRAW, LOST, STAY, PLAYING };
    private STATUS status;
    private double balance;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
    public Player(String name)
    {
        this.username = name;
        this.status = STATUS.PLAYING;
    }

    /** GETTER AND SETTER METHODS --------------------------------------------------------------------------------------------------- **/
    public String getUsername() {
        return this.username;
    }

    public int getBalance()
    {
        return DatabaseManager.getBalance(this.username);
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

    /** Deposits the desired amount into a players balance
     * @param amount: The integer amount the user wants to deposit
     **/
    public void depositBalance(int amount)
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
