package model.hand.representation;

import java.util.List;

import model.player.Player;

/**
 * A hand extension that sets up and completes a hand of Texas Hold 'Em.
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/2016
 */
public class HoldEmHand extends Hand {
    /**
     * Creates a Texas Hold Em' Hand representation, with the starting blinds
     * specified by the {@code params}.
     *
     * @param smallBlind
     *            the amount of the small blind
     * @param bigBlind
     *            the amount of the big blind
     */
    public HoldEmHand(int smallBlind, int bigBlind) {
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
    }
}
