package model.hand.analyzer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;
import model.hand.representation.HandRank;

import static extensions.ListExtensions.reverseList;
import static model.hand.analyzer.AnalyzerHelpers.*;

/**
 * The HandAnalyzer is represented by the Hand's best rank, the best five card hand, the ranks of any pairs,
 * the full hand, and the ranks of the full house if the hand contains one.
 */
public abstract class HandAnalyzer {

    private HandRank topRank;
    private SimpleAnalyzer topHandAnalyzer;
    private List<Rank> bestHandRanks;
    private ArrayList<Rank> pairRanks;
    protected List<Card> fullHand;
    private ArrayList<Rank> fullHouseRanks;
    private List<List<Card>> allHands;


    public HandAnalyzer(List<Card> fullHand) {
        this();
        this.fullHand = fullHand;
        analyze();
    }

    private HandAnalyzer() {
        topRank = HandRank.HIGH_CARD;
        bestHandRanks = new ArrayList<>();
        pairRanks = new ArrayList<>();
        fullHouseRanks = new ArrayList<>();
    }

    public final HandRank getTopRank() {
        return topRank;
    }

    public final List<Rank> getBestHandRanks() {
        return bestHandRanks;
    }

    @Override
    public final String toString() {
        return String.format("%s Best: %s %s", fullHand, bestHandRanks, topRank);
    }

    public abstract List<List<Card>> fiveCardCombinations(List<Card> fullHand);

    public void analyze() {
        allHands = new ArrayList<>(this.fiveCardCombinations(this.fullHand));
        findTopRank();
        List<SimpleAnalyzer> topRankAnalyzers = createAnalyzersForTopRankHands();
        findTopHand(topRankAnalyzers);
        pairRanks = (ArrayList<Rank>) topHandAnalyzer.getPairRanks();
        reverseList(pairRanks);
        fullHouseRanks = (ArrayList<Rank>) topHandAnalyzer.getFullHouseRanks();
    }

    private void findTopRank() {
        FiveCardAnalyzer analyzer = new FiveCardAnalyzer(allHands.get(0));
        topRank = analyzer.getRank();
        for (int i = 1; i < allHands.size(); i++) {
            HandRank.HandRankComparator hRC = new HandRank.HandRankComparator();
            FiveCardAnalyzer fiveCardAnalyzer = new FiveCardAnalyzer(allHands.get(i));
            HandRank rankCheck = fiveCardAnalyzer.getRank();
            if (hRC.compare(rankCheck, topRank) > 0) {
                topRank = rankCheck;
            }
        }
    }

    private List<SimpleAnalyzer> createAnalyzersForTopRankHands() {
        List<SimpleAnalyzer> topRankAnalyzers = new ArrayList<>();
        HandRank.HandRankComparator handRankComparator = new HandRank.HandRankComparator();
        for (List<Card> hand : allHands) {
            FiveCardAnalyzer fiveCardAnalyzer = new FiveCardAnalyzer(hand);
            HandRank rankCheck = fiveCardAnalyzer.getRank();
            if (handRankComparator.compare(rankCheck, topRank) == 0) {
                SimpleAnalyzer analyzer = new SimpleAnalyzer(hand);
                topRankAnalyzers.add(analyzer);
            }
        }
        return topRankAnalyzers;
    }

    private void findTopHand(List<SimpleAnalyzer> topRankAnalyzers) {
        topHandAnalyzer = topRankAnalyzers.get(0);
        HandAnalyzerComparator handAnalyzerComparator = new HandAnalyzerComparator();
        for (int i = 1; i < topRankAnalyzers.size(); i++) {
            if (handAnalyzerComparator.compare(topRankAnalyzers.get(i), topHandAnalyzer) > 0) {
                topHandAnalyzer = topRankAnalyzers.get(i);
            }
        }
        bestHandRanks = topHandAnalyzer.getBestHandRanks();
    }

    final List<Rank> getNonPairRanks() {
        List<Rank> nonPairRanks = new ArrayList<>();
        for (Rank aBestHand : this.bestHandRanks) {
            for (Rank pairRank : this.pairRanks) {
                if (!aBestHand.equals(pairRank)) {
                    nonPairRanks.add(aBestHand);
                }
            }
        }
        reverseList(nonPairRanks);
        return nonPairRanks;
    }

    final List<Card> getFullHand() {
        return fullHand;
    }

    final void setTopRank(HandRank rank) {
        topRank = rank;
    }

    final void findPairRanks() {
        EnumMap<Rank, Integer> rankMap = new EnumMap<>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<>(Suit.class);
        handToEnumMaps(fullHand, rankMap, suitMap);

        for (Entry<Rank, Integer> entry : rankMap.entrySet()) {
            if (entry.getValue() > 1) {
                pairRanks.add(entry.getKey());
            }
        }
        reverseList(pairRanks);
    }

    final void findFullHouseRanks() {
        EnumMap<Rank, Integer> rankMap = new EnumMap<>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<>(Suit.class);
        handToEnumMaps(fullHand, rankMap, suitMap);

        Iterator<Entry<Rank, Integer>> entriesRank = rankMap.entrySet().iterator();
        Rank threeKindRank = null, pairRank = null;
        while (entriesRank.hasNext()) {
            Entry<Rank, Integer> entry = entriesRank.next();
            if (entry.getValue() == TRIPS_FREQUENCY) {
                threeKindRank = entry.getKey();
            } else if (entry.getValue() == PAIR_FREQUENCY) {
                pairRank = entry.getKey();
            }
        }
        if (threeKindRank != null && pairRank != null) {
            fullHouseRanks.add(threeKindRank);
            fullHouseRanks.add(pairRank);
        }
    }

    final void findBestHand() {
        fullHand.forEach(card -> bestHandRanks.add(card.getRank()));
    }

    final List<Rank> getPairRanks() {
        return pairRanks;
    }

    final List<Rank> getFullHouseRanks() {
        return fullHouseRanks;
    }
}
