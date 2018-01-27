package test.cardTest;

//import static org.junit.Assert.assertEquals;
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
    public final void testImageIndexFromCardAceClubs() {
        //Card c1 = new Card(Rank.ACE, Suit.CLUBS);

        //int index = c1.getIndexFromCard(c1);

        //assertEquals(view.ViewInterface.cardImages[index],
        //    view.ViewInterface.aceC);
    }

    @Test
    public final void testImageIndexFromCardNineHearts() {
        //Card c1 = new Card(Rank.NINE, Suit.HEARTS);

        //int index = c1.getIndexFromCard(c1);

        //assertEquals(view.ViewInterface.cardImages[index],
        //      view.ViewInterface.nineH);
    }
}
