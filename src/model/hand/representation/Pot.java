package model.hand.representation;

import java.util.ArrayList;
import java.util.List;

import model.player.Player;

public class Pot {

	private double amount;
	
	private List<Player> players;
	
	private double amountOwed;
	
	private int numPlayersPaid;
	
	public Pot(List<Player> players) {
		this.players = new ArrayList<Player>();
		this.players.addAll(players);
		this.amount = 0;
		this.amountOwed = 0;
		this.numPlayersPaid = 0;
	}

	public double getAmount() {
		return amount;
	}

	public void addAmount(double amount, int numPlayers) {
		this.amount += amount;
		this.numPlayersPaid += numPlayers;
		if(numPlayersPaid == players.size()) {
			this.amountOwed = 0;
			this.numPlayersPaid = 0;
		}
	}
	
	public int getNumPlayersPaid() {
		return this.numPlayersPaid;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void removePlayer(Player p) {
		this.players.remove(p);
	}
	
	public double getAmountOwed() {
		return this.amountOwed;
	}
	
	public double setAmountOwed(double amount) {
		double amountOver = 0;
		if(this.amountOwed < amount) {
			amountOver = this.numPlayersPaid * (amount - this.amountOwed);
			//this.amount -= amountOver;
			this.amountOwed = amount;
		} else {
			amountOver = this.numPlayersPaid * (this.amountOwed - amount);
		}
		this.amountOwed = amount;
		return amountOver;
	}
	
	public void updatePot() {
		this.amount = this.amountOwed * this.numPlayersPaid;
	}
	
	 @Override
	public String toString() {
		String playerStr = "";
		for(Player p : players) {
			playerStr += " " + p.getName() + ": " + p.getBalance();
		}
		return "Amount in pot: " + this.amount + ", Amount owed: " + this.amountOwed + ", Number of players paid: " + this.numPlayersPaid + 
				", Players in pot: " + playerStr;
	}
}