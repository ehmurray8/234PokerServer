package model.hand.representation;

import java.util.Comparator;

public enum HandRank {

    HIGH_CARD(1, "High Card", 1), PAIR(2, "Pair", 2), TWO_PAIR(3, "Two Pair", 3), THREE_OF_A_KIND(4, "Three of a Kind", 5),
    STRAIGHT(5, "Straight", 4), FLUSH(6, "Flush", 7), FULL_HOUSE(7, "Full House", 6), FOUR_OF_A_KIND(8, "Four of a Kind", 8),
    STRAIGHT_FLUSH(9, "Straight Flush", 9), ROYAL_FLUSH(10, "Royal Flush", 10);

    private final int strength;
    private final String string;
    private final int shortDeckStrength;

    HandRank(int strength, String string, int shortDeckStrength) {
        this.strength = strength;
        this.string = string;
        this.shortDeckStrength = shortDeckStrength;
    }

    public int getStrength() {
        return strength;
    }

    public int getShortDeckStrength() {
        return shortDeckStrength;
    }

    @Override
    public String toString() {
        return string;
    }

    public static final class HandRankComparator implements Comparator<HandRank> {

        @Override
        public int compare(HandRank hr1, HandRank hr2) {
            return hr1.getStrength() - hr2.getStrength();
        }
    }
}
