package game;

import model.option.Option;

public class TestClient {

    private Rules.GameType desiredGameType;
    private Option desiredOption;

    public Rules.GameType getDesiredGameType() {
        return desiredGameType;
    }

    public void setDesiredGameType(Rules.GameType gameType) {
        desiredGameType = gameType;
    }

    public void setDesiredOption(Option option) {
        desiredOption = option;
    }

    public Option getDesiredOption() {
        return desiredOption;
    }
}
