package test.handTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import model.hand.analyzer.HandAnalyzer;
import model.hand.analyzer.HoldEmAnalyzer;
import model.hand.analyzer.OmahaAnalyzer;
import model.card.Card;
import model.player.Player;
import model.hand.representation.Hand;
import model.hand.representation.HoldEmHand;
import model.hand.representation.OmahaHand;
import model.hand.representation.Pot;

public class HandTest {

    @Test
    public void testTwoPlayerHoldEm() {
        Player p1 = new Player(100, "P1");
        Player p2 = new Player(100, "P2");
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        Hand hand = new HoldEmHand(25, 50, 0, players);
        hand.dealInitialHand();
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
    
    @Test
    public void testTwoPlayerOmahaEm() {
        Player p1 = new Player(100, "P1");
        Player p2 = new Player(100, "P2");
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(p1);
        players.add(p2);
        Hand hand = new OmahaHand(25, 50, 0, players);
        hand.dealInitialHand();
        hand.dealFlop();
        hand.dealTurn();
        hand.dealRiver();
        List<Card> p1Hand = new ArrayList<Card>();
        List<Card> p2Hand = new ArrayList<Card>();
        p1Hand.addAll(p1.getHand());
        p1Hand.addAll(hand.getCommunityCards());
        p2Hand.addAll(p2.getHand());
        p2Hand.addAll(hand.getCommunityCards());
        HandAnalyzer hA1 = new OmahaAnalyzer(p1Hand);
        HandAnalyzer hA2 = new OmahaAnalyzer(p2Hand);
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
    
    @Test
    public void testChargeAntes() {
    	Player p1 = new Player(5, "P1");
    	Player p2 = new Player(11, "P2");
    	Player p3 = new Player(21, "P3");
    	Player p4 = new Player(50, "P4");
    	Player p5 = new Player(7, "P5");
    	Player p6 = new Player(100, "P6");
    	List<Player> players = new ArrayList<Player>(Arrays.asList(new Player[]{p1, p2, p3, p4, p5, p6}));
    	Hand hand = new HoldEmHand(60, 120, 30, players);
    	hand.dealInitialHand();
    	hand.chargeAntes();
    	assertTrue(hand.getAllPots().size() == 4);
    	assertTrue(hand.getAllPots().get(0).getAmount() == 30);
    	assertTrue(hand.getAllPots().get(1).getAmount() == 10);
    	assertTrue(hand.getAllPots().get(2).getAmount() == 16);
    	assertTrue(hand.getAllPots().get(3).getAmount() == 30);
    	assertTrue(hand.getPots().get(0).getAmount() == 18);
    	for(Pot p : hand.getAllPots()) {
    		assertTrue(p.getAmountOwed() == 0);
    		assertTrue(p.getNumPlayersPaid() == 0);
    	}
    	assertTrue(p1.getBalance() == 0);
    	assertTrue(p2.getBalance() == 0);
    	assertTrue(p3.getBalance() == 0);
    	assertTrue(p4.getBalance() == 20);
    	assertTrue(p5.getBalance() == 0);
    	assertTrue(p6.getBalance() == 70);
    	
    	hand.chargeSmallBlind(3);
    	hand.chargeBigBlind(5);
    	
    	for(Pot p : hand.getAllPots()) {
    		System.out.println(p.toString());
    	}
    	System.out.println(hand.getPots());
    }
}
