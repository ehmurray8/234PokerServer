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

    TestGame(List<Player> players, Rules rules, ClientHandler clientHandler, List<Card> communityCards) {
        super(players, rules, clientHandler);
        dealerNum = 0;
        this.communityCards = communityCards;
    }

    void setNumRuns(int numRuns) {
        this.numRuns = numRuns;
    }

    void moveDealer() {
        incrementDealerNum();
    }

    Hand createHand() {
        return new TestHand(rules, playersInHand, 2);
    }

    @Override
    public void runGame() {
        for(int i = 0; i < numRuns; i++) {
            gameIteration();
        }
    }

    @Override
    public void runHand() {
        handleAction();
    }

    @Override
    public void handleFlop() {
        var hand = (TestHand) currentHand;
        communityCards.subList(0, 3).forEach(hand::addCommunityCard);
        bettingRound(true);
    }

    @Override
    public void handleTurn() {
        var hand = (TestHand) currentHand;
        hand.addCommunityCard(communityCards.get(3));
        bettingRound(true);
    }

    @Override
    public void handleRiver() {
        var hand = (TestHand)  currentHand;
        hand.addCommunityCard(communityCards.get(4));
        bettingRound(true);
    }
}
