package model.hand.representation;

import java.util.ArrayList;

import model.card.Card;

/**
 * This interface defines a Hand that contains informations about the pot, the
 * blinds, and the community cards.
 *
 * <p>
 * Each implementation of the hand also contains the ability to deal the initial
 * hand to each player. The dealing capability was included in the hand to
 * utilize abstraction when creating a hand.
 * </p>
 */
public interface HandInterface {

    /**
     * Deals the top card from {@code deck}.
     *
     * @return the {@code Card} dealt
     */
    Card dealCard();

    /**
     * Community card getter method to return the ArrayList of the {@code Card}s.
     *
     * @return the community cards represented as an ArrayList
     */
    ArrayList<Card> getCommunityCards();

    /**
     * Burns a card, and then deals the flop.
     */
    void dealFlop();

    /**
     * Burns a card, and then deals the turn.
     */
    void dealTurn();

    /**
     * Burns a card, and then deals the river.
     */
    void dealRiver();

    /**
     * Deals the initial hand to all members of {@code players}.
     */
	void dealInitialHand();
}
