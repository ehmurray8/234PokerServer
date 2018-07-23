package model.card;

import java.util.Collections;
import java.util.Stack;

import model.card.Card.Rank;
import model.card.Card.Suit;

/** Deck class describes an object of a deck of shuffled cards used for the game of poker. */
public class Deck extends Stack<Card> {

    /** Constructs an object that creates, shuffles, and stores all 52 distinct {@code Card} objects as this. */
    public Deck() {
        super();
        for (Suit s : Suit.values()) {
            for (int i = 1; i < Rank.values().length; i++) {
                push(new Card(Rank.values()[i], s));
            }
        }
        Collections.shuffle(this);
    }

    /**
     * Deals a card from the top of {@code deck}, it returns the Card that was removed.
     *
     * @return the Card removed from {@code deck}
     */
    public final Card dealCard() {
        return pop();
    }
}
