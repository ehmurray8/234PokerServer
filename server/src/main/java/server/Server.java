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

    public static void main(String[] mainArgs) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(3001);

        final SocketIOServer server = new SocketIOServer(config);

        var defaultRules = new Rules(1.0, 2.0, 0, 30, 6, Rules.GameType.HOLDEM);
        var defaultClientHandler = new ClientHandler(server);

        var singleGame = new Game(Collections.emptyList(), defaultRules, defaultClientHandler);
        var usernameMap = new HashMap<String, UUID>();


        server.addConnectListener(client -> {
            System.out.println(client.toString());
        });

        server.addEventListener("joinGame", Map.class, (client, data, ackRequest) -> {
            var map = (LinkedHashMap) data;
            var username = (String) map.get("username");

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
        });

        server.addDisconnectListener(client -> {
            System.out.println("Disconnect");
        });

        server.start();
    }
}
