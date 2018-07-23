package model.player;

import java.util.ArrayList;

import model.card.Card;

/**
 * This interface defines the methods a Player should have.
 *
 * <p>
 * The Player has a hand, a balance, a name, and a boolean correlating to whether or not a player has folded.
 * </p>
 */
public interface PlayerInterface {
    /**
     * Returns the player's balance.
     *
     * @return {@code balance}
     */
    double getBalance();

    /**
     * Returns the player's hand.
     *
     * @return {@code hand}
     */
    ArrayList<Card> getHand();

    /**
     * Adds a {@code Card} to {@code hand}.
     *
     * @param card code Card} to add to {@code hand}
     */
    void addCard(Card card);

    /**
     * Folds by clearing {@code hand} and setting {@code hasFolded} to true.
     */
    void fold();

    /**
     * Returns whether or not the player has folded.
     *
     * @return {@code hasFolded}
     */
    boolean hasFolded();

    /**
     * Returns the player's name.
     *
     * @return {@code name}
     */
    String getName();

    /**
     * Updates the player's balance.
     *
     * @param amt to add to {@code hand}
     */
	void updateBalance(double amt);
}
