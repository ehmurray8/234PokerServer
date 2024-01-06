package model.hand.representation;

import java.util.ArrayList;
import java.util.List;
import model.player.Player;

public class Pot {

  private double amount;
  private final List<Player> players;
  private double amountOwed;
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

  public double getAmount() {
    return amount;
  }

  void addAmount(double amount, int numPlayers) {
    this.amount += amount;
    numPlayersPaid += numPlayers;
    if (numPlayersPaid == players.size()) {
      amountOwed = 0;
      numPlayersPaid = 0;
    }
  }

  void removeAmount(double amount) {
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

  public double getAmountOwed() {
    return amountOwed;
  }

  double setAmountOwed(double amount) {
    double amountOver;
    if (amountOwed < amount) {
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
    players.forEach(player -> playerStr.append(" ").append(player.getName()).append(": ")
        .append(player.getBalance()));
    return "Amount in pot: " + amount + ", Amount owed: " + amountOwed + ", Number of players paid: "
        + numPlayersPaid + ", Players in pot: " + playerStr;
  }
}