package model.hand.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;

/**
 * Abstract representation of a HandAnalyzer object, used to analyze hands to find the {@code hand} with
 * the best possible {@code HandRank}.
 *
 * <p>
 * The HandAnalyzer is represented by the Hand's best rank, the best five card hand, the ranks of any pairs,
 * the full hand, and the ranks of the full house if the hand contains one.
 * </p>
 */
public abstract class HandAnalyzer implements HandAnalyzerInterface {

    private static final int PAIR = 2, TRIPS = 3, QUADS = 4, STRAIGHT = 5, BROADWAY = 6;

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

    private HandRank topRank;
    private List<Rank> bestHandRanks;
    private ArrayList<Rank> pairRanks;
    private List<Card> fullHand;
    private ArrayList<Rank> fullHouseRanks;


    public HandAnalyzer(List<Card> fullHand) {
        this();
        this.fullHand = fullHand;
    }

    private HandAnalyzer() {
        topRank = HandRank.HIGH_CARD;
        bestHandRanks = new ArrayList<>();
        pairRanks = new ArrayList<>();
        fullHouseRanks = new ArrayList<>();
    }

    @Override
    public final HandRank getTopRank() {
        return topRank;
    }

    @Override
    public final List<Rank> getBestHandRanks() {
        return bestHandRanks;
    }

    @Override
    public final String toString() {
        return fullHand + " Best: " + bestHandRanks + " " + topRank;
    }

    public abstract List<List<Card>> fiveCardCombinations(List<Card> fullHand);

    /**
     * {@inheritDoc}
     *
     * THIS METHOD IS REQUIRED TO BE CALLED BEFORE ACCESSING ANY DATA FROM {@code this}.
     */
    @Override
    public void analyze() {
        List<List<Card>> allHands = new ArrayList<>(this.fiveCardCombinations(this.fullHand));

        this.topRank = this.analyzeFiveCardHand(allHands.get(0));

        //Determine topRank
        for (int i = 1; i < allHands.size(); i++) {
            HandRank.HandRankComparator hRC = new HandRank.HandRankComparator();
            HandRank rankCheck = this.analyzeFiveCardHand(allHands.get(i));
            if (hRC.compare(rankCheck, this.topRank) > 0) {
                this.topRank = rankCheck;
            }
        }

        //Create SimpleAnalyzer's of the hands of topRank
        List<SimpleAnalyzer> topRankAnalyzers = new ArrayList<>();
        for (List<Card> allHand : allHands) {
            HandRank.HandRankComparator hRC = new HandRank.HandRankComparator();
            HandRank rankCheck = this.analyzeFiveCardHand(allHand);
            if (hRC.compare(rankCheck, this.topRank) == 0) {
                SimpleAnalyzer temp = new SimpleAnalyzer(allHand);
                temp.analyze();
                topRankAnalyzers.add(temp);
            }
        }

        //Find the best Simple Analyzer
        SimpleAnalyzer topHand = topRankAnalyzers.get(0);
        for (int i = 1; i < topRankAnalyzers.size(); i++) {
            HandAnalyzerComparator hAC = new HandAnalyzerComparator();
            if (hAC.compare(topRankAnalyzers.get(i), topHand) > 0) {
                topHand = topRankAnalyzers.get(i);
            }
        }

        //Set the instance variables
        this.bestHandRanks = topHand.getBestHandRanks();
        this.pairRanks = (ArrayList<Rank>) topHand.getPairRanks();
        Collections.sort(this.pairRanks);
        Collections.reverse(this.pairRanks);
        this.fullHouseRanks = (ArrayList<Rank>) topHand.getFullHouseRanks();
    }

    /**
     * The ranks of the cards that aren't pairs.
     *
     * @return the ranks of the cards that aren't part of a pair.
     */
    final List<Rank> getNonPairRanks() {
        List<Rank> nonPairRanks = new ArrayList<>();

        for (Rank aBestHand : this.bestHandRanks) {
            for (Rank pairRank : this.pairRanks) {
                if (!aBestHand.equals(pairRank)) {
                    nonPairRanks.add(aBestHand);
                }
            }
        }
        Collections.sort(nonPairRanks);
        Collections.reverse(nonPairRanks);
        return nonPairRanks;
    }

    /**
     * Getter method to return the instance variable fullHand.
     *
     * @return the full hand
     */
    final List<Card> getFullHand() {
        return fullHand;
    }

    /**
     * Setter method for the instance variable topRank.
     *
     * @param rank to set topRank to
     */
    final void setTopRank(HandRank rank) {
        topRank = rank;
    }

    /**
     * Used to correctly initialize {@code pairRanks} for a Simple Analyzer implementation.
     *
     * <p>
     * Code was included in the abstract class to allow it access to private instance variables.
     * </p>
     */
    final void findPairRanks() {
        EnumMap<Rank, Integer> rankMap = new EnumMap<>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<>(Suit.class);

        this.handToEnumMaps(fullHand, rankMap, suitMap);

        for (Entry<Rank, Integer> entry : rankMap.entrySet()) {
            if (entry.getValue() > 1) {
                pairRanks.add(entry.getKey());
            }
        }
        Collections.sort(pairRanks);
        Collections.reverse(pairRanks);
    }

    /**
     * Used to correctly initialize {@code fullHouseRanks} for a Simple Analyzer implementation.
     *
     * <p>
     * Code was included in the abstract class to allow it access to private instance variables.
     * </p>
     */
    final void findFullHouseRanks() {
        EnumMap<Rank, Integer> rankMap = new EnumMap<>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<>(Suit.class);

        this.handToEnumMaps(fullHand, rankMap, suitMap);

        Iterator<Entry<Rank, Integer>> entriesRank = rankMap.entrySet().iterator();
        Rank threeKindRank = null, pairRank = null;
        while (entriesRank.hasNext()) {
            Entry<Rank, Integer> entry = entriesRank.next();
            if (entry.getValue() == TRIPS) {
                threeKindRank = entry.getKey();
            } else if (entry.getValue() == PAIR) {
                pairRank = entry.getKey();
            }
        }
        if (threeKindRank != null && pairRank != null) {
            this.fullHouseRanks.add(threeKindRank);
            this.fullHouseRanks.add(pairRank);
        }
    }

    final void findBestHand() {
        fullHand.forEach(card -> bestHandRanks.add(card.getRank()));
    }

    final List<Rank> getPairRanks() {
        return pairRanks;
    }

    /**
     * Returns the ranks associated with a full house.
     *
     * @return the {@code Rank}s associated with a full house, otherwise {}
     */
    final List<Rank> getFullHouseRanks() {
        return fullHouseRanks;
    }

    /**
     * Analyzes a five card hand to determine its best {@code HandRank}.
     *
     * @param hand hand to analyze
     * @return the best {@code HandRank} that can describe {@code hand}
     */
    final HandRank analyzeFiveCardHand(List<Card> hand) {
        HandRank rank = HandRank.HIGH_CARD;
        EnumMap<Rank, Integer> rankMap = new EnumMap<>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<>(Suit.class);

        this.handToEnumMaps(hand, rankMap, suitMap);

        ArrayList<Rank> pairRanks = new ArrayList<>();
        boolean hasStraight, has3Kind = false, has4Kind = false, hasFlush = false, hasBroadway = false;

        //Check for 3 of a kind, and 4 of a kind
        for (Entry<Rank, Integer> entry : rankMap.entrySet()) {
            if (entry.getValue() > 1) {
                pairRanks.add(entry.getKey());
                if (entry.getValue().equals(TRIPS)) {
                    has3Kind = true;
                    rank = HandRank.THREE_OF_A_KIND;
                } else if (entry.getValue().equals(QUADS)) {
                    has4Kind = true;
                    rank = HandRank.FOUR_OF_A_KIND;
                }
            }
        }

        //Pair checks
        if (!has3Kind && !has4Kind && pairRanks.size() > 0) {
            if (pairRanks.size() == 1) {
                rank = HandRank.PAIR;
            } else {
                rank = HandRank.TWO_PAIR;
            }
        }

        //Flush check
        if (suitMap.size() == 1 && !has4Kind) {
            hasFlush = true;
            rank = HandRank.FLUSH;
        }

        //Full House check
        if (has3Kind && pairRanks.size() == 2) {
            rank = HandRank.FULL_HOUSE;
        }

        //Straight check
        if (rankMap.size() == STRAIGHT) {
            if (rankMap.containsKey(Rank.ACE)) {
                rankMap.put(Rank.ONE, 1);
            }
            List<Rank> temp = Arrays .asList(Arrays.copyOf(rankMap.keySet().toArray(),
                            rankMap.keySet().toArray().length, Rank[].class));
            List<Rank> rankList = new ArrayList<>(temp);
            Collections.sort(rankList);

            hasStraight = true;
            //Used hand size to only view the first five elements
            for (int i = 0; i < hand.size() - 1 && hasStraight; i++) {
                if (rankList.get(i + 1).getStrength()
                        - rankList.get(i).getStrength() != 1) {
                    hasStraight = false;
                }
            }
            //Check for broadway straight
            if (!hasStraight && rankMap.size() == BROADWAY) {
                hasBroadway = true;
                for (int i = 1; i < rankMap.size() - 1 && hasBroadway; i++) {
                    if (rankList.get(i + 1).getStrength()
                            - rankList.get(i).getStrength() != 1) {
                        hasBroadway = false;
                    }
                }
                rankList.remove(Rank.ONE);
            } else if (rankMap.size() == BROADWAY) {
                rankList.remove(Rank.ACE);
            }

            //Check for types of straights
            if ((hasStraight || hasBroadway) && hasFlush) {
                if (hasBroadway) {
                    rank = HandRank.ROYAL_FLUSH;
                } else {
                    rank = HandRank.STRAIGHT_FLUSH;
                }
            } else if (hasStraight) {
                rank = HandRank.STRAIGHT;
            }
        }
        return rank;
    }

    /**
     * Creates two {@code enumMap}s used to store rank and suit by number of occurrences, respectively.
     *
     * @param hand hand to break down into maps
     * @param rankMap map to store {@code Rank}s as keys and their number of as their values
     * @param suitMap map to store {@code Suit}s as keys and their number of as their values
     */
    private void handToEnumMaps(List<Card> hand, EnumMap<Rank, Integer> rankMap, EnumMap<Suit, Integer> suitMap) {
        for (Card c : hand) {
            if (rankMap.size() > 0 && rankMap.containsKey(c.getRank())) {
                Integer oldValue = rankMap.remove(c.getRank());
                rankMap.put(c.getRank(), oldValue + 1);
            } else {
                rankMap.put(c.getRank(), 1);
            }
            if (suitMap.size() > 0 && suitMap.containsKey(c.getSuit())) {
                Integer oldValue = suitMap.remove(c.getSuit());
                suitMap.put(c.getSuit(), oldValue + 1);
            } else {
                suitMap.put(c.getSuit(), 1);
            }
        }
    }

    /**
     * Recurse through {@code fullHand} and returns all the possible combinations of size k.
     *
     * @param fullHand seven card hand to get the combinations from
     * @param k size of the combinations
     * @return all the combinations of size k
     */
    final List<List<Card>> recurseCombinations(List<Card> fullHand, int k) {
        List<List<Card>> allCombos = new ArrayList<>();
        if (k == 0) {
            allCombos.add(new ArrayList<>());
            return allCombos;
        }
        if (k > fullHand.size()) {
            return allCombos;
        }

        List<Card> groupWithoutC = new ArrayList<>(fullHand);
        Card c = groupWithoutC.remove(groupWithoutC.size() - 1);

        List<List<Card>> combosWithoutC = this .recurseCombinations(groupWithoutC, k);
        List<List<Card>> combosWithC = this.recurseCombinations(groupWithoutC, k - 1);
        for (List<Card> combo : combosWithC) {
            combo.add(c);
        }
        allCombos.addAll(combosWithoutC);
        allCombos.addAll(combosWithC);
        return allCombos;
    }

    /**
     * Used to compare {@code HandAnalyzer}s to each other.
     */
    public static final class HandAnalyzerComparator implements Comparator<HandAnalyzer> {
        @Override
        public int compare(HandAnalyzer hA1, HandAnalyzer hA2) {
            int value = hA1.getTopRank().getStrength() - hA2.getTopRank().getStrength();
            if (hA1.getTopRank().equals(HandRank.ROYAL_FLUSH)) {
                return 0;
            } else if (value == 0) {
                List<Rank> sortedHand1 = hA1.getBestHandRanks();
                List<Rank> sortedHand2 = hA2.getBestHandRanks();
                Collections.sort(sortedHand1);
                Collections.sort(sortedHand2);
                Collections.reverse(sortedHand1);
                Collections.reverse(sortedHand2);
                HandRank rank = hA1.getTopRank();
                if (rank.equals(HandRank.HIGH_CARD) || rank.equals(HandRank.FLUSH)) {
                    for (int i = 0; i < sortedHand1.size(); i++) {
                        if (!sortedHand1.get(i).equals(sortedHand2.get(i))) {
                            return sortedHand1.get(i).getStrength() - sortedHand2.get(i).getStrength();
                        }
                    }
                } else if (rank.equals(HandRank.STRAIGHT) || rank.equals(HandRank.STRAIGHT_FLUSH)) {
                    boolean hasLowAceStraight1 = false, hasLowAceStraight2 = false;
                    if (sortedHand1.contains(Rank.ACE) && sortedHand1.contains(Rank.TWO)) {
                        hasLowAceStraight1 = true;
                    }
                    if (sortedHand2.contains(Rank.ACE) && sortedHand2.contains(Rank.TWO)) {
                        hasLowAceStraight2 = true;
                    }
                    if (hasLowAceStraight1 && !hasLowAceStraight2) {
                        return -1;
                    } else if (hasLowAceStraight2 && !hasLowAceStraight1) {
                        return 1;
                    } else {
                        for (int i = 0; i < sortedHand1.size(); i++) {
                            if (!sortedHand1.get(i).equals(sortedHand2.get(i))) {
                                return sortedHand1.get(i).getStrength() - sortedHand2.get(i).getStrength();
                            }
                        }
                    }
                } else if (rank.equals(HandRank.PAIR) || rank.equals(HandRank.TWO_PAIR)
                        || rank.equals(HandRank.THREE_OF_A_KIND) || rank.equals(HandRank.FOUR_OF_A_KIND)) {
                    for (int i = 0; i < hA1.getPairRanks().size(); i++) {
                        if (!hA1.getPairRanks().get(i).equals(hA2.getPairRanks().get(i))) {
                            return hA1.getPairRanks().get(i).getStrength() - hA2.getPairRanks().get(i).getStrength();
                        }
                    }
                    List<Rank> nonPairRanks1 = hA1.getNonPairRanks();
                    List<Rank> nonPairRanks2 = hA2.getNonPairRanks();
                    for (int i = 0; i < nonPairRanks1.size(); i++) {
                        if (!nonPairRanks1.get(i).equals(nonPairRanks2.get(i))) {
                            return nonPairRanks1.get(i).getStrength() - nonPairRanks2.get(i).getStrength();
                        }
                    }
                } else if (rank.equals(HandRank.FULL_HOUSE)) {
                    for (int i = 0; i < hA1.getFullHouseRanks().size(); i++) {
                        if (!hA1.getFullHouseRanks().get(i).equals(hA2.getFullHouseRanks().get(i))) {
                            return hA1.getFullHouseRanks().get(i).getStrength()
                                    - hA2.getFullHouseRanks().get(i).getStrength();
                        }
                    }
                }
            }
            return value;
        }
    }
}
