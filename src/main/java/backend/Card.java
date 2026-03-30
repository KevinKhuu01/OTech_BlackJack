package backend;

public class Card {
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum Face { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    private final Face face;
    private final Suit suit;

    public Card(Face face, Suit suit) {
        this.face = face;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return this.face + " of " + this.suit;
    }

    // Getters can be added here if needed
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
                return 1; // placeholder since you're ignoring it
            default:
                throw new IllegalStateException("Unexpected value: " + face);
        }
    }
}
