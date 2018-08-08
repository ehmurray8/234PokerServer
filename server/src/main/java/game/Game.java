package game;
import model.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import game.Rules.GameType;
import model.hand.representation.*;
import model.option.Option;

/** */
public class Game {

	public static class TableFullException extends Exception { }

	public static class TableEmptyException extends Exception { }

	/**
	 * List of the players currently at the table. The list represents the players in order with the
	 * first player in the array sitting in seat 1 (left of the dealer)."
	 */
	private Player[] players;
	private Rules rules;
	private int numPlayers;
	private int dealerNum;
	private int currentAction;
	private ArrayList<Player> playersInHand;
	private boolean debug = false;
	private TestClient testClient;

	/**
	 * TODO Map the players array to a list of clients that allow for communicating between
	 */
	public Game(List<Player> players, Rules rules, boolean debug, TestClient testClient) {
		this(players, rules);
		this.debug = debug;
		this.testClient = testClient;
	}

	public Game(List<Player> players, Rules rules) {
		this.players = new Player[rules.getMaxCapacity()];
		for(int i = 0; i < players.size(); i++) {
			this.players[i] = players.get(i);
		}
		this.rules = rules;
		numPlayers = players.size();
		dealerNum = initDealerNum();
		playersInHand = new ArrayList<>();
		currentAction = smallBlindNum();
	}

	public int getDealerNum() {
		return dealerNum;
	}

	/**
	 * Return the position of the starting dealer.
	 * 
	 * TODO: Implement this method properly
	 * 
	 * @return position of initial dealer
	 */
	public int initDealerNum() {
	    if(debug) {
	    	return 0;
		}
		return 0;
	}
	
	public void incrementDealerNum() {
		do {
            dealerNum = (dealerNum + 1) % players.length;
		} while (players[dealerNum] == null || players[dealerNum].isSittingOut());
	}
	
	public int findStartingLocation() {
		int startLoc = -1;
		int num = 1;
		do {
			Player player = players[(dealerNum + num) % players.length];
			if(player != null) {
                startLoc = playersInHand.indexOf(player);
			}
			num ++;
		} while (startLoc == -1);
		return startLoc;
	}
	
	public int smallBlindNum() {
		int num = 1;
		int smallBlindNum;
		do {
            smallBlindNum = (dealerNum + num) % players.length;
            num++;
		} while (players[smallBlindNum] == null || players[smallBlindNum].isSittingOut());
		return smallBlindNum;
	}
	
	public int bigBlindNum() {
		int num = 1;
		int bigBlindNum;
		do {
			bigBlindNum = (smallBlindNum() + num) % players.length;
			num++;
		} while(players[bigBlindNum] == null || players[dealerNum].isSittingOut());
		return bigBlindNum;
	}
	
	public void incrementCurrentAction() {
        currentAction = (currentAction + 1) % playersInHand.size();
	}

	public int getCurrentAction() {
		return currentAction;
	}

	/**
	 * Adds a player to an open seat.
	 * 
	 * @param player to add
	 */
	public void addPlayer(Player player) throws TableFullException {
	    if(tableFull()) { throw new TableFullException(); }

		for(int i = 0; i < players.length; i++) {
			if(players[i] == null) {
				players[i] = player;
				numPlayers++;
				break;
			}
		}
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public Player[] getPlayers() {
		return players;
	}
	
	public void removePlayer(Player player) throws TableEmptyException {
	    if(tableEmpty()) { throw new TableEmptyException(); }

		for(int i = 0; i < players.length; i++) {
			if(players[i].equals(player)) {
				players[i] = null;
			}
		}
		this.numPlayers--;
		if(this.rules.isTourney()) {
			if(numPlayers < rules.getPrizes().length) {
				System.out.println("Player " + player.toString() + " wins $" + rules.getPrizes()[numPlayers]);
				// Need to figure out how to return money to the players account
			}
		} else {
			System.out.println("Player " + player.toString() + " is exiting the game with " + player.getBalance());
            // Need to figure out how to return money to the players account
		}
	}
	
	/**
	 * Checks whether or not the table is currently full.
	 * 
	 * @return whether or not table is full
	 */
	public boolean tableFull() {
		return numPlayers == rules.getMaxCapacity();
	}
	
	/**
	 * Ask the player currently dealing what game they would like to deal.
	 * 
	 * TODO: Implement this method correctly 
	 * 
	 * @return game type to deal
	 */
	public GameType askDealerForGameType() {
		if(debug) {
			return testClient.getDesiredGameType();
		}
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4);
        switch(randomNum) {
        case 1:
        	return GameType.HOLDEM;
        case 2:
        	return GameType.PINEAPPLE;
        case 3:
        	return GameType.OMAHA;
        default:
        	return GameType.HOLDEM;
        }
	}
	
	public void removeBrokePlayers() {
		for (Player player : players) {
			if (player != null && player.getBalance() == 0) {
				player.setSittingOut(true);
			}
		}
	}
	
	public void bettingRound(Hand currHand, boolean resetBetting) {
        currentAction = findStartingLocation();
        if(resetBetting) {
            currHand.setupBetRound();
        }
        List<Option> currOptions;
        while(currHand.playersBetting()) {
            Player player = playersInHand.get(currentAction);
            currOptions = currHand.generateOptions(player);
            Option option = askPlayerForOption(currOptions, player);
            currHand.executeOption(player, option);
            incrementCurrentAction();
            System.out.println(player.getName() + ": " + option.toString());
        }
	}
	
	public Option askPlayerForOption(List<Option> options, Player player) {
		if (debug) {
			return testClient.getDesiredOption();
		}
        int randomNum = ThreadLocalRandom.current().nextInt(0, options.size());
        return options.get(randomNum);
	}
	
	private boolean tableEmpty() {
		for(Player p : players) {
			if(p != null) {
				return false;
			}
		}
		return true;
	}
	
	private boolean stillBetting() {
		int count = 0;
		for(Player p : playersInHand) {
			if(!p.hasFolded()) {
				count ++;
			}
		}
		return count > 1;
	}
	
	/**
	 * Main loop of an individual game, the game loops infinitely as long as there
	 * are players at the table. This loop uses the hand class to keep track of the
	 * game state, and uses Game helper methods to communicate with the client.
	 * 
	 * TODO: Handle dead and inactive game
	 * TODO: Handle showing cards
	 */
	public void startGame() {
        GameType currGameType;
        Hand currHand;
		while(!tableEmpty()) {
			for(Player p : players) {
				if(p != null && !p.isSittingOut()) {
					playersInHand.add(p);
					p.resetStatus();
				}
			}
			
			if(this.playersInHand.size() <= 1) {
				// Inactive game
				System.out.println("Inactive game.");
				break;
			}
	
			currGameType = rules.getGameType();
			if(currGameType == GameType.MIXED) {
				currGameType = askDealerForGameType();
			}
			
			boolean isHoldemAnalyzer = true;
			switch(currGameType){
			case HOLDEM:
				currHand = new TexasHoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				break;
			case PINEAPPLE:
				//currHand = new PineappleHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				currHand = new TexasHoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				break;
			case OMAHA:
				currHand = new OmahaHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				isHoldemAnalyzer = false;
				break;
			default:
				currHand = new TexasHoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
			}
			
			System.out.println(currHand.toString());
			
			currHand.dealInitialHand();
			if(this.rules.getAnte() > 0) {
                currHand.chargeAntes();
			}
			currHand.chargeSmallBlind(smallBlindNum());
			currHand.chargeBigBlind(bigBlindNum());
			currentAction = playersInHand.indexOf(players[bigBlindNum()]);
			
			for(Player p : players) {
				System.out.println(p.toString());
			}
			
            bettingRound(currHand, false);
            if(stillBetting()) {
                currHand.dealFlop();
            }
			
			for(Player p : players) {
				System.out.println(p.toString());
			}
			System.out.println("Flop: " + currHand.getCommunityCards().toString());
			System.out.println("Pot: " + currHand.getOpenPots().get(0).getAmount());
			
			bettingRound(currHand, true);
			if(stillBetting()) {
                currHand.dealTurn();
			}
			
			System.out.println("Turn: " + currHand.getCommunityCards().toString());
			System.out.println("Pot: " + currHand.getOpenPots().get(0).getAmount());

			bettingRound(currHand, true);
			if(stillBetting()) {
                currHand.dealRiver();
			}

			System.out.println("River: " + currHand.getCommunityCards().toString());
			System.out.println("Pot: " + currHand.getOpenPots().get(0).getAmount());

			bettingRound(currHand, true);
			
			System.out.println("Pot: " + currHand.getOpenPots().get(0).getAmount());
			currHand.payWinners(isHoldemAnalyzer);
			
			// Show cards
			for(Player p : players) {
				System.out.println(p.toString());
				p.resetStatus();
			}
			
			removeBrokePlayers();
			incrementDealerNum();
			playersInHand.clear();
		}
		// Dead game
		System.out.println("Dead Game");
	}
}