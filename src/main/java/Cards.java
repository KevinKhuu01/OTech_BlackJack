public class Cards {
    public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES }
    public enum Rank { TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE }

    private final Rank rank;
    private final Suit suit;

    public Cards(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return this.rank + " of " + this.suit;
    }

    // Getters can be added here if needed
    public Rank getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }
}
