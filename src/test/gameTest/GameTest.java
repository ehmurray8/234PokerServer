/**
 * 
 */
package test.gameTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import game.Game;
import game.Rules;
import game.Rules.GameType;
import model.player.Player;

/**
 * @author Emmet
 */
public class GameTest {

	@Test
	public void testGame1() {
		Player p1 = new Player(500, "P1");
		Player p2 = new Player(500, "P2");
		List<Player> players = new ArrayList<Player>(Arrays.asList(new Player[] {p1, p2}));
		Rules rules = new Rules(2., 4., 1., 30, 2, GameType.MIXED);
		Game game = new Game(players, rules);
		game.startGame();
		assertTrue(p1.getBalance() + p2.getBalance() == 1000.);
	}
}
