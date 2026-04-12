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
    public Card(Face face, Suit suit) {
        this.face = face;
        this.suit = suit;
    }

    /** METHODS (GETTERS) --------------------------------------------------------------------------------------------------- **/
    @Override
    public String toString() {
        return this.face + " of " + this.suit;
    }

    // Getters for face, suit and value of cards
    public Face getFace() {
        return this.face;
    }

    public Suit getSuit() {
        return this.suit;
    }

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
