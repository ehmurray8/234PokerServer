package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

/**
 * An implementation of {@code HandAnalyzer} used for analyzing and comparing five card hands.
 */
public class SimpleAnalyzer extends HandAnalyzer {
    /**
     * Creates an instance of {@code HandAnalyzer} by calling the super
     * constructor.
     *
     * <p>
     * Converts {@code fullHand} into its {@code Rank}s and stores it in
     * this.bestHand. The SimpleAnalyzer can be used to check its top rank
     * without calling {@code super.analyze}, but this method is required for
     * performing analysis about {@code pairRanks}, {@code fullHouseRanks}, and
     * {@code bestHand}.
     * </p>
     *
     * @param fullHand hand to analyze
     */
    SimpleAnalyzer(List<Card> fullHand) {
        super(fullHand);
        ArrayList<Card> fullHandAr = new ArrayList<>(super.getFullHand());
        super.setTopRank(super.analyzeFiveCardHand(fullHandAr));
    }

    @Override
    public List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * This method is not required to check the top rank of this, but is necessary for accessing any other information.
     */
    @Override
    public final void analyze() {
        super.findPairRanks();
        super.findFullHouseRanks();
        super.findBestHand();
    }
}
