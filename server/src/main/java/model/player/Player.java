package model.player;

import java.util.ArrayList;
import java.util.Comparator;

import model.card.Card;

/**
 * This class defines a Player playing in the poker game.
 *
 * <p>
 * The player is defined by a name, their current balance, a list of {@code Card} objects,
 * and a boolean value to determine whether they've folded.
 * </p>
 */
public class Player implements PlayerInterface  {

    /** {@code CardComparator} object used throughout the class to compare {@code Card} objects.
     */
    private static final Comparator<Card> CARD_COMPARATOR = new Card.CardComparator();

    /** Name of the player. */
    private String name;

    /** The player's balance. */
    private double balance;

    /** The player's hand. */
    private ArrayList<Card> hand;

    /** Whether or not the player has folded. */
    private boolean hasFolded;

    private double amountThisTurn;
    
    private boolean isSittingOut;

    /** Creates a new representation of a player. */
    public void resetStatus() {
        this.hand = new ArrayList<>();
        this.hasFolded = false;
        this.clearAmtThisTurn();
    }

    /**
     * Player constructor accepts a starting balance and a name to set {@code balance} and {@code name} respectively,
     * also initializes other variables used throughout the class.
     *
     * @param balance balance
     * @param name Player's name
     */
    public Player(double balance, String name) {
        this.balance = balance;
        this.name = name;
        this.resetStatus();
        this.isSittingOut = false;
    }

    public boolean isSittingOut() {
		return isSittingOut;
	}

	public void setSittingOut(boolean isSittingOut) {
		this.isSittingOut = isSittingOut;
	}

	public double getAmountThisTurn() {
        return this.amountThisTurn;
    }

    /**
     * Add the amount this turn, and deduct from the player's balance.
     *
     * @param addAmt the amount to add for this turn
     * @return true if the player has enough in their balance
     */
    public boolean addAmountThisTurn(double addAmt) {
    	if(addAmt <= this.balance) { 
    		if(this.amountThisTurn == -1) {
    			this.amountThisTurn = 0;
    		}
            this.amountThisTurn += addAmt;
            this.updateBalance(-1 * addAmt);
            return true;
    	} else {
    		return false;
    	}
    }

    public void clearAmtThisTurn() {
        this.amountThisTurn = 0;
    }
    
    public void noActionThisTurn() {
    	this.amountThisTurn = -1;
    }

    @Override
    public final double getBalance() {
        return this.balance;
    }

    @Override
    public final void updateBalance(double amt) {
        this.balance += amt;
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
        this.hasFolded = true;
    }
    
    @Override
    public final boolean hasFolded() {
        return this.hasFolded;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final String toString() {
        return this.name + ", Balance: " + this.balance + " " + this.hand.toString() + " Folded: " + this.hasFolded;
    }
}
