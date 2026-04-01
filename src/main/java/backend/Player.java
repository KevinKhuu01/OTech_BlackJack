package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private int playerId;
    private String name;
    private Random random = new Random();
    private int cardValue;
    private List<Card> hand;
    private int handTotal;
    public enum STATUS { WIN, LOST, STAY, PLAYING };
    private STATUS status;
    private int balance;

    // Constructor
    public Player(String name, int id)
    {
        this.name = name;
        this.cardValue = 0;
        this.hand = new ArrayList<Card>();
        this.handTotal = 0;
        this.status = status.PLAYING;
        this.balance = 1000;
        this.playerId = id;
    }

    // Getters and setters
    public String getName() {
        return this.name;
    }

    public int getBalance()
    {
        return this.balance;
    }

    public void withdrawBalance(int amount)
    {
        this.balance -= amount;
    }

    public void depositBalance(int amount)
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

    public List<Card> getHand() {
        return this.hand;
    }

    public int getPlayerId() {return this.playerId;}

    public int getHandTotal()
    {
        handTotal = 0;
        for(Card c : hand)
        {
            if(c.getFace() == Card.Face.ACE)
            {
                if((handTotal + 11) > 21)
                {
                    handTotal += 1;
                }
                else
                {
                    handTotal += 11;
                }
            }
            else
            {
                handTotal += c.getValue();
            }
        }
        return handTotal;
    }
}
