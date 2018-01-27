package model.card;

import java.util.Collections;
import java.util.Stack;

import model.card.Card.Rank;
import model.card.Card.Suit;

/**
 * Deck class describes an object of a deck of shuffled cards used for the game
 * of poker.
 *
 * <p>
 * The hand is represented by a {@code Stack}.
 * </p>
 *
 * @author Emmet Murray
 * @version 3.0
 * @since 9/26/2016
 */
public class Deck {

    /**
     * The representation of a deck of cards.
     */
    private Stack<Card> deck;

    /**
     * Constructs an object that creates, shuffles, and stores all 52 distinct
     * {@code Card} objects as this.
     *
     */
    public Deck() {
        this.deck = new Stack<Card>();
        for (Suit s : Suit.values()) {
            for (int i = 1; i < Rank.values().length; i++) {
                this.deck.push(new Card(Rank.values()[i], s));
            }
        }
        this.shuffle();
    }

    /**
     * Deals a card from the top of {@code deck}, it returns the Card that was
     * removed.
     *
     * @return the Card removed from {@code deck}
     */
    public final Card dealCard() {
        return this.deck.pop();
    }

    /**
     * Returns the amount of cards left in {@code deck}.
     *
     * @return Amount of cards in {@code deck}
     */
    public final int numCardsLeft() {
        return this.deck.size();
    }

    /**
     * Shuffles the cards in {@code deck} and stores them in {@code deck}.
     */
    public final void shuffle() {
        Collections.shuffle(this.deck);
    }
}
