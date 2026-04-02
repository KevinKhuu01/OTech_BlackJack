package backend;

import java.io.IOException;
import java.util.*;
import static java.util.Collections.shuffle;

    public class GameLogic {

        // Fields
        private static Map<String, Player> playerList;
        private static HashMap<Player, Integer> betList;
        private static Stack<Card> deck = new Stack<Card>();

        // Constructor
        public GameLogic()
        {
            playerList = new HashMap<String, Player>();
            betList = new HashMap<Player, Integer>();
            addPlayer("dealer");
        }

        // Methods
        // Adds a new player to the current game
        public static void addPlayer(String name)
        {
            int random = (int)(Math.random() * 1000);
            Player p = new Player(name, random);
            playerList.put(name, p);
        }

        // Starts game: Ensures there are players in the game, create a new randomized deck, deals 2 cards to each player
        public static void startGame()
        {
            try
            {
                if(playerList.size() <= 0)
                {
                    throw new IOException("No players in game!");
                }
                newDeck();
                for(Player p : playerList.values())
                {
                    for(int i = 0; i < 2; i++)
                    {
                        hit(p);
                    }
                }
                getGameState();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        // Returns collection of current players in game
        public static Collection<Player> getPlayers() {
            return playerList.values();
        }

        // Creates a new deck of cards
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

        // randomizes deck order
        private static void shuffleDeck()
        {
            shuffle(deck);
        }

        // takes card off top of deck and gives to player
        public static void hit(Player p)
        {
            if(p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.WIN)
            {
                return;
            }

            p.getHand().add(deck.pop());
            checkWin(p);
        }

        public static void stay(Player p) {
            p.setStatus(Player.STATUS.STAY);
        }

        // check if a player has won
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

        // prints all player balances (to be removed or refractored to return list)
        public static void printBalance()
        {
            for(Player p : playerList.values())
            {
                System.out.println(p.getName() + ": $" + p.getBalance());
            }
            System.out.println();
        }

        // places a bet for a player
        private static void playerBet(Player p, int bet)
        {
            betList.put(p, bet);
            p.withdrawBalance(bet);
        }

        // prints out current game state with player's name, hand, hand total, and win/loss status
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

        public static Player getPlayer(String name) {
            return playerList.get(name);
        }
//
//    public static void main(String[] args)
//    {
//        GameLogic game1 = new GameLogic();
//        startGame();
//        printBalance();
//    }
    }
