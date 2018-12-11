package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class SimpleAnalyzer extends HandAnalyzer {

    SimpleAnalyzer(List<Card> fullHand, boolean isShortDeck) {
        super(fullHand);
        ArrayList<Card> fullHandList = new ArrayList<>(getFullHand());
        if(isShortDeck) {
            ShortDeckFiveCardAnalyzer fiveCardAnalyzer = new ShortDeckFiveCardAnalyzer(fullHandList);
            setTopRank(fiveCardAnalyzer.getRank());
        } else {
            FiveCardAnalyzer fiveCardAnalyzer = new FiveCardAnalyzer(fullHandList);
            setTopRank(fiveCardAnalyzer.getRank());
        }
    }

    @Override
    public List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        return null;
    }

    @Override
    public final void analyze() {
        findPairRanks();
        findFullHouseRanks();
        findBestHand();
    }
}
