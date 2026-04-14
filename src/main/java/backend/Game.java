package backend;

import java.io.IOException;
import java.util.*;
import static java.util.Collections.shuffle;

/**
 * Game logic object for each table. Handles all blackjack rules and logical components
 */

public class Game {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
        private Map<String, Player> playerList;
        private HashMap<Player, Integer> currentBets = new HashMap<>();
        private HashMap<Player, Integer> nextBets;
        private HashMap<Player, List<Card>> playerHands;
        private HashMap<Player, Integer> playerHandTotals;
        private Stack<Card> deck = new Stack<Card>();
        private Player dealer;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
        /** Creates an instance of Game with:
         * A new list of players
         * Player hands
         * The total of each player's hand
         * The bets being placed
         * A dealer Player instance for the game
         **/
        public Game()
        {
            playerList = new HashMap<>();
            playerHands = new HashMap<>();
            playerHandTotals = new HashMap<>();
            currentBets = new HashMap<>();
            nextBets = new HashMap<>();
            dealer = new Player("dealer");
            playerList.put("dealer", dealer);
        }

    /** GETTERS AND UTILITY (HELPER) METHODS --------------------------------------------------------------------------------------------------- **/

        public int getPlayerCount()
        {
            return playerList.size() -1;
        }

        /** Method to add a player to the playerList
         * @param p The Player being added
         **/
        public void addPlayer(Player p) {
            playerList.put(p.getUsername(), p);
        }

        /** Getter for players
         * @param name Name of the player
         * @return Player name from playerList**/
        public Player getPlayer(String name) {
            return playerList.get(name);
        }

        /** Method to remove a player from the playerList
         * @param playerName the name of the Player
         **/
        public void removePlayer(String playerName)
        {
            playerList.remove(playerName);
        }

        /** Getter to retrieve the hand of a Player
         * @param p: the Player who's hand is to be retrieved
         * @return Players hand**/
        public List<Card> getHand(Player p) {
            return playerHands.get(p);
        }

        /** Computes the total value of the cards a Player has.
         * Handles case where a player may have an ACE card and make it worth 1 if hand total is above 11
         @param p The Player whos hand total is being computed
         **/
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


    /** HELPER METHOD FOR PLAYER JOINING OCCUPIED TABLE ------------------------------------------------------------------ **/

        /** Initializes a player for a new round
         * Sets player status to PLAYING
         * Creates a new hand if the player does not already have one
         * Places the player's bet for the round
         * Deals two initial cards to the player
         **/
        public void dealNewPlayer(Player p)
        {
            p.setStatus(Player.STATUS.PLAYING);

            if(!playerHands.containsKey(p))
            {
                playerHands.put(p, new ArrayList<Card>());
            }
            placeRoundBet(p);
            hit(p);
            hit(p);
        }

    /** HIDE DEALER CARD FEATURE --------------------------------------------------------------------------------------------------- **/

        /** Converts dealer's hand into a string of encoded card values
         * Optionally hides the dealer’s first card (used during gameplay)
         * Returns comma-separated card codes
         * Uses "backside" placeholder when the first card is hidden
         **/
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


        /** Computes dealer’s visible hand total
         * If first card is hidden, excludes it from calculation
         * Treats Ace as 11 when visible
         * Returns full total if cards are not hidden
         **/
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

    /** GAME INITIALIZATION --------------------------------------------------------------------------------------------------- **/

        /** Initializes a new game round
         * Validates that players exist
         * Resets deck, hands, and player states
         * Assigns bets to all non-dealer players
         * Deals two cards to each player and dealer
         * Handles automatic outcomes for blackjack (21)
         **/
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
                dealer.setStatus(Player.STATUS.PLAYING);

                for(Player p : playerList.values())
                {
                    p.setStatus(Player.STATUS.PLAYING);
                    playerHands.put(p, new ArrayList<Card>());

                    if(!p.getUsername().equalsIgnoreCase("dealer"))
                    {
                        placeRoundBet(p);
                    }
                }

                for(Player p : playerList.values())
                {
                    hit(p);
                    hit(p);
                }

                // if dealer gets 21, everyone stays and loses unless they also have 21
                if (getHandTotal(dealer) == 21) {
                    for (Player p : playerList.values()) {
                        if (p.getStatus() == Player.STATUS.PLAYING) {
                            stay(p);
                        }
                    }
                } else {
                    // if any players get 21, make them stay
                    for (Player p : playerList.values()) {
                        if (getHandTotal(p) == 21) {
                            stay(p);
                        }
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        /** Creates a new deck of 52 cards
         * Iterates through all faces and suits
         * Pushes cards onto stack and shuffles them
         **/
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

    /** GAME LOGIC --------------------------------------------------------------------------------------------------- **/

        /** Deals one card to a player from the deck
         * Prevents action if player is no longer active
         * Adds card to player's hand
         * Triggers win/loss check after drawing
         **/
        public void hit(Player p)
        {
            if(p.getStatus() == Player.STATUS.LOST
                    || p.getStatus() == Player.STATUS.WIN
                    || p.getStatus() == Player.STATUS.STAY
                    || p.getStatus() == Player.STATUS.DRAW)
            {
                return;
            }

            getHand(p).add(deck.pop());
            checkWin(p);
        }

        /** Sets player status to STAY if currently playing
         * Checks if all players have finished their turns
         * If so, dealer draws until reaching at least 17
         * Determines dealer outcome (stay or bust)
         * Triggers win evaluation for all players
         **/
        public void stay(Player p)
        {
            if (p.getStatus() == Player.STATUS.PLAYING)
            {
                p.setStatus(Player.STATUS.STAY);
            }
            boolean checkAllStay = true;

            // check if everyone has stayed
            for(Player pl : playerList.values())
            {
                if(!pl.getUsername().equalsIgnoreCase("dealer")
                        && pl.getStatus() == Player.STATUS.PLAYING)
                {
                    checkAllStay = false;
                    break;
                }
            }

            // If everyone has stayed then finish the round by checking who has won
            if(checkAllStay)
            {
                while(getHandTotal(dealer) < 17)
                {
                    hit(dealer);
                }
                if (getHandTotal(dealer) >= 17 && getHandTotal(dealer) <= 21)
                {
                    dealer.setStatus(Player.STATUS.STAY);
                }
                else if(getHandTotal(dealer) > 21)
                {
                    dealer.setStatus(Player.STATUS.LOST);
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

        /** Determines the outcome for a player
         * Handles bust, blackjack, dealer bust, and comparisons
         * Updates player status (WIN, LOST, DRAW)
         * Distributes or refunds bets accordingly
         **/
        public void checkWin(Player p)
        {
            if (p.getStatus() == Player.STATUS.WIN || p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.DRAW) {
                return;
            }

            int playerTotal = getHandTotal(p);
            int dealerTotal = getHandTotal(dealer);

            // Check if dealer is over 21
            if (p.getUsername().equalsIgnoreCase("dealer")) {
                if (playerTotal > 21) {
                    p.setStatus(Player.STATUS.LOST);
                }
                return;
            }

            // if p is player and busts
            if(playerTotal > 21)
            {
                p.setStatus(Player.STATUS.LOST);
                currentBets.remove(p);
                stay(p);
                return;
            }

            // if p is player and has 21
            if (playerTotal == 21 && p.getStatus() != Player.STATUS.STAY)
            {
                stay(p);
                return;
            }

            // if dealer is still playing (not won, lost, or stay)
            if (dealer.getStatus() != Player.STATUS.STAY && dealer.getStatus() != Player.STATUS.LOST) {
                return;
            }

            // Do now do end of round comparisons if the player is still deciding to hit/stay.
            if (p.getStatus() == Player.STATUS.PLAYING) {
                return;
            }

            // if p is the player and dealer has busted (previous check was if p was dealer)
            if (dealerTotal > 21) {
                p.setStatus(Player.STATUS.WIN);
                if(currentBets.containsKey(p))
                {
                    p.depositBalance(currentBets.get(p) * 2);
                    currentBets.remove(p);
                }
                return;
            }

            // end of round comparisons
            if (playerTotal > dealerTotal) {
                p.setStatus(Player.STATUS.WIN);
                if(currentBets.containsKey(p))
                {
                    p.depositBalance(currentBets.get(p) * 2);
                    currentBets.remove(p);
                }
                return;
            }
            else if (playerTotal < dealerTotal) {
                p.setStatus(Player.STATUS.LOST);
                currentBets.remove(p);
                return;
            }

            // if both players have won or have equal hands (remaining conditions), declare draw
            p.setStatus(Player.STATUS.DRAW);
            if(currentBets.containsKey(p))
            {
                p.depositBalance(currentBets.get(p));
                currentBets.remove(p);
            }
        }

    //    // check if a.java player has won
    //    public void checkWin(Player p)
    //    {
    //        if (p.getStatus() == Player.STATUS.WIN || p.getStatus() == Player.STATUS.LOST || p.getStatus() == Player.STATUS.DRAW) {
    //            return;
    //        }
    //
    //        int playerTotal = getHandTotal(p);
    //        int dealerTotal = getHandTotal(dealer);
    //
    //        // Check if dealer is over 21
    //        if (p.getUsername().equalsIgnoreCase("dealer")) {
    //            if (playerTotal > 21) {
    //                p.setStatus(Player.STATUS.LOST);
    //            }
    //            return;
    //        }
    //
    //        // if p is player and busts
    //        if(playerTotal > 21)
    //        {
    //            p.setStatus(Player.STATUS.LOST);
    //            currentBets.remove(p);
    //            stay(p);
    //            return;
    //        }
    //
    //        // if p is player and has 21
    //        if (playerTotal == 21 && p.getStatus() != Player.STATUS.STAY)
    //        {
    //            stay(p);
    //            return;
    //        }
    //
    //        // if dealer is still playing (not won, lost, or stay)
    //        if (dealer.getStatus() != Player.STATUS.STAY && dealer.getStatus() != Player.STATUS.LOST) {
    //            return;
    //        }
    //
    //        if (p.getStatus() == Player.STATUS.PLAYING) {
    //            return;
    //        }
    //
    //        // if p is the player and dealer has busted (previous check was if p was dealer)
    //        if (dealerTotal > 21) {
    //            p.setStatus(Player.STATUS.WIN);
    //            if(currentBets.containsKey(p))
    //            {
    //                p.depositBalance(currentBets.get(p) * 2);
    //                currentBets.remove(p);
    //            }
    //            return;
    //        }
    //
    //        // end of round comparisons
    //        if (playerTotal > dealerTotal) {
    //            p.setStatus(Player.STATUS.WIN);
    //            if(currentBets.containsKey(p))
    //            {
    //                p.depositBalance(currentBets.get(p) * 2);
    //                currentBets.remove(p);
    //            }
    //            return;
    //        }
    //        else if (playerTotal < dealerTotal) {
    //            p.setStatus(Player.STATUS.LOST);
    //            currentBets.remove(p);
    //            return;
    //        }
    //
    //        // if both players have won or have equal hands (remaining conditions), declare draw
    //        p.setStatus(Player.STATUS.DRAW);
    //        if(currentBets.containsKey(p))
    //        {
    //            p.depositBalance(currentBets.get(p));
    //            currentBets.remove(p);
    //        }
    //    }


        /** Sets the player’s bet for the next round
         * Ensures player has sufficient balance
         * Defaults to 0 if insufficient funds
         **/
        public void setNextBet(Player p, int bet) {

            if(p.getBalance() >= bet)
            {
                nextBets.put(p, bet);
            }
            else
            {
                nextBets.put(p, 0);
            }
        }

        /** Deducts the player's bet from their balance
         * Moves bet into current round bets
         * Defaults to 100 if no prior bet is set
         * Sets bet to 0 if player has insufficient balance
         **/
        public void placeRoundBet(Player p) {
            int bet = nextBets.getOrDefault(p, 100);
            if(p.getBalance() >= bet)
            {
                currentBets.put(p, bet);
                p.withdrawBalance(bet);
            }
            else
            {
                currentBets.put(p, 0);
            }
        }

    /** GAME STATUS AND GAME UPDATE METHODS--------------------------------------------------------------------------- **/

        /** Generates a formatted string representing current game state
         * Includes dealer and player hands, totals, and balances
         * Hides dealer’s first card if round is still active
         **/
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
        }

        /** Boolean method to Check whether all players have completed their turns
         * @return true if no players are still in PLAYING state
         **/
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

        /** Returns the result of the round for a player
         * Possible values: WIN, LOSE, DRAW, PLAYING
         **/
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

    /** CARD ENCODING AND DECODING MESSAGES --------------------------------------------------------------------------------------------------- **/

        /** Converts a player's hand into encoded string format
         * @return toString; comma-separated card representations
         **/
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

        /** Converts a single card into its string representation
         * Uses format: Face + Suit (e.g., AH, 10D, KS)
         * Maps face values (A, K, Q, J, 10–2)
         * Maps suits (H, S, D, C)
         * @param card The card being converted
         * @return face and suit of card
         **/
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
