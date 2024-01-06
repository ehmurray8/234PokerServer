package model.card;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

  @Test
  public void testDeckInit() {
    Deck deck = new Deck();

    for (Card.Rank r : Card.Rank.values()) {
      if (r != Card.Rank.ONE) {
        for (Card.Suit s : Card.Suit.values()) {
          assertTrue(deck.contains(new Card(r, s)));
        }
      }
    }
  }

  @Test
  public void testDeckShuffled() {
    assertNotEquals(Collections.singletonList(new Deck()).toArray(),
        Collections.singletonList(new Deck()).toArray());
  }

  @Test
  public void testDealCard() {
    Deck deck = new Deck();

    assertEquals(deck.lastElement(), deck.pop());
    assertEquals(51, deck.size());
  }
}

