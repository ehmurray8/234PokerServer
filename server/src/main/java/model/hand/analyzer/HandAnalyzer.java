package model.hand.analyzer;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import model.card.Card;
import model.card.Card.Rank;
import model.hand.representation.HandRank;

import static model.hand.analyzer.AnalyzerHelpers.*;

/**
 * The HandAnalyzer is represented by the Hand's best rank, the best five model.card model.hand, the ranks of any pairs,
 * the full model.hand, and the ranks of the full house if the model.hand contains one.
 */
public abstract class HandAnalyzer {

    public static final HandAnalyzerComparator HAND_ANALYZER_COMPARATOR = new HandAnalyzerComparator();
    private static final HandRank.HandRankComparator HAND_RANK_COMPARATOR = new HandRank.HandRankComparator();

    private HandRank topRank;
    private SimpleAnalyzer topHandAnalyzer;
    private List<Rank> bestHandRanks;
    private List<Rank> pairRanks;
    List<Card> fullHand;
    private ArrayList<Rank> fullHouseRanks;
    private List<List<Card>> allHands;
    private Map<Rank, Integer> rankMap;
    private List<Card> bestHand;


    HandAnalyzer(List<Card> fullHand) {
        topRank = HandRank.HIGH_CARD;
        bestHandRanks = new ArrayList<>();
        pairRanks = new ArrayList<>();
        fullHouseRanks = new ArrayList<>();
        bestHand = new ArrayList<>();
        this.fullHand = fullHand;
        rankMap = handToRankMap(fullHand);
        analyze();
    }

    public final HandRank getTopRank() {
        return topRank;
    }

    final List<Rank> getBestHandRanks() {
        return bestHandRanks;
    }

    public List<Card> getBestHand() {
        return bestHand;
    }

    @Override
    public final String toString() {
        return String.format("%s Best: %s %s", fullHand, bestHandRanks, topRank);
    }

    protected abstract List<List<Card>> fiveCardCombinations(List<Card> fullHand);

    void analyze() {
        allHands = new ArrayList<>(this.fiveCardCombinations(this.fullHand));
        findTopRank();
        List<SimpleAnalyzer> topRankAnalyzers = createAnalyzersForTopRankHands();
        findTopHand(topRankAnalyzers);
        pairRanks = topHandAnalyzer.getPairRanks();
        pairRanks.sort(Collections.reverseOrder());
        fullHouseRanks = (ArrayList<Rank>) topHandAnalyzer.getFullHouseRanks();
    }

    private void findTopRank() {
        FiveCardAnalyzer analyzer;
        analyzer = new FiveCardAnalyzer(allHands.get(0));
        topRank = analyzer.getRank();
        allHands.forEach(this::compareTopRank);
    }

    private void compareTopRank(List<Card> hand) {
        var rankCheck = getHandRank(hand);
        if (HAND_RANK_COMPARATOR.compare(rankCheck, topRank) > 0) {
            topRank = rankCheck;
        }
    }

    private HandRank getHandRank(List<Card> hand) {
        return new FiveCardAnalyzer(hand).getRank();
    }

    private List<SimpleAnalyzer> createAnalyzersForTopRankHands() {
        List<SimpleAnalyzer> topRankAnalyzers = new ArrayList<>();
        allHands.forEach(hand -> addTopRankAnalyzers(hand, topRankAnalyzers));
        return topRankAnalyzers;
    }

    private void addTopRankAnalyzers(List<Card> hand, List<SimpleAnalyzer> topRankAnalyzers) {
        var rankCheck = getHandRank(hand);
        if (HAND_RANK_COMPARATOR.compare(rankCheck, topRank) == 0) {
            SimpleAnalyzer analyzer = new SimpleAnalyzer(hand);
            topRankAnalyzers.add(analyzer);
        }
    }

    private void findTopHand(List<SimpleAnalyzer> topRankAnalyzers) {
        topHandAnalyzer = Collections.max(topRankAnalyzers, HAND_ANALYZER_COMPARATOR);
        bestHandRanks = topHandAnalyzer.getBestHandRanks();
        bestHand = topHandAnalyzer.getFullHand();
    }

    final List<Rank> getNonPairRanks() {
        return bestHandRanks.stream().filter(rank -> !pairRanks.contains(rank)).sorted(Collections.reverseOrder()).collect(Collectors.toList());
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
        pairRanks.sort(Collections.reverseOrder());
    }

    final void findFullHouseRanks() {
        rankMap.forEach(this::addFullHouseRankKey);
        if(fullHouseRanks.size() != 2) {
            fullHouseRanks.clear();
        }
    }

    private void addFullHouseRankKey(Rank rank, int count) {
        if (count == TRIPS_FREQUENCY) {
            fullHouseRanks.add(0, rank);
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
