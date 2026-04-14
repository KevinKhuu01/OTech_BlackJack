package backend;

import database.DatabaseManager;

public class Player {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    private final String username;
    public enum STATUS { WIN, DRAW, LOST, STAY, PLAYING };
    private STATUS status;
    private double balance;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/

    /** Constructor for Player. Creates an instance of player with a username and sets their status to PLAYING
     * @param name The name of the user
     **/
    public Player(String name)
    {
        this.username = name;
        this.status = STATUS.PLAYING;
    }

    /** GETTER AND SETTER METHODS --------------------------------------------------------------------------------------------------- **/

    /** Getter for usernames
     * @return Username of Player
     **/
    public String getUsername() {
        return this.username;
    }

    /** Getter for the balance of a users funds from the database **/
    public int getBalance()
    {
        return DatabaseManager.getBalance(this.username);
    }

    /** Setter for user balance
     * @param balance The new balance that will be set for a Player
     **/
    public void setBalance(double balance)
    {
        if (!DatabaseManager.setBalance(this, balance))
        {
            System.out.println("Database error with setting balance");
        }
    }

    /** Method to withdraw an amount from a Players balance
     * @param amount The amount to be withdrawn
     **/
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

    /** Getter for the status of a Player
     * @return STATUS. Can be either WIN, DRAW, LOST, STAY or PLAYING**/
    public STATUS getStatus()
    {
        return this.status;
    }

    /** Setter for a Players status
     * @param status The new status of the user
     **/
    public void setStatus(STATUS status)
    {
        this.status = status;
    }
}
