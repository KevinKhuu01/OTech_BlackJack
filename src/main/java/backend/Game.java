package backend;

import java.io.IOException;
import java.util.*;
import static java.util.Collections.shuffle;

    public class Game {

        // Fields
        private Map<String, Player> playerList;
        private HashMap<Player, Integer> betList;
        private HashMap<Player, List<Card>> playerHands;
        private HashMap<Player, Integer> playerHandTotals;
        private Stack<Card> deck = new Stack<Card>();
        private Player dealer;

        // Constructor
        public Game()
        {
            playerList = new HashMap<>();
            betList = new HashMap<>();
            playerHands = new HashMap<>();
            playerHandTotals = new HashMap<>();
            dealer = new Player("dealer");
            playerList.put("dealer", dealer);
        }

        /** Methods **/

        // Player Management ------------------------------------
        public int getPlayerCount()
        {
            return playerList.size() -1;
        }

        public void addPlayer(Player p) {
            playerList.put(p.getUsername(), p);
        }

        public Player getPlayer(String name) {
            return playerList.get(name);
        }


        // Hand Helper Methods ------------------------------------

        public List<Card> getHand(Player p) {
            return playerHands.get(p);
        }

        public int getHandTotal(Player p)
        {
            int handTotal = 0;
            int numOfAces = 0;
            for(Card c : getHand(p))
            {
                if(c.getFace() == Card.Face.ACE)
                {
                    handTotal += 11;
                    numOfAces++;
                }
                else
                {
                    handTotal += c.getValue();
                }
            }

            while(handTotal > 21 && numOfAces > 0)
            {
                handTotal -= 10;
                numOfAces--;
            }
            return handTotal;
        }


        // Game Logical Component Methods

        public void dealNewPlayer(Player p)
        {
            p.setStatus(Player.STATUS.PLAYING);

            if(!playerHands.containsKey(p))
            {
                playerHands.put(p, new ArrayList<Card>());
            }
            playerBet(p, 100);
            hit(p);
            hit(p);
        }


        private String dealerHandToCodes(boolean hideFirstCard){
            List<Card> hand = getHand(dealer);

            if (hand == null || hand.isEmpty()){
                return "";
            }
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < hand.size(); i++){
                if(i == 0 && hideFirstCard){
                    sb.append("backside");
                }
                else {
                    sb.append(cardToCode(hand.get(i)));
                }
                if (i < hand.size() - 1){
                    sb.append(",");
                }
            }
            return sb.toString();
        }


       private int getVisibleDealerTotal(boolean hideFirstCard){
            List<Card> hand = getHand(dealer);

            if(!hideFirstCard){
                return getHandTotal(dealer);
            }

            int total = 0;

            for(int i = 1; i < hand.size(); i++){
                Card c = hand.get(i);
                if(c.getFace() == Card.Face.ACE){
                    total += 11;
                }
                else {
                    total += c.getValue();
                }
            }
            return total;
       }
        // Starts game: Ensures there are players in the game, create a new deck, deals 2 cards to each player
        public void startGame()
        {
            try
            {
                if(playerList.isEmpty())
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

                    if(!p.getUsername().equalsIgnoreCase("dealer"))
                    {
                        playerBet(p, 100); // change bet amount to bet method
                    }
                }

                for(Player p : playerList.values())
                {
                    hit(p);
                    hit(p);
                    checkWin(p);
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        // Generates new deck
        private void newDeck()
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
        public void hit(Player p)
        {
            if(p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.WIN)
            {
                return;
            }

            getHand(p).add(deck.pop());
            checkWin(p);
        }

        public void stay(Player p)
        {
            p.setStatus(Player.STATUS.STAY);
            boolean checkAllStay = true;

            // check if everyone has stayed
            for(Player pl : playerList.values())
            {
                if(!pl.getUsername().equalsIgnoreCase("dealer")
                        && pl.getStatus() != Player.STATUS.STAY
                        && pl.getStatus() != Player.STATUS.LOST
                        && pl.getStatus() != Player.STATUS.WIN)
                {
                    checkAllStay = false;
                    break;
                }
            }

            // If everyone has stayed then finish the round by checking who has won
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
                for (Player pl : playerList.values())
                {
                    if(!pl.getUsername().equalsIgnoreCase("dealer"))
                    {
                        checkWin(pl);
                    }
                }
            }
        }

        // check if a.java player has won
        public String checkWin(Player p)
        {
            int playerTotal = getHandTotal(p);
            int dealerTotal = getHandTotal(dealer);

            // If p is dealer
            if (p.getUsername().equalsIgnoreCase("dealer")) {
                // Check if dealer has lost
                if (playerTotal > 21) {
                    p.setStatus(Player.STATUS.LOST);
                    return "lost";
                }
                // check if dealer has won
                if (playerTotal == 21) {
                    p.setStatus(Player.STATUS.WIN);
                    return "won";
                }
                return "playing";
            }

            // if p is player and busts
            if(playerTotal > 21)
            {
                p.setStatus(Player.STATUS.LOST);
                return "lost";
            }

            // if p is player and has 21
            if (playerTotal == 21) {
                p.setStatus(Player.STATUS.WIN);
                p.depositBalance(betList.get(p) * 2);
                return "won";
            }

            // if dealer is still playing (not won, lost, or stay)
            if (dealer.getStatus() != Player.STATUS.STAY && dealer.getStatus() != Player.STATUS.LOST) {
                return "playing";
            }

            // if p is the player and dealer has busted (previous check was if p was dealer)
            if (dealerTotal > 21) {
                p.setStatus(Player.STATUS.WIN);
                p.depositBalance(betList.get(p) * 2);
                return "won";
            }

            // end of round comparisons
            if (playerTotal > dealerTotal) {
                p.setStatus(Player.STATUS.WIN);
                p.depositBalance(betList.get(p) * 2);
                return "won";
            }
            else if (playerTotal < dealerTotal) {
                p.setStatus(Player.STATUS.LOST);
                return "lost";
            }

            // if both players have won or have equal hands (remaining conditions), declare draw
            p.setStatus(Player.STATUS.DRAW);
            p.depositBalance(betList.get(p));

            return "draw";
        }

        // places a bet for player p
        private void playerBet(Player p, int bet)
        {
            betList.put(p, bet);
            p.withdrawBalance(bet);
        }

        // Connection handling ------------------------------------------------------------------

        public String getGameState(String name) {
            Player realDealer = playerList.get("dealer");
            Player player = playerList.get(name);

            if (realDealer == null || player == null) {
                return "UPDATE:0:|0:|0";
            }

            boolean hideDealerCard = realDealer.getStatus() != Player.STATUS.STAY &&
                    realDealer.getStatus() != Player.STATUS.LOST;

            StringBuilder s = new StringBuilder();
            s.append("UPDATE:")
                    .append(getVisibleDealerTotal(hideDealerCard))
                    .append(":").append(dealerHandToCodes(hideDealerCard))
                    .append("|")
                    .append(getHandTotal(player))
                    .append(":")
                    .append(handToCodes(player))
                    .append("|")
                    .append(player.getBalance());

            for(Player p : playerList.values())
            {
                if(p != player && !p.getUsername().equalsIgnoreCase("dealer"))
                {
                    s.append("|")
                            .append(p.getUsername())
                            .append("|")
                            .append(getHandTotal(p))
                            .append(":")
                            .append(handToCodes(p))
                            .append("|")
                            .append(p.getBalance());
                }

            }
            return s.toString();
//            return "UPDATE:"
//                    + getVisibleDealerTotal(hideDealerCard) + ":" + dealerHandToCodes(hideDealerCard)
//                    + "|"
//                    + getHandTotal(player) + ":" + handToCodes(player)
//                    + "|"
//                    + player.getBalance();
        }

        public boolean isRoundOver() {
            for (Player p : playerList.values()) {
                if (!p.getUsername().equalsIgnoreCase("dealer")) {
                    if (p.getStatus() == Player.STATUS.PLAYING) {
                        return false;
                    }
                }
            }
            return true;
        }

        public String getRoundResult(String playerName) {
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
            if(p.getStatus() == Player.STATUS.DRAW)
            {
                return "DRAW";
            }
            return "PLAYING";
        }

        private String handToCodes(Player p) {
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

        private String cardToCode(Card card) {
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


    }
