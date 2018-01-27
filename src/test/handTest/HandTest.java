package test.handTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.HoldEmAnalyzer;
import model.card.Card;
import model.player.Player;
import model.hand.representation.Hand;
import model.hand.representation.HoldEmHand;

public class HandTest {

    @Test
    public void testTwoPlayerHoldEm() {
        Hand hand = new HoldEmHand(25, 50);
        Player p1 = new Player(100, "P1");
        Player p2 = new Player(100, "P2");
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        hand.dealInitialHand(players);
        hand.dealFlop();
        hand.dealTurn();
        hand.dealRiver();
        List<Card> p1Hand = new ArrayList<Card>();
        List<Card> p2Hand = new ArrayList<Card>();
        p1Hand.addAll(p1.getHand());
        p1Hand.addAll(hand.getCommunityCards());
        p2Hand.addAll(p2.getHand());
        p2Hand.addAll(hand.getCommunityCards());
        HandAnalyzer hA1 = new HoldEmAnalyzer(p1Hand);
        HandAnalyzer hA2 = new HoldEmAnalyzer(p2Hand);
        hA1.analyze();
        hA2.analyze();
        System.out.println(hand.toString());
        System.out.println(hA1.toString());
        System.out.println(hA2.toString());
        Comparator<HandAnalyzer> hAC = new HandAnalyzer.HandAnalyzerComparator();
        if (hAC.compare(hA1, hA2) > 0) {
            System.out.println("Player 1 wins");
        } else if (hAC.compare(hA1, hA2) < 0) {
            System.out.println("Player 2 wins");
        } else {
            System.out.println("Split the pot");
        }
    }
}
