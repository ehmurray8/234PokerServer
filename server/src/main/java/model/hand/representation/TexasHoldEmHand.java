package model.hand.representation;

import java.util.ArrayList;
import java.util.stream.IntStream;

import model.player.Player;

public class TexasHoldEmHand extends Hand {

    public TexasHoldEmHand(double smallBlindAmount, double bigBlindAmount, double anteAmount,
                           ArrayList<Player> players, double minimumChipAmount) {
        super(smallBlindAmount, bigBlindAmount, anteAmount, players, minimumChipAmount);
        numberOfCards = 2;
    }

    public TexasHoldEmHand(double smallBlindAmount, double bigBlindAmount, double anteAmount,
                           ArrayList<Player> players, boolean isShortDeck, double minimumChipAmount) {
        super(smallBlindAmount, bigBlindAmount, anteAmount, players, isShortDeck, minimumChipAmount);
        numberOfCards = 2;
    }

    @Override
    public final void dealInitialHand() {
        IntStream.range(0, 2).forEach(iteration ->
            players.forEach(player -> player.addCard(deck.pop()))
        );
    }
}
