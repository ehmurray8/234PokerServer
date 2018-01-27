package model.hand.representation;

import java.util.ArrayList;
import java.util.List;

import model.card.Card;
import model.player.Player;

/**
 * This interface defines a Hand that contains informations about the pot, the
 * blinds, and the community cards.
 *
 * <p>
 * Each implementation of the hand also contains the ability to deal the initial
 * hand to each player. The dealing capability was included in the hand to
 * utilize abstraction when creating a hand.
 * </p>
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/16
 *
 */
public interface HandInterface {

    /**
     * Deals the initial hand to all members of {@code players}.
     *
     * @param players
     *            the player's to deal the cards to
     */
    void dealInitialHand(List<Player> players);

    /**
     * Returns the small blind for the current hand.
     *
     * @return this.smallBlind
     */
    int getSmallBlind();

    /**
     * Returns the big blind for the current hand.
     *
     * @return this.bigBlind
     */
    int getBigBlind();

    /**
     * Deals the top card from {@code deck}.
     *
     * @return the {@code Card} dealt
     */
    Card dealCard();

    /**
     * Clears the pot.
     *
     * @custom.clears this.pot
     */
    void clearPot();

    /**
     * Adds {@code amount} to {@code pot}, and if {@code amount} is greater than
     * {@code maxBetTurn}; {@code maxBetTurn} is set to {@code amount}.
     *
     * @param addAmount
     *            The amount to add to the pot
     */
    void updatePot(int addAmount);

    /**
     * Pot value getter method for the pot instance variable.
     *
     * @return {@code pot}
     */
    int getPot();

    /**
     * Community card getter method to return the ArrayList of the
     * {@code Card}s.
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
     * Adds {@code addAmt} to the side pot at {@code index}.
     *
     * @param index
     *            the index of the side pot to add to
     * @param amtAdd
     *            the amount of money to add to the side pot
     */
    void addToSidePot(int index, int amtAdd);

    /**
     * Clears the list of side pots.
     */
    void clearSidePots();
}
