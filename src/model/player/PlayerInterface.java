package model.player;

import java.util.ArrayList;

import model.card.Card;

/**
 * This interface defines the methods a Player should have.
 *
 * <p>
 * The Player has a hand, a balance, a name, and a boolean correlating to
 * whether or not a player has folded.
 * </p>
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/16
 */
public interface PlayerInterface {
    /**
     * Returns the player's balance.
     *
     * @return {@code balance}
     */
    int getBalance();

    /**
     * Updates the player's balance.
     *
     * @param amount
     *            Amount to add to {@code hand}
     * @custom.updates balance balance = #balance + amount
     */
    void updateBalance(int amount);

    /**
     * Returns the player's hand.
     *
     * @return {@code hand}
     */
    ArrayList<Card> getHand();

    /**
     * Adds a {@code Card} to {@code hand}.
     *
     * @param card
     *            {@code Card} to add to {@code hand}
     * @custom.updates hand hand = #hand + card
     */
    void addCard(Card card);

    /**
     * Folds by clearing {@code hand} and setting {@code hasFolded} to true.
     */
    void fold();

    /**
     * Discards a card, by removing it from {@code hand}.
     *
     * @param card
     *            The {@code Card} to discard
     */
    void discard(Card card);

    /**
     * Returns whether or not the player has folded.
     *
     * @return {@code hasFolded}
     */
    boolean hasFolded();

    /**
     * Resets {@code hasFolded}.
     */
    void newHand();

    /**
     * Returns the player's name.
     *
     * @return {@code name}
     */
    String getName();
}
