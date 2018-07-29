package model.card;

import java.util.Collections;
import java.util.Stack;

import model.card.Card.Rank;
import model.card.Card.Suit;

public class Deck extends Stack<Card> {

    public Deck() {
        super();
        addAllCards();
        Collections.shuffle(this);
    }

    private void addAllCards() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(rank, suit);
                push(card);
            }
        }
    }
}
