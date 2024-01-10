package model.hand.representation;

import java.util.ArrayList;
import java.util.Arrays;
import model.card.Card;
import model.player.Player;

public class TestHand extends TexasHoldEmHand {

    public TestHand(
        double smallBlindAmount,
        double bigBlindAmount,
        double anteAmount,
        ArrayList<Player> players
    ) {
        super(smallBlindAmount, bigBlindAmount, anteAmount, players);
    }

    public void setCommunityCards(Card[] cards) {
        communityCards.addAll(Arrays.asList(cards));
    }

    public void addCommunityCard(Card card) {
        communityCards.add(card);
    }
}
