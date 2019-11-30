package model.hand.representation;

import java.util.ArrayList;
import java.util.stream.IntStream;

import model.player.Player;

public class PineappleHand extends Hand {

    public PineappleHand(double smallBlind, double bigBlind, double ante, ArrayList<Player> players, double minimumChipAmount) {
        super(smallBlind, bigBlind, ante, players, minimumChipAmount);
    }

    @Override
    public final void dealInitialHand() {
        IntStream.range(0, 3).forEach(iteration ->
            players.forEach(player -> player.addCard(deck.dealCard()))
        );
    }
}
