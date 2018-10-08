package cardTest;

import model.card.Card.Rank;
import model.card.Card.Suit;
import model.card.Card;
import model.card.Deck;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DeckTest {

    @Test
    public void testDeckInit() {
        Deck deck = new Deck();

        for (Rank r : Rank.values()) {
            if (r != Rank.ONE) {
                for (Suit s : Suit.values()) {
                    assertTrue(deck.contains(new Card(r, s)));
                }
            }
        }
    }

    @Test
    public void testDeckShuffled() {
        assertNotEquals(Collections.singletonList(new Deck()).toArray(), Collections.singletonList(new Deck()).toArray());
    }

    @Test
    public void testDealCard() {
        Deck deck = new Deck();

        assertEquals(deck.lastElement(), deck.pop());
        assertEquals(51, deck.size());
    }
}
