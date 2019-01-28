package server;

import client.ClientHandler;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import game.Game;
import game.Rules;
import model.player.Player;

import java.util.*;

public class Server {

    private static final double DEFAULT_BALANCE = 200.0;

    private static final String WELCOME_EVENT = "welcomeStatus";
    private static final String RETURN_TO_GAME_EVENT = "returnToGame";
    public static final String GAME_UPDATE_EVENT = "gameUpdate";
    private static final String JOIN_GAME_EVENT = "joinGame";

    public static void main(String[] mainArgs) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3001);

        final SocketIOServer server = new SocketIOServer(config);

        var defaultRules = new Rules(1.0, 2.0, 0, 30, 6, Rules.GameType.OMAHA, 1);
        var defaultClientHandler = new ClientHandler(server);

        var singleGame = new Game(Collections.emptyList(), defaultRules, defaultClientHandler);
        var usernameMap = new HashMap<String, UUID>();

        server.addConnectListener(client -> {
            System.out.println("Connect: " + client.toString());
        });

        server.addEventListener(WELCOME_EVENT, Map.class, (client, data, ackRequest) -> {
            System.out.println("Welcome status " + client.getSessionId());
            String username;
            try {
                username = (String) data.get("username");
            } catch (ClassCastException ignored) {
                username = null;
            }

            if (username != null && usernameMap.containsKey(username)) {
                defaultClientHandler.addClient(usernameMap.get(username), client);
                var payload = new HashMap<String, Boolean>();
                payload.put("joinGame", true);
                System.out.println("Sending return to game event");
                client.sendEvent(RETURN_TO_GAME_EVENT, payload);
                var message = defaultClientHandler.getPendingMessage(usernameMap.get(username));
                if (message != null) {
                    client.sendEvent(GAME_UPDATE_EVENT, message);
                }
            }
        });

        server.addEventListener(JOIN_GAME_EVENT, Map.class, (client, data, ackRequest) -> {
            System.out.println("Join Game: " + client.getSessionId());
            LinkedHashMap map;
            String username;
            try {
                map = (LinkedHashMap) data;
                username = (String) map.get("username");
            } catch (ClassCastException ignored) { return; }

            if (usernameMap.containsKey(username)) {
                var playerId = usernameMap.get(username);
                if (!singleGame.hasPlayer(playerId)) {
                    var player = new Player(DEFAULT_BALANCE, username, usernameMap.get(username));
                    singleGame.addPlayer(player);
                    defaultClientHandler.addClient(playerId, client);
                }
            } else {
                var uuid = UUID.randomUUID();
                var player = new Player(DEFAULT_BALANCE, username, uuid);
                usernameMap.put(username, uuid);
                singleGame.addPlayer(player);
                defaultClientHandler.addClient(uuid, client);
            }

            if (singleGame.getNumPlayers() >= 2) {
                System.out.println("Starting game...");
                singleGame.runGame();
            }
        });

        server.addDisconnectListener(client -> {
            System.out.println("Disconnect");
        });

        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        server.start();
    }
}
