package model.hand.representation;

import java.util.Comparator;

public enum HandRank {

    HIGH_CARD(1, "High Card"), PAIR(2, "Pair"), TWO_PAIR(3, "Two Pair"), THREE_OF_A_KIND(4, "Three of a Kind"),
    STRAIGHT(5, "Straight"), FLUSH(6, "Flush"), FULL_HOUSE(7, "Full House"), FOUR_OF_A_KIND(8, "Four of a Kind"),
    STRAIGHT_FLUSH(9, "Straight Flush"), ROYAL_FLUSH(10, "Royal Flush");

    private int strength;
    private String string;

    HandRank(int strength, String string) {
        this.strength = strength;
        this.string = string;
    }

    public int getStrength() {
        return strength;
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
