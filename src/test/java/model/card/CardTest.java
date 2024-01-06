package model.card;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

  @Test
  public final void testCardComparatorDifferent() {
    Card c1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
    Card c2 = new Card(Card.Rank.TWO, Card.Suit.CLUBS);

    Comparator<Card> cc = new Card.CardComparator();

    assertTrue(cc.compare(c1, c2) > 0);
    assertTrue(cc.compare(c2, c1) < 0);
  }

  @Test
  public final void testCardComparatorSame() {
    Card c1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
    Card c2 = new Card(Card.Rank.ACE, Card.Suit.SPADES);

    Comparator<Card> cc = new Card.CardComparator();

    assertEquals(0, cc.compare(c1, c2));
  }

  @Test
  public final void testCardEqual() {
    Card c1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
    Card c2 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);

    assertEquals(c1, c2);
  }

  @Test
  public final void testCardNotEqualSuit() {
    Card c1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
    Card c2 = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);

    assertNotEquals(c1, c2);
  }

  @Test
  public final void testCardNotEqualRank() {
    Card c1 = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
    Card c2 = new Card(Card.Rank.JACK, Card.Suit.DIAMONDS);

    assertNotEquals(c1, c2);
  }

  @Test
  public final void testHashCode() {
    Card c1 = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
    Card c2 = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
    Card c3 = new Card(Card.Rank.JACK, Card.Suit.SPADES);
    Card c4 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);

    assertEquals(c1.hashCode(), c2.hashCode());
    assertNotEquals(c1.hashCode(), c3.hashCode());
    assertNotEquals(c1.hashCode(), c4.hashCode());
  }
}
