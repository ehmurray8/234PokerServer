package handTest;

import java.util.ArrayList;
import java.util.Arrays;

import model.card.Card;
import model.hand.representation.*;
import model.option.Option;
import model.player.TestPlayer;
import org.junit.Before;
import org.junit.Test;

import model.player.Player;

import static org.junit.Assert.*;


public class HandTest {

	private TestHand hand;
	private TestPlayer player1;
	private TestPlayer player2;
	private ArrayList<Player> players;
	private final Option fold = new Option(Option.OptionType.FOLD, 0);
	private final Option bet = new Option(Option.OptionType.BET, 100);
	private final Option check = new Option(Option.OptionType.CHECK, 0);

	@Before
	public void setup() {
		player1 = new TestPlayer(2000, "P1");
		player2 = new TestPlayer(2000, "P2");
		players = new ArrayList<>(Arrays.asList(player1, player2));
		hand = new TestHand(60, 120, 30, players);
	}

	@Test
	public void testDealHoldemHand() {
		hand.dealInitialHand();

		assertEquals(2, player1.getHand().size());
		assertEquals(2, player2.getHand().size());
		assertNotEquals(player1.getHand(), player2.getHand());
	}

	@Test
	public void testDealOmahaHand() {
		var omahaHand = new OmahaHand(20, 40, 0, players);
		omahaHand.dealInitialHand();

		assertEquals(4, player1.getHand().size());
		assertEquals(4, player2.getHand().size());
		assertNotEquals(player1.getHand(), player2.getHand());
	}

	@Test
	public void testDealPineappleHand() {
		var pineappleHand = new PineappleHand(20, 40, 0, players);
		pineappleHand.dealInitialHand();

		assertEquals(3, player1.getHand().size());
		assertEquals(3, player2.getHand().size());
		assertNotEquals(player1.getHand(), player2.getHand());
	}

	@Test
	public void testDealFlop() {
		hand.dealFlop();
		assertEquals(hand.getCommunityCards().size(), 3);
	}

	@Test
	public void testDealTurn() {
		hand.dealTurn();
		assertEquals(hand.getCommunityCards().size(), 1);
	}

	@Test
	public void testDealRiver() {
		hand.dealRiver();
		assertEquals(hand.getCommunityCards().size(), 1);
	}

	@Test
	public void testChargeAntesBasic() {
		hand.dealInitialHand();
		hand.chargeAntes();

        assertEquals(1970., player1.getBalance(), 0.0);
        assertEquals(1970., player2.getBalance(), 0.0);
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
		hand.dealInitialHand();
		hand.chargeSmallBlind(1);
		hand.chargeBigBlind(0);
        assertEquals(1880., player1.getBalance(), 0.0);
        assertEquals(1940., player2.getBalance(), 0.0);
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
        assertEquals(4, hand.getClosedPots().size());
        assertEquals(30, hand.getClosedPots().get(0).getAmount(), 0.0);
        assertEquals(10, hand.getClosedPots().get(1).getAmount(), 0.0);
        assertEquals(16, hand.getClosedPots().get(2).getAmount(), 0.0);
        assertEquals(30, hand.getClosedPots().get(3).getAmount(), 0.0);
        assertEquals(18, hand.getOpenPots().get(0).getAmount(), 0.0);
		for (Pot p : hand.getClosedPots()) {
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

	@Test
	public void testPlayersBetting() {
		var player = new Player(10.0, "P1");
		var players = new ArrayList<Player>();
		players.add(player);
		Hand hand = new TexasHoldEmHand(10.0, 20.0, 0.0, players);
		assertFalse(hand.playersBetting());
	}

	@Test
	public void testPlayersBettingOneNotFolded() {
	    player1.fold();
	    assertFalse(hand.playersBetting());
	}

	@Test
	public void testPlayersBettingNoMoney() {
		player1.updateBalance(-2000);
		player2.updateBalance(-2000);

		assertFalse(hand.playersBetting());
	}

	@Test
	public void testPlayersBettingPlayerOwesMoney() {
		hand.executeOption(player1, bet);
		assertTrue(hand.playersBetting());
	}

	@Test
	public void testPlayersBettingBetAndCall() {
		var call = new Option(Option.OptionType.CALL, 100);

		hand.executeOption(player1, bet);
		hand.executeOption(player2, call);

		assertFalse(hand.playersBetting());
	}

	@Test
	public void testPlayersBettingBetAndRaise() {
		var raise = new Option(Option.OptionType.BET, 500);

		hand.executeOption(player1, bet);
		hand.executeOption(player2, raise);

		assertTrue(hand.playersBetting());
	}

	@Test
	public void testPlayersBettingCheckRaiseCall() {
		var raise = new Option(Option.OptionType.RAISE, 500);
		var call = new Option(Option.OptionType.CALL, 400);

		hand.executeOption(player1, check);
		hand.executeOption(player2, bet);
		hand.executeOption(player1, raise);
		hand.executeOption(player2, call);

		assertFalse(hand.playersBetting());
	}

	@Test
	public void testFold() {
		hand.executeOption(player1, fold);
		assertTrue(player1.hasFolded());
		assertFalse(players.contains(player1));
		assertEquals(player1.getBalance(), 2000, 0);
		assertEquals(0, player1.getAmountThisTurn(), 0);
		assertEquals(0, hand.getTotalAmountInPots(), 0);
	}

	@Test
	public void testCheck() {
		hand.executeOption(player1, check);
		assertFalse(player1.hasFolded());
		assertTrue(players.contains(player1));
		assertEquals(player1.getBalance(), 2000, 0);
		assertEquals(0, player1.getAmountThisTurn(), 0);
		assertEquals(0, hand.getTotalAmountInPots(), 0);
	}

	@Test
	public void testBet() {
		hand.executeOption(player1, bet);
		assertFalse(player1.hasFolded());
		assertTrue(players.contains(player1));
		assertEquals(player1.getBalance(), 1900, 0);
		assertEquals(100, player1.getAmountThisTurn(), 0);
		assertEquals(100, hand.getTotalAmountInPots(), 0);
	}

	@Test
	public void testRaise() {
		var raise = new Option(Option.OptionType.RAISE, 100);

		hand.executeOption(player1, raise);
		assertFalse(player1.hasFolded());
		assertTrue(players.contains(player1));
		assertEquals(player1.getBalance(), 1900, 0);
		assertEquals(100, player1.getAmountThisTurn(), 0);
		assertEquals(100, hand.getTotalAmountInPots(), 0);
	}

	@Test
	public void testCall() {
		var call = new Option(Option.OptionType.CALL, 200);

		hand.executeOption(player1, call);
		assertFalse(player1.hasFolded());
		assertTrue(players.contains(player1));
		assertEquals(player1.getBalance(), 1800, 0);
		assertEquals(200, player1.getAmountThisTurn(), 0);
		assertEquals(200, hand.getTotalAmountInPots(), 0);
	}

	@Test
	public void testAllIn() {
		var allIn = new Option(Option.OptionType.ALLIN, player1.getBalance());

		hand.executeOption(player1, allIn);
		assertFalse(player2.hasFolded());
		assertTrue(players.contains(player1));
		assertEquals(player1.getBalance(), 0, 0);
		assertEquals(2000, player1.getAmountThisTurn(), 0);
		assertEquals(2000, hand.getTotalAmountInPots(), 0);
	}

	@Test
    public void testPayReturnToPlayer() {
	    hand.executeOption(player1, bet);
	    hand.executeOption(player2, fold);
	    hand.payWinners();
	    assertEquals(2000, player1.getBalance(), 0);
	    assertEquals(2000, player2.getBalance(), 0);
    }

    @Test
	public void testPaySoleWinner() {
		var raise = new Option(Option.OptionType.RAISE, 500);

		hand.executeOption(player1, bet);
		hand.executeOption(player2, raise);
		hand.executeOption(player1, fold);
		hand.payWinners();
        assertEquals(1900, player1.getBalance(), 0);
		assertEquals(2100, player2.getBalance(), 0);
	}

	@Test
    public void testPlayerWinMultiplePots() {
	    setupBoard1();

        Card player1Card = new Card(Card.Rank.KING, Card.Suit.CLUBS);
        Card player1Card1 = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);

        Card player2Card = new Card(Card.Rank.JACK, Card.Suit.DIAMONDS);
        Card player2Card1 = new Card(Card.Rank.SIX, Card.Suit.CLUBS);

        player1.setHand(new Card[]{player1Card, player1Card1});
        player2.setHand(new Card[]{player2Card, player2Card1});
	    hand.payWinners();

	    assertEquals(6000, player1.getBalance(), 0);
	    assertEquals(0, player2.getBalance(), 0);
    }

    @Test
    public void testPlayerWinSinglePotWithRefund() {
	    setupBoard1();

        Card player1Card = new Card(Card.Rank.THREE, Card.Suit.CLUBS);
        Card player1Card1 = new Card(Card.Rank.FOUR, Card.Suit.SPADES);

        Card player2Card = new Card(Card.Rank.ACE, Card.Suit.DIAMONDS);
        Card player2Card1 = new Card(Card.Rank.SEVEN, Card.Suit.DIAMONDS);

        player1.setHand(new Card[]{player1Card, player1Card1});
        player2.setHand(new Card[]{player2Card, player2Card1});
        hand.payWinners();

        assertEquals(2000, player1.getBalance(), 0);
        assertEquals(4000, player2.getBalance(), 0);
    }

    private void setupBoard1() {
	    Card card1 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
	    Card card2 = new Card(Card.Rank.JACK, Card.Suit.CLUBS);
	    Card card3 = new Card(Card.Rank.TEN, Card.Suit.DIAMONDS);
	    Card card4 = new Card(Card.Rank.THREE, Card.Suit.SPADES);
	    Card card5 = new Card(Card.Rank.TWO, Card.Suit.CLUBS);

	    hand.setCommunityCards(new Card[]{card1, card2, card3, card4, card5});

	    var bigBet = new Option(Option.OptionType.BET, 2100);
	    var bigCall = new Option(Option.OptionType.CALL, 2100);

        player1.updateBalance(2000);

        hand.executeOption(player1,  bigBet);
        hand.executeOption(player2, bigCall);
    }

    @Test
	public void testGenerateOptionsForBrokePlayer() {
		player1.updateBalance(-2000);
		var options = hand.generateOptions(player1);
		assertEquals(0, options.size());
	}

	@Test
	public void testGenerateOptionsBet() {
		var options = hand.generateOptions(player1);
		assertTrue(options.contains(new Option(Option.OptionType.BET, 120)));
		assertTrue(options.contains(new Option(Option.OptionType.CHECK, 0)));
		assertTrue(options.contains(new Option(Option.OptionType.ALLIN, player1.getBalance())));
	}

	@Test
	public void testGenerateOptionsShort() {
		player1.updateBalance(-100);
		var allin = new Option(Option.OptionType.ALLIN, player2.getBalance());
		hand.executeOption(player2, allin);

		var options = hand.generateOptions(player1);
		assertTrue(options.contains(new Option(Option.OptionType.CALL, allin.getAmount())));
		assertTrue(options.contains(new Option(Option.OptionType.FOLD, 0)));
	}

	@Test
	public void testGenerateOptionsNotEnoughToRaise() {
		player1.updateBalance(-1380);
		var bigBet = new Option(Option.OptionType.BET, 500);
		hand.executeOption(player2, bigBet);

		var options = hand.generateOptions(player1);
		assertTrue(options.contains(new Option(Option.OptionType.CALL, 500)));
		assertTrue(options.contains(new Option(Option.OptionType.ALLIN, 620)));
		assertTrue(options.contains(new Option(Option.OptionType.FOLD, 0)));
	}

	@Test
	public void testGenerateOptionsRaise() {
		hand.executeOption(player2, bet);

		var options = hand.generateOptions(player1);
        assertTrue(options.contains(new Option(Option.OptionType.CALL, bet.getAmount())));
        assertTrue(options.contains(new Option(Option.OptionType.RAISE, bet.getAmount() + 120)));
        assertTrue(options.contains(new Option(Option.OptionType.ALLIN, player1.getBalance())));
        assertTrue(options.contains(new Option(Option.OptionType.FOLD, 0)));
	}
}
