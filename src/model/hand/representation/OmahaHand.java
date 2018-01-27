package model.hand.representation;

import java.util.List;

import model.player.Player;

/**
 * A hand extension that sets up and completes a hand of Omaha poker.
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/16
 */
public class OmahaHand extends Hand {

    /**
     * Creates a Omaha Hand representation, with the starting blinds specified
     * by the {@code params}.
     *
     * @param smallBlind
     *            the amount of the small blind
     * @param bigBlind
     *            the amount of the big blind
     */
    public OmahaHand(int smallBlind, int bigBlind) {
        super(smallBlind, bigBlind);
    }

    @Override
    public final void dealInitialHand(List<Player> players) {
        for (Player player : players) {
            player.addCard(super.dealCard());
        }
        for (Player player : players) {
            player.addCard(super.dealCard());
        }
        for (Player player : players) {
            player.addCard(super.dealCard());
        }
        for (Player player : players) {
            player.addCard(super.dealCard());
        }
    }
}
