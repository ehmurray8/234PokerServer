package model.hand.representation;

import game.Rules;
import model.card.Card;
import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.OmahaAnalyzer;
import model.player.Player;

import java.util.List;

public class TestOmahaHand extends TestHand {

    public TestOmahaHand(final Rules rules, final List<Player> players, final int numberOfCards) {
        super(rules, players, numberOfCards);
    }

    @Override
    public HandAnalyzer createAnalyzer(List<Card> cards) {
        return new OmahaAnalyzer(cards);
    }
}
