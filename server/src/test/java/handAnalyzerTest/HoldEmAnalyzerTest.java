package handAnalyzerTest;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import model.hand.representation.Hand;
import model.hand.representation.HandRank;
import org.junit.Test;

import model.hand.analyzer.HoldEmAnalyzer;
import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;

public class HoldEmAnalyzerTest {
    @Test
    public void testTopRankHighCard() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
        Card card3 = new Card(Rank.THREE, Suit.SPADES);
        Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
        Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.HIGH_CARD);
    }

    @Test
    public void testRankPair() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card card3 = new Card(Rank.THREE, Suit.SPADES);
        Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
        Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.PAIR);
    }

    @Test
    public void testTwoPair() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card card3 = new Card(Rank.THREE, Suit.SPADES);
        Card card4 = new Card(Rank.THREE, Suit.HEARTS);
        Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.TWO_PAIR);
    }

    @Test
    public void test3Kind() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card card3 = new Card(Rank.ACE, Suit.SPADES);
        Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
        Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.THREE_OF_A_KIND);
    }

    @Test
    public void testFullHouse() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card card3 = new Card(Rank.ACE, Suit.SPADES);
        Card card4 = new Card(Rank.THREE, Suit.HEARTS);
        Card card5 = new Card(Rank.THREE, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.FULL_HOUSE);
    }

    @Test
    public void test4Kind() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card card3 = new Card(Rank.ACE, Suit.SPADES);
        Card card4 = new Card(Rank.ACE, Suit.HEARTS);
        Card card5 = new Card(Rank.EIGHT, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.FOUR_OF_A_KIND);
    }

    @Test
    public void testStraight() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
        Card card3 = new Card(Rank.THREE, Suit.SPADES);
        Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
        Card card5 = new Card(Rank.FIVE, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.STRAIGHT);
    }

    @Test
    public void testStraightFlush() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.TWO, Suit.CLUBS);
        Card card3 = new Card(Rank.THREE, Suit.CLUBS);
        Card card4 = new Card(Rank.FOUR, Suit.CLUBS);
        Card card5 = new Card(Rank.FIVE, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.STRAIGHT_FLUSH);
    }

    @Test
    public void testRoyalFlush() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
        Card card4 = new Card(Rank.JACK, Suit.CLUBS);
        Card card5 = new Card(Rank.TEN, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.ROYAL_FLUSH);
    }

    @Test
    public void testFlush() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
        Card card4 = new Card(Rank.THREE, Suit.CLUBS);
        Card card5 = new Card(Rank.TEN, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.FLUSH);
    }

    @Test
    public void testStraightOutOfOrder() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.TWO, Suit.DIAMONDS);
        Card card3 = new Card(Rank.THREE, Suit.SPADES);
        Card card4 = new Card(Rank.FOUR, Suit.HEARTS);
        Card card5 = new Card(Rank.TWO, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.FIVE, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.STRAIGHT);
    }

    @Test
    public void testFlushOutOfOrder() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
        Card card4 = new Card(Rank.THREE, Suit.DIAMONDS);
        Card card5 = new Card(Rank.TEN, Suit.CLUBS);
        Card card6 = new Card(Rank.NINE, Suit.CLUBS);
        Card card7 = new Card(Rank.TEN, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.FLUSH);
    }

    @Test
    public void testStraightFlushOutOfOrder() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.TWO, Suit.CLUBS);
        Card card3 = new Card(Rank.THREE, Suit.CLUBS);
        Card card4 = new Card(Rank.FOUR, Suit.CLUBS);
        Card card5 = new Card(Rank.FIVE, Suit.HEARTS);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.FIVE, Suit.CLUBS);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.STRAIGHT_FLUSH);
    }

    @Test
    public void testRoyalFlushOutOfOrder() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
        Card card4 = new Card(Rank.JACK, Suit.CLUBS);
        Card card5 = new Card(Rank.TEN, Suit.SPADES);
        Card card6 = new Card(Rank.NINE, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TEN, Suit.CLUBS);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer hA = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(hA.getTopRank(), HandRank.ROYAL_FLUSH);
    }

    @Test
    public void testStraightAndFlush() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.TWO, Suit.CLUBS);
        Card card3 = new Card(Rank.THREE, Suit.CLUBS);
        Card card4 = new Card(Rank.FOUR, Suit.CLUBS);
        Card card5 = new Card(Rank.FIVE, Suit.HEARTS);
        Card card6 = new Card(Rank.TEN, Suit.CLUBS);
        Card card7 = new Card(Rank.ACE, Suit.HEARTS);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer holdEmAnalyzer = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(holdEmAnalyzer.getTopRank(), HandRank.FLUSH);
    }

    @Test
    public void testBroadwayStraight() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
        Card card4 = new Card(Rank.JACK, Suit.CLUBS);
        Card card5 = new Card(Rank.TEN, Suit.HEARTS);
        Card card6 = new Card(Rank.NINE, Suit.HEARTS);
        Card card7 = new Card(Rank.EIGHT, Suit.HEARTS);

        Card[] cards = { card1, card2, card3, card4, card5, card6, card7 };

        HoldEmAnalyzer holdEmAnalyzer = new HoldEmAnalyzer(Arrays.asList(cards));
        assertEquals(holdEmAnalyzer.getTopRank(), HandRank.STRAIGHT);
    }
}
