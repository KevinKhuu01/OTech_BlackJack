package backend;

/**
 * Card object - helper class
 */

public class Card {
    /** FIELDS --------------------------------------------------------------------------------------------------- **/
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum Face { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    private final Face face;
    private final Suit suit;

    /** CONSTRUCTOR --------------------------------------------------------------------------------------------------- **/
    /** Constructor for Card
     * @param face The value or 'face' of the card. ranges from TWO-TEN and JACK,QUEEN,KING,ACE.
     * @param suit The type of the card. Can be one of either CLUBS, DIAMONDS, HEARTS or SPADES.
     **/
    public Card(Face face, Suit suit) {
        this.face = face;
        this.suit = suit;
    }

    /** METHODS (GETTERS) --------------------------------------------------------------------------------------------------- **/

    /** toString method to show the cards face and suit
     *@return 'face' of 'suit' **/
    @Override
    public String toString() {
        return this.face + " of " + this.suit;
    }

    /** Getter for face of a card
     * @return face of card
     **/
    public Face getFace() {
        return this.face;
    }

    /** Getter for suit of a card
     * @return suit of card
     **/
    public Suit getSuit() {
        return this.suit;
    }

    /** Getter for value of a card
     * @return value of card
     **/
    public int getValue()
    {
        switch (face) {
            case TWO:
                return 2;
            case THREE:
                return 3;
            case FOUR:
                return 4;
            case FIVE:
                return 5;
            case SIX:
                return 6;
            case SEVEN:
                return 7;
            case EIGHT:
                return 8;
            case NINE:
                return 9;
            case TEN:
            case JACK:
            case QUEEN:
            case KING:
                return 10;
            case ACE:
                return 1;
            default:
                throw new IllegalStateException("Unexpected value: " + face);
        }
    }
}
