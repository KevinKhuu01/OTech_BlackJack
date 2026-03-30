package backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private Random random = new Random();
    private int cardValue;
    private List<Card> hand;
    private int handTotal;
    public enum STATUS { WIN, LOST, STAY, PLAYING };
    private STATUS status;
    private int balance;

    // Constructor
    public Player(String name)
    {
        this.name = name;
        this.cardValue = 0;
        this.hand = new ArrayList<Card>();
        this.handTotal = 0;
        this.status = status.PLAYING;
        this.balance = 1000;
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

    public int getHandTotal()
    {
        handTotal = 0;
        for(Card c : hand)
        {
            handTotal += c.getValue();
        }
        return handTotal;
    }

    public void setCardValue(Card card) {
        this.hand.add(card);
    }
}
