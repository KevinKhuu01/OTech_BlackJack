import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private Random random = new Random();
    private int cardValue;
    private List<Integer> hand;
    private int handTotal;
    public enum STATUS { WIN, LOST, STAY, PLAYING };
    private STATUS status;
    private int balance;

    // Constructor
    public Player(String name)
    {
        this.name = name;
        this.cardValue = 0;
        this.hand = new ArrayList<Integer>();
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

    public List<Integer> getHand() {
        return this.hand;
    }

    public int getHandTotal()
    {
        handTotal = 0;
        for(int i : hand)
        {
            handTotal += i;
        }
        return handTotal;
    }

    public void setCardValue(int cardValue) {
        this.hand.add(cardValue);
    }

    // Methods
    public void playerHit()
    {
        if(this.status == STATUS.LOST || this.status == STATUS.WIN)
        {
            return;
        }
        int randomCard = 0;

        for(int i = 0; i < 2; i++)
        {
            randomCard = random.nextInt(13) + 1;
            if(randomCard > 11)
            {
                randomCard = 10;
            }
        }
        setCardValue(randomCard);
    }
}
