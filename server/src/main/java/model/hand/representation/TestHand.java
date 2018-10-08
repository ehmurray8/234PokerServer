package model.hand.representation;

import model.card.Card;
import model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

public class TestHand extends Hand {

    public TestHand(double smallBlindAmount, double bigBlindAmount, double anteAmount, ArrayList<Player> players) {
        super(smallBlindAmount, bigBlindAmount, anteAmount, players);
    }

    @Override
    public void dealInitialHand() {
        IntStream.range(0, 2).forEach(iteration ->
                players.forEach(player -> player.addCard(deck.pop()))
        );
    }

    public void setCommunityCards(Card[] cards) {
        communityCards.addAll(Arrays.asList(cards));
    }
}
