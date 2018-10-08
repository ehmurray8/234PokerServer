package model.hand.representation;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.IntStream;

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
 * The class deals the communityCards cards and allows for the players to make bets. The hand object runs through a
 * full poker hand, and decides its winner.
 * </p>
 */
public abstract class Hand {

    final Deck deck;
    private final ArrayList<Card> communityCards;
    private double smallBlindAmount;
    private double bigBlindAmount;
    private double anteAmount;
    ArrayList<Player> players;

    /**	Pots that are currently opened, main pot is at index 0. */
    private final List<Pot> openPots;
    
    /** All openPots that have been opened during the hand. */
    private final List<Pot> closedPots;

    Hand(double smallBlindAmount, double bigBlindAmount, double anteAmount, ArrayList<Player> players) {
        this();
        this.smallBlindAmount = smallBlindAmount;
        this.bigBlindAmount = bigBlindAmount;
        this.anteAmount = anteAmount;
        this.players = players;
        openPots.add(new Pot(players));
    }

    private Hand() {
        deck = new Deck();
        communityCards = new ArrayList<>();
        openPots = new ArrayList<>();
        closedPots = new ArrayList<>();
    }

    public abstract void dealInitialHand();

    public List<Pot> getOpenPots() {
    	return openPots;
    }
    
    public List<Pot> getClosedPots() {
    	return closedPots;
    }

    public double getTotalAmountInPots() {
        return closedPots.stream().mapToDouble(Pot::getAmount).sum() + openPots.stream().mapToDouble(Pot::getAmount).sum();
    }

    public final ArrayList<Card> getCommunityCards() {
        return communityCards;
    }

    public final String toString() {
        double totalAmountInPots = openPots.stream().map(Pot::getAmount).reduce(0.0, (pot1, pot2) -> pot1 + pot2);
        return communityCards.toString() + " Pot: " + totalAmountInPots + " Big: " +
               bigBlindAmount + " Small: " + smallBlindAmount;
    }

    public final void dealFlop() {
        deck.pop();
        IntStream.range(0, 3).forEach(iteration -> communityCards.add(deck.pop()));
    }

    public final void dealTurn() {
        deck.pop();
        communityCards.add(deck.pop());
    }

    public final void dealRiver() {
        deck.pop();
        communityCards.add(deck.pop());
    }

    private void chargeAmount(double amount, List<Player> playersToCharge) {
        playersToCharge.forEach(player -> chargePlayerAmount(player, amount));
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
                amountPaid = payOpenPot(pot, amountPaid);
            }
        }
        if(amountPaid > 0) {
            addAmountToCurrentPot(amountPaid);
        }
    }

    private double payOpenPot(Pot pot, double amount) {
        return amount >= pot.getAmountOwed() ? addFullAmountToPot(pot, amount)
                                             : addRemainingAmountToPot(pot, amount);
    }

    private double addFullAmountToPot(Pot pot, double amountPaid) {
        amountPaid -= pot.getAmountOwed();
        pot.addAmount(pot.getAmountOwed(), 1);
        return amountPaid;
    }

    private double addRemainingAmountToPot(Pot pot, double amountPaid) {
        pot.addAmount(pot.getAmountOwed() - amountPaid, 1);
        return 0;
    }

    private void addAmountToCurrentPot(double amount) {
        Pot currPot = openPots.get(openPots.size() - 1);
        currPot.setAmountOwed(currPot.getAmountOwed() + amount);
        currPot.addAmount(amount, 1);
    }

    private void chargePlayerRemainingBalance(Player player, double fullAmount) {
        double playerStartingBalance = player.getBalance();
        int numPotsPaid = payAllPotsPossible(player);
        int nextPotIndex = numPotsPaid + 1;
        double nextPotAmount = getNextPotAmount(numPotsPaid);
        double amountOwed = fullAmount - playerStartingBalance;
        setupNextPot(numPotsPaid, player, amountOwed);
        if(nextPotIndex + 1 < openPots.size()) {
            Pot currentPot = openPots.get(numPotsPaid);
            openPots.get(nextPotIndex).setAmountOwed(nextPotAmount - currentPot.getAmountOwed());
            currentPot.updatePot();
        }
        IntStream.range(nextPotIndex, openPots.size() - 1).forEach(i -> openPots.get(i).getPlayers().remove(player));
        player.addAmountThisTurn(player.getBalance());
    }

    private int payAllPotsPossible(Player player) {
        int numPotsPaid = 0;
        for(Pot pot : openPots) {
            if(pot.getAmountOwed() == 0 || !player.addAmountThisTurn(pot.getAmountOwed())) {
                break;
            }
            pot.addAmount(pot.getAmountOwed(), 1);
            numPotsPaid++;
        }
        return numPotsPaid;
    }

    private double getNextPotAmount(int currentPotIndex) {
        double nextPotAmount = 0;
        if(currentPotIndex + 1 < openPots.size()) {
            nextPotAmount = openPots.get(currentPotIndex).getAmountOwed();
        }
        return nextPotAmount;
    }

    private void setupNextPot(int currentPotIndex, Player player, double amountOwed) {
        int nextPotIndex = currentPotIndex + 1;
        List<Player> playersInCurrentPot = openPots.get(currentPotIndex).getPlayers();
        Pot nextPot = new Pot(playersInCurrentPot);
        openPots.add(nextPotIndex, nextPot);
        nextPot.removePlayer(player);
        nextPot.setAmountOwed(amountOwed);
        Pot currentPot = openPots.get(currentPotIndex);
        double carryOver = currentPot.setAmountOwed(player.getBalance());
        int numberPlayersPaid = currentPot.getNumPlayersPaid();
        nextPot.addAmount(carryOver, numberPlayersPaid);
        currentPot.addAmount(player.getBalance(), 1);
    }
    
    private void removeBrokePlayers() {
        IntStream.rangeClosed(players.size() - 1, 0).filter(i -> players.get(i).getBalance() == 0).forEach(i ->
                players.remove(i)
        );
    }
    
    private void removeOldPots() {
    	for(int i = openPots.size() - 2; i >= 0; i--) {
            closedPots.add(0, openPots.remove(i));
    	}
    }

    public final void chargeAntes() {
    	openPots.get(0).setAmountOwed(anteAmount);
    	chargeAmount(anteAmount, players);
    	players.forEach(Player::clearAmtThisTurn);
    	removeOldPots();
    }
    
    public final void chargeBigBlind(int bigBlindPos) {
    	double potNum = bigBlindAmount;
    	if(this.openPots.size() > 1) {
    		potNum = bigBlindAmount - openPots.get(0).getAmountOwed();
    	}
    	openPots.get(openPots.size() - 1).setAmountOwed(potNum);
        chargeAmount(bigBlindAmount, Collections.singletonList(players.get(bigBlindPos)));
    	removeBrokePlayers();
    	removeOldPots();
    }
    
    public final void chargeSmallBlind(int smallBlindPos) {
    	openPots.get(0).setAmountOwed(smallBlindAmount);
        chargeAmount(smallBlindAmount, Collections.singletonList(players.get(smallBlindPos)));
    }
    
    public void setupBetRound() {
        players.forEach(Player::noActionThisTurn);
    }
    
    public boolean playersBetting() {
        int numNotFolded = (int) players.stream().filter(player -> !player.hasFolded()).count();
        if (players.size() < 2 || numNotFolded < 2) {
            return false;
        }

        double amountOwed = openPots.stream().mapToDouble(Pot::getAmountOwed).sum();
        int numPlayersWithMoney = (int) players.stream().filter(player -> player.getBalance() > 0).count();
        boolean playerNoAction = players.stream().anyMatch(player -> player.getAmountThisTurn() == -1);

        return numPlayersWithMoney > 0 && (amountOwed != 0 || (numPlayersWithMoney >= 2 && playerNoAction));
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
    		var currentPot = openPots.get(openPots.size()-1);
    		var amountOwed = option.getAmount();
            currentPot.setAmountOwed(amountOwed);
    	case CALL:
    		chargeAmount(option.getAmount(), Collections.singletonList(player));
    		break;
    	default:
    		break;
    	}
    }
    
    public void payWinners() {
        openPots.forEach(this::payPotWinner);
        closedPots.forEach(this::payPotWinner);
    }

    private void payPotWinner(Pot pot) {
        int numLeft = (int) pot.getPlayers().stream().filter(player -> !player.hasFolded()).count();
        if(numLeft == 1) {
            payOnlyRemainingPlayer(pot);
        } else {
            findAndPayPotWinners(pot);
        }
    }

    private void payOnlyRemainingPlayer(Pot pot) {
        double winnings = pot.getAmount();
        DecimalFormat df = new DecimalFormat(".##");
        winnings = Double.parseDouble(df.format(winnings));
        for(int i = 0; i < players.size(); i++) {
            if(!players.get(i).hasFolded() && !players.get(i).isSittingOut()) {
                pot.getPlayers().get(i).updateBalance(winnings);
            }
        }
    }

    private void findAndPayPotWinners(Pot pot) {
        List<HandAnalyzer> analyzers = new ArrayList<>();
        players.forEach(player -> createAnalyzers(player, analyzers));
        List<Integer> potWinnerIndexes = new ArrayList<>();
        IntStream.range(0, pot.getPlayers().size()).filter(i -> analyzers.get(i) != null).forEach(i -> {
            if(potWinnerIndexes.isEmpty()) {
                potWinnerIndexes.add(i);
            } else {
                updatePotWinnerIndexes(analyzers, potWinnerIndexes, i);
            }
        });
        DecimalFormat df = new DecimalFormat(".##");
        double winnings = Double.parseDouble(df.format(pot.getAmount() / potWinnerIndexes.size()));
        potWinnerIndexes.forEach(i -> pot.getPlayers().get(i).updateBalance(winnings));
    }

    private void createAnalyzers(Player player, List<HandAnalyzer> analyzers) {
        if(!player.hasFolded() && !player.isSittingOut()) {
            addAnalyzer(player, analyzers);
        } else {
            analyzers.add(null);
        }
    }

    private void addAnalyzer(Player player, List<HandAnalyzer> analyzers) {
        List<Card> hand = new ArrayList<>();
        hand.addAll(player.getHand());
        hand.addAll(getCommunityCards());
        analyzers.add(isHoldem() ? new HoldEmAnalyzer(hand) : new OmahaAnalyzer(hand));
    }

    private boolean isHoldem() {
        return !(this instanceof OmahaHand);
    }

    private void updatePotWinnerIndexes(List<HandAnalyzer> handAnalyzers, List<Integer> potWinnerIndexes, int index) {
        HandAnalyzer newAnalyzer = handAnalyzers.get(index);
        HandAnalyzer currentWinnerAnalyzer = handAnalyzers.get(potWinnerIndexes.get(0));
        if(HandAnalyzer.HAND_ANALYZER_COMPARATOR.compare(newAnalyzer, currentWinnerAnalyzer) > 0) {
            potWinnerIndexes.clear();
            potWinnerIndexes.add(index);
        } else if(HandAnalyzer.HAND_ANALYZER_COMPARATOR.compare(newAnalyzer, currentWinnerAnalyzer) == 0) {
            potWinnerIndexes.add(index);
        }
    }
    
    public List<Option> generateOptions(Player player) {
    	if(player.getBalance() > 0) {
    	    return createOptionsForNonBrokePlayer(player);
    	}
        return new ArrayList<>();
    }

    private List<Option> createOptionsForNonBrokePlayer(Player player) {
        List<Option> options = new ArrayList<>();
        double amountOwed = openPots.stream().mapToDouble(Pot::getAmountOwed).sum();
        if(player.getAmountThisTurn() != -1) {
            amountOwed -= player.getAmountThisTurn();
        }
        if(amountOwed == 0) {
            createCheckAndBetOptions(options, player);
        } else {
            createFoldCallRaiseOptions(options, player, amountOwed);
        }
        if(amountOwed + bigBlindAmount <= player.getBalance()) {
            options.add(new Option(Option.OptionType.ALLIN, player.getBalance()));
        }
        return options;
    }

    private void createCheckAndBetOptions(List<Option> options, Player player) {
        options.add(new Option(Option.OptionType.CHECK, 0));
        if(bigBlindAmount < player.getBalance()) {
            options.add(new Option(Option.OptionType.BET, bigBlindAmount));
        }
    }

    private void createFoldCallRaiseOptions(List<Option> options, Player player, double amountOwed) {
        options.add(new Option(Option.OptionType.FOLD, 0));
        if(amountOwed < player.getBalance()) {
            options.add(new Option(Option.OptionType.CALL, amountOwed));
        }
        if(amountOwed + bigBlindAmount < player.getBalance()) {
            int count = (int) players.stream()
                    .filter(p -> p.getBalance() > 0 && !p.hasFolded() && !p.isSittingOut()).count();
            if(count > 1) {
                options.add(new Option(Option.OptionType.RAISE, bigBlindAmount + amountOwed));
            }
        }
    }
}
