package model.hand.analyzer;

import java.util.List;

import model.card.Card;

/** Implementation of {@code HandAnalyzer} used for Hold 'em and Pineapple hands. */
public class HoldEmAnalyzer extends HandAnalyzer {

    /**
     * Creates an instance of {@code HandAnalyzer} by passing {@code fullHand} to the super constructor.
     *
     * @param fullHand hand to analyze
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
