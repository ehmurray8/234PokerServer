package model.hand.analyzer;

import java.util.ArrayList;
import java.util.List;

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
        this.playerHand = new ArrayList<>();
        this.community = new ArrayList<>();
        for (int i = 0; i < PLAYER_HAND_LENGTH; i++) {
            this.playerHand.add(super.getFullHand().get(i));
        }
        for (int i = PLAYER_HAND_LENGTH; i < super.fullHand.size(); i++) {
            this.community.add(super.getFullHand().get(i));
        }
        super.analyze();
    }

    public final List<List<Card>> fiveCardCombinations(List<Card> fullHand) {
        List<List<Card>> combinationsTotal = new ArrayList<>();
        List<List<Card>> combinationsPlayer = recurseCombinations(this.playerHand, 2);
        List<List<Card>> combinationsCommunity = recurseCombinations(this.community, 3);

        for (List<Card> aCombinationsComm : combinationsCommunity) {
            for (List<Card> aCombinationsPlayer : combinationsPlayer) {
                List<Card> temp = new ArrayList<>();
                temp.addAll(aCombinationsComm);
                temp.addAll(aCombinationsPlayer);
                combinationsTotal.add(temp);
            }
        }
        return combinationsTotal;
    }
}
