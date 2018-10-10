package client;

import game.Rules;
import model.option.Option;

import java.util.List;
import java.util.UUID;

public class ClientHandler {

    public Rules.GameType getDesiredGameType(UUID playerId) {
        return Rules.GameType.HOLDEM;
    }

    public Option getDesiredOption(UUID playerId, List<Option> options) {
        return options.get(0);
    }
}
