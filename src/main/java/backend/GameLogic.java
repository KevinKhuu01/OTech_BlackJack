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
        private static Player dealer;

        // Constructor
        public GameLogic()
        {
            playerList = new HashMap<String, Player>();
            betList = new HashMap<Player, Integer>();
            playerHands = new HashMap<Player, List<Card>>();
            playerHandTotals = new HashMap<Player, Integer>();
            dealer = new Player("dealer");
            playerList.put("dealer", dealer);
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
            Player p = new Player(name);
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
                    p.setStatus(Player.STATUS.PLAYING);
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

        public static void stay(Player p)
        {
            p.setStatus(Player.STATUS.STAY);
            boolean checkAllStay = true;

            // check if everyone has stayed
            for(Player pl : playerList.values())
            {
                if(!pl.getUsername().equalsIgnoreCase("dealer") && pl.getStatus() != Player.STATUS.STAY)
                {
                    checkAllStay = false;
                    break;
                }
            }

            if(checkAllStay)
            {
                while (getHandTotal(dealer) < 17)
                {
                    hit(dealer);
                }

                if (getHandTotal(dealer) >= 17 && getHandTotal(dealer) <= 21)
                {
                    dealer.setStatus(Player.STATUS.STAY);
                }
                settleRound();
            }
        }

        // check if a player has won
        public static boolean checkWin(Player p)
        {
            if(getHandTotal(p) == 21)
            {
                p.setStatus(Player.STATUS.WIN);
                int depositAmount = betList.get(p) * 2;
                p.depositBalance(depositAmount);
                return true;
            }
            else if(getHandTotal(p) > 21)
            {
                p.setStatus(Player.STATUS.LOST);
                if(p.getUsername().equals("dealer"))
                {
                    for(Player player : playerList.values())
                    {
                        if(!player.getUsername().equals("dealer"))
                        {
                            int depositAmount = betList.get(p) * 2;
                            p.depositBalance(depositAmount);
                        }
                    }
                }
            }

            return false;
        }

        // prints all player balances (to be removed or refractored to return list)
        public static void printBalance()
        {
            for(Player p : playerList.values())
            {
                System.out.println(p.getUsername() + ": $" + p.getBalance());
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
            Player realDealer = playerList.get("dealer");
            Player player = null;

            for (Player p : playerList.values()) {
                if (!p.getUsername().equalsIgnoreCase("dealer")) {
                    player = p;
                    break;
                }
            }

            if (realDealer == null || player == null) {
                return "UPDATE:0:|0:";
            }

            return "UPDATE:"
                    + getHandTotal(realDealer) + ":" + handToCodes(realDealer)
                    + "|"
                    + getHandTotal(player) + ":" + handToCodes(player);
        }

        private static String handToCodes(Player p) {
            List<Card> hand = getHand(p);
            if (hand == null || hand.isEmpty()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hand.size(); i++) {
                sb.append(cardToCode(hand.get(i)));
                if (i < hand.size() - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }

        private static String cardToCode(Card card) {
            String face = "";
            String suit = "";

            switch (card.getFace()) {
                case ACE: face = "A"; break;
                case KING: face = "K"; break;
                case QUEEN: face = "Q"; break;
                case JACK: face = "J"; break;
                case TEN: face = "10"; break;
                case NINE: face = "9"; break;
                case EIGHT: face = "8"; break;
                case SEVEN: face = "7"; break;
                case SIX: face = "6"; break;
                case FIVE: face = "5"; break;
                case FOUR: face = "4"; break;
                case THREE: face = "3"; break;
                case TWO: face = "2"; break;
            }

            switch (card.getSuit()) {
                case HEARTS: suit = "H"; break;
                case SPADES: suit = "S"; break;
                case DIAMONDS: suit = "D"; break;
                case CLUBS: suit = "C"; break;
            }

            return face + suit;
        }

        public static Player getPlayer(String name) {
            return playerList.get(name);
        }

        public static String getRoundResult(String playerName) {
            Player p = playerList.get(playerName);

            if (p == null) {
                return "PLAYING";
            }
            if (p.getStatus() == Player.STATUS.WIN) {
                return "WIN";
            }
            if (p.getStatus() == Player.STATUS.LOST) {
                return "LOSE";
            }
            return "PLAYING";
        }

        private static void settleRound() {
            int dealerTotal = getHandTotal(dealer);

            for (Player p : playerList.values()) {
                if (p.getUsername().equalsIgnoreCase("dealer")) {
                    continue;
                }

                if (p.getStatus() == Player.STATUS.WIN || p.getStatus() == Player.STATUS.LOST) {
                    continue;
                }

                int playerTotal = getHandTotal(p);

                if (dealerTotal > 21 || playerTotal > dealerTotal) {
                    p.setStatus(Player.STATUS.WIN);

                    Integer bet = betList.get(p);
                    if (bet != null) {
                        p.depositBalance(bet * 2);
                    }
                } else {
                    p.setStatus(Player.STATUS.LOST);
                }
            }
        }
    }
