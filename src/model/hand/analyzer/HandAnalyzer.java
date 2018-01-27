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
 * Abstract representation of a HandAnalyzer object, used to analyze hands to
 * find the {@code hand} with the best possible {@code HandRank}.
 *
 * <p>
 * The HandAnalyzer is represented by the Hand's best rank, the best five card
 * hand, the ranks of any pairs, the full hand, and the ranks of the full house
 * if the hand contains one.
 * </p>
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/16
 */
public abstract class HandAnalyzer implements HandAnalyzerInterface {

    /**
     * Constants used for analyzing Hands.
     */
    public static final int PAIR = 2, TRIP = 3, QUAD = 4, FIVE = 5, SIX = 6;

    /**
     * The different hand ranks for cards in poker.
     *
     * @author Emmet Murray
     * @version 1.0
     * @since 5/1/2016
     */
    public enum HandRank {
        /**
         * High Card.
         */
        HIGH_CARD(1, "High Card"),

        /**
         * Pair.
         */
        PAIR(2, "Pair"),

        /**
         * Two Pair.
         */
        TWO_PAIR(3, "Two Pair"),

        /**
         * Three of a Kind.
         */
        THREE_OF_A_KIND(4, "Three of a Kind"),

        /**
         * Straight.
         */
        STRAIGHT(5, "Straight"),

        /**
         * Flush.
         */
        FLUSH(6, "Flush"),

        /**
         * Full House.
         */
        FULL_HOUSE(7, "Full House"),

        /**
         * Four of a Kind.
         */
        FOUR_OF_A_KIND(8, "Four of a Kind"),

        /**
         * Straight Flush.
         */
        STRAIGHT_FLUSH(9, "Straight Flush"),

        /**
         * Royal Flush.
         */
        ROYAL_FLUSH(10, "Royal Flush");

        /**
         * Number associated with the HandRank, used for comparing HandRanks.
         */
        private int handRankNum;

        private String str;

        /**
         * Constructor to create a HandRank object.
         *
         * @param handRankNum
         *            the number to associate with the HandRank
         * @param str string representation of this
         */
        HandRank(int handRankNum, String str) {
            this.handRankNum = handRankNum;
            this.str = str;
        }

        /**
         * Returns the number associated with the HandRank.
         *
         * @return the number associated with the HandRank
         */
        public int getHandRankNum() {
            return this.handRankNum;
        }

        @Override
        public String toString() {
            return this.str;
        }

        /**
         * Used to compare {@code HandRank}s to each other.
         *
         * @author Emmet Murray
         * @version 1.0
         * @since 9/26/16
         */
        public static final class HandRankComparator
                implements Comparator<HandRank> {

            @Override
            public int compare(HandRank hr1, HandRank hr2) {
                return hr1.getHandRankNum() - hr2.getHandRankNum();
            }
        }
    }

    /**
     * The {@code HandRank} of the best five card Hand.
     */
    private HandRank topRank;

    /**
     * The best 5 card hand, sorted lowest to highest.
     */
    private List<Rank> bestHand;

    /**
     * The {@code Rank}(s) of any pairs the hand contains.
     *
     * @custom.ensures |pairRanks| = number of pairs if|pairRanks| = 2 =&gt; {top, second}
     */
    private ArrayList<Rank> pairRanks;

    /**
     * The List of all the {@code Rank}s ranks in the full seven or nine card
     * hand.
     */
    private List<Card> fullHand;

    /**
     * Ranks of the full house.
     *
     * @custom.ensures {three of a kind rank, two of a kind rank}, or |fullHouseRanks|
     *          = 0
     */
    private ArrayList<Rank> fullHouseRanks;

    private List<Card> bestHandCards;

    /**
     * Initializes the object by setting the instance variables to their default
     * values.
     *
     * <p>
     * The constructor constructs a new HandAnalyzer, but the analyze method
     * must be called on it before any data can be accessed from this.
     * </p>
     *
     * @param fullHand
     *            the fullHand to analyze using the object
     */
    public HandAnalyzer(List<Card> fullHand) {
        this.topRank = HandRank.HIGH_CARD;
        this.bestHand = new ArrayList<Rank>();
        this.pairRanks = new ArrayList<Rank>();
        this.fullHouseRanks = new ArrayList<Rank>();
        this.fullHand = fullHand;
    }

    @Override
    public final HandRank getTopRank() {
        return this.topRank;
    }

    @Override
    public final List<Rank> getBestHand() {
        return this.bestHand;
    }

    @Override
    public final String toString() {
        return this.fullHand + " Best: " + this.bestHand + " " + this.topRank;
    }

    /**
     * {@inheritDoc}
     *
     * THIS METHOD IS REQUIRED TO BE CALLED BEFORE ACESSING ANY DATA FROM
     * {@code this}.
     *
     * @custom.updates pairRanks, bestHand, fullHouseRanks, topRank
     */
    @Override
    public void analyze() {
        List<List<Card>> allHands = new ArrayList<List<Card>>(
                this.fiveCardCombinations(this.fullHand));

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
        List<SimpleAnalyzer> topRankAnalyzers = new ArrayList<SimpleAnalyzer>();
        for (int i = 0; i < allHands.size(); i++) {
            HandRank.HandRankComparator hRC = new HandRank.HandRankComparator();
            HandRank rankCheck = this.analyzeFiveCardHand(allHands.get(i));
            if (hRC.compare(rankCheck, this.topRank) == 0) {
                SimpleAnalyzer temp = new SimpleAnalyzer(allHands.get(i));
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
        this.bestHand = topHand.getBestHand();
        this.bestHandCards = topHand.getFullHand();
        this.pairRanks = (ArrayList<Rank>) topHand.getPairRanks();
        Collections.sort(this.pairRanks);
        flip(this.pairRanks);
        this.fullHouseRanks = (ArrayList<Rank>) topHand.getFullHouseRanks();
    }

    public List<Card> getBestHandCards() {
        return this.bestHandCards;
    }

    /**
     * The ranks of the cards that aren't pairs.
     *
     * @return the ranks of the cards that aren't part of a pair.
     * @custom.requires |pairRanks| &gt; 0
     * @custom.ensures return is sorted in decreasing order
     */
    protected final List<Rank> getNonPairRanks() {
        List<Rank> nonPairRanks = new ArrayList<Rank>();

        for (int i = 0; i < this.bestHand.size(); i++) {
            for (int j = 0; j < this.pairRanks.size(); j++) {
                if (!this.bestHand.get(i).equals(this.pairRanks.get(j))) {
                    nonPairRanks.add(this.bestHand.get(i));
                }
            }
        }
        Collections.sort(nonPairRanks);
        flip(nonPairRanks);
        return nonPairRanks;
    }

    /**
     * Getter method to return the instance variable fullHand.
     *
     * @return the full hand
     */
    protected final List<Card> getFullHand() {
        return this.fullHand;
    }

    /**
     * Setter method for the instance variable topRank.
     *
     * @param rank
     *            HandRank to set topRank to
     * @custom.updates topRank
     */
    protected final void setTopRank(HandRank rank) {
        this.topRank = rank;
    }

    /**
     * Used to correctly initialize {@code pairRanks} for a Simple Analyzer
     * implementation.
     *
     * <p>
     * Code was included in the abstract class to allow it access to private
     * instance variables.
     * </p>
     *
     * @custom.ensures |return| = 2 -&gt; {high rank, low rank}
     * @custom.requires this is a SimpleAnalyzer
     */
    protected final void findPairRanks() {
        EnumMap<Rank, Integer> rankMap = new EnumMap<Rank, Integer>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<Suit, Integer>(Suit.class);

        this.handToEnumMaps(this.fullHand, rankMap, suitMap);

        Iterator<Entry<Rank, Integer>> entriesRank = rankMap.entrySet()
                .iterator();
        while (entriesRank.hasNext()) {
            Entry<Rank, Integer> entry = entriesRank.next();
            if (entry.getValue() > 1) {
                this.pairRanks.add(entry.getKey());
            }
        }
        Collections.sort(this.pairRanks);
        flip(this.pairRanks);
    }

    /**
     * Used to correctly initialize {@code fullHouseRanks} for a Simple Analyzer
     * implementation.
     *
     * <p>
     * Code was included in the abstract class to allow it access to private
     * instance variables.
     * </p>
     *
     * @custom.ensures |return| = 2 -&gt; {3Kind rank, Pair rank}
     * @custom.requires this is a SimpleAnalyzer
     */
    protected final void findFullHouseRanks() {
        EnumMap<Rank, Integer> rankMap = new EnumMap<Rank, Integer>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<Suit, Integer>(Suit.class);

        this.handToEnumMaps(this.fullHand, rankMap, suitMap);

        Iterator<Entry<Rank, Integer>> entriesRank = rankMap.entrySet()
                .iterator();
        Rank threeKindRank = null, pairRank = null;
        boolean has3Kind = false, hasPair = false;
        while (entriesRank.hasNext()) {
            Entry<Rank, Integer> entry = entriesRank.next();
            if (entry.getValue() == TRIP) {
                threeKindRank = entry.getKey();
                has3Kind = true;
            } else if (entry.getValue() == PAIR) {
                pairRank = entry.getKey();
                hasPair = true;
            }
        }
        if (threeKindRank != null && pairRank != null && has3Kind && hasPair) {
            this.fullHouseRanks.add(threeKindRank);
            this.fullHouseRanks.add(pairRank);
        }
    }

    /**
     * Method to find best hand in a Simple Analyzer.
     *
     * <p>
     * Code was included in the abstract class to allow it access to private
     * instance variables.
     * </p>
     *
     * @custom.requires this is a SimpleAnalyzer
     */
    protected final void findBestHand() {
        for (Card c : this.fullHand) {
            this.bestHand.add(c.getRank());
        }
    }

    /**
     * Returns the ranks of the pairs.
     *
     * @return any {@code Rank}s associated with a pair contained in the full
     *         hand
     * @custom.ensures |return| = number of pairs if|pairRanks| = 2 =&gt; {top, second}
     */
    protected final List<Rank> getPairRanks() {
        return this.pairRanks;
    }

    /**
     * Returns the ranks associated with a full house.
     *
     * @return the {@code Rank}s associated with a full house, otherwise {}
     * @custom.ensures {three of a kind rank, two of a kind rank}, or |fullHouseRanks|
     *          = 0
     */
    protected final List<Rank> getFullHouseRanks() {
        return this.fullHouseRanks;
    }

    /**
     * Analyzes a five card hand to determine its best {@code HandRank}.
     *
     * @param hand
     *            the hand to analyze
     * @return the best {@code HandRank} that can describe {@code hand}
     * @custom.requires |hand| = 5
     */
    protected final HandRank analyzeFiveCardHand(List<Card> hand) {
        HandRank rank = HandRank.HIGH_CARD;
        EnumMap<Rank, Integer> rankMap = new EnumMap<Rank, Integer>(Rank.class);
        EnumMap<Suit, Integer> suitMap = new EnumMap<Suit, Integer>(Suit.class);

        this.handToEnumMaps(hand, rankMap, suitMap);

        ArrayList<Rank> pairRanks = new ArrayList<Rank>();
        boolean hasStraight = false, has3Kind = false, has4Kind = false,
                hasFlush = false, hasBroadway = false;

        //Check for 3 of a kind, and 4 of a kind
        Iterator<Entry<Rank, Integer>> entriesRank = rankMap.entrySet()
                .iterator();
        while (entriesRank.hasNext()) {
            Entry<Rank, Integer> entry = entriesRank.next();
            if (entry.getValue() > 1) {
                pairRanks.add(entry.getKey());
                if (entry.getValue().equals(Integer.valueOf(TRIP))) {
                    has3Kind = true;
                    rank = HandRank.THREE_OF_A_KIND;
                } else if (entry.getValue().equals(Integer.valueOf(QUAD))) {
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
        if (rankMap.size() == FIVE) {
            if (rankMap.containsKey(Rank.ACE)) {
                rankMap.put(Rank.ONE, Integer.valueOf(1));
            }
            List<Rank> temp = Arrays
                    .asList(Arrays.copyOf(rankMap.keySet().toArray(),
                            rankMap.keySet().toArray().length, Rank[].class));
            List<Rank> rankList = new ArrayList<Rank>(temp);
            Collections.sort(rankList);

            hasStraight = true;
            //Used hand size to only view the first five elements
            for (int i = 0; i < hand.size() - 1 && hasStraight; i++) {
                if (rankList.get(i + 1).getRankNum()
                        - rankList.get(i).getRankNum() != 1) {
                    hasStraight = false;
                }
            }
            //Check for broadway straight
            if (!hasStraight && rankMap.size() == SIX) {
                hasBroadway = true;
                for (int i = 1; i < rankMap.size() - 1 && hasBroadway; i++) {
                    if (rankList.get(i + 1).getRankNum()
                            - rankList.get(i).getRankNum() != 1) {
                        hasBroadway = false;
                    }
                }
                rankList.remove(Rank.ONE);
            } else if (rankMap.size() == SIX) {
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
     * Creates two {@code enumMap}s used to store rank and suit by number of
     * occurrences, respectively.
     *
     * @param hand
     *            the hand to break down into maps
     * @param rankMap
     *            the map to store {@code Rank}s as keys and their number of
     *            occurrences as their values
     * @param suitMap
     *            the map to store {@code Suit}s as keys and their number of
     *            occurrences as their values
     */
    protected final void handToEnumMaps(List<Card> hand,
            EnumMap<Rank, Integer> rankMap, EnumMap<Suit, Integer> suitMap) {
        for (Card c : hand) {
            if (rankMap.size() > 0 && rankMap.containsKey(c.getRank())) {
                Integer oldValue = rankMap.remove(c.getRank());
                rankMap.put(c.getRank(),
                        Integer.valueOf(oldValue.intValue() + 1));
            } else {
                rankMap.put(c.getRank(), Integer.valueOf(1));
            }
            if (suitMap.size() > 0 && suitMap.containsKey(c.getSuit())) {
                Integer oldValue = suitMap.remove(c.getSuit());
                suitMap.put(c.getSuit(),
                        Integer.valueOf(oldValue.intValue() + 1));
            } else {
                suitMap.put(c.getSuit(), Integer.valueOf(1));
            }
        }
    }

    /**
     * Recurses through {@code fullHand} and returns all the possible
     * combinations of size k.
     *
     * @param fullHand
     *            the seven card hand to get the combinations from
     * @param k
     *            the size of the combinations
     * @return all the combinations of size k
     */
    protected final List<List<Card>> recurseCombinations(List<Card> fullHand,
            int k) {
        List<List<Card>> allCombos = new ArrayList<List<Card>>();
        // base cases for recursion
        if (k == 0) {
            // There is only one combination of size 0, the empty team.
            allCombos.add(new ArrayList<Card>());
            return allCombos;
        }
        if (k > fullHand.size()) {
            // There can be no teams with size larger than the group size,
            // so return allCombos without putting any teams in it.
            return allCombos;
        }

        // Create a copy of the group with one item removed.
        List<Card> groupWithoutC = new ArrayList<Card>(fullHand);
        Card c = groupWithoutC.remove(groupWithoutC.size() - 1);

        List<List<Card>> combosWithoutC = this
                .recurseCombinations(groupWithoutC, k);
        List<List<Card>> combosWithC = this.recurseCombinations(groupWithoutC,
                k - 1);
        for (List<Card> combo : combosWithC) {
            combo.add(c);
        }
        allCombos.addAll(combosWithoutC);
        allCombos.addAll(combosWithC);
        return allCombos;
    }

    /**
     * Flips the elements in {@code list}.
     *
     * @param list
     *            the list to flip
     * @param <T>
     *            generic type
     * @custom.updates list
     * @custom.ensures list = rev($list)
     */
    protected static final <T> void flip(List<T> list) {
        for (int i = 0; i < list.size() / 2; i++) {
            T temp = list.get(i);
            list.set(i, list.get(list.size() - i - 1));
            list.set(list.size() - i - 1, temp);
        }
    }

    /**
     * Used to compare {@code HandAnalyzer}s to each other.
     *
     * @author Emmet Murray
     * @version 1.0
     * @since 9/26/16
     */
    public static final class HandAnalyzerComparator
            implements Comparator<HandAnalyzer> {
        @Override
        public int compare(HandAnalyzer hA1, HandAnalyzer hA2) {
            int value = hA1.getTopRank().getHandRankNum()
                    - hA2.getTopRank().getHandRankNum();
            if (hA1.getTopRank().equals(HandRank.ROYAL_FLUSH)) {
                value = 0;
            } else if (value == 0) {
                List<Rank> sortedHand1 = hA1.getBestHand();
                List<Rank> sortedHand2 = hA2.getBestHand();
                Collections.sort(sortedHand1);
                Collections.sort(sortedHand2);
                flip(sortedHand1);
                flip(sortedHand2);
                HandRank rank = hA1.getTopRank();
                if (rank.equals(HandRank.HIGH_CARD)
                        || rank.equals(HandRank.FLUSH)) {
                    boolean done = false;
                    for (int i = 0; i < sortedHand1.size() && !done; i++) {
                        if (!sortedHand1.get(i).equals(sortedHand2.get(i))) {
                            value = sortedHand1.get(i).getRankNum()
                                    - sortedHand2.get(i).getRankNum();
                            done = true;
                        }
                    }
                } else if (rank.equals(HandRank.STRAIGHT)
                        || rank.equals(HandRank.STRAIGHT_FLUSH)) {
                    boolean hasLowAceStraight1 = false,
                            hasLowAceStraight2 = false;
                    if (sortedHand1.contains(Rank.ACE)
                            && sortedHand1.contains(Rank.TWO)) {
                        hasLowAceStraight1 = true;
                    }
                    if (sortedHand2.contains(Rank.ACE)
                            && sortedHand2.contains(Rank.TWO)) {
                        hasLowAceStraight2 = true;
                    }
                    if (hasLowAceStraight1 && !hasLowAceStraight2) {
                        value = -1;
                    } else if (hasLowAceStraight2 && !hasLowAceStraight1) {
                        value = 1;
                    } else {
                        boolean done = false;
                        for (int i = 0; i < sortedHand1.size() && !done; i++) {
                            if (!sortedHand1.get(i)
                                    .equals(sortedHand2.get(i))) {
                                value = sortedHand1.get(i).getRankNum()
                                        - sortedHand2.get(i).getRankNum();
                                done = true;
                            }
                        }
                    }
                } else if (rank.equals(HandRank.PAIR)
                        || rank.equals(HandRank.TWO_PAIR)
                        || rank.equals(HandRank.THREE_OF_A_KIND)
                        || rank.equals(HandRank.FOUR_OF_A_KIND)) {
                    boolean done = false;
                    for (int i = 0; i < hA1.getPairRanks().size()
                            && !done; i++) {
                        if (!hA1.getPairRanks().get(i)
                                .equals(hA2.getPairRanks().get(i))) {
                            value = hA1.getPairRanks().get(i).getRankNum()
                                    - hA2.getPairRanks().get(i).getRankNum();
                            done = true;
                        }
                    }
                    if (!done) {
                        List<Rank> nonPairRanks1 = hA1.getNonPairRanks();
                        List<Rank> nonPairRanks2 = hA2.getNonPairRanks();
                        for (int i = 0; i < nonPairRanks1.size()
                                && !done; i++) {
                            if (!nonPairRanks1.get(i)
                                    .equals(nonPairRanks2.get(i))) {
                                value = nonPairRanks1.get(i).getRankNum()
                                        - nonPairRanks2.get(i).getRankNum();
                                done = true;
                            }
                        }
                    }
                } else if (rank.equals(HandRank.FULL_HOUSE)) {
                    boolean done = false;
                    for (int i = 0; i < hA1.getFullHouseRanks().size()
                            && !done; i++) {
                        if (!hA1.getFullHouseRanks().get(i)
                                .equals(hA2.getFullHouseRanks().get(i))) {
                            value = hA1.getFullHouseRanks().get(i).getRankNum()
                                    - hA2.getFullHouseRanks().get(i)
                                            .getRankNum();
                            done = true;
                        }
                    }
                }
                flip(sortedHand1);
                flip(sortedHand2);
            }
            return value;
        }
    }
}
