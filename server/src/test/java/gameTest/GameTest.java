/**
 * 
 */
package gameTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import game.TestClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import game.Game;
import game.Game.TableFullException;
import game.Rules;
import game.Rules.GameType;
import model.player.Player;

public class GameTest {

	private Rules rules;
	private TestClient testClient;
	private ArrayList<Player> players;

	@Before
	public void setUp() {
	    rules = new Rules(1, 2, 1, 20, 6, GameType.MIXED);
	    Player p1 = new Player(1000, "Player 1");
		Player p2 = new Player(1000, "Player 2");
	    players = new ArrayList<>(Arrays.asList(p1, p2));
	    testClient = new TestClient();
	}

	@Test
	public void testIncrementDealerNum() {
		players.add(null);
		players.add(new Player(1000, "Player 4"));
		Game game = new Game(players, rules, true, testClient);

		assertEquals(0, game.getDealerNum());

		game.incrementDealerNum();
		assertEquals(1, game.getDealerNum());

		game.incrementDealerNum();
		assertEquals(3, game.getDealerNum());

		game.incrementDealerNum();
		assertEquals(0, game.getDealerNum());
	}

	@Test
	public void testSmallBlind() {
		players.add(null);
		players.add(new Player(1000, "Player 4"));
		Game game = new Game(players, rules, true, testClient);

		assertEquals(1, game.smallBlindNum());

		game.incrementDealerNum();
		assertEquals(3, game.smallBlindNum());

		game.incrementDealerNum();
		assertEquals(0, game.smallBlindNum());
	}

	@Test
	public void testBigBlind() {
		players.add(null);
		players.add(new Player(1000, "Player 4"));
		Game game = new Game(players, rules, true, testClient);

		assertEquals(3, game.bigBlindNum());

		game.incrementDealerNum();
		assertEquals(0, game.bigBlindNum());
	}

	@Test
	@Ignore
	public void testIncrementCurrentAction() {
		players.add(null);
		players.add(new Player(1000, "Player 4"));
		Game game = new Game(players, rules, true, testClient);

		assertEquals(1, game.getCurrentAction());

		game.incrementCurrentAction();
		assertEquals(3, game.getCurrentAction());

		game.incrementCurrentAction();
		assertEquals(0, game.getCurrentAction());
	}

	@Test
	@Ignore
	public void testAddPlayer() {
		players.add(null);
		players.add(new Player(1000, "Player 4"));
		Game game = new Game(players, rules, true, testClient);

		try {
			game.addPlayer(new Player(1000, "Player 3"));
			assertNotEquals(null, game.getPlayers()[2]);

			game.addPlayer(new Player(1000, "Player 5"));
			assertEquals(5, game.getNumPlayers());

			game.addPlayer(new Player(1000, "Player 6"));
			assertEquals(6, game.getNumPlayers());
		} catch (TableFullException tfe) {
			fail("Table should not be full.");
		}

		assertThrows(TableFullException.class, () -> game.addPlayer(new Player(1000, "Player 7")));
	}


//	@Test
	public void testGame1() {
		Player p1 = new Player(500, "P1");
		Player p2 = new Player(500, "P2");
		List<Player> players = new ArrayList<>(Arrays.asList(p1, p2));
		Rules rules = new Rules(2., 4., 1., 30, 2, GameType.MIXED);
		testClient = new TestClient();
		Game game = new Game(players, rules, true, testClient);
		game.startGame();
        assertEquals(1000., p1.getBalance() + p2.getBalance(), 0.0);
	}
}
