package handAnalyzerTest;

import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;
import model.hand.analyzer.*;
import model.hand.representation.HandRank;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShortDeckAnalyzerTest {

    private static ShortDeckHandAnalyzerComparator SHORT_DECK_COMPARATOR = new ShortDeckHandAnalyzerComparator();

    @Test
    void testWheelStraight() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.SIX, Suit.CLUBS);
        Card card3 = new Card(Rank.SEVEN, Suit.CLUBS);
        Card card4 = new Card(Rank.EIGHT, Suit.DIAMONDS);
        Card card5 = new Card(Rank.NINE, Suit.SPADES);

        Card[] cards = { card1, card2, card3, card4, card5 };

        ShortDeckFiveCardAnalyzer hA = new ShortDeckFiveCardAnalyzer(Arrays.asList(cards));
        assertEquals(HandRank.STRAIGHT, hA.getRank());
    }

    @Test
    void testFlushOverBoat() {
        Card card1 = new Card(Rank.ACE, Suit.CLUBS);
        Card card2 = new Card(Rank.KING, Suit.CLUBS);
        Card card3 = new Card(Rank.SEVEN, Suit.CLUBS);
        Card card4 = new Card(Rank.FIVE, Suit.CLUBS);
        Card card5 = new Card(Rank.TEN, Suit.CLUBS);
        Card card6 = new Card(Rank.FOUR, Suit.DIAMONDS);
        Card card7 = new Card(Rank.TWO, Suit.HEARTS);

        Card[] cards1 = { card1, card2, card3, card4, card5, card6, card7 };

        Card card8 = new Card(Rank.THREE, Suit.CLUBS);
        Card card9 = new Card(Rank.THREE, Suit.DIAMONDS);
        Card card10 = new Card(Rank.THREE, Suit.HEARTS);
        Card card11 = new Card(Rank.TEN, Suit.CLUBS);
        Card card12 = new Card(Rank.TEN, Suit.SPADES);
        Card card13 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card card14 = new Card(Rank.KING, Suit.CLUBS);

        Card[] cards2 = { card8, card9, card10, card11, card12, card13, card14 };

        HandAnalyzer handAnalyzer1 = new HoldEmAnalyzer(Arrays.asList(cards1), true);
        HandAnalyzer handAnalyzer2 = new HoldEmAnalyzer(Arrays.asList(cards2), true);
        int comparison = SHORT_DECK_COMPARATOR.compare(handAnalyzer1, handAnalyzer2);
        assertTrue(comparison > 0);
    }

     @Test
     void testTripsOverStraight() {
        // Broadway straight
         Card card1 = new Card(Rank.ACE, Suit.CLUBS);
         Card card2 = new Card(Rank.KING, Suit.CLUBS);
         Card card3 = new Card(Rank.QUEEN, Suit.CLUBS);
         Card card4 = new Card(Rank.JACK, Suit.DIAMONDS);
         Card card5 = new Card(Rank.TEN, Suit.CLUBS);
         Card card6 = new Card(Rank.FOUR, Suit.DIAMONDS);
         Card card7 = new Card(Rank.TWO, Suit.HEARTS);

         Card[] cards1 = { card1, card2, card3, card4, card5, card6, card7 };

         // Set of 3s
         Card card8 = new Card(Rank.THREE, Suit.CLUBS);
         Card card9 = new Card(Rank.THREE, Suit.DIAMONDS);
         Card card10 = new Card(Rank.THREE, Suit.HEARTS);
         Card card11 = new Card(Rank.ACE, Suit.CLUBS);
         Card card12 = new Card(Rank.QUEEN, Suit.SPADES);
         Card card13 = new Card(Rank.TWO, Suit.DIAMONDS);
         Card card14 = new Card(Rank.KING, Suit.CLUBS);

         Card[] cards2 = { card8, card9, card10, card11, card12, card13, card14 };

        HandAnalyzer handAnalyzer1 = new HoldEmAnalyzer(Arrays.asList(cards1), true);
        HandAnalyzer handAnalyzer2 = new HoldEmAnalyzer(Arrays.asList(cards2), true);
        int comparison = SHORT_DECK_COMPARATOR.compare(handAnalyzer1, handAnalyzer2);
        assertTrue(comparison < 0);
     }
}
