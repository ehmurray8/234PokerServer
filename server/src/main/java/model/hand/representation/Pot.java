package model.hand.representation;

import java.util.ArrayList;
import java.util.List;

import model.player.Player;

public class Pot {

	private int amount;
	private final List<Player> players;
	private int amountOwed;
	private int numPlayersPaid;
	
	Pot(List<Player> players) {
	    this();
		this.players.addAll(players);
	}

	private Pot() {
		this.players = new ArrayList<>();
		this.amount = 0;
		this.amountOwed = 0;
		this.numPlayersPaid = 0;
	}

	public int getAmount() {
		return amount;
	}

	void addAmount(int amount, int numPlayers) {
		this.amount += amount;
		numPlayersPaid += numPlayers;
		if(numPlayersPaid == players.size()) {
			amountOwed = 0;
			numPlayersPaid = 0;
		}
	}

	void removeAmount(int amount) {
		this.amount -= amount;
	}
	
	public int getNumPlayersPaid() {
		return numPlayersPaid;
	}

	List<Player> getPlayers() {
		return players;
	}

	void removePlayer(Player p) {
		players.remove(p);
	}
	
	public int getAmountOwed() {
		return amountOwed;
	}
	
	int setAmountOwed(int amount) {
		int amountOver;
		if(amountOwed < amount) {
			amountOver = numPlayersPaid * (amount - amountOwed);
			this.numPlayersPaid = 0;
		} else {
			amountOver = numPlayersPaid * (amountOwed - amount);
		}
		amountOwed = amount;
		return amountOver;
	}
	
	void updatePot() {
		amount = amountOwed * numPlayersPaid;
	}

	 @Override
	public String toString() {
		StringBuilder playerStr = new StringBuilder();
		players.forEach(player -> playerStr.append(" ").append(player.getName()).append(": ").append(player.getBalance()));
		return "Amount in pot: " + amount + ", Amount owed: " + amountOwed + ", Number of players paid: " +
				numPlayersPaid + ", Players in pot: " + playerStr;
	}
}