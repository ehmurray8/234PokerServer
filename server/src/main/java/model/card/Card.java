package model.card;

import java.util.Comparator;

public class Card {

    private final Rank rank;
    private final Suit suit;

    public enum Rank {

        ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(11), QUEEN(12), KING(13), ACE(14);

        private final int strength;

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

    public String toClientString() {
        String rank, suit;
        switch(this.rank) {
        case JACK:
            rank = "J";
            break;
        case QUEEN:
            rank = "Q";
            break;
        case KING:
            rank = "K";
            break;
        case ACE:
            rank = "A";
            break;
        default:
            rank = Integer.toString(this.rank.ordinal());
        }
        switch(this.suit) {
        case SPADES:
            suit = "s";
            break;
        case DIAMONDS:
            suit = "d";
            break;
        case CLUBS:
            suit = "c";
            break;
        default:
            suit = "h";
        }
        return String.format("%s%s", rank, suit);
    }
    
    public static final class CardComparator implements Comparator<Card> {

        @Override
        public int compare(Card c1, Card c2) {
            return c1.getRank().getStrength() - c2.getRank().getStrength();
        }
    }
}
