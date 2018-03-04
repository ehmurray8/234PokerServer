/**
 * 
 */
package game;
import model.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import game.Rules.GameType;
import model.hand.representation.*;
import model.option.Option;

/**
 * @author Emmet
 *
 */
public class Game {

	/**
	 * List of the players currently at the table. The list represents
	 * the players in order with the first player in the array sitting
	 * in seat 1 (left of the dealer)."
	 */
	Player[] players;
	Rules rules;
	int numPlayers;
	int dealerNum;
	int smallBlindNum;
	int bigBlindNum;
	int currentAction;
	ArrayList<Player> playersInHand;
	
	/**
	 * TODO Map the players array to a list of clients that allow for communicating between
	 */
	public Game(List<Player> players, Rules rules) {
		this.players = new Player[rules.getMaxCapacity()];
		for(int i = 0; i < players.size(); i++) {
			this.players[i] = players.get(i);
		}
		this.rules = rules;
		this.numPlayers = players.size();
		this.dealerNum = initDealerNum();
		updateSmallBlindNum();
		updateBigBlindNum();
		this.playersInHand = new ArrayList<Player>();
		this.currentAction = 0;
	}
	
	/**
	 * Return the position of the starting dealer.
	 * 
	 * TODO: Implement this method properly
	 * 
	 * @return position of initial dealer
	 */
	public int initDealerNum() {
		return 0;
	}
	
	public void incrementDealerNum() {
		do {
            this.dealerNum = (this.dealerNum + 1) % this.players.length;
		} while (this.players[this.dealerNum] == null || this.players[this.dealerNum].isSittingOut());
	}
	
	public int findStartingLocation() {
		int startLoc = -1;
		int num = 1;
		do {
			Player player = players[(this.dealerNum + num) % this.players.length];
			if(player != null) {
                startLoc = playersInHand.indexOf(player);
			}
		} while (startLoc == -1);
		return startLoc;
	}
	
	public void updateSmallBlindNum() {
		int num = 1;
		do {
            this.smallBlindNum = (this.dealerNum + num) % this.players.length;
            num++;
		} while (this.players[this.smallBlindNum] == null || this.players[this.smallBlindNum].isSittingOut());
	}
	
	public void updateBigBlindNum() {
		int num = 1;
		do {
			this.bigBlindNum = (this.smallBlindNum + num) % this.players.length;
			num++;
		} while(this.players[this.bigBlindNum] == null || this.players[this.dealerNum].isSittingOut());
	}
	
	public void updateCurrentAction() {
        this.currentAction = (this.currentAction + 1) % playersInHand.size();
	}
	
	/**
	 * Adds a player to an open seat, caller must check to make sure their
	 * is room at the table for the player to sit.
	 * 
	 * @param player to add
	 */
	public void addPlayer(Player player) {
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i] == null) {
				this.players[i] = player;
				this.numPlayers++;
				break;
			}
		}
	}
	
	public void removePlayer(Player player) {
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i].equals(player)) {
				this.players[i] = null;
			}
		}
		this.numPlayers--;
		if(this.rules.isTourney()) {
			if(this.numPlayers < this.rules.getPrizes().length) {
				System.out.println("Player " + player.toString() + " wins $" + this.rules.getPrizes()[this.numPlayers]);
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
		return this.numPlayers == this.rules.getMaxCapacity();
	}
	
	/**
	 * Ask the player currently dealing what game they would like to deal.
	 * 
	 * TODO: Implement this method correctly 
	 * 
	 * @return game type to deal
	 */
	public GameType askDealerForGameType() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 4);
        switch(randomNum) {
        case 1:
        	return GameType.HOLDEM;
        case 2:
        	return GameType.PINEAPPLE;
        case 3:
        	return GameType.OMAHA;
        default:
        	return GameType.HOLDEM; }
	}
	
	public void removeBrokePlayers() {
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i] != null && this.players[i].getBalance() == 0) {
				this.removePlayer(this.players[i]);
			}
		}
	}
	
	public void bettingRound(Hand currHand, boolean resetBetting) {
        this.currentAction = findStartingLocation();
        if(resetBetting) {
            currHand.setupBetRound();
        }
        List<Option> currOptions;
        while(currHand.playersBetting()) {
            Player player = playersInHand.get(this.currentAction);
            currOptions = currHand.generateOptions(player);
            Option option = askPlayerForOption(currOptions, player);
            currHand.executeOption(player, option);
            updateCurrentAction();
            System.out.println(player.getName() + ": " + option.toString());
        }
	}
	
	public Option askPlayerForOption(List<Option> options, Player player) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, options.size());
        randomNum = 1;
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
		if(count > 1) {
			return true;
		}
		return false;
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
        int count = 0;
		while(!tableEmpty() && count < 5) {
			for(Player p : players) {
				if(p != null && !p.isSittingOut()) {
					this.playersInHand.add(p);
					p.newHand();
				}
			}
			
			if(this.playersInHand.size() <= 1) {
				// Inactive game
				System.out.println("Inactive game.");
				break;
			}
	
			currGameType = this.rules.getGameType();
			if(currGameType == GameType.MIXED) {
				currGameType = askDealerForGameType();
			}
			
			boolean isHoldemAnalyzer = true;
			switch(currGameType){
			case HOLDEM:
				currHand = new HoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				break;
			case PINEAPPLE:
				//currHand = new PineappleHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				currHand = new HoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				break;
			case OMAHA:
				currHand = new OmahaHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
				isHoldemAnalyzer = false;
				break;
			default:
				currHand = new HoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand);
			}
			
			System.out.println(currHand.toString());
			
			currHand.dealInitialHand();
			if(this.rules.getAnte() > 0) {
                currHand.chargeAntes();
			}
			currHand.chargeSmallBlind(smallBlindNum);
			currHand.chargeBigBlind(bigBlindNum);
			this.currentAction = playersInHand.indexOf(players[bigBlindNum]);
			
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
			
			bettingRound(currHand, true);
			if(stillBetting()) {
                currHand.dealTurn();
			}
			
			System.out.println("Turn: " + currHand.getCommunityCards().toString());

			bettingRound(currHand, true);
			if(stillBetting()) {
                currHand.dealRiver();
			}

			System.out.println("River: " + currHand.getCommunityCards().toString());

			bettingRound(currHand, true);
			
			currHand.payWinners(isHoldemAnalyzer);
			
			// Show cards
			
			for(Player p : players) {
				System.out.println(p.toString());
				p.resetStatus();
			}
			
			removeBrokePlayers();
			
			incrementDealerNum();
			updateSmallBlindNum();
			updateBigBlindNum();
			this.playersInHand.clear();
			count++;
			System.out.println("Hand number: " + count);
		}
		
		// Dead game
		System.out.println("Dead Game");
	}
	
}