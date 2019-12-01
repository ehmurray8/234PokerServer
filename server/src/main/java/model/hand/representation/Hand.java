package model.hand.representation;

import java.util.*;
import java.util.stream.IntStream;

import game.Rules;
import model.card.Card;
import model.card.Deck;
import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.HoldEmAnalyzer;
import model.option.Option;
import model.player.Player;


public abstract class Hand {

    private final Deck deck;
    private final Rules rules;
    private final int numberOfCards;
    private final List<Card> communityCards;
    private final List<Player> players;
    private int lastRaiseAmount = 0;
    private final Set<Card> winningCards;
    private int winnings = 0;
    private List<Player> winningPlayers;
    private String winningHandString = "";

    /**	Pots that are currently opened, main pot is at index 0. */
    private final List<Pot> openPots;
    
    /** All openPots that have been opened during the hand. */
    private final List<Pot> closedPots;

    protected Hand(final Rules rules, final List<Player> players, final int numberOfCards) {
        this.rules = rules;
        this.numberOfCards = numberOfCards;
        this.players = players;
        deck = new Deck();
        communityCards = new ArrayList<>();
        openPots = new ArrayList<>();
        closedPots = new ArrayList<>();
        winningCards = new HashSet<>();
        openPots.add(new Pot(players));
    }

    public static Hand createHand(final Rules rules, final List<Player> players, final Rules.GameType handGameType) {
        if (players == null)  {
            throw new NullPointerException("Cannot create a Hand with a null list of players");
        }
        if (players.size() < 2) {
            throw new IllegalArgumentException("Cannot create a Hand with less than 2 players.");
        }
        switch (handGameType) {
            case HOLDEM: return new TexasHoldEmHand(rules, players, 2);
            case PINEAPPLE: return new TexasHoldEmHand(rules, players, 3);
            case OMAHA: return new OmahaHand(rules, players, 4);
            case MIXED: throw new IllegalArgumentException("Cannot create a Hand with a GameType of MIXED.");
        }
        return null;
    }

    public void dealInitialHand() {
        IntStream.range(0, numberOfCards).forEach(iteration ->
            players.forEach(player -> player.addCard(deck.dealCard()))
        );
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public int getBetStepSize() {
        return rules.getMinimumChipAmount();
    }

    List<Pot> getOpenPots() {
    	return openPots;
    }
    
    List<Pot> getClosedPots() {
    	return closedPots;
    }

    public Set<Card> getWinningCards() {
        return winningCards;
    }

    public int getTotalAmountInPots() {
        return closedPots.stream().mapToInt(Pot::getAmount).sum() + openPots.stream().mapToInt(Pot::getAmount).sum();
    }

    public int getWinnings() {
        return winnings;
    }

    public List<Player> getWinningPlayers() {
        if (winningPlayers == null) {
            return new ArrayList<>();
        }
        return winningPlayers;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public final void dealFlop() {
        deck.dealCard();
        IntStream.range(0, 3).forEach(iteration -> getCommunityCards().add(deck.dealCard()));
    }

    public final void dealTurn() {
        deck.dealCard();
        getCommunityCards().add(deck.dealCard());
    }

    public final void dealRiver() {
        deck.dealCard();
        getCommunityCards().add(deck.dealCard());
    }

    private void chargeAmount(int amount, List<Player> playersToCharge) {
        playersToCharge.forEach(player -> chargePlayerAmount(player, amount));
    }

    private void chargePlayerAmount(Player player, int amount) {
        if(player.addAmountThisTurn(amount)) {
            playerPaidFullAmount(amount);
        } else {
            chargePlayerRemainingBalance(player, amount);
        }
    }

    private void playerPaidFullAmount(int amountPaid) {
        for(Pot pot : openPots) {
            if(pot.getAmountOwed() != 0) {
                amountPaid = payOpenPot(pot, amountPaid);
            }
        }
        if(amountPaid > 0) {
            addAmountToCurrentPot(amountPaid);
        }
    }

    private int payOpenPot(Pot pot, int amount) {
        return amount >= pot.getAmountOwed() ? addFullAmountToPot(pot, amount)
                                             : addRemainingAmountToPot(pot, amount);
    }

    private int addFullAmountToPot(Pot pot, int amountPaid) {
        amountPaid -= pot.getAmountOwed();
        pot.addAmount(pot.getAmountOwed(), 1);
        return amountPaid;
    }

    private int addRemainingAmountToPot(Pot pot, int amountPaid) {
        pot.addAmount(amountPaid, 1);
        return 0;
    }

    private void addAmountToCurrentPot(int amount) {
        Pot currPot = openPots.get(openPots.size() - 1);
        currPot.setAmountOwed(currPot.getAmountOwed() + amount);
        currPot.addAmount(amount, 1);
    }

    private void chargePlayerRemainingBalance(Player player, int fullAmount) {
        int playerStartingBalance = player.getBalance();
        int numPotsPaid = payAllPotsPossible(player);
        int nextPotIndex = numPotsPaid + 1;
        int nextPotAmount = getNextPotAmount(numPotsPaid);
        int amountOwed = fullAmount - playerStartingBalance;
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

    private int getNextPotAmount(int currentPotIndex) {
        int nextPotAmount = 0;
        if(currentPotIndex + 1 < openPots.size()) {
            nextPotAmount = openPots.get(currentPotIndex).getAmountOwed();
        }
        return nextPotAmount;
    }

    private void setupNextPot(int currentPotIndex, Player player, int amountOwed) {
        int nextPotIndex = currentPotIndex + 1;
        List<Player> playersInCurrentPot = openPots.get(currentPotIndex).getPlayers();
        Pot nextPot = new Pot(playersInCurrentPot);
        openPots.add(nextPotIndex, nextPot);
        nextPot.removePlayer(player);
        nextPot.setAmountOwed(amountOwed);
        Pot currentPot = openPots.get(currentPotIndex);
        int carryOver = currentPot.setAmountOwed(player.getBalance());
        int numberPlayersPaid = currentPot.getNumPlayersPaid();
        nextPot.addAmount(carryOver, numberPlayersPaid);
        currentPot.removeAmount(carryOver * numberPlayersPaid);
        currentPot.addAmount(player.getBalance(), 1);
    }

    private void removeBrokePlayers() {
        IntStream.rangeClosed(players.size() - 1, 0).filter(i -> players.get(i).getBalance() == 0).forEach(players::remove);
    }
    
    private void removeOldPots() {
    	for(int i = openPots.size() - 2; i >= 0; i--) {
            closedPots.add(0, openPots.remove(i));
    	}
    }

    public final void chargeAntes() {
    	openPots.get(0).setAmountOwed(rules.getAnte());
    	chargeAmount(rules.getAnte(), players);
    	players.forEach(Player::clearAmtThisTurn);
    	removeOldPots();
    }
    
    public final void chargeBigBlind(int bigBlindPos) {
    	int potNum = rules.getBigBlind();
    	lastRaiseAmount = rules.getBigBlind();
    	if(this.openPots.size() > 1) {
    		potNum = rules.getBigBlind() - openPots.get(0).getAmountOwed();
    	}
    	openPots.get(openPots.size() - 1).setAmountOwed(potNum);
        chargeAmount(rules.getBigBlind(), Collections.singletonList(players.get(bigBlindPos)));
    	removeBrokePlayers();
    	removeOldPots();
    }
    
    public final void chargeSmallBlind(int smallBlindPos) {
    	openPots.get(0).setAmountOwed(rules.getSmallBlind());
        chargeAmount(rules.getSmallBlind(), Collections.singletonList(players.get(smallBlindPos)));
    }
    
    public void setupBetRound() {
        players.forEach(Player::noActionThisTurn);
        lastRaiseAmount = 0;
    }

    public boolean onlyOnePlayerLeftInHand() {
        var numNotFolded = (int) players.stream().filter(player -> !player.hasFolded()).count();
        return players.size() < 2 || numNotFolded < 2;
    }

    
    public boolean playersBetting() {
        if (onlyOnePlayerLeftInHand()) {
            return false;
        }

        int amountOwed = openPots.stream().mapToInt(Pot::getAmountOwed).sum();
        int numPlayersWithMoney = (int) players.stream().filter(player -> player.getBalance() > 0).count();
        boolean playerNoAction = players.stream().anyMatch(player -> player.getAmountThisTurn() == -1);

        return numPlayersWithMoney > 0 && (amountOwed != 0 || (numPlayersWithMoney >= 2 && playerNoAction));
    }
    
    public void executeOption(Player player, Option option) {
    	Option.OptionType type = option.getType();
        var currentPot = openPots.get(openPots.size()-1);
    	switch(type) {
    	case FOLD:
    		player.fold();
    		players.remove(player);
    		break;
        case CHECK:
            player.check();
            break;
    	case RAISE:
        case ALLIN:
        case BET:
            var startingAmountThisTurn = player.setupRaise();
            if(startingAmountThisTurn > 0) {
                currentPot.removeAmount(startingAmountThisTurn);
                lastRaiseAmount = option.getAmount() - startingAmountThisTurn;
            } else {
                lastRaiseAmount = option.getAmount();
            }
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
        winnings = pot.getAmount();
        for (Player player : pot.getPlayers()) {
            if (!player.hasFolded() && !player.isSittingOut()) {
                player.updateBalance(winnings);
                winningPlayers = new ArrayList<>(Collections.singletonList(player));
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
            }
            updatePotWinnerIndexes(analyzers, potWinnerIndexes, i);
        });
        winnings = pot.getAmount() / potWinnerIndexes.size();
        winningPlayers = new ArrayList<>();
        potWinnerIndexes.forEach(i -> {
            var player = pot.getPlayers().get(i);
            player.updateBalance(winnings);
            winningPlayers.add(player);
        });
        System.out.println(winningHandString);
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
        analyzers.add(createAnalyzer(hand));
    }

    HandAnalyzer createAnalyzer(List<Card> cards) {
        return new HoldEmAnalyzer(cards);
    }

    private void updatePotWinnerIndexes(List<HandAnalyzer> handAnalyzers, List<Integer> potWinnerIndexes, int index) {
        HandAnalyzer newAnalyzer = handAnalyzers.get(index);
        HandAnalyzer currentWinnerAnalyzer = handAnalyzers.get(potWinnerIndexes.get(0));
        if(HandAnalyzer.HAND_ANALYZER_COMPARATOR.compare(newAnalyzer, currentWinnerAnalyzer) > 0) {
            winningCards.clear();
            winningCards.addAll(newAnalyzer.getBestHand());
            winningHandString = newAnalyzer.getTopRank().toString();
            potWinnerIndexes.clear();
            potWinnerIndexes.add(index);
        } else if(HandAnalyzer.HAND_ANALYZER_COMPARATOR.compare(newAnalyzer, currentWinnerAnalyzer) == 0) {
            winningHandString = newAnalyzer.getTopRank().toString();
            winningCards.addAll(newAnalyzer.getBestHand());
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
        int amountOwed = openPots.stream().mapToInt(Pot::getAmountOwed).sum();
        if(player.getAmountThisTurn() != -1) {
            amountOwed -= player.getAmountThisTurn();
        }
        if(amountOwed == 0) {
            createCheckAndBetOptions(options, player);
        } else {
            createFoldCallRaiseOptions(options, player, amountOwed);
        }
        if(amountOwed + minBetAmount() <= player.getBalance()) {
            var amountThisTurn = player.getAmountThisTurn();
            if(amountThisTurn < 0) {
                amountThisTurn = 0;
            }
            options.add(new Option(Option.OptionType.ALLIN, player.getBalance() + amountThisTurn));
        }
        return options;
    }

    private void createCheckAndBetOptions(List<Option> options, Player player) {
        options.add(new Option(Option.OptionType.CHECK, 0));
        if(minBetAmount() < player.getBalance()) {
            options.add(new Option(Option.OptionType.BET, minBetAmount()));
        }
    }

    private void createFoldCallRaiseOptions(List<Option> options, Player player, int amountOwed) {
        options.add(new Option(Option.OptionType.FOLD, 0));
        if(amountOwed != 0) {
            if (amountOwed <= player.getBalance()) {
                options.add(new Option(Option.OptionType.CALL, amountOwed));
            } else {
                options.add(new Option(Option.OptionType.CALL, player.getBalance()));
            }
        }
        if(amountOwed + minBetAmount() < player.getBalance()) {
            int count = (int) players.stream()
                    .filter(p -> p.getBalance() > 0 && !p.hasFolded() && !p.isSittingOut()).count();
            if(count > 1) {
                var amountThisTurn = player.getAmountThisTurn();
                if(amountThisTurn < 0) {
                    amountThisTurn = 0;
                }
                var raiseAmount = amountThisTurn + amountOwed + lastRaiseAmount;
                options.add(new Option(Option.OptionType.RAISE, raiseAmount));
            }
        }
    }

    private int minBetAmount() {
        return rules.getBigBlind() > 0 ? rules.getBigBlind() : rules.getAnte();
    }
}
