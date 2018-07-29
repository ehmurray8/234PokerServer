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

    private static final Comparator<Card> CARD_COMPARATOR = new Card.CardComparator();
    private String name;
    private double balance;
    private ArrayList<Card> hand;
    private boolean hasFolded;
    private double amountThisTurn;
    private boolean isSittingOut;

    public void resetStatus() {
        hand = new ArrayList<>();
        hasFolded = false;
        clearAmtThisTurn();
    }

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
        return amountThisTurn;
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
        amountThisTurn = 0;
    }
    
    public void noActionThisTurn() {
    	amountThisTurn = -1;
    }

    @Override
    public final double getBalance() {
        return balance;
    }

    @Override
    public final void updateBalance(double amt) {
        balance += amt;
    }

    @Override
    public final ArrayList<Card> getHand() {
        return hand;
    }

    @Override
    public final void addCard(Card card) {
        this.hand.add(card);
        this.hand.sort(CARD_COMPARATOR);
    }

    @Override
    public final void fold() {
        hasFolded = true;
    }
    
    @Override
    public final boolean hasFolded() {
        return hasFolded;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final String toString() {
        return name + ", Balance: " + balance + " " + hand.toString() + " Folded: " + hasFolded;
    }
}
