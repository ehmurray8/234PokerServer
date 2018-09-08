package model.hand.analyzer;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import model.card.Card;
import model.card.Card.Rank;
import model.hand.representation.HandRank;

import static extensions.ListExtensions.reverseList;
import static model.hand.analyzer.AnalyzerHelpers.*;

/**
 * The HandAnalyzer is represented by the Hand's best rank, the best five card hand, the ranks of any pairs,
 * the full hand, and the ranks of the full house if the hand contains one.
 */
public abstract class HandAnalyzer {

    private static HandRank.HandRankComparator HAND_RANK_COMPARATOR = new HandRank.HandRankComparator();
    private static HandAnalyzerComparator HAND_ANALYZER_COMPARATOR = new HandAnalyzerComparator();

    private HandRank topRank;
    private SimpleAnalyzer topHandAnalyzer;
    private List<Rank> bestHandRanks;
    private List<Rank> pairRanks;
    List<Card> fullHand;
    private ArrayList<Rank> fullHouseRanks;
    private List<List<Card>> allHands;
    private Map<Rank, Integer> rankMap;


    HandAnalyzer(List<Card> fullHand) {
        this();
        this.fullHand = fullHand;
        rankMap = handToRankMap(fullHand);
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

    final List<Rank> getBestHandRanks() {
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
        pairRanks = topHandAnalyzer.getPairRanks();
        reverseList(pairRanks);
        fullHouseRanks = (ArrayList<Rank>) topHandAnalyzer.getFullHouseRanks();
    }

    private void findTopRank() {
        FiveCardAnalyzer analyzer = new FiveCardAnalyzer(allHands.get(0));
        topRank = analyzer.getRank();
        allHands.forEach(this::compareTopRank);
    }

    private void compareTopRank(List<Card> hand) {
        FiveCardAnalyzer fiveCardAnalyzer = new FiveCardAnalyzer(hand);
        HandRank rankCheck = fiveCardAnalyzer.getRank();
        if (HAND_RANK_COMPARATOR.compare(rankCheck, topRank) > 0) {
            topRank = rankCheck;
        }
    }

    private List<SimpleAnalyzer> createAnalyzersForTopRankHands() {
        List<SimpleAnalyzer> topRankAnalyzers = new ArrayList<>();
        allHands.forEach(hand -> addTopRankAnalyzers(hand, topRankAnalyzers));
        return topRankAnalyzers;
    }

    private void addTopRankAnalyzers(List<Card> hand, List<SimpleAnalyzer> topRankAnalyzers) {
        FiveCardAnalyzer fiveCardAnalyzer = new FiveCardAnalyzer(hand);
        HandRank rankCheck = fiveCardAnalyzer.getRank();
        if (HAND_RANK_COMPARATOR.compare(rankCheck, topRank) == 0) {
            SimpleAnalyzer analyzer = new SimpleAnalyzer(hand);
            topRankAnalyzers.add(analyzer);
        }
    }

    private void findTopHand(List<SimpleAnalyzer> topRankAnalyzers) {
        topHandAnalyzer = Collections.max(topRankAnalyzers, HAND_ANALYZER_COMPARATOR);
        bestHandRanks = topHandAnalyzer.getBestHandRanks();
    }

    final List<Rank> getNonPairRanks() {
        List<Rank> nonPairRanks = new ArrayList<>();
        bestHandRanks.forEach(rank -> {
            if(!pairRanks.contains(rank)) {
                nonPairRanks.add(rank);
            }
        });
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
        pairRanks = rankMap.entrySet().stream().filter(entry -> entry.getValue() > 1).map(Entry::getKey)
                .collect(Collectors.toList());
        reverseList(pairRanks);
    }

    final void findFullHouseRanks() {
        rankMap.forEach(this::addFullHouseRankKey);
        if(fullHouseRanks.size() != 2) {
            fullHouseRanks.clear();
        }
    }

    private void addFullHouseRankKey(Rank rank, int count) {
        if (count == TRIPS_FREQUENCY) {
            fullHouseRanks.add(rank);
        } else if (count == PAIR_FREQUENCY) {
            fullHouseRanks.add(rank);
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
