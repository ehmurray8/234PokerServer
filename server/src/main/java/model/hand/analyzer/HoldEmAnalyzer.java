package model.hand.analyzer;

import java.util.List;

import model.card.Card;

import static model.hand.analyzer.AnalyzerHelpers.recurseCombinations;

public class HoldEmAnalyzer extends HandAnalyzer {

    public HoldEmAnalyzer(List<Card> fullHand) {
        super(fullHand);
    }

    public HoldEmAnalyzer(List<Card> fullHand, boolean isShortDeck) {
        super(fullHand, isShortDeck);
    }

    @Override
    public final List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        return recurseCombinations(fullHand, 5);
    }
}
