package model.hand.analyzer;

import java.util.List;

import model.hand.analyzer.HandAnalyzer.HandRank;
import model.card.Card;
import model.card.Card.Rank;

/**
 * Analyzes a hand of five {@code Card}s or more used for comparing hand's to each other.
 */
public interface HandAnalyzerInterface {

    /**
     * The top {@code HandRank} contained in the full hand.
     *
     * @return the best {@code HandRank}
     */
    HandRank getTopRank();

    /**
     * The best five {@code Rank}s representing the best hand contained in the full hand.
     *
     * @return the best {@code Rank}s of the best hand
     */
    List<Rank> getBestHand();

    /**
     * Analyzes the hand, essentially the main method.
     */
    void analyze();
}
