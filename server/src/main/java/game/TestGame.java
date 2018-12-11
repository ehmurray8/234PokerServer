package game;

import client.ClientHandler;
import model.card.Card;
import model.hand.representation.Hand;
import model.hand.representation.TestHand;
import model.player.Player;

import java.util.List;

public class TestGame extends Game {

    private int numRuns = 0;
    private List<Card> communityCards;

    public TestGame(List<Player> players, Rules rules, ClientHandler clientHandler, List<Card> communityCards) {
        super(players, rules, clientHandler);
        dealerNum = 0;
        this.communityCards = communityCards;
    }

    public void setNumRuns(int numRuns) {
        this.numRuns = numRuns;
    }

    public void moveDealer() {
        incrementDealerNum();
    }

    @Override
    public void runGame() {
        for(int i = 0; i < numRuns; i++) {
            gameIteration();
        }
    }

    @Override
    public void runHand(Hand currentHand) {
        handleAction(currentHand);
    }

    @Override
    public void handleFlop(Hand currentHand) {
        TestHand hand = (TestHand) currentHand;
        communityCards.subList(0, 3).forEach(hand::addCommunityCard);
        bettingRound(currentHand, true);
    }

    @Override
    public void handleTurn(Hand currentHand) {
        TestHand hand = (TestHand) currentHand;
        hand.addCommunityCard(communityCards.get(3));
        bettingRound(currentHand, true);
    }

    @Override
    public void handleRiver(Hand currentHand) {
        TestHand hand = (TestHand)  currentHand;
        hand.addCommunityCard(communityCards.get(4));
        bettingRound(currentHand, true);
    }
}
