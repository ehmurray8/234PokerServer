package model.hand.representation;

import java.util.List;

import game.Rules;
import model.player.Player;

class TexasHoldEmHand extends Hand {

    TexasHoldEmHand(final Rules rules, final List<Player> players, final int numberOfCards) {
        super(rules, players, numberOfCards);
    }
}
