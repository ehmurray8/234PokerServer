package model.card;

import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;

public class ShortDeck extends Stack<Card> {

    public ShortDeck() {
        super();
        addAllCards();
        Collections.shuffle(this);
    }

    private void addAllCards() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Arrays.stream(Card.Rank.values()).filter(this::validRank).collect(Collectors.toList())) {
                Card card = new Card(rank, suit);
                push(card);
            }
        }
    }

    private boolean validRank(Card.Rank rank) {
        return rank != Card.Rank.ONE && rank != Card.Rank.TWO && rank != Card.Rank.THREE && rank != Card.Rank.FOUR
                && rank != Card.Rank.FIVE;
    }
}
