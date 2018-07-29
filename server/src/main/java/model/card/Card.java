package model.card;

import java.util.Comparator;

public class Card {

    private Rank rank;
    private Suit suit;

    public enum Rank {

        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

        private int strength;

        Rank(int strength) {
            this.strength = strength;
        }

        public int getStrength() {
            return strength;
        }
    }

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public final Rank getRank() {
        return rank;
    }

    public final Suit getSuit() {
        return suit;
    }

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
        return this.rank.getStrength() + (this.suit.ordinal() * OFF_SET);
    }

    @Override
    public final String toString() {
        return "{" + rank + ", " + suit + "}";
    }
    
    public static final class CardComparator implements Comparator<Card> {

        @Override
        public int compare(Card c1, Card c2) {
            return c1.getRank().getStrength() - c2.getRank().getStrength();
        }
    }
}
