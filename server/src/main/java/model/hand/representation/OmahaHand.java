package model.hand.representation;

import java.util.List;

import game.Rules;
import model.card.Card;
import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.OmahaAnalyzer;
import model.player.Player;

class OmahaHand extends Hand {

    OmahaHand(final Rules rules, final List<Player> players, final int numberOfCards) {
        super(rules, players, numberOfCards);
    }

    @Override
    public HandAnalyzer createAnalyzer(List<Card> cards) {
        return new OmahaAnalyzer(cards);
    }
}
