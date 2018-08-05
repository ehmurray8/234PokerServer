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
    private HandRank rank;

    @Override
    public int compare(HandAnalyzer ha1, HandAnalyzer ha2) {
        handAnalyzer = ha1;
        otherHandAnalyzer = ha2;
        int value = handAnalyzer.getTopRank().getStrength() - otherHandAnalyzer.getTopRank().getStrength();
        if (value == 0 && !handAnalyzer.getTopRank().equals(HandRank.ROYAL_FLUSH)) {
            initSortedHands();
            rank = handAnalyzer.getTopRank();
            if (rank.equals(HandRank.HIGH_CARD) || rank.equals(HandRank.FLUSH)) {
                return compareHighCardOrFlush();
            } else if (rank.equals(HandRank.STRAIGHT) || rank.equals(HandRank.STRAIGHT_FLUSH)) {
                return compareStraight();
            } else if (rank.equals(HandRank.PAIR) || rank.equals(HandRank.TWO_PAIR)
                    || rank.equals(HandRank.THREE_OF_A_KIND) || rank.equals(HandRank.FOUR_OF_A_KIND)) {
                return comparePairs();
            } else if (rank.equals(HandRank.FULL_HOUSE)) {
                return compareFullHouse();
            }
        }
        return value;
    }

    private void initSortedHands() {
        sortedHand = handAnalyzer.getBestHandRanks();
        otherSortedHand = otherHandAnalyzer.getBestHandRanks();
        reverseList(sortedHand);
        reverseList(otherSortedHand);
    }

    private int compareHighCardOrFlush() {
        for (int i = 0; i < sortedHand.size(); i++) {
            if (!sortedHand.get(i).equals(otherSortedHand.get(i))) {
                return sortedHand.get(i).getStrength() - otherSortedHand.get(i).getStrength();
            }
        }
        return 0;
    }

    private int compareStraight() {
        boolean hasLowAceStraight1 = false, hasLowAceStraight2 = false;
        if (sortedHand.contains(Card.Rank.ACE) && sortedHand.contains(Card.Rank.TWO)) {
            hasLowAceStraight1 = true;
        }
        if (otherSortedHand.contains(Card.Rank.ACE) && otherSortedHand.contains(Card.Rank.TWO)) {
            hasLowAceStraight2 = true;
        }
        if (hasLowAceStraight1 && !hasLowAceStraight2) {
            return -1;
        } else if (hasLowAceStraight2 && !hasLowAceStraight1) {
            return 1;
        } else {
            for (int i = 0; i < sortedHand.size(); i++) {
                if (!sortedHand.get(i).equals(otherSortedHand.get(i))) {
                    return sortedHand.get(i).getStrength() - otherSortedHand.get(i).getStrength();
                }
            }
        }
        return 0;
    }

    private int comparePairs() {
        for (int i = 0; i < handAnalyzer.getPairRanks().size(); i++) {
            if (!handAnalyzer.getPairRanks().get(i).equals(otherHandAnalyzer.getPairRanks().get(i))) {
                return handAnalyzer.getPairRanks().get(i).getStrength() - otherHandAnalyzer.getPairRanks().get(i).getStrength();
            }
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
