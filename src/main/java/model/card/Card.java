package model.card;

import java.util.Comparator;

public class Card {

    private static final int HASHCODE_OFFSET = 100;

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
                return this.getRank().equals(otherCard.getRank()) && this.getSuit()
                    .equals(otherCard.getSuit());
            }
        } catch (NullPointerException e) {
            // TODO: Fix me
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return this.rank.getStrength() + (this.suit.ordinal() * HASHCODE_OFFSET);
    }

    @Override
    public final String toString() {
        return String.format("{%s, %s}", rank, suit);
    }

    public String toClientString() {
        String rank;
        String suit;
        rank = switch (this.rank) {
            case JACK -> "J";
            case QUEEN -> "Q";
            case KING -> "K";
            case ACE -> "A";
            default -> Integer.toString(this.rank.ordinal());
        };
        suit = switch (this.suit) {
            case SPADES -> "s";
            case DIAMONDS -> "d";
            case CLUBS -> "c";
            default -> "h";
        };
        return String.format("%s%s", rank, suit);
    }

    public static final class CardComparator implements Comparator<Card> {

        @Override
        public int compare(Card c1, Card c2) {
            return c1.getRank().getStrength() - c2.getRank().getStrength();
        }
    }
}
