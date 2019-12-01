package model.hand.analyzer;

import model.card.Card;
import model.hand.representation.HandRank;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class HandAnalyzerComparator implements Comparator<HandAnalyzer> {
    
    private List<Card.Rank> sortedHand;
    private List<Card.Rank> otherSortedHand;
    private HandAnalyzer handAnalyzer;
    private HandAnalyzer otherHandAnalyzer;

    @Override
    public int compare(HandAnalyzer ha1, HandAnalyzer ha2) {
        handAnalyzer = ha1;
        otherHandAnalyzer = ha2;
        int value = handAnalyzer.getTopRank().getStrength() - otherHandAnalyzer.getTopRank().getStrength();
        return compareStrengths(value);
    }

    private int compareStrengths(int value) {
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
        sortedHand.sort(Collections.reverseOrder());
        otherSortedHand.sort(Collections.reverseOrder());
    }

    private int compareEqualRanks(HandRank rank) {
        switch (rank) {
            case HIGH_CARD:
            case FLUSH:
                return compareCardsDescending(sortedHand, otherSortedHand);
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
        return compareCardsDescending(sortedHand, otherSortedHand);
    }

    private boolean checkForWheelStraight(List<Card.Rank> hand) {
        return hand.contains(Card.Rank.ACE) && hand.contains(Card.Rank.TWO);
    }

    private int comparePairs() {
        var pairRanks = handAnalyzer.getPairRanks();
        pairRanks.sort(Collections.reverseOrder());
        var otherPairRanks = otherHandAnalyzer.getPairRanks();
        otherPairRanks.sort(Collections.reverseOrder());

        int comparison = compareCardsDescending(pairRanks, otherPairRanks);
        if (comparison != 0) {
            return comparison;
        }

        comparison = compareCardsDescending(sortedHand, otherSortedHand);
        if(comparison != 0) {
            return comparison;
        }
        List<Card.Rank> nonPairRanks = handAnalyzer.getNonPairRanks();
        List<Card.Rank> otherNonPairRanks = otherHandAnalyzer.getNonPairRanks();
        return compareCardsDescending(nonPairRanks, otherNonPairRanks);
    }

    private int compareFullHouse() {
        List<Card.Rank> fullHouseRanks = handAnalyzer.getFullHouseRanks();
        List<Card.Rank> otherFullHouseRanks = otherHandAnalyzer.getFullHouseRanks();
        return compareCardsDescending(fullHouseRanks, otherFullHouseRanks);
    }

    private int compareCardsDescending(List<Card.Rank> first, List<Card.Rank> second) {
        for (int i = 0; i < first.size(); i++) {
            if (!first.get(i).equals(second.get(i))) {
                return first.get(i).getStrength() - second.get(i).getStrength();
            }
        }
        return 0;
    }
}
