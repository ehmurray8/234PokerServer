package model.hand.analyzer;

import model.card.Card;
import model.hand.representation.HandRank;

import java.util.Comparator;
import java.util.List;

import static extensions.ListExtensions.reverseList;

public final class HandAnalyzerComparator implements Comparator<HandAnalyzer> {
    
    private List<Card.Rank> sortedHand;
    private List<Card.Rank> otherSortedHand;
    private HandAnalyzer handAnalyzer;
    private HandAnalyzer otherHandAnalyzer;

    @Override
    public int compare(HandAnalyzer ha1, HandAnalyzer ha2) {
        handAnalyzer = ha1;
        otherHandAnalyzer = ha2;
        int value = handAnalyzer.getTopRank().getStrength() - otherHandAnalyzer.getTopRank().getStrength();
        if (value == 0 && !handAnalyzer.getTopRank().equals(HandRank.ROYAL_FLUSH)) {
            initSortedHands();
            HandRank rank = handAnalyzer.getTopRank();
            return compareEqualRanks(rank);
        }
        return value;
    }

    private void initSortedHands() {
        sortedHand = handAnalyzer.getBestHandRanks();
        otherSortedHand = otherHandAnalyzer.getBestHandRanks();
        reverseList(sortedHand);
        reverseList(otherSortedHand);
    }

    private int compareEqualRanks(HandRank rank) {
        switch (rank) {
            case HIGH_CARD:
            case FLUSH:
                return compareCardsDescending();
            case STRAIGHT:
            case STRAIGHT_FLUSH:
                return compareStraight();
            case PAIR:
            case TWO_PAIR:
            case THREE_OF_A_KIND:
            case FOUR_OF_A_KIND:
                return comparePairs();
            case FULL_HOUSE:
                return compareFullHouse();
            default:
                return 0;
        }
    }

    private int compareStraight() {
        boolean hasWheelStraight = checkForWheelStraight(sortedHand);
        boolean otherHasWheelStraight = checkForWheelStraight(otherSortedHand);

        if (hasWheelStraight && !otherHasWheelStraight) {
            return -1;
        } else if (otherHasWheelStraight && !hasWheelStraight) {
            return 1;
        }
        return compareCardsDescending();
    }

    private boolean checkForWheelStraight(List<Card.Rank> hand) {
        return hand.contains(Card.Rank.ACE) && hand.contains(Card.Rank.TWO);
    }

    private int compareCardsDescending() {
        for (int i = 0; i < sortedHand.size(); i++) {
            if (!sortedHand.get(i).equals(otherSortedHand.get(i))) {
                return sortedHand.get(i).getStrength() - otherSortedHand.get(i).getStrength();
            }
        }
        return 0;
    }

    private int comparePairs() {
        int comparison = compareCardsDescending();
        if(comparison != 0) {
            return comparison;
        }
        List<Card.Rank> nonPairRanks1 = handAnalyzer.getNonPairRanks();
        List<Card.Rank> nonPairRanks2 = otherHandAnalyzer.getNonPairRanks();
        for (int i = 0; i < nonPairRanks1.size(); i++) {
            if (!nonPairRanks1.get(i).equals(nonPairRanks2.get(i))) {
                return nonPairRanks1.get(i).getStrength() - nonPairRanks2.get(i).getStrength();
            }
        }
        return 0;
    }

    private int compareFullHouse() {
        for (int i = 0; i < handAnalyzer.getFullHouseRanks().size(); i++) {
            if (!handAnalyzer.getFullHouseRanks().get(i).equals(otherHandAnalyzer.getFullHouseRanks().get(i))) {
                return handAnalyzer.getFullHouseRanks().get(i).getStrength()
                        - otherHandAnalyzer.getFullHouseRanks().get(i).getStrength();
            }
        }
        return 0;
    }
}
