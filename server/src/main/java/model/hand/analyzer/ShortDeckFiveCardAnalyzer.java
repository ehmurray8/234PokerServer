package model.hand.analyzer;

import model.card.Card;

import java.util.List;

class ShortDeckFiveCardAnalyzer extends FiveCardAnalyzer {

    ShortDeckFiveCardAnalyzer(List<Card> hand) {
        super(hand);
    }

    @Override
    void checkCardsAreSequential(List<Card.Rank> rankList) {
        hasStraight = true;
        for (int i = 0; i < hand.size() - 1 && hasStraight; i++) {
            var higherRank = rankList.get(i + 1).getStrength();
            var lowerRank = rankList.get(i).getStrength();
            if(higherRank != 1) {
                higherRank -= 4;
            }
            if(lowerRank != 1) {
                lowerRank -= 4;
            }
            if (higherRank - lowerRank != 1) {
                hasStraight = false;
            }
        }
    }
}
