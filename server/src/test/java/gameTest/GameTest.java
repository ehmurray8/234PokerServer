package gameTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

import client.OptionSelection.SelectionType;
import client.OptionSelection;
import client.TestClientHandler;
import game.Game;
import game.TestGame;
import model.card.Card;
import model.player.TestPlayer;
import org.junit.Before;
import org.junit.Test;

import game.Rules;
import game.Rules.GameType;
import model.player.Player;

public class GameTest {

    private Rules rules;
	private ArrayList<Player> players;
	private TestClientHandler clientHandler;
	private TestGame game;

	@Before
	public void setUp() {
		rules = new Rules(1, 2, 1, 20, 6, GameType.TEST);
	    var p1 = new Player(1000, "Player 1");
		var p2 = new Player(1000, "Player 2");
		var p4 = new Player(1000, "Player 4");
		players = new ArrayList<>(Arrays.asList(p1, p2, null, p4));
        clientHandler = new TestClientHandler();
		game = new TestGame(players, rules, clientHandler, Collections.emptyList());
	}

	@Test
	public void testIncrementDealerNum() {
		assertEquals(0, game.getDealerNum());

		game.moveDealer();
		assertEquals(1, game.getDealerNum());

		game.moveDealer();
		assertEquals(3, game.getDealerNum());

		game.moveDealer();
		assertEquals(0, game.getDealerNum());
	}

	@Test
	public void testSmallBlind() {
		assertEquals(1, game.smallBlindNum());

		game.moveDealer();
		assertEquals(3, game.smallBlindNum());

		game.moveDealer();
		assertEquals(0, game.smallBlindNum());
	}

	@Test
	public void testBigBlind() {
		assertEquals(3, game.bigBlindNum());
		game.moveDealer();
		assertEquals(0, game.bigBlindNum());
	}

	@Test
	public void testTableNotFull() {
		assertTrue(!game.tableFull());
	}

	@Test
	public void testTableFull() {
	    try {
			game.addPlayer(new Player(100, "P3"));
			game.addPlayer(new Player(100, "P5"));
			game.addPlayer(new Player(100, "P6"));
		} catch(Game.TableFullException ignored) {}
		assertTrue(game.tableFull());
	}

	@Test
	public void testAddPlayer() {
		try {
			game.addPlayer(new Player(100, "P3"));
		} catch (Game.TableFullException ignored) { }
        assertEquals(4, game.getNumPlayers());
	}

	@Test
    public void testOverflowTable() {
	    try {
	        game.addPlayer(new Player(100, "P3"));
            game.addPlayer(new Player(100, "P5"));
            game.addPlayer(new Player(100, "P6"));
            game.addPlayer(new Player(100, "P7"));
            fail();
        } catch (Game.TableFullException ignored) { }
        assertEquals(6, game.getNumPlayers());
    }

    @Test
    public void testAddToEmptyTable() {
        for(var player : players) {
            game.removePlayer(player);
        }

	    try {
	        game.addPlayer(new Player(100, "P1"));
        } catch(Game.TableFullException ignored) { }
        assertEquals(1, game.getNumPlayers());
    }

    @Test
    public void testRemovePlayer() {
        game.removePlayer(players.get(0));
        assertEquals(2, game.getNumPlayers());
    }

    @Test
    public void testRemoveAll() {
	    players.forEach(game::removePlayer);
	    assertEquals(0, game.getNumPlayers());
    }

    @Test
    public void testNumPlayers() {
	    assertEquals(3, game.getNumPlayers());
    }

    @Test
    public void testTableNotEmpty() {
	    assertTrue(!game.tableEmpty());
    }

    @Test
    public void testTableEmpty() {
	    players.forEach(game::removePlayer);
	    assertTrue(game.tableEmpty());
    }

    @Test
    public void testGameLoop() {
        var player1 = new TestPlayer(200, "P1");
        var player2 = new TestPlayer(200, "P2");

        var preFlopList = Arrays.asList(new OptionSelection(SelectionType.CALL),
                                        new OptionSelection(SelectionType.CHECK));

        var flopList = Arrays.asList(new OptionSelection(SelectionType.CHECK),
                                     new OptionSelection(SelectionType.BET),
                                     new OptionSelection(SelectionType.CALL));

        var turnList = Arrays.asList(new OptionSelection(SelectionType.CHECK),
                                     new OptionSelection(SelectionType.CHECK));

        var riverList = Arrays.asList(new OptionSelection(SelectionType.BET),
                                      new OptionSelection(SelectionType.RAISE),
                                      new OptionSelection(SelectionType.CALL));

        var optionsList = new ArrayList<OptionSelection>();
        optionsList.addAll(preFlopList);
        optionsList.addAll(flopList);
        optionsList.addAll(turnList);
        optionsList.addAll(riverList);

        clientHandler.setOptionNumList(optionsList);

        var game = setupTwoPlayerTestGame(player1, player2);
        game.setNumRuns(1);
        game.runGame();

        assertEquals(191.0, player1.getBalance(), .1);
        assertEquals(209.0, player2.getBalance(), .1);
    }

    @Test
    public void testPreFlopRaise() {
        var player1 = new TestPlayer(200, "P1");
        var player2 = new TestPlayer(200, "P2");

        var preFlopList = Arrays.asList(new OptionSelection(SelectionType.RAISE, 10),
                                        new OptionSelection(SelectionType.CALL));

        var flopList = Arrays.asList(new OptionSelection(SelectionType.BET, 20),
                                     new OptionSelection(SelectionType.RAISE, 50),
                                     new OptionSelection(SelectionType.CALL));

        var turnList = Arrays.asList(new OptionSelection(2),
                                     new OptionSelection(SelectionType.CALL));

        var optionsList = new ArrayList<OptionSelection>();
        optionsList.addAll(preFlopList);
        optionsList.addAll(flopList);
        optionsList.addAll(turnList);

        clientHandler.setOptionNumList(optionsList);

        var game = setupTwoPlayerTestGame(player1, player2);
        game.setNumRuns(1);
        game.runGame();

        assertEquals(0, player1.getBalance(), 0.1);
        assertEquals(400, player2.getBalance(), 0.1);
    }

    @Test
    public void testRaiseWar() {
	    var player1 = new TestPlayer(2000, "P1");
        var player2 = new TestPlayer(1000, "P2");

        var preFlopList = Arrays.asList(new OptionSelection(SelectionType.CALL),
                                        new OptionSelection(SelectionType.BET, 10),
                                        new OptionSelection(SelectionType.RAISE, 25),
                                        new OptionSelection(SelectionType.CALL));
        // $56 in pot

        var flopList = Arrays.asList(new OptionSelection(SelectionType.BET, 40),
                                     new OptionSelection(SelectionType.RAISE, 100),
                                     new OptionSelection(SelectionType.RAISE, 200),
                                     new OptionSelection(SelectionType.RAISE, 400),
                                     new OptionSelection(3),
                                     new OptionSelection(SelectionType.CALL));

        var optionsList = new ArrayList<OptionSelection>();
        optionsList.addAll(preFlopList);
        optionsList.addAll(flopList);

        clientHandler.setOptionNumList(optionsList);

        var game = setupTwoPlayerTestGame(player1, player2);
        game.setNumRuns(1);
        game.runGame();

        assertEquals(1000, player1.getBalance(), 0.1);
        assertEquals(2000, player2.getBalance(), 0.1);
    }

    @Test
    public void  testTwoGameRuns() {
        var player1 = new Player(200, "P1");
        var player2 = new Player(200, "P2");

        var preFlopList = Arrays.asList(new OptionSelection(SelectionType.CALL), new OptionSelection(SelectionType.CHECK));

        var flopList = Arrays.asList(new OptionSelection(SelectionType.BET), new OptionSelection(SelectionType.CALL));

        var turnList = Arrays.asList(new OptionSelection(SelectionType.BET, 10),
                new OptionSelection(SelectionType.RAISE, 25), new OptionSelection(SelectionType.CALL));

        var riverList = Arrays.asList(new OptionSelection(SelectionType.CHECK), new OptionSelection(SelectionType.CHECK));

        var nextHandList = Arrays.asList(new OptionSelection(SelectionType.RAISE), new OptionSelection(SelectionType.FOLD));

        var allActions = new ArrayList<OptionSelection>();
        allActions.addAll(preFlopList);
        allActions.addAll(flopList);
        allActions.addAll(turnList);
        allActions.addAll(riverList);
        allActions.addAll(nextHandList);

        clientHandler.setOptionNumList(allActions);

        var game = setupTwoPlayerTestGame(player1, player2);
        game.setNumRuns(2);
        game.runGame();

        assertEquals(173, player1.getBalance(), 0.1);
        assertEquals(227, player2.getBalance(), 0.1);
    }

    private TestGame setupTwoPlayerTestGame(Player player1, Player player2) {
        var card1 = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        var card2 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);
        var card3 = new Card(Card.Rank.KING, Card.Suit.DIAMONDS);
        var card4 = new Card(Card.Rank.KING, Card.Suit.HEARTS);

        player1.addCard(card1);
        player1.addCard(card2);
        player2.addCard(card3);
        player2.addCard(card4);

        var communityCards = Arrays.asList(new Card(Card.Rank.TWO, Card.Suit.HEARTS),
                new Card(Card.Rank.TEN, Card.Suit.CLUBS), new Card(Card.Rank.FOUR, Card.Suit.DIAMONDS),
                new Card(Card.Rank.SEVEN, Card.Suit.CLUBS), new Card(Card.Rank.KING, Card.Suit.SPADES));
        var game = new TestGame(Arrays.asList(player1, player2), rules, clientHandler, communityCards);

        clientHandler.setGameType(GameType.HOLDEM);
        return game;
    }
}
