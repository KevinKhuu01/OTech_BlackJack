import java.util.*;

public class GameLogic {
    private static Map<String, Player> playerList;
    private static int betAmount;

    public GameLogic()
    {
        playerList = new HashMap<String, Player>();
        addPlayer("dealer");
    }

    public static void addPlayer(String name)
    {
        Player p = new Player(name);
        playerList.put(name, p);
    }

    public static void startGame(int bet)
    {
        printBalance();
        betAmount = bet;
        System.out.println("Bet amount: " + betAmount);
        for(Player p : playerList.values())
        {
            p.withdrawBalance(betAmount);
            for(int i = 0; i < 2; i++)
            {
                p.playerHit();
            }
        }
        printBalance();
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

    public static void hit(String name)
    {
        playerList.get(name).playerHit();
        printGame();
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
            p.depositBalance(betAmount*2);
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
                        player.setStatus(Player.STATUS.WIN);
                        player.depositBalance(betAmount*2);
                    }
                }
            }
        }

        return p.getStatus().toString();
    }

    public static void main(String[] args)
    {
        GameLogic game1 = new GameLogic();
        addPlayer("Kevin");
        addPlayer("bob");
        startGame(100);

        hit("Kevin");
        hit("bob");
        hit("dealer");
        printBalance();
    }
}
