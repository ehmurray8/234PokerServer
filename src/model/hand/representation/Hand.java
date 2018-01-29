package model.hand.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import model.card.Card;
import model.card.Deck;
import model.player.Player;

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
     * The amount of the current small blind.
     */
    private double smallBlind;

    /**
     * The amount of the current big blind.
     */
    private double bigBlind;
    
    /**
     * The amount of the current ante.
     */
    private double ante;

    /**
     *	Pots that are currently opened, main pot is at index 0. 
     */
    private List<Pot> pots;
    
    /**
     * All pots that have been opened during the hand.
     */
    private List<Pot> allPots;

    protected List<Player> players;

    /**
     * The Hand constructor initializes an abstract hand representing one turn
     * in a game of poker.
     *
     * @param smallBlind
     *            the amount of the small blind
     * @param bigBlind
     *            the amount of the big blind
     * @param ante
     * 			  the amount of the ante
     */
    public Hand(double smallBlind, double bigBlind, double ante, List<Player> players) {
        this.deck = new Deck();
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.ante = ante;
        this.community = new ArrayList<Card>();
        this.pots = new ArrayList<Pot>();
        this.pots.add(new Pot(players));
        this.allPots = new ArrayList<Pot>();
        this.players = players;
    }
    
    public List<Pot> getPots() {
    	return pots;
    }
    
    public List<Pot> getAllPots() {
    	return allPots;
    }

    @Override
    public final Card dealCard() {
        return this.deck.dealCard();
    }

    @Override
    public final ArrayList<Card> getCommunityCards() {
        return this.community;
    }

    @Override
    public final String toString() {
    	int amount = 0;
    	for(Pot pot : pots) {
    		amount += pot.getAmount();
    	}
        return this.community.toString() + " Pot: " + amount + " Big: "
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

    public final void chargeAmount(double amount, List<Player> playersToCharge) {
    	this.pots.get(0).setAmountOwed(amount);
    	for(Player p : playersToCharge) {
    		if(p.getBalance() > 0) {
                if(p.addAmountThisTurn(amount)) {
                    for(Pot pot : pots) {
                        if(pot.getAmountOwed() != 0) {
                            pot.addAmount(pot.getAmountOwed(), 1);
                        }
                    }
                } else {
                    int numPotsPaid = 0;
                    double startBalance = p.getBalance();
                    for(Pot pot : pots) {
                        if(pot.getAmountOwed() == 0 || !p.addAmountThisTurn(pot.getAmountOwed())) {
                            break;
                        }
                        pot.addAmount(pot.getAmountOwed(), 1);
                        numPotsPaid++;
                    }
                    double nextPotAmt = 0;
                    if(numPotsPaid + 1 < pots.size()) {
                        nextPotAmt = pots.get(numPotsPaid).getAmountOwed();
                    }
                    pots.add(numPotsPaid + 1, new Pot(pots.get(numPotsPaid).getPlayers()));
                    pots.get(numPotsPaid + 1).removePlayer(p);
                    pots.get(numPotsPaid + 1).setAmountOwed(amount - startBalance);
                    double carryOver = pots.get(numPotsPaid).setAmountOwed(p.getBalance());
                    int numPaid = pots.get(numPotsPaid).getNumPlayersPaid();
                    pots.get(numPotsPaid + 1).addAmount(carryOver, numPaid);
                    pots.get(numPotsPaid).addAmount(p.getBalance(), 1);
                    if(numPotsPaid + 2 < pots.size()) {
                        pots.get(numPotsPaid + 1).setAmountOwed(nextPotAmt - pots.get(numPotsPaid).getAmountOwed());
                        pots.get(numPotsPaid).updatePot();
                    }
                    for(int i = pots.size() - 1; i > numPotsPaid; i--){
                        pots.get(i).getPlayers().remove(p);
                    }
                    p.addAmountThisTurn(p.getBalance());
                }
    		}
    	}
    }
    
    public final void removeBrokePlayers() {
    	for(int i = players.size() - 1; i >= 0; i--) {
    		if(players.get(i).getBalance() == 0) {
    			players.remove(i);
    		}
    	}
    }
    
    public final void removeOldPots() {
    	for(int i = this.pots.size() - 2; i >= 0; i--) {
            this.allPots.add(0, this.pots.remove(i));
    	}
    }

    public final void chargeAntes() {
    	chargeAmount(this.ante, this.players);
    	for(Player p : this.players) {
    		p.clearAmtThisTurn();
    	}
    	removeOldPots();
    }
    
    public final void chargeBigBlind(int bigBlindPos) {
        chargeAmount(this.bigBlind, Arrays.asList(new Player[]{this.players.get(bigBlindPos)}));
    	removeBrokePlayers();
    	removeOldPots();
    }
    
    public final void chargeSmallBlind(int smallBlindPos) {
        chargeAmount(this.smallBlind, Arrays.asList(new Player[]{this.players.get(smallBlindPos)}));
    }
}
