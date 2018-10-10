package game;

import client.ClientHandler;
import model.player.Player;

import java.util.List;

public class TestGame extends Game {

    public TestGame(List<Player> players, Rules rules, ClientHandler clientHandler) {
        super(players, rules, clientHandler);
        dealerNum = 0;
    }

    public void moveDealer() {
        incrementDealerNum();
    }
}
