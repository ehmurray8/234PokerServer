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
     * @param ante
     * 			  the amount of the ante
     * @param players
     * 			  the players in the current hand
     */
    public PineappleHand(double smallBlind, double bigBlind, double ante, List<Player> players) {
        super(smallBlind, bigBlind, ante, players);
    }

    @Override
    public final void dealInitialHand() {
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
