package model.card;

import java.util.Comparator;

/** This class describes a Card object, it has a suit and a rank to describe a playing card. */
public class Card {

    /** The ranks of playing cards. */
    public enum Rank {
        /**
         * One, used only for check for the straight ACE - FIVE.
         */
        ONE(1),

        TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13),

        /**
         * Ace, used for determining high card.
         */
        ACE(14);

        /**
         * The number associated with the Rank, used for comparison.
         */
        private int rankNum;

        /**
         * Constructs a Rank enum value.
         *
         * @param rankNum the number associated with the enum value
         */
        Rank(int rankNum) {
            this.rankNum = rankNum;
        }

        /**
         * Returns the number associated with the enum value.
         *
         * @return the number associated with the enum value
         */
        public int getRankNum() {
            return this.rankNum;
        }
    }

    /** The suits of playing cards. */
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    /** The {@code Rank} of {@code this}. */
    private Rank rank;

    /** The {@code Suit} of {@code this}. */
    private Suit suit;

    /**
     * Card constructor accepts a Rank and a Suit and stores them in the rank
     * and suit instance variables when the card is created.
     *
     * @param rank rank to give
     * @param suit suit of (@code this}
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Rank getter method.
     *
     * @return Card's rank
     */
    public final Rank getRank() {
        return this.rank;
    }

    /**
     * Suit getter method.
     *
     * @return Card's suit
     */
    public final Suit getSuit() {
        return this.suit;
    }

    /** {@inheritDoc} */
    @Override
    public final boolean equals(Object obj) {
        try {
            if (!obj.getClass().isAssignableFrom(this.getClass())) {
                return false;
            }
            final Card otherCard = (Card) obj;
            return this.getRank().equals(otherCard.getRank()) && this.getSuit().equals(otherCard.getSuit());
        } catch (NullPointerException npe) {
            return false;
        }
    }

    @Override
    public final int hashCode() {
        final int OFF_SET = 100;
        return this.rank.getRankNum() + (this.suit.ordinal() * OFF_SET);
    }

    @Override
    public final String toString() {
        return "{" + this.rank + ", " + this.suit + "}";
    }
    
    /** Compares {@code c1} and {@code c2} solely by rank. */
    public static final class CardComparator implements Comparator<Card> {

        /** {@inheritDoc} */
        @Override
        public int compare(Card c1, Card c2) {
            return c1.getRank().getRankNum() - c2.getRank().getRankNum();
        }
    }
}
