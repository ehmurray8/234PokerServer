package model.hand.analyzer;

import java.util.List;

import model.card.Card;

/**
 * Implementation of {@code HandAnalyzer} used for Hold 'em and Pineapple hands.
 *
 * @author Emmet Murray
 * @version 1.0
 * @since 9/26/16
 */
public class HoldEmAnalyzer extends HandAnalyzer {

    /**
     * Creates an instance of {@code HandAnalyzer} by passing {@code fullHand}
     * to the super constructor.
     *
     * @param fullHand
     *            the hand to analyze
     * @custom.requires the hand was dealt during a Hold 'em or Pineapple {@code Hand}
     */
    public HoldEmAnalyzer(List<Card> fullHand) {
        super(fullHand);
    }

    @Override
    public final List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        final int numCard = 5;

        return super.recurseCombinations(fullHand, numCard);
    }
}
