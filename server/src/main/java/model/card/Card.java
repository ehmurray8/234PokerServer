package model.card;

import java.util.Comparator;

public class Card {

    private final Rank rank;
    private final Suit suit;

    public enum Rank {

        ONE(1, "ace"), TWO(2, "2"), THREE(3, "3"),
        FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"),
        SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"),
        TEN(10, "10"), JACK(11, "jack"), QUEEN(12, "queen"),
        KING(13, "king"), ACE(14, "ace");

        private final int strength;
        private final String clientName;

        Rank(int strength, String clientName) {
            this.strength = strength;
            this.clientName = clientName;
        }

        public String getClientName() {
            return this.clientName;
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

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public final boolean equals(Object obj) {
        try {
            if (obj.getClass().isAssignableFrom(this.getClass())) {
                final Card otherCard = (Card) obj;
                return this.getRank().equals(otherCard.getRank()) && this.getSuit().equals(otherCard.getSuit());
            }
        } catch (NullPointerException ignored) { }
        return false;
    }

    @Override
    public final int hashCode() {
        final int OFF_SET = 100;
        return this.rank.getStrength() + (this.suit.ordinal() * OFF_SET);
    }

    @Override
    public final String toString() {
        return String.format("{%s, %s}", rank, suit);
    }
    
    public static final class CardComparator implements Comparator<Card> {

        @Override
        public int compare(Card c1, Card c2) {
            return c1.getRank().getStrength() - c2.getRank().getStrength();
        }
    }
}
