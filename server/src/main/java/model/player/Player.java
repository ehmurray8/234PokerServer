package model.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

import model.card.Card;

import static java.util.UUID.randomUUID;

public class Player  {

    private static int NO_ACTION_THIS_TURN = -1;

    private static final Comparator<Card> CARD_COMPARATOR = new Card.CardComparator();
    private final String name;
    private double balance;
    ArrayList<Card> hand;
    private boolean hasFolded;
    private double amountThisTurn;
    private boolean isSittingOut;
    private UUID playerId;

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
        this.playerId = randomUUID();
    }

    public UUID getPlayerId() {
        return playerId;
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

    public double setupRaise() {
        double startingAmount = amountThisTurn;
        if(amountThisTurn > 0) {
            balance += amountThisTurn;
            amountThisTurn = 0;
        }
        return startingAmount;
    }

    /**
     * Add the amount this turn, and deduct from the player's balance.
     *
     * @param addAmt the amount to add for this turn
     * @return true if the player has enough in their balance
     */
    public boolean addAmountThisTurn(double addAmt) {
    	if(addAmt <= this.balance) { 
    		if(amountThisTurn == NO_ACTION_THIS_TURN) {
    			amountThisTurn = 0;
    		}
            amountThisTurn += addAmt;
            updateBalance(-1 * addAmt);
            return true;
    	} else {
    		return false;
    	}
    }

    public void check() {
        clearAmtThisTurn();
    }

    public void clearAmtThisTurn() {
        amountThisTurn = 0;
    }
    
    public void noActionThisTurn() {
    	amountThisTurn = NO_ACTION_THIS_TURN;
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

    public void addCard(Card card) {
        hand.add(card);
        hand.sort(CARD_COMPARATOR);
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
