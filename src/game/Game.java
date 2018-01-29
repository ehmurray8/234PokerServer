/**
 * 
 */
package game;
import model.player.Player;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import game.Rules.GameType;
import model.hand.representation.*;

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
	ArrayList<Player> playersInHand;
	
	public Game(ArrayList<Player> players, Rules rules) {
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
		} while (this.players[this.dealerNum] != null);
	}
	
	public void updateSmallBlindNum() {
		int num = 1;
		do {
            this.smallBlindNum = (this.dealerNum + num) % this.players.length;
            num++;
		} while (this.players[this.smallBlindNum] != null);
	}
	
	public void updateBigBlindNum() {
		int num = 1;
		do {
			this.bigBlindNum = (this.smallBlindNum + num) % this.players.length;
		} while(this.players[this.bigBlindNum] != null);
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
        	return GameType.HOLDEM;
        }
	}
	
	public void removeBrokePlayers() {
		for(int i = 0; i < this.players.length; i++) {
			if(this.players[i] != null || this.players[i].getBalance() == 0) {
				this.removePlayer(this.players[i]);
			}
		}
	}
	
	public void startHand() {
        GameType currGameType;
        Hand currHand;
		while(this.players.length > 0) {
			for(Player p : players) {
				if(p != null && !p.isSittingOut()) {
					this.playersInHand.add(p);
					p.newHand();
				}
			}
	
			currGameType = this.rules.getGameType();
			if(currGameType == GameType.MIXED) {
				currGameType = askDealerForGameType();
			}
			
			switch(currGameType){
			case HOLDEM:
				currHand = new HoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), Arrays.asList(players));
				break;
			case PINEAPPLE:
				currHand = new PineappleHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), Arrays.asList(players));
				break;
			case OMAHA:
				currHand = new OmahaHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), Arrays.asList(players));
				break;
			default:
				currHand = new HoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), Arrays.asList(players));
			}
			
			currHand.dealInitialHand();
			if(this.rules.getAnte() > 0) {
                currHand.chargeAntes();
			}
			currHand.chargeSmallBlind(smallBlindNum);
			currHand.chargeBigBlind(bigBlindNum);
			
			
			//TODO: Need to add betting round
			currHand.dealFlop();
			//TODO: Need to add betting round
			currHand.dealTurn();
			//TODO: Need to add betting round
			currHand.dealRiver();
			//TODO: Need to add betting round
			
			//Need to check winner of every pot
			
			//Distribute winnings
			
			removeBrokePlayers();
			
			incrementDealerNum();
			updateSmallBlindNum();
			updateBigBlindNum();
		}
	}
	
}