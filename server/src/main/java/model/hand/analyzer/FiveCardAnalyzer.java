package model.hand.analyzer;

import model.card.Card;
import model.hand.representation.HandRank;

import java.util.*;

import static model.hand.analyzer.AnalyzerHelpers.*;

class FiveCardAnalyzer  {

    private HandRank rank;
    private final Map<Card.Rank, Integer> rankMap;
    private final Map<Card.Suit, Integer> suitMap;
    private final List<Card> hand;
    private final ArrayList<Card.Rank> pairRanks;
    private boolean hasStraight, has3Kind = false, has4Kind = false, hasFlush = false, hasBroadway = false;

    public HandRank getRank() {
        return rank;
    }

    FiveCardAnalyzer(List<Card> hand) {
        this.hand = hand;
        rank = HandRank.HIGH_CARD;
        rankMap = handToRankMap(hand);
        suitMap =  handToSuitMap(hand);
        pairRanks = new ArrayList<>();
        analyze();
    }

    private void analyze() {
        checkPairs();
        checkFullHouse();
        checkFlush();
        if (rankMap.size() == STRAIGHT_LENGTH) {
            checkStraight();
        }
    }

    private void checkPairs() {
        rankMap.entrySet().stream().filter(entry -> entry.getValue() > 1).forEach(this::checkTripsAndQuads);
        if (!has3Kind && !has4Kind && pairRanks.size() > 0) {
            checkPairAndTwoPair();
        }
    }

    private void checkTripsAndQuads(Map.Entry<Card.Rank, Integer> rankIntegerEntry) {
        pairRanks.add(rankIntegerEntry.getKey());
        if (rankIntegerEntry.getValue().equals(TRIPS_FREQUENCY)) {
            setTrips();
        } else if (rankIntegerEntry.getValue().equals(QUADS_FREQUENCY)) {
            setQuads();
        }
    }

    private void setTrips() {
        has3Kind = true;
        rank = HandRank.THREE_OF_A_KIND;
    }

    private void setQuads() {
        has4Kind = true;
        rank = HandRank.FOUR_OF_A_KIND;
    }

    private void checkPairAndTwoPair() {
        rank = pairRanks.size() == 1 ? HandRank.PAIR : HandRank.TWO_PAIR;
    }

    private void checkFlush() {
        if (suitMap.size() == 1 && !has4Kind) {
            hasFlush = true;
            rank = HandRank.FLUSH;
        }
    }

    private void checkFullHouse() {
        if (has3Kind && pairRanks.size() == 2) {
            rank = HandRank.FULL_HOUSE;
        }
    }

    private void checkStraight() {
        addRankOne();
        ArrayList<Card.Rank> rankList = createSortedRankList();
        checkCardsAreSequential(rankList);
        if (rankMap.size() == BROADWAY_LENGTH) {
            handleAceStraight(rankList);
        } else {
            rankMap.remove(Card.Rank.ONE);
        }
        setStraightRank();
    }

    private void addRankOne() {
        if (rankMap.containsKey(Card.Rank.ACE)) {
            rankMap.put(Card.Rank.ONE, 1);
        }
    }

    private void checkCardsAreSequential(List<Card.Rank> rankList) {
        hasStraight = true;
        for (int i = 0; i < hand.size() - 1 && hasStraight; i++) {
            if (rankList.get(i + 1).getStrength() - rankList.get(i).getStrength() != 1) {
                hasStraight = false;
            }
        }
    }

    private void handleAceStraight(List<Card.Rank> rankList) {
        if (!hasStraight) {
            checkBroadwayStraight(rankList);
        } else {
            rankList.remove(Card.Rank.ACE);
        }
    }

    private void checkBroadwayStraight(List<Card.Rank> rankList) {
        hasBroadway = true;
        for (int i = 1; i < rankMap.size() - 1 && hasBroadway; i++) {
            if (rankList.get(i + 1).getStrength() - rankList.get(i).getStrength() != 1) {
                hasBroadway = false;
            }
        }
        rankList.remove(Card.Rank.ONE);
    }

    private void setStraightRank() {
        if(hasStraight || hasBroadway) {
            rank = HandRank.STRAIGHT;
            if(hasFlush) {
                rank = hasBroadway ? HandRank.ROYAL_FLUSH : HandRank.STRAIGHT_FLUSH;
            }
        }
    }

    private ArrayList<Card.Rank> createSortedRankList() {
        ArrayList<Card.Rank> rankList = new ArrayList<>(Arrays.asList(Arrays.copyOf(rankMap.keySet().toArray(),
                    rankMap.keySet().toArray().length, Card.Rank[].class)));
        Collections.sort(rankList);
        return rankList;
    }
}
