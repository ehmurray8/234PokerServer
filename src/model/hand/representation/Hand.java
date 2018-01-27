package model.hand.representation;

import java.util.ArrayList;

import model.card.Card;
import model.card.Deck;

/**
 * The abstract class implements {@code HandInterface}, the class describes a
 * generic poker hand, one full turn in a poker game.
 *
 * <p>
 * The class deals the community cards and allows for the players to make bets.
 * The hand object runs through a full poker hand, and decides its winner.
 * </p>
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/2016
 */
public abstract class Hand implements HandInterface {

    /**
     * The {@code Deck} used for this.
     */
    private Deck deck;

    /**
     * The community cards dealt during the hand.
     */
    private ArrayList<Card> community;

    /**
     * The amount currently in the pot.
     */
    private int pot;

    /**
     * The amount of the current small blind.
     */
    private int smallBlind;

    /**
     * The amount of the current big blind.
     */
    private int bigBlind;

    /**
     * A list of the amounts associated with any side pots.
     *
     * <p>
     * 1st position is the 1st side pot, NOT the main pot.
     * </p>
     */
    private ArrayList<Integer> sidePots;

    /**
     * The Hand constructor initializes an abstract hand representing one turn
     * in a game of poker.
     *
     * @param smallBlind
     *            the amount of the small blind
     * @param bigBlind
     *            the amount of the big blind
     */
    public Hand(int smallBlind, int bigBlind) {
        this.deck = new Deck();
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.pot = smallBlind + bigBlind;
        this.community = new ArrayList<Card>();
        this.sidePots = new ArrayList<Integer>();
    }

    @Override
    public final int getSmallBlind() {
        return this.smallBlind;
    }

    @Override
    public final int getBigBlind() {
        return this.bigBlind;
    }

    @Override
    public final Card dealCard() {
        return this.deck.dealCard();
    }

    @Override
    public final void clearPot() {
        this.pot = 0;
    }

    @Override
    public final void updatePot(int addAmount) {
        this.pot += addAmount;
    }

    @Override
    public final int getPot() {
        return this.pot;
    }

    @Override
    public final ArrayList<Card> getCommunityCards() {
        return this.community;
    }

    @Override
    public final String toString() {
        return this.community.toString() + " Pot: " + this.pot + " Big: "
                + this.bigBlind + " Small: " + this.smallBlind;
    }

    @Override
    public final void dealFlop() {
        this.deck.dealCard();
        this.community.add(this.deck.dealCard());
        this.community.add(this.deck.dealCard());
        this.community.add(this.deck.dealCard());
    }

    @Override
    public final void dealTurn() {
        this.deck.dealCard();
        this.community.add(this.deck.dealCard());
    }

    @Override
    public final void dealRiver() {
        this.deck.dealCard();
        this.community.add(this.deck.dealCard());
    }

    @Override
    public final void addToSidePot(int index, int amtAdd) {
        if (index - 1 < this.sidePots.size()) {
            this.sidePots.add(amtAdd);
        } else {
            this.sidePots.set(index, amtAdd + this.sidePots.get(index));
        }
    }

    @Override
    public final void clearSidePots() {
        this.sidePots = new ArrayList<Integer>();
    }
}
