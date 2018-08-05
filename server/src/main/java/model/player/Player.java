package model.player;

import java.util.ArrayList;
import java.util.Comparator;

import model.card.Card;

public class Player  {

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
    		if(amountThisTurn == -1) {
    			amountThisTurn = 0;
    		}
            amountThisTurn += addAmt;
            updateBalance(-1 * addAmt);
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

    public final double getBalance() {
        return balance;
    }

    public final void updateBalance(double amt) {
        balance += amt;
    }

    public final ArrayList<Card> getHand() {
        return hand;
    }

    public final void addCard(Card card) {
        this.hand.add(card);
        this.hand.sort(CARD_COMPARATOR);
    }

    public final void fold() {
        hasFolded = true;
    }
    
    public final boolean hasFolded() {
        return hasFolded;
    }

    public final String getName() {
        return name;
    }

    @Override
    public final String toString() {
        return String.format("%s Balance: %s %s Folded: %s", name, balance, hand.toString(), hasFolded);
    }
}
