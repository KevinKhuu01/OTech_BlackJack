package backend;

import java.util.*;
import static java.util.Collections.shuffle;

public class GameLogic {
    private static Map<String, Player> playerList;
    private static HashMap<Player, Integer> betList;
    private static Stack<Card> deck = new Stack<Card>();
    public GameLogic()
    {
        playerList = new HashMap<String, Player>();
        betList = new HashMap<Player, Integer>();
        addPlayer("dealer");
    }

    public static void addPlayer(String name)
    {
        Player p = new Player(name);
        playerList.put(name, p);
    }

    public static void startGame()
    {
        newDeck();
        for(Player p : playerList.values())
        {
            for(int i = 0; i < 2; i++)
            {
                hit(p);
            }
        }
        printGame();
    }

    public static void printBalance()
    {
        for(Player p : playerList.values())
        {
            System.out.println(p.getName() + ": $" + p.getBalance());
        }
        System.out.println();
    }

    public static void hit(Player p)
    {
        if(p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.WIN)
        {
            return;
        }

        p.getHand().add(deck.pop());
    }

    public static void printGame()
    {
        for(Player p : playerList.values())
        {
            System.out.println(p.getName() + ": " + p.getHand() + " (" + p.getHandTotal() + ") " + " [" + checkWin(p) + "]");
        }
        System.out.println();
    }

    public static String checkWin(Player p)
    {
        if(p.getHandTotal() == 21)
        {
            p.setStatus(Player.STATUS.WIN);
            int depositAmount = betList.get(p) * 2;
            p.depositBalance(depositAmount);
        }
        else if(p.getHandTotal() > 21)
        {
            p.setStatus(Player.STATUS.LOST);
            if(p.getName().equals("dealer"))
            {
                for(Player player : playerList.values())
                {
                    if(!player.getName().equals("dealer"))
                    {
                        int depositAmount = betList.get(p) * 2;
                        p.depositBalance(depositAmount);
                    }
                }
            }
        }

        return p.getStatus().toString();
    }

    private static void newDeck()
    {
        for(Card.Face face : Card.Face.values())
        {
            for(Card.Suit suit : Card.Suit.values())
            {
                deck.push(new Card(face, suit));
            }
        }
        shuffleDeck();
    }

    private static void shuffleDeck()
    {
        shuffle(deck);
    }

    private static void playerBet(Player p, int bet)
    {
        betList.put(p, bet);
    }

    public static void main(String[] args)
    {
        GameLogic game1 = new GameLogic();
        addPlayer("Kevin");
        addPlayer("bob");
        startGame();
        printBalance();
    }
}
