package model.hand.analyzer;

import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the HandAnalyzerComparator class to determine if it can correctly compare hands.
 */
public class HandAnalyzerTest {

  /**
   * The HandAnalyzerComparator used throughout the class to compare the class.
   */
  private final Comparator<HandAnalyzer> handAnalyzerComparator = new HandAnalyzerComparator();

  @Test
  public void testAceKingHighCardComp() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.KING, Suit.CLUBS);
    Card oCard2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHighCardTie() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6,
        oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testHighCardLowestCardWin() {
    Card card1 = new Card(Rank.SEVEN, Suit.CLUBS);
    Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.JACK, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.SEVEN, Suit.CLUBS);
    Card oCard2 = new Card(Rank.SIX, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.JACK, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) < 0);
  }

  @Test
  public void testSamePairHighCard() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.KING, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHigherPair() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.TWO, Suit.CLUBS);
    Card oCard2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHigherTopPairTwoPair() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.KING, Suit.CLUBS);
    Card oCard2 = new Card(Rank.KING, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHigherLowPairTwoPair() {
    Card card1 = new Card(Rank.KING, Suit.CLUBS);
    Card card2 = new Card(Rank.KING, Suit.DIAMONDS);
    Card card3 = new Card(Rank.EIGHT, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.KING, Suit.CLUBS);
    Card oCard2 = new Card(Rank.KING, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHigherLastCardTwoPair() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.KING, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testTwoPairEqual() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.FOUR, Suit.CLUBS);
    Card oCard6 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testHigherTrip3Kind() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.KING, Suit.CLUBS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHigherOtherCard3Kind() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.THREE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.KING, Suit.CLUBS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testThreeKindEqual() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.ACE, Suit.SPADES);
    Card oCard4 = new Card(Rank.SIX, Suit.HEARTS);
    Card oCard5 = new Card(Rank.TWO, Suit.CLUBS);
    Card oCard6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testHigherPair4Kind() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.ACE, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.KING, Suit.CLUBS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testHigherOtherCard4Kind() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.ACE, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.KING, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.TEN, Suit.CLUBS);
    Card oCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.ACE, Suit.SPADES);
    Card oCard4 = new Card(Rank.ACE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard6 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.NINE, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void test4KindSame() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.ACE, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.FIVE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.ACE, Suit.SPADES);
    Card oCard4 = new Card(Rank.ACE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testStraightSame() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.TWO, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.ACE, Suit.CLUBS);
    Card card6 = new Card(Rank.FOUR, Suit.DIAMONDS);
    Card card7 = new Card(Rank.FIVE, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.FOUR, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.FIVE, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testHigherStraight() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.FOUR, Suit.CLUBS);
    Card card6 = new Card(Rank.FIVE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.SIX, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TWO, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.FOUR, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.FIVE, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
    assertTrue(this.handAnalyzerComparator.compare(hA2, hA1) < 0);
  }

  @Test
  public void testStraightHigherMiddle() {
    Card card1 = new Card(Rank.JACK, Suit.CLUBS);
    Card card2 = new Card(Rank.QUEEN, Suit.DIAMONDS);
    Card card3 = new Card(Rank.TEN, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.NINE, Suit.CLUBS);
    Card card6 = new Card(Rank.KING, Suit.DIAMONDS);
    Card card7 = new Card(Rank.SIX, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.QUEEN, Suit.CLUBS);
    Card oCard2 = new Card(Rank.JACK, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TEN, Suit.SPADES);
    Card oCard4 = new Card(Rank.NINE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.FOUR, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.FIVE, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testFlushSame() {
    Card card1 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.ACE, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.TEN, Suit.DIAMONDS);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testHigherFlush() {
    Card card1 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.ACE, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.JACK, Suit.DIAMONDS);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard4 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testFullHouseHigherPair() {
    Card card1 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card2 = new Card(Rank.ACE, Suit.CLUBS);
    Card card3 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card4 = new Card(Rank.TWO, Suit.HEARTS);
    Card card5 = new Card(Rank.TWO, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.JACK, Suit.DIAMONDS);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TWO, Suit.SPADES);
    Card oCard4 = new Card(Rank.TWO, Suit.HEARTS);
    Card oCard5 = new Card(Rank.THREE, Suit.CLUBS);
    Card oCard6 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testFullHouseHigherTrip() {
    Card card1 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card card2 = new Card(Rank.THREE, Suit.CLUBS);
    Card card3 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.TWO, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.JACK, Suit.DIAMONDS);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TWO, Suit.SPADES);
    Card oCard4 = new Card(Rank.TWO, Suit.HEARTS);
    Card oCard5 = new Card(Rank.THREE, Suit.CLUBS);
    Card oCard6 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.TEN, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testFullHouseSame() {
    Card card1 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card card2 = new Card(Rank.THREE, Suit.CLUBS);
    Card card3 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card4 = new Card(Rank.THREE, Suit.HEARTS);
    Card card5 = new Card(Rank.TWO, Suit.CLUBS);
    Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.JACK, Suit.DIAMONDS);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.NINE, Suit.DIAMONDS);
    Card oCard2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TWO, Suit.SPADES);
    Card oCard4 = new Card(Rank.TWO, Suit.HEARTS);
    Card oCard5 = new Card(Rank.THREE, Suit.CLUBS);
    Card oCard6 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.THREE, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testStraightFlushSame() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card card3 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card card4 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card card5 = new Card(Rank.FOUR, Suit.DIAMONDS);
    Card card6 = new Card(Rank.FIVE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.SEVEN, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.SPADES);
    Card oCard2 = new Card(Rank.THREE, Suit.SPADES);
    Card oCard3 = new Card(Rank.TWO, Suit.SPADES);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.FOUR, Suit.SPADES);
    Card oCard7 = new Card(Rank.FIVE, Suit.SPADES);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, this.handAnalyzerComparator.compare(hA1, hA2));
  }

  @Test
  public void testHigherStraightFlush() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.TWO, Suit.SPADES);
    Card card3 = new Card(Rank.ACE, Suit.SPADES);
    Card card4 = new Card(Rank.THREE, Suit.SPADES);
    Card card5 = new Card(Rank.FOUR, Suit.SPADES);
    Card card6 = new Card(Rank.FIVE, Suit.SPADES);
    Card card7 = new Card(Rank.SIX, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.CLUBS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TWO, Suit.DIAMONDS);
    Card oCard4 = new Card(Rank.THREE, Suit.HEARTS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.FOUR, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.FIVE, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertTrue(this.handAnalyzerComparator.compare(hA1, hA2) > 0);
  }

  @Test
  public void testRoyalFlushTie() {
    Card card1 = new Card(Rank.ACE, Suit.CLUBS);
    Card card2 = new Card(Rank.KING, Suit.CLUBS);
    Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
    Card card4 = new Card(Rank.JACK, Suit.CLUBS);
    Card card5 = new Card(Rank.TEN, Suit.CLUBS);
    Card card6 = new Card(Rank.FIVE, Suit.DIAMONDS);
    Card card7 = new Card(Rank.SIX, Suit.SPADES);

    Card[] cards = {card1, card2, card3, card4, card5, card6, card7};

    Card oCard1 = new Card(Rank.ACE, Suit.DIAMONDS);
    Card oCard2 = new Card(Rank.THREE, Suit.DIAMONDS);
    Card oCard3 = new Card(Rank.TEN, Suit.DIAMONDS);
    Card oCard4 = new Card(Rank.JACK, Suit.DIAMONDS);
    Card oCard5 = new Card(Rank.EIGHT, Suit.CLUBS);
    Card oCard6 = new Card(Rank.QUEEN, Suit.DIAMONDS);
    Card oCard7 = new Card(Rank.KING, Suit.DIAMONDS);

    Card[] otherCards = {oCard1, oCard2, oCard3, oCard4, oCard5, oCard6, oCard7};

    HandAnalyzer hA1 = new HoldEmAnalyzer(Arrays.asList(cards));
    HandAnalyzer hA2 = new HoldEmAnalyzer(Arrays.asList(otherCards));

    assertEquals(0, handAnalyzerComparator.compare(hA1, hA2));
  }
}
