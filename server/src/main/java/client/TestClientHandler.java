package client;

import game.Rules;
import model.option.Option;

import java.util.List;
import java.util.UUID;

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

    public void resetOptionNum() {
        currentSelectionNum = 0;
    }

    @Override
    public Rules.GameType getDesiredGameType(UUID playerId) {
        return gameType;
    }

    @Override
    public Option getDesiredOption(UUID playerId, List<Option> options) {
        var selection = optionNumList.get(currentSelectionNum++);
        var option = options.get(selection.getIndex());
        if(selection.getAmount() != 0) {
            return new Option(option.getType(), selection.getAmount());
        }
        return option;
    }
}
