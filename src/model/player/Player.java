package model.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

import model.card.Card;

/**
 * This class defines a Player playing in the poker game.
 *
 * <p>
 * The player is defined by a name, their current balance, a list of
 * {@code Card} objects, and a boolean value to determine whether they've
 * folded.
 * </p>
 *
 * @author Emmet Murray
 * @version 1.0
 * @since 9/20/16
 */
public class Player implements PlayerInterface, Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    /**
     * The options that each player has during their turn.
     *
     * @author Emmet Murray
     * @version 1.0
     * @since 5/1/2016
     */
    public enum Option {
        /**
         * Fold, only available once someone has bet, ends the player's turn.
         */
        FOLD,

        /**
         * Check, only available if no one has bet, keeps the player in the turn
         * for no charge.
         */
        CHECK,

        /**
         * Call, only available if someone has bet, keeps the player in the turn
         * for however much was bet.
         */
        CALL,

        /**
         * Raise or Bet, always available, forces other players to call the bet
         * for them to stay in the turn.
         */
        RAISE,

        /**
         * All in, always available, bets all of the players chips and forces
         * other players to call the bet to stay in the turn.
         */
        ALLIN;
    }

    /**
     * {@code CardComparator} object used throughout the class to compare
     * {@code Card} objects.
     */
    private static final Comparator<Card> CARD_COMPARATOR = new Card.CardComparator();

    /**
     * Name of the player.
     */
    private String name;

    /**
     * The player's balance.
     */
    private int balance;

    /**
     * The player's hand.
     */
    private ArrayList<Card> hand;

    /**
     * Whether or not the player has folded.
     */
    private boolean hasFolded;

    private int amountThisTurn;

    /**
     * Creates a new representation of a player.
     *
     * @param balance
     *            the player's balance
     * @param name
     *            the player's name
     */
    private void createNewRep(int balance, String name) {
        this.balance = balance;
        this.name = name;
        this.hand = new ArrayList<Card>();
        this.hasFolded = false;
        this.amountThisTurn = 0;
    }

    /**
     * Player constructor accepts a starting balance and a name to set
     * {@code balance} and {@code name} respectively, also initializes other
     * variables used throughout the class.
     *
     * @param balance
     *            Starting balance
     * @param name
     *            Player's name
     */
    public Player(int balance, String name) {
        this.createNewRep(balance, name);
    }

    public int getAmountThisTurn() {
        return this.amountThisTurn;
    }

    public void addAmountThisTurn(int addAmt) {
        this.amountThisTurn += addAmt;
    }

    public void clearAmtThisTurn() {
        this.amountThisTurn = 0;
    }

    @Override

    public final int getBalance() {
        return this.balance;
    }

    @Override
    public final void updateBalance(int amount) {
        this.balance += amount;
    }

    @Override
    public final ArrayList<Card> getHand() {
        return this.hand;
    }

    @Override
    public final void addCard(Card card) {
        this.hand.add(card);
        this.hand.sort(CARD_COMPARATOR);
    }

    @Override
    public final void fold() {
        this.hand = new ArrayList<Card>();
        this.hasFolded = true;
    }

    @Override
    public final void discard(Card card) {
        this.hand.remove(card);
    }

    @Override
    public final boolean hasFolded() {
        return this.hasFolded;
    }

    @Override
    public final void newHand() {
        String name = this.name;
        int balance = this.balance;
        this.createNewRep(balance, name);
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final String toString() {
        return this.name + ", Balance: " + this.balance + " "
                + this.hand.toString() + " Folded: " + this.hasFolded;
    }
}
