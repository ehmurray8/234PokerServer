package gameTest;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.*;

import game.TestClient;
import org.junit.Before;
import org.junit.Test;

import game.Game;
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
}
