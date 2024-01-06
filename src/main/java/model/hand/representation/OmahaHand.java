package model.hand.representation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import model.card.Card;
import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.OmahaAnalyzer;
import model.player.Player;

public class OmahaHand extends Hand {

  public OmahaHand(double smallBlind, double bigBlind, double ante, ArrayList<Player> players) {
    super(smallBlind, bigBlind, ante, players);
  }

  @Override
  public final void dealInitialHand() {
    IntStream.range(0, 4).forEach(iteration ->
        players.forEach(player -> player.addCard(deck.pop()))
    );
  }

  @Override
  public HandAnalyzer createAnalyzer(List<Card> cards) {
    return new OmahaAnalyzer(cards);
  }
}
