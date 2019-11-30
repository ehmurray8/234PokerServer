package model.card;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;

import model.card.Card.Rank;
import model.card.Card.Suit;

public class Deck {

    private Stack<Card> cardStack;

    public Deck() {
        this.cardStack = new Stack<>();
        addAllCards();
        Collections.shuffle(cardStack);
    }

    private void addAllCards() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Arrays.stream(Rank.values()).filter(rank -> rank != Rank.ONE).collect(Collectors.toList())) {
                Card card = new Card(rank, suit);
                cardStack.push(card);
            }
        }
    }

    public Card dealCard() {
        if (cardStack.empty()) {
            throw new IllegalStateException("Deck is empty, there are no more cards to deal.");
        }
        return cardStack.pop();
    }
}
