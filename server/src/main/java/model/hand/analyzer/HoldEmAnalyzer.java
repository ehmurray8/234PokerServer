package model.hand.analyzer;

import java.util.List;

import model.card.Card;

import static model.hand.analyzer.AnalyzerHelpers.recurseCombinations;

public class HoldEmAnalyzer extends HandAnalyzer {

    public HoldEmAnalyzer(List<Card> fullHand) {
        super(fullHand);
    }

    @Override
    public final List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        return recurseCombinations(fullHand, 5);
    }
}
