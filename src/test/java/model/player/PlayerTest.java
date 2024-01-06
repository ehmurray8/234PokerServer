package model.player;


import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

  @Test
  public void testAddCard() {
    Player p1 = new Player(0, "");
    ArrayList<Card> cards = new ArrayList<>();
    cards.add(new Card(Rank.ACE, Suit.CLUBS));
    cards.add(new Card(Rank.TWO, Suit.DIAMONDS));
    cards.add(new Card(Rank.EIGHT, Suit.HEARTS));
    cards.add(new Card(Rank.THREE, Suit.SPADES));

    for (Card card : cards) {
      p1.addCard(card);
    }

    cards.sort(new Card.CardComparator());
    assertEquals(p1.getHand(), cards);
  }

  @Test
  public void testResetStatus() {
    Player p1 = new Player(0, "Player");
    p1.addCard(new Card(Rank.ACE, Suit.CLUBS));
    p1.addAmountThisTurn(100);
    p1.resetStatus();

    assertEquals(0, p1.getBalance(), 0);
    assertFalse(p1.hasFolded());
    assertTrue(p1.getHand().isEmpty());
  }


  @Test
  public void testAddAmountThisTurnSuccess() {
    Player player = new Player(1000, "Player");
    boolean canAdd = player.addAmountThisTurn(10);

    assertTrue(canAdd);
    assertEquals(990, player.getBalance(), 0.01);
    assertEquals(10, player.getAmountThisTurn(), 0.01);
  }

  @Test
  public void testAddAmountThisTurnFailure() {
    Player player = new Player(1000, "Player");
    boolean canAdd = player.addAmountThisTurn(10000);

    assertFalse(canAdd);
    assertEquals(1000, player.getBalance(), 0.01);
    assertEquals(0, player.getAmountThisTurn(), 0.01);
  }

  @Test
  public void testNoActionThisTurn() {
    Player player = new Player(1000, "Player");
    player.noActionThisTurn();

    assertEquals(-1, player.getAmountThisTurn(), 0.01);
  }
}
