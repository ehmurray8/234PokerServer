package model.card;

import model.card.Card.Rank;
import model.card.Card.Suit;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DeckTest {

    @Test
    public void testDeckInit() {
        List<Card> cards = getAllCards(new Deck());
        for (Rank r : Rank.values()) {
            if (r != Rank.ONE) {
                for (Suit s : Suit.values()) {
                    assertTrue(cards.contains(new Card(r, s)));
                }
            }
        }
    }

    @Test
    public void testDeckShuffled() {
        List<Card> deck1Cards = getAllCards(new Deck());
        List<Card> deck2Cards = getAllCards(new Deck());
        assertNotEquals(deck1Cards, deck2Cards);
    }

    @Test(expected = IllegalStateException.class)
    public void testDealTooManyCards() {
        Deck deck = new Deck();
        getAllCards(deck);
        deck.dealCard();
    }

    private List<Card> getAllCards(final Deck deck) {
        return IntStream.range(0, 52).mapToObj(__ -> deck.dealCard()).collect(Collectors.toList());
    }
}
