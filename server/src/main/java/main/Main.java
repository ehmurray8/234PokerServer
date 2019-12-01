package main;

import game.Rules;
import server.Server;

import static game.Rules.GameType.OMAHA;

public class Main {
    public static void main(String[] mainArgs) {
        Rules defaultRules = Rules.builder().smallBlind(1).bigBlind(2).ante(0).timeLimitSecs(30).maxCapacity(6)
                .gameType(OMAHA).minimumChipAmount(1).build();
        Server server = new Server(defaultRules);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}
