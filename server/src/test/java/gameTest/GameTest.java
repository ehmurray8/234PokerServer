package gameTest;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

import client.ClientHandler;
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
	private ClientHandler clientHandler;
	private TestGame game;

	@Before
	public void setUp() {
		rules = new Rules(1, 2, 1, 20, 6, GameType.MIXED);
	    var p1 = new Player(1000, "Player 1");
		var p2 = new Player(1000, "Player 2");
		var p4 = new Player(1000, "Player 4");
		players = new ArrayList<>(Arrays.asList(p1, p2, null, p4));
		game = new TestGame(players, rules, clientHandler);
	    clientHandler = new ClientHandler();
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
	    var card1 = new Card(Card.Rank.ACE, Card.Suit.SPADES);
        var card2 = new Card(Card.Rank.ACE, Card.Suit.CLUBS);

        var card3 = new Card(Card.Rank.KING, Card.Suit.DIAMONDS);
        var card4 = new Card(Card.Rank.KING, Card.Suit.HEARTS);

        var player1 = new TestPlayer(200, "P1");
        var player2 = new TestPlayer(200, "P2");
        var game = new Game(Arrays.asList(player1, player2), rules, clientHandler);

        player1.setHand(new Card[]{card1, card2});
        player2.setHand(new Card[]{card3, card4});
    }
}
