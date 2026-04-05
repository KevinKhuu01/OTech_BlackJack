package backend;

import java.io.IOException;
import java.util.*;
import static java.util.Collections.shuffle;

    public class GameLogic {

        // Fields
        private static Map<String, Player> playerList;
        private static HashMap<Player, Integer> betList;
        private static HashMap<Player, List<Card>> playerHands;
        private static HashMap<Player, Integer> playerHandTotals;
        private static Stack<Card> deck = new Stack<Card>();

        // Constructor
        public GameLogic()
        {
            playerList = new HashMap<String, Player>();
            betList = new HashMap<Player, Integer>();
            addPlayer("dealer");
            playerHands = new HashMap<Player, List<Card>>();
            playerHandTotals = new HashMap<Player, Integer>();
        }

        /** Methods **/
        // Retrieve cards in hand
        public static List<Card> getHand(Player p) {
            return playerHands.get(p);
        }

        // Retrieve total sum of cards in hand
        public static int getHandTotal(Player p)
        {
            int handTotal = 0;
            for(Card c : getHand(p))
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

                // Reset game
                newDeck();
                playerHands.clear();
                playerHandTotals.clear();

                for(Player p : playerList.values())
                {
                    playerHands.put(p, new ArrayList<Card>());
                    playerBet(p, 100); // change bet amount to bet method

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

        // Creates a new deck of cards
        private static void newDeck()
        {
            deck.clear();
            for(Card.Face face : Card.Face.values())
            {
                for(Card.Suit suit : Card.Suit.values())
                {
                    deck.push(new Card(face, suit));
                }
            }
            shuffle(deck);
        }

        // takes card off top of deck and gives to player
        public static void hit(Player p)
        {
            if(p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.WIN)
            {
                return;
            }

            getHand(p).add(deck.pop());
            checkWin(p);
        }

        public static void stay(Player p) {
            p.setStatus(Player.STATUS.STAY);
        }

        // check if a player has won
        public static String checkWin(Player p)
        {
            if(getHandTotal(p) == 21)
            {
                p.setStatus(Player.STATUS.WIN);
                int depositAmount = betList.get(p) * 2;
                p.depositBalance(depositAmount);
            }
            else if(getHandTotal(p) > 21)
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
                result += getHand(p) + " ";
                result += "(" + getHandTotal(p) + ") ";
                result += "[" + p.getStatus() + "]";
                result += "\n";
            }

            return result;
        }

        public static Player getPlayer(String name) {
            return playerList.get(name);
        }

    }
