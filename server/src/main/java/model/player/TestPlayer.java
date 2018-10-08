package model.player;

import model.card.Card;

import java.util.Arrays;

public class TestPlayer extends Player {

    public TestPlayer(double balance, String name) {
        super(balance, name);
    }

    public void setHand(Card[] cards) {
        hand.addAll(Arrays.asList(cards));
    }
}
