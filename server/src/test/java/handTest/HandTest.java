package handTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import model.hand.representation.OmahaHand;
import org.junit.Test;

import model.player.Player;
import model.hand.representation.Hand;
import model.hand.representation.HoldEmHand;
import model.hand.representation.Pot;


public class HandTest {

    @Test
    public void testDealHoldemHand() {
        Player p1 = new Player(100, "P1");
        Player p2 = new Player(100, "P2");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Hand hand = new HoldEmHand(25, 50, 0, players);
        hand.dealInitialHand();

        assertEquals(2, p1.getHand().size());
        assertEquals(2, p2.getHand().size());
        assertNotEquals(p1.getHand(), p2.getHand());
    }

    @Test
	public void testDealOmahaHand() {
    	Player p1 = new Player(100, "P1");
        Player p2 = new Player(100, "P2");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        Hand hand = new OmahaHand(25, 50, 0, players);
        hand.dealInitialHand();

        assertEquals(4, p1.getHand().size());
        assertEquals(4, p2.getHand().size());
        assertNotEquals(p1.getHand(), p2.getHand());
	}

    @Test
    public void testDealFlop() {
        Hand hand = new HoldEmHand(25, 50, 0, new ArrayList<>());
        hand.dealFlop();
        assertEquals(hand.getCommunityCards().size(), 3);
    }

    @Test
    public void testDealTurn() {
        Hand hand = new HoldEmHand(25, 50,0, new ArrayList<>());
        hand.dealTurn();
        assertEquals(hand.getCommunityCards().size(), 1);
    }

    @Test
    public void testDealRiver() {
        Hand hand = new HoldEmHand(25, 50,0, new ArrayList<>());
        hand.dealRiver();
        assertEquals(hand.getCommunityCards().size(), 1);
    }

    @Test
    public void testChargeAntesBasic() {
    	Player p1 = new Player(2000, "P1");
    	Player p2 = new Player(2000, "P2");
    	ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(p1, p2));
    	Hand hand = new HoldEmHand(60, 120, 30, players);
    	hand.dealInitialHand();
    	hand.chargeAntes();

    	assertTrue(p1.getBalance() == 1970.);
    	assertTrue(p2.getBalance() == 1970.);
    	assertTrue(hand.getPots().get(0).getAmount() == 60.);
    }
    
    
    @Test
    public void testChargeAntesBasicSixHanded() {
        Player p1 = new Player(5000, "P1");
    	Player p2 = new Player(11000, "P2");
    	Player p3 = new Player(2100, "P3");
    	Player p4 = new Player(5000, "P4");
    	Player p5 = new Player(700, "P5");
    	Player p6 = new Player(100000, "P6");
    	ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(p1, p2, p3, p4, p5, p6));
    	Hand hand = new HoldEmHand(60, 120, 30, players);
    	hand.dealInitialHand();
    	hand.chargeAntes();
    	assertTrue(p1.getBalance() == 4970);
    	assertTrue(p2.getBalance() == 10970);
    	assertTrue(p3.getBalance() == 2070);
    	assertTrue(p4.getBalance() == 4970);
    	assertTrue(p5.getBalance() == 670);
    	assertTrue(p6.getBalance() == 99970);
    	assertTrue(hand.getPots().get(0).getAmount() == 180);
    }
    
    @Test
    public void testBlindsBasic() {
        Player p1 = new Player(2000, "P1");
    	Player p2 = new Player(2000, "P2");
    	ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(p1, p2));
    	Hand hand = new HoldEmHand(60, 120, 30, players);
    	hand.dealInitialHand();
    	hand.chargeSmallBlind(1);
    	hand.chargeBigBlind(0);
    	assertTrue(p1.getBalance() == 1880.);
    	assertTrue(p2.getBalance() == 1940.);
    	assertTrue(hand.getPots().get(0).getAmount() == 180.);
    }
    
    @Test
    public void testChargeBlindsBasicSixHanded() {
        Player p1 = new Player(5000, "P1");
    	Player p2 = new Player(11000, "P2");
    	Player p3 = new Player(2100, "P3");
    	Player p4 = new Player(5000, "P4");
    	Player p5 = new Player(700, "P5");
    	Player p6 = new Player(100000, "P6");
    	ArrayList<Player> players = new ArrayList<Player>(Arrays.asList(p1, p2, p3, p4, p5, p6));
    	Hand hand = new HoldEmHand(60, 120, 30, players);
    	hand.dealInitialHand();
    	hand.chargeSmallBlind(1);
    	hand.chargeBigBlind(2);
    	assertTrue(p1.getBalance() == 5000);
    	assertTrue(p2.getBalance() == 10940);
    	assertTrue(p3.getBalance() == 1980);
    	assertTrue(p4.getBalance() == 5000);
    	assertTrue(p5.getBalance() == 700);
    	assertTrue(p6.getBalance() == 100000);
    	assertTrue(hand.getPots().get(0).getAmount() == 180);
    }
    
    @Test
    public void testChargeForcedBets() {
    	Player p1 = new Player(5, "P1");
    	Player p2 = new Player(11, "P2");
    	Player p3 = new Player(21, "P3");
    	Player p4 = new Player(50, "P4");
    	Player p5 = new Player(7, "P5");
    	Player p6 = new Player(100, "P6");
    	ArrayList<Player> players = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6));
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

    	assertTrue(p4.getBalance() == 0);
    	assertTrue(p4.getAmountThisTurn() == 20);
    	assertTrue(p6.getBalance() == 0);
    	assertTrue(p6.getAmountThisTurn() == 70);
    }
}
