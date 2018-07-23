package cardTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;

import org.junit.Test;

import model.card.Card;
import model.card.Card.Rank;
import model.card.Card.Suit;

public class CardTest {

    @Test
    public final void testCardComparatorDifferent() {
        Card c1 = new Card(Rank.ACE, Suit.CLUBS);
        Card c2 = new Card(Rank.TWO, Suit.CLUBS);

        Comparator<Card> cc = new Card.CardComparator();

        assertTrue(cc.compare(c1, c2) > 0);
        assertTrue(cc.compare(c2, c1) < 0);
    }

    @Test
    public final void testCardComparatorSame() {
        Card c1 = new Card(Rank.ACE, Suit.CLUBS);
        Card c2 = new Card(Rank.ACE, Suit.SPADES);

        Comparator<Card> cc = new Card.CardComparator();

        assertTrue(cc.compare(c1, c2) == 0);
    }

    @Test
    public final void testCardEqual() {
        Card c1 = new Card(Rank.ACE, Suit.CLUBS);
        Card c2 = new Card(Rank.ACE, Suit.CLUBS);

        assertEquals(c1, c2);
    }

    @Test
    public final void testCardNotEqualSuit() {
        Card c1 = new Card(Rank.ACE, Suit.CLUBS);
        Card c2 = new Card(Rank.ACE, Suit.DIAMONDS);

        assertNotEquals(c1, c2);
    }

    @Test
    public final void testCardNotEqualRank() {
        Card c1 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card c2 = new Card(Rank.JACK, Suit.DIAMONDS);

        assertNotEquals(c1, c2);
    }

    @Test
    public final void testHashCode() {
        Card c1 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card c2 = new Card(Rank.ACE, Suit.DIAMONDS);
        Card c3 = new Card(Rank.JACK, Suit.SPADES);
        Card c4 = new Card(Rank.ACE, Suit.CLUBS);

        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1.hashCode(), c3.hashCode());
        assertNotEquals(c1.hashCode(), c4.hashCode());
    }
}
