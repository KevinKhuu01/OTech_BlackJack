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
        if(!playerList.containsKey(name)) {
            Player p = new Player(name, playerList.size());
            playerList.put(name, p);
        }
    }

    public static Player getPlayer(String name) {
        return playerList.get(name);
    }

    public static Collection<Player> getPlayers() {
        return playerList.values();
    }

    public static void startGame()
    {
        deck.clear();
        newDeck();

        for (Player p : playerList.values()) {
            p.getHand().clear();
            p.setStatus(Player.STATUS.PLAYING);
        }

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
        if(p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.WIN || p.getStatus() == Player.STATUS.STAY)
        {
            return;
        }

        if (!deck.isEmpty()) {
            p.getHand().add(deck.pop());
        }

        if (p.getHandTotal() > 21) {
            p.setStatus(Player.STATUS.LOST);
        } else if (p.getHandTotal() == 21) {
            p.setStatus(Player.STATUS.WIN);
        }
    }

    public static void stay(Player p) {
        p.setStatus(Player.STATUS.STAY);
    }

    public static String getGameState() {
        String result = "";

        for (Player p : playerList.values()) {
            result += p.getName() + ": ";
            result += p.getHand() + " ";
            result += "(" + p.getHandTotal() + ") ";
            result += "[" + p.getStatus() + "]";
            result += "\n";
        }

        return result;
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
