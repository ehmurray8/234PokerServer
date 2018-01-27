package model.hand.representation;

import java.util.List;

import model.player.Player;

/**
 * A hand extension that sets up and completes a hand of Pineapple Poker.
 *
 * @author Emmet Murray
 * @version 1.0
 * @since TBD
 */
public class PineappleHand extends Hand {

    /**
     * Creates a Pineapple Hand representation, with the starting blinds
     * specified by the {@code params}.
     *
     * @param smallBlind
     *            the amount of the small blind
     * @param bigBlind
     *            the amount of the big blind
     */
    public PineappleHand(int smallBlind, int bigBlind) {
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
    }
}
