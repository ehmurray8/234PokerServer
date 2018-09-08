package model.hand.representation;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.IntStream;

import model.card.Card;
import model.card.Deck;
import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.HandAnalyzerComparator;
import model.hand.analyzer.HoldEmAnalyzer;
import model.hand.analyzer.OmahaAnalyzer;
import model.option.Option;
import model.player.Player;

/**
 * The abstract class implements {@code HandInterface}, the class describes a generic poker hand,
 * one full turn in a poker game.
 *
 * <p>
 * The class deals the communityCards cards and allows for the players to make bets. The hand object runs through a
 * full poker hand, and decides its winner.
 * </p>
 */
public abstract class Hand implements HandInterface {

    final Deck deck;
    private final ArrayList<Card> communityCards;
    private double smallBlindAmount;
    private double bigBlindAmount;
    private double anteAmount;
    ArrayList<Player> players;

    /**	Pots that are currently opened, main pot is at index 0. */
    private final List<Pot> openPots;
    
    /** All openPots that have been opened during the hand. */
    private final List<Pot> allPots;

    Hand(double smallBlindAmount, double bigBlindAmount, double anteAmount, ArrayList<Player> players) {
        this();
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
        this.anteAmount = anteAmount;
        this.players = players;
        openPots.add(new Pot(this.players));
    }

    private Hand() {
        deck = new Deck();
        communityCards = new ArrayList<>();
        openPots = new ArrayList<>();
        allPots = new ArrayList<>();
    }

    public List<Pot> getOpenPots() {
    	return openPots;
    }
    
    public List<Pot> getAllPots() {
    	return allPots;
    }

    @Override
    public final ArrayList<Card> getCommunityCards() {
        return communityCards;
    }

    @Override
    public final String toString() {
        double totalAmountInPots = openPots.stream().map(Pot::getAmount).reduce(0.0, (pot1, pot2) -> pot1 + pot2);
        return communityCards.toString() + " Pot: " + totalAmountInPots + " Big: " +
               bigBlindAmount + " Small: " + smallBlindAmount;
    }

    @Override
    public final void dealFlop() {
        deck.pop();
        IntStream.range(0, 3).forEach(iteration -> communityCards.add(deck.pop()));
    }

    @Override
    public final void dealTurn() {
        deck.pop();
        communityCards.add(deck.pop());
    }

    @Override
    public final void dealRiver() {
        deck.pop();
        communityCards.add(deck.pop());
    }



    private void chargeAmount(double amount, List<Player> playersToCharge) {
        playersToCharge.stream().filter(player -> player.getBalance() > 0)
                .forEach(player -> chargePlayerAmount(player, amount));
    }

    private void chargePlayerAmount(Player player, double amount) {
        if(player.addAmountThisTurn(amount)) {
            playerPaidFullAmount(amount);
        } else {
            chargePlayerRemainingBalance(player, amount);
        }
    }

    private void playerPaidFullAmount(double amountPaid) {
        for(Pot pot : openPots) {
            if(pot.getAmountOwed() != 0) {
                if(amountPaid >= pot.getAmountOwed()) {
                    amountPaid -= pot.getAmountOwed();
                    pot.addAmount(pot.getAmountOwed(), 1);
                } else {
                    pot.addAmount(pot.getAmountOwed() - amountPaid, 1);
                    amountPaid = 0;
                }
            }
        }
        if(amountPaid > 0) {
            Pot currPot = openPots.get(openPots.size() - 1);
            currPot.setAmountOwed(currPot.getAmountOwed() + amountPaid);
            currPot.addAmount(amountPaid, 1);
        }
    }

    private void chargePlayerRemainingBalance(Player player, double fullAmount) {
        int numPotsPaid = 0;
            double startBalance = player.getBalance();
            for(Pot pot : openPots) {
                if(pot.getAmountOwed() == 0 || !player.addAmountThisTurn(pot.getAmountOwed())) {
                    break;
                }
                pot.addAmount(pot.getAmountOwed(), 1);
                numPotsPaid++;
            }
            double nextPotAmt = 0;
            if(numPotsPaid + 1 < openPots.size()) {
                nextPotAmt = openPots.get(numPotsPaid).getAmountOwed();
            }
            openPots.add(numPotsPaid + 1, new Pot(openPots.get(numPotsPaid).getPlayers()));
            openPots.get(numPotsPaid + 1).removePlayer(player);
            openPots.get(numPotsPaid + 1).setAmountOwed(fullAmount - startBalance);
            double carryOver = openPots.get(numPotsPaid).setAmountOwed(player.getBalance());
            int numPaid = openPots.get(numPotsPaid).getNumPlayersPaid();
            openPots.get(numPotsPaid + 1).addAmount(carryOver, numPaid);
            openPots.get(numPotsPaid).addAmount(player.getBalance(), 1);
            if(numPotsPaid + 2 < openPots.size()) {
                openPots.get(numPotsPaid + 1).setAmountOwed(nextPotAmt - openPots.get(numPotsPaid).getAmountOwed());
                openPots.get(numPotsPaid).updatePot();
            }
            for(int i = openPots.size() - 1; i > numPotsPaid; i--){
                openPots.get(i).getPlayers().remove(player);
            }
            player.addAmountThisTurn(player.getBalance());
    }
    
    private void removeBrokePlayers() {
        IntStream.rangeClosed(players.size() - 1, 0).filter(i -> players.get(i).getBalance() == 0).forEach(i ->
                players.remove(i)
        );
    }
    
    private void removeOldPots() {
    	for(int i = this.openPots.size() - 2; i >= 0; i--) {
            this.allPots.add(0, this.openPots.remove(i));
    	}
    }

    public final void chargeAntes() {
    	this.openPots.get(0).setAmountOwed(this.anteAmount);
    	chargeAmount(this.anteAmount, this.players);
    	for(Player p : this.players) {
    		p.clearAmtThisTurn();
    	}
    	removeOldPots();
    }
    
    public final void chargeBigBlind(int bigBlindPos) {
    	double potNum = this.bigBlindAmount;
    	if(this.openPots.size() > 1) {
    		potNum = this.bigBlindAmount - this.openPots.get(0).getAmountOwed();
    	}
    	this.openPots.get(this.openPots.size() - 1).setAmountOwed(potNum);
        chargeAmount(this.bigBlindAmount, Collections.singletonList(this.players.get(bigBlindPos)));
    	removeBrokePlayers();
    	removeOldPots();
    }
    
    public final void chargeSmallBlind(int smallBlindPos) {
    	this.openPots.get(0).setAmountOwed(this.smallBlindAmount);
        chargeAmount(this.smallBlindAmount, Collections.singletonList(this.players.get(smallBlindPos)));
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

        for (Pot p : openPots) {
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
    		this.openPots.get(this.openPots.size()-1).setAmountOwed(this.openPots.get(this.openPots.size()-1).getAmountOwed()
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
        Comparator<HandAnalyzer> hAC = new HandAnalyzerComparator();
        int numLeft = 0;
    	for(Pot pot : openPots) {
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
                            analyzers.add(analyzer);
                        } else {
                            analyzer = new OmahaAnalyzer(hand);
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
                List<Integer> potWinnerIndexes = new ArrayList<>();
                for(int i = 0; i < pot.getPlayers().size(); i++) {
                    if(analyzers.get(i) != null) {
                        if(potWinnerIndexes.isEmpty()) {
                            potWinnerIndexes.add(i);
                        } else {
                            if(hAC.compare(analyzers.get(i), analyzers.get(potWinnerIndexes.get(0))) > 0) {
                                potWinnerIndexes.clear();
                                potWinnerIndexes.add(i);
                            } else if(hAC.compare(analyzers.get(i), analyzers.get(potWinnerIndexes.get(0))) == 0) {
                                potWinnerIndexes.add(i);
                            }
                        }
                    }
                    
                }
                double winnings = pot.getAmount() / potWinnerIndexes.size();
                DecimalFormat df = new DecimalFormat(".##");
                winnings = Double.parseDouble(df.format(winnings));
                for(int pwi : potWinnerIndexes) {
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
            for(Pot p : openPots) {
                numOwed += p.getAmountOwed();
            }
            if(player.getAmountThisTurn() != -1) {
                numOwed -= player.getAmountThisTurn();
            }
            if(numOwed == 0) {
                options.add(new Option(Option.OptionType.CHECK, 0));
                if(this.bigBlindAmount < player.getBalance()) {
                    options.add(new Option(Option.OptionType.BET, this.bigBlindAmount));
                }
            } else {
                options.add(new Option(Option.OptionType.FOLD, 0));
                if(numOwed < player.getBalance()) {
                    options.add(new Option(Option.OptionType.CALL, numOwed));
                }
                if(numOwed + this.bigBlindAmount < player.getBalance()) {
                	int count = 0;
                	for(Player p : players) {
                		if(p.getBalance() > 0 && !p.hasFolded() && !p.isSittingOut()) {
                			count++;
                		}
                	}
                	if(count > 1) {
                        options.add(new Option(Option.OptionType.RAISE, this.bigBlindAmount + numOwed));
                	}
                }
            }
            if(numOwed + this.bigBlindAmount <= player.getBalance()) {
                options.add(new Option(Option.OptionType.ALLIN, player.getBalance()));
            }
    	}
        return options;
    }
}
