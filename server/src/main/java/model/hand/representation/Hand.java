package model.hand.representation;

import java.text.DecimalFormat;
import java.util.*;

import model.card.Card;
import model.card.Deck;
import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.HoldEmAnalyzer;
import model.hand.analyzer.OmahaAnalyzer;
import model.option.Option;
import model.player.Player;

/**
 * The abstract class implements {@code HandInterface}, the class describes a generic poker hand,
 * one full turn in a poker game.
 *
 * <p>
 * The class deals the community cards and allows for the players to make bets. The hand object runs through a
 * full poker hand, and decides its winner.
 * </p>
 */
public abstract class Hand implements HandInterface {

    /** The {@code Deck} used for this. */
    private Deck deck;

    /** The community cards dealt during the hand. */
    private ArrayList<Card> community;

    /** The amount of the current small blind. */
    private double smallBlind;

    /** The amount of the current big blind. */
    private double bigBlind;
    
    /** The amount of the current ante. */
    private double ante;

    /**	Pots that are currently opened, main pot is at index 0. */
    private List<Pot> pots;
    
    /** All pots that have been opened during the hand. */
    private List<Pot> allPots;

    protected ArrayList<Player> players;

    /**
     * The Hand constructor initializes an abstract hand representing one turn in a game of poker.
     *
     * @param smallBlind the amount of the small blind
     * @param bigBlind the amount of the big blind
     * @param ante amount of the ante
     * @param players the players currently playing the hand
     */
    Hand(double smallBlind, double bigBlind, double ante, ArrayList<Player> players) {
        this.deck = new Deck();
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.ante = ante;
        this.community = new ArrayList<>();
        this.pots = new ArrayList<>();
        this.pots.add(new Pot(players));
        this.allPots = new ArrayList<>();
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
        return this.community.toString() + " Pot: " + amount + " Big: " + this.bigBlind + " Small: " + this.smallBlind;
    }

    @Override
    public final void dealFlop() {
        this.dealCard();
        this.community.add(this.dealCard());
        this.community.add(this.dealCard());
        this.community.add(this.dealCard());
    }

    @Override
    public final void dealTurn() {
        this.dealCard();
        this.community.add(this.dealCard());
    }

    @Override
    public final void dealRiver() {
        this.deck.dealCard();
        this.community.add(this.dealCard());
    }

    private void chargeAmount(double amount, List<Player> playersToCharge) {
    	for(Player p : playersToCharge) {
    		if(p.getBalance() > 0) {
    			double tempAmount = amount;
                if(p.addAmountThisTurn(amount)) {
                    for(Pot pot : pots) {
                        if(pot.getAmountOwed() != 0) {
                        	if(tempAmount >= pot.getAmountOwed()) {
                                tempAmount -= pot.getAmountOwed();
                                pot.addAmount(pot.getAmountOwed(), 1);
                        	} else {
                        		pot.addAmount(pot.getAmountOwed() - tempAmount, 1);
                        		tempAmount = 0;
                        	}
                        }
                    }
                    if(tempAmount > 0) {
                    	Pot currPot = pots.get(pots.size() - 1);
                    	currPot.setAmountOwed(currPot.getAmountOwed() + tempAmount);
                    	currPot.addAmount(tempAmount, 1);
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
    
    private void removeBrokePlayers() {
    	for(int i = players.size() - 1; i >= 0; i--) {
    		if(players.get(i).getBalance() == 0) {
    			players.remove(i);
    		}
    	}
    }
    
    private void removeOldPots() {
    	for(int i = this.pots.size() - 2; i >= 0; i--) {
            this.allPots.add(0, this.pots.remove(i));
    	}
    }

    public final void chargeAntes() {
    	this.pots.get(0).setAmountOwed(this.ante);
    	chargeAmount(this.ante, this.players);
    	for(Player p : this.players) {
    		p.clearAmtThisTurn();
    	}
    	removeOldPots();
    }
    
    public final void chargeBigBlind(int bigBlindPos) {
    	double potNum = this.bigBlind;
    	if(this.pots.size() > 1) {
    		potNum = this.bigBlind - this.pots.get(0).getAmountOwed();
    	}
    	this.pots.get(this.pots.size() - 1).setAmountOwed(potNum);
    	//this.pots.get(this.pots.size() - 1).
        chargeAmount(this.bigBlind, Collections.singletonList(this.players.get(bigBlindPos)));
    	removeBrokePlayers();
    	removeOldPots();
    }
    
    public final void chargeSmallBlind(int smallBlindPos) {
    	this.pots.get(0).setAmountOwed(this.smallBlind);
        chargeAmount(this.smallBlind, Collections.singletonList(this.players.get(smallBlindPos)));
    }
    
    public void setupBetRound() {
    	for(Player p : this.players) {
    		p.noActionThisTurn();
    	}
    }
    
    public boolean playersBetting() {
        double amountOwed = 0;
        int numPlayersWithMoney = 0;
        boolean res = false;
        int numNotFolded = 0;

        if (players.size() < 2) {
            return false;
        }

        for (Player p : players) {
            if (!p.hasFolded()) {
                numNotFolded += 1;
            }
        }
        if (numNotFolded < 2) {
            return false;
        }

        for (Pot p : pots) {
            amountOwed += p.getAmountOwed();
        }

        for (Player p : players) {
            if (p.getBalance() > 0) {
                numPlayersWithMoney++;
            }
            if (p.getAmountThisTurn() == -1) {
                res = true;
            }
        }

        return numPlayersWithMoney > 0 && amountOwed != 0 || numPlayersWithMoney >= 2 && res;

    }
    
    public void executeOption(Player player, Option option) {
    	Option.OptionType type = option.getType();
    	switch(type) {
    	case FOLD:
    		player.fold();
    		players.remove(player);
    		break;
    	case RAISE:
    	case BET:
    	case ALLIN:
    		this.pots.get(this.pots.size()-1).setAmountOwed(this.pots.get(this.pots.size()-1).getAmountOwed() 
    				+ option.getAmount());
    	case CALL:
    		chargeAmount(option.getAmount(), Collections.singletonList(player));
    		break;
    	default:
    		break;
    	}
    }
    
    public void payWinners(boolean isHoldem) {
    	List<HandAnalyzer> analyzers;
    	HandAnalyzer analyzer;
    	List<Card> hand;
        Comparator<HandAnalyzer> hAC = new HandAnalyzer.HandAnalyzerComparator();
        int numLeft = 0;
    	for(Pot pot : pots) {
            analyzers = new ArrayList<>();
    		for(Player player : pot.getPlayers()) {
                hand = new ArrayList<>();
                hand.addAll(player.getHand());
                hand.addAll(this.getCommunityCards());
                System.out.println("Paying Winners Check: " + player.toString() + ", Full hand: " + hand.toString());
                if (this.getCommunityCards().size() == 5) {
                    if(!player.hasFolded() && !player.isSittingOut()) {
                        if(isHoldem) {
                            analyzer = new HoldEmAnalyzer(hand);
                            analyzer.analyze();
                            analyzers.add(analyzer);
                        } else {
                            analyzer = new OmahaAnalyzer(hand);
                            analyzer.analyze();
                            analyzers.add(analyzer);
                        }
                        System.out.println(player.getName() + ": " + analyzer.toString());
                    } else {
                        analyzers.add(null);
                    }
                } else if(!player.hasFolded()) {
                	numLeft++;
                }
    		}
    		if(numLeft == 1) {
                double winnings = pot.getAmount(); 
                DecimalFormat df = new DecimalFormat(".##");
                winnings = Double.parseDouble(df.format(winnings));
                for(int i = 0; i < players.size(); i++) {
                	if(!players.get(i).hasFolded() && !players.get(i).isSittingOut()) {
                        System.out.println(pot.getPlayers().get(i).getName() + " has won " + winnings + "!");
                        pot.getPlayers().get(i).updateBalance(winnings);
                	}
                }
                System.out.println("Left over: " + (pot.getAmount() - winnings));
    		} else {
                List<Integer> potWinnerIdxs = new ArrayList<>();
                //potWinnerIdxs.add(0);
                for(int i = 0; i < pot.getPlayers().size(); i++) {
                    if(analyzers.get(i) != null) {
                        if(potWinnerIdxs.isEmpty()) {
                            potWinnerIdxs.add(i);
                        } else {
                            if(hAC.compare(analyzers.get(i), analyzers.get(potWinnerIdxs.get(0))) > 0) {
                                potWinnerIdxs.clear();
                                potWinnerIdxs.add(i);
                            } else if(hAC.compare(analyzers.get(i), analyzers.get(potWinnerIdxs.get(0))) == 0) {
                                potWinnerIdxs.add(i);
                            }
                        }
                    }
                    
                }
                double winnings = pot.getAmount() / potWinnerIdxs.size();
                DecimalFormat df = new DecimalFormat(".##");
                winnings = Double.parseDouble(df.format(winnings));
                for(int pwi : potWinnerIdxs) {
                    System.out.println(pot.getPlayers().get(pwi).getName() + " has won " + winnings + "!");
                    pot.getPlayers().get(pwi).updateBalance(winnings);
                }
                pot.getPlayers().get(0).updateBalance(pot.getAmount() - winnings);
                System.out.println("Left over: " + (pot.getAmount() - winnings));
    		}
    	}
    }
    
    public List<Option> generateOptions(Player player) {
    	double numOwed  = 0;
    	List<Option> options = new ArrayList<>();
    	if(player.getBalance() > 0) {
            for(Pot p : pots) {
                numOwed += p.getAmountOwed();
            }
            if(player.getAmountThisTurn() != -1) {
                numOwed -= player.getAmountThisTurn();
            }
            if(numOwed == 0) {
                options.add(new Option(Option.OptionType.CHECK, 0));
                if(this.bigBlind < player.getBalance()) {
                    options.add(new Option(Option.OptionType.BET, this.bigBlind));
                }
            } else {
                options.add(new Option(Option.OptionType.FOLD, 0));
                if(numOwed < player.getBalance()) {
                    options.add(new Option(Option.OptionType.CALL, numOwed));
                }
                if(numOwed + this.bigBlind < player.getBalance()) {
                	int count = 0;
                	for(Player p : players) {
                		if(p.getBalance() > 0 && !p.hasFolded() && !p.isSittingOut()) {
                			count++;
                		}
                	}
                	if(count > 1) {
                        options.add(new Option(Option.OptionType.RAISE, this.bigBlind + numOwed));
                	}
                }
            }
            if(numOwed + this.bigBlind <= player.getBalance()) {
                options.add(new Option(Option.OptionType.ALLIN, player.getBalance()));
            }
    	}
        return options;
    }
}
