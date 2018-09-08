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
import model.hand.representation.TexasHoldEmHand;
import model.hand.representation.Pot;


public class HandTest {

	@Test
	public void testDealHoldemHand() {
		Player p1 = new Player(100, "P1");
		Player p2 = new Player(100, "P2");
		ArrayList<Player> players = new ArrayList<>();
		players.add(p1);
		players.add(p2);
		Hand hand = new TexasHoldEmHand(25, 50, 0, players);
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
		Hand hand = new TexasHoldEmHand(25, 50, 0, new ArrayList<>());
		hand.dealFlop();
		assertEquals(hand.getCommunityCards().size(), 3);
	}

	@Test
	public void testDealTurn() {
		Hand hand = new TexasHoldEmHand(25, 50, 0, new ArrayList<>());
		hand.dealTurn();
		assertEquals(hand.getCommunityCards().size(), 1);
	}

	@Test
	public void testDealRiver() {
		Hand hand = new TexasHoldEmHand(25, 50, 0, new ArrayList<>());
		hand.dealRiver();
		assertEquals(hand.getCommunityCards().size(), 1);
	}

	@Test
	public void testChargeAntesBasic() {
		Player p1 = new Player(2000, "P1");
		Player p2 = new Player(2000, "P2");
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(p1, p2));
		Hand hand = new TexasHoldEmHand(60, 120, 30, players);
		hand.dealInitialHand();
		hand.chargeAntes();

        assertEquals(1970., p1.getBalance(), 0.0);
        assertEquals(1970., p2.getBalance(), 0.0);
		assertEquals(60., hand.getOpenPots().get(0).getAmount(), .001);
	}


	@Test
	public void testChargeAntesBasicSixHanded() {
		Player p1 = new Player(5000, "P1");
		Player p2 = new Player(11000, "P2");
		Player p3 = new Player(2100, "P3");
		Player p4 = new Player(5000, "P4");
		Player p5 = new Player(700, "P5");
		Player p6 = new Player(100000, "P6");
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6));
		Hand hand = new TexasHoldEmHand(60, 120, 30, players);
		hand.dealInitialHand();
		hand.chargeAntes();
        assertEquals(4970, p1.getBalance(), 0.0);
        assertEquals(10970, p2.getBalance(), 0.0);
        assertEquals(2070, p3.getBalance(), 0.0);
        assertEquals(4970, p4.getBalance(), 0.0);
        assertEquals(670, p5.getBalance(), 0.0);
        assertEquals(99970, p6.getBalance(), 0.0);
        assertEquals(180, hand.getOpenPots().get(0).getAmount(), 0.0);
	}

	@Test
	public void testBlindsBasic() {
		Player p1 = new Player(2000, "P1");
		Player p2 = new Player(2000, "P2");
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(p1, p2));
		Hand hand = new TexasHoldEmHand(60, 120, 30, players);
		hand.dealInitialHand();
		hand.chargeSmallBlind(1);
		hand.chargeBigBlind(0);
        assertEquals(1880., p1.getBalance(), 0.0);
        assertEquals(1940., p2.getBalance(), 0.0);
        assertEquals(180., hand.getOpenPots().get(0).getAmount(), 0.0);
	}

	@Test
	public void testChargeBlindsBasicSixHanded() {
		Player p1 = new Player(5000, "P1");
		Player p2 = new Player(11000, "P2");
		Player p3 = new Player(2100, "P3");
		Player p4 = new Player(5000, "P4");
		Player p5 = new Player(700, "P5");
		Player p6 = new Player(100000, "P6");
		ArrayList<Player> players = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6));
		Hand hand = new TexasHoldEmHand(60, 120, 30, players);
		hand.dealInitialHand();
		hand.chargeSmallBlind(1);
		hand.chargeBigBlind(2);
        assertEquals(5000, p1.getBalance(), 0.0);
        assertEquals(10940, p2.getBalance(), 0.0);
        assertEquals(1980, p3.getBalance(), 0.0);
        assertEquals(5000, p4.getBalance(), 0.0);
        assertEquals(700, p5.getBalance(), 0.0);
        assertEquals(100000, p6.getBalance(), 0.0);
        assertEquals(180, hand.getOpenPots().get(0).getAmount(), 0.0);
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
		Hand hand = new TexasHoldEmHand(60, 120, 30, players);
		hand.dealInitialHand();
		hand.chargeAntes();
        assertEquals(4, hand.getAllPots().size());
        assertEquals(30, hand.getAllPots().get(0).getAmount(), 0.0);
        assertEquals(10, hand.getAllPots().get(1).getAmount(), 0.0);
        assertEquals(16, hand.getAllPots().get(2).getAmount(), 0.0);
        assertEquals(30, hand.getAllPots().get(3).getAmount(), 0.0);
        assertEquals(18, hand.getOpenPots().get(0).getAmount(), 0.0);
		for (Pot p : hand.getAllPots()) {
            assertEquals(0, p.getAmountOwed(), 0.0);
            assertEquals(0, p.getNumPlayersPaid());
		}
        assertEquals(0, p1.getBalance(), 0.0);
        assertEquals(0, p2.getBalance(), 0.0);
        assertEquals(0, p3.getBalance(), 0.0);
        assertEquals(20, p4.getBalance(), 0.0);
        assertEquals(0, p5.getBalance(), 0.0);
        assertEquals(70, p6.getBalance(), 0.0);

		hand.chargeSmallBlind(3);
		hand.chargeBigBlind(5);

        assertEquals(0, p4.getBalance(), 0.0);
        assertEquals(20, p4.getAmountThisTurn(), 0.0);
        assertEquals(0, p6.getBalance(), 0.0);
        assertEquals(70, p6.getAmountThisTurn(), 0.0);
	}
}
