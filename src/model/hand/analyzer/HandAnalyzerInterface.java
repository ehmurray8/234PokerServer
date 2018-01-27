package model.hand.analyzer;

import java.util.List;

import model.hand.analyzer.HandAnalyzer.HandRank;
import model.card.Card;
import model.card.Card.Rank;

/**
 * Analyzes a hand of five {@code Card}s or more used for comparing hand's to
 * each other.
 *
 * @author Emmet Murray
 * @version 1.0
 * @since 9/26/16
 */
public interface HandAnalyzerInterface {

    /**
     * The top {@code HandRank} contained in the full hand.
     *
     * @return the best {@code HandRank}
     */
    HandRank getTopRank();

    /**
     * The best five {@code Rank}s representing the best hand contained in the
     * full hand.
     *
     * @return the best {@code Rank}s of the best hand
     */
    List<Rank> getBestHand();

    /**
     * The list of all the five {@code Card} hands in {@code fullHand}.
     *
     * <p>
     * This method is optional when implementing this interface.
     * </p>
     *
     * @param fullHand
     *            the full list of cards to analyze
     * @return all the five card hand combinations
     */
    List<List<Card>> fiveCardCombinations(List<Card> fullHand);

    /**
     * Analyzes the hand, essentially the main method.
     *
     * @updates topRank, bestHand, pairRanks, fullHouseRanks
     */
    void analyze();
}
