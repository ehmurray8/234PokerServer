package client;

import game.Rules;
import model.option.Option;

import java.util.List;
import java.util.UUID;

public class TestClientHandler extends ClientHandler {

    private Rules.GameType gameType = Rules.GameType.HOLDEM;
    private Option option = null;

    public void setGameType(Rules.GameType gameType) {
        this.gameType = gameType;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    @Override
    public Rules.GameType getDesiredGameType(UUID playerId) {
        return gameType;
    }

    @Override
    public Option getDesiredOption(UUID playerId, List<Option> options) {
        return option;
    }
}
