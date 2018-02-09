package model.hand.representation;

import java.util.ArrayList;

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
     * @param ante
     * 			  the amount of the ante
     * @param players
     * 			  the players in the current hand
     */
    public HoldEmHand(double smallBlind, double bigBlind, double ante, ArrayList<Player> players) {
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
    }
}
