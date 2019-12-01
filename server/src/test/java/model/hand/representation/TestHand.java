package model.hand.representation;

import game.Rules;
import model.card.Card;
import model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHand extends Hand {

    private List<Card> testCommunityCards;

    public TestHand(final Rules rules, final List<Player> players, final int numberOfCards) {
        super(rules, players, numberOfCards);
        this.testCommunityCards = new ArrayList<>();
    }

    void setCommunityCards(Card[] cards) {
        testCommunityCards.addAll(Arrays.asList(cards));
    }

    public void addCommunityCard(Card card) {
        testCommunityCards.add(card);
    }

    @Override
    public List<Card> getCommunityCards() {
        return testCommunityCards;
    }
}
