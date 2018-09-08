package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import model.card.Card;

import static model.hand.analyzer.AnalyzerHelpers.recurseCombinations;

public class OmahaAnalyzer extends HandAnalyzer {

    private static final int PLAYER_HAND_LENGTH = 4;
    private List<Card> playerHand;
    private List<Card> community;

    public OmahaAnalyzer(List<Card> fullHand) {
        super(fullHand);
    }

    @Override
    public void analyze() {
        playerHand = new ArrayList<>();
        community = new ArrayList<>();
        IntStream.range(0, PLAYER_HAND_LENGTH).forEach(i -> playerHand.add(fullHand.get(i)));
        IntStream.range(PLAYER_HAND_LENGTH, fullHand.size()).forEach(i -> community.add(fullHand.get(i)));
        super.analyze();
    }

    public final List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        List<List<Card>> combinationsTotal = new ArrayList<>();
        List<List<Card>> combinationsPlayer = recurseCombinations(playerHand, 2);
        List<List<Card>> combinationsCommunity = recurseCombinations(community, 3);

        for (List<Card> communityCombos : combinationsCommunity) {
            for (List<Card> playerCombos : combinationsPlayer) {
                List<Card> temp = new ArrayList<>();
                temp.addAll(communityCombos);
                temp.addAll(playerCombos);
                combinationsTotal.add(temp);
            }
        }
        return combinationsTotal;
    }
}
