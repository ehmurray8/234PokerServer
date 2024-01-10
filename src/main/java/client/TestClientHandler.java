package client;

import game.RegularUpdate;
import game.Rules;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import model.option.Option;

public class TestClientHandler extends ClientHandler {

    private Rules.GameType gameType = Rules.GameType.HOLDEM;
    private List<OptionSelection> optionNumList = null;
    private int currentSelectionNum = 0;


    public void setGameType(Rules.GameType gameType) {
        this.gameType = gameType;
    }

    public void setOptionNumList(List<OptionSelection> optionNumList) {
        this.optionNumList = optionNumList;
    }

    @Override
    public Rules.GameType getDesiredGameType(UUID playerId) {
        return gameType;
    }

    @Override
    public Option getDesiredOption(
        UUID playerId, HashMap<UUID, RegularUpdate> updates, List<Option> options
    ) {
        var selection = optionNumList.get(currentSelectionNum++);
        var option = options.get(selection.getIndex());
        if (selection.getAmount() != 0) {
            return new Option(option.getType(), selection.getAmount());
        }
        return option;
    }
}
