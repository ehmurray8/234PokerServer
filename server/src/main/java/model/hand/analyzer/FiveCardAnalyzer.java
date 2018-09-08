package model.hand.analyzer;

import model.card.Card;
import model.hand.representation.HandRank;

import java.util.*;

import static model.hand.analyzer.AnalyzerHelpers.*;

public class FiveCardAnalyzer  {

    private HandRank rank;
    private Map<Card.Rank, Integer> rankMap;
    private Map<Card.Suit, Integer> suitMap;
    private List<Card> hand;
    private ArrayList<Card.Rank> pairRanks;
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

        checkTripsAndQuads();
        checkPair();
        checkFlush();
        checkFullHouse();
        checkStraight();
    }

    private void checkTripsAndQuads() {
        for (Map.Entry<Card.Rank, Integer> entry : rankMap.entrySet()) {
            if (entry.getValue() > 1) {
                pairRanks.add(entry.getKey());
                if (entry.getValue().equals(TRIPS_FREQUENCY)) {
                    has3Kind = true;
                    rank = HandRank.THREE_OF_A_KIND;
                } else if (entry.getValue().equals(QUADS_FREQUENCY)) {
                    has4Kind = true;
                    rank = HandRank.FOUR_OF_A_KIND;
                }
            }
        }
    }

    private void checkPair() {
        if (!has3Kind && !has4Kind && pairRanks.size() > 0) {
            if (pairRanks.size() == 1) {
                rank = HandRank.PAIR;
            } else {
                rank = HandRank.TWO_PAIR;
            }
        }
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
        if (rankMap.size() == STRAIGHT_LENGTH) {
            if (rankMap.containsKey(Card.Rank.ACE)) {
                rankMap.put(Card.Rank.ONE, 1);
            }
            ArrayList<Card.Rank> rankList = createSortedRankList();

            hasStraight = true;
            for (int i = 0; i < hand.size() - 1 && hasStraight; i++) {
                if (rankList.get(i + 1).getStrength() - rankList.get(i).getStrength() != 1) {
                    hasStraight = false;
                }
            }
            checkBroadwayStraight(rankList);
            setStraightRank();
        }
    }

    private void checkBroadwayStraight(List<Card.Rank> rankList) {
        if (rankMap.size() == BROADWAY_LENGTH) {
            if (!hasStraight) {
                hasBroadway = true;
                for (int i = 1; i < rankMap.size() - 1 && hasBroadway; i++) {
                    if (rankList.get(i + 1).getStrength() - rankList.get(i).getStrength() != 1) {
                        hasBroadway = false;
                    }
                }
                rankList.remove(Card.Rank.ONE);
            } else {
                rankList.remove(Card.Rank.ACE);
            }
        }
    }

    private void setStraightRank() {
        if ((hasStraight || hasBroadway) && hasFlush) {
            if (hasBroadway) {
                rank = HandRank.ROYAL_FLUSH;
            } else {
                rank = HandRank.STRAIGHT_FLUSH;
            }
        } else if (hasStraight || hasBroadway) {
            rank = HandRank.STRAIGHT;
        }
    }


    private ArrayList<Card.Rank> createSortedRankList() {
        ArrayList<Card.Rank> rankList = new ArrayList<>(Arrays.asList(Arrays.copyOf(rankMap.keySet().toArray(),
                    rankMap.keySet().toArray().length, Card.Rank[].class)));
        Collections.sort(rankList);
        return rankList;
    }
}
