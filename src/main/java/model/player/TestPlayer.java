package model.player;

import java.util.Arrays;
import model.card.Card;

public class TestPlayer extends Player {

    private int maxCards = 2;

    public TestPlayer(double balance, String name) {
        super(balance, name);
    }

    public void setHand(Card[] cards) {
        hand.addAll(Arrays.asList(cards));
    }

    public void setMaxCards(int max) {
        maxCards = max;
    }

    @Override
    public void addCard(Card card) {
        if (hand.size() < maxCards) {
            super.addCard(card);
        }
    }
}
