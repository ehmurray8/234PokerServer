package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;

public class SimpleAnalyzer extends HandAnalyzer {
    SimpleAnalyzer(List<Card> fullHand) {
        super(fullHand);
        ArrayList<Card> fullHandList = new ArrayList<>(super.getFullHand());
        FiveCardAnalyzer fiveCardAnalyzer = new FiveCardAnalyzer(fullHandList);
        super.setTopRank(fiveCardAnalyzer.getRank());
    }

    @Override
    public List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        return null;
    }

    @Override
    public final void analyze() {
        super.findPairRanks();
        super.findFullHouseRanks();
        super.findBestHand();
    }
}
