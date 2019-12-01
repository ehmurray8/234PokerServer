package server;

import client.ClientHandler;
import client.ClientMessage;
import client.ClientWrapper;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import game.Game;
import game.Rules;
import model.option.Option;
import model.player.Player;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    private static final int DEFAULT_BALANCE = 200;
    private static final int NUMBER_OF_THREADS = 5;

    private static final String WELCOME_EVENT = "welcomeStatus";
    private static final String RETURN_TO_GAME_EVENT = "returnToGame";
    public static final String GAME_UPDATE_EVENT = "gameUpdate";
    private static final String JOIN_GAME_EVENT = "joinGame";

    private static final Configuration CONFIGURATION;

    static {
        CONFIGURATION = new Configuration();
        CONFIGURATION.setHostname("192.168.0.163");
        CONFIGURATION.setPort(3001);
    }

    private SocketIOServer server;
    private Map<String, UUID> usernameMap;
    private Map<UUID, UUID> playerIdToGameIdMap;
    private Map<UUID, Game> gameMap;

    private ExecutorService threadPoolExecutor;

    private final UUID singleGameId;

    public Server(final Rules rules) {
        server = new SocketIOServer(CONFIGURATION);
        usernameMap = new HashMap<>();
        playerIdToGameIdMap = new HashMap<>();
        gameMap = new HashMap<>();
        singleGameId = UUID.randomUUID();
        setupGameOptionEvent(singleGameId);
        Game game = new Game(Collections.emptyList(), rules, new ClientHandler());
        gameMap.put(singleGameId, game);
        threadPoolExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    public void start() {
        setupConnectAndDisconnectListeners();
        setupWelcomeEvent();
        setupJoinGameEvent();
        server.start();
    }

    public void stop() {
        server.stop();
    }

    private void setupConnectAndDisconnectListeners() {
        server.addConnectListener(client -> System.out.println("Connect: " + client.getSessionId()));
        server.addDisconnectListener(client -> System.out.println("Disconnect: " + client.getSessionId()));
    }

    private void setupWelcomeEvent() {
        handleServerEvent(WELCOME_EVENT, (client, data, ackRequest) -> {
            String username = getUsername(data);
            UUID playerId = usernameMap.get(username);
            UUID gameId = playerIdToGameIdMap.get(playerId);
            if (gameId != null) {
                Game game = gameMap.get(gameId);
                addClientToGame(game, client, playerId);
                Map<String, UUID> payload = new HashMap<>();
                payload.put("gameId", gameId);
                System.out.println("Sending return to game event, for game: " + gameId);
                client.sendEvent(RETURN_TO_GAME_EVENT, payload);
                ClientMessage message = game.getClientHandler().getPendingMessage(usernameMap.get(username));
                if (message != null) {
                    client.sendEvent(GAME_UPDATE_EVENT, message);
                }
            }
        });
    }

    private void setupJoinGameEvent() {
        handleServerEvent(JOIN_GAME_EVENT, (client, data, ackRequest) -> {
            Game game;
            String username = getUsername(data);
            UUID playerId = usernameMap.get(username);
            if (playerId == null) {
                UUID newPlayerId = UUID.randomUUID();
                usernameMap.put(username, newPlayerId);
                game = getOpenGame();
                addPlayerToGame(newPlayerId, username, client, singleGameId);
            } else {
                UUID gameId = playerIdToGameIdMap.get(playerId);
                game = gameMap.get(gameId) == null ? getOpenGame() : gameMap.get(gameId);
                if (gameId == null || !game.hasPlayer(playerId)) {
                    addPlayerToGame(playerId, username, client, gameId);
                } else {
                    addClientToGame(game, client, playerId);
                }
            }

            if (game.getNumPlayers() >= 2) {
                threadPoolExecutor.execute(game::runGame);
            }
        });
    }

    private void addPlayerToGame(final UUID playerId, final String username, final SocketIOClient client, final UUID gameId) {
        playerIdToGameIdMap.put(playerId, gameId);
        Player player = new Player(DEFAULT_BALANCE, username, playerId);
        Game game = gameMap.get(gameId);
        game.addPlayer(player);
        addClientToGame(game, client, playerId);
    }

    private Game getOpenGame() {
        return gameMap.get(singleGameId);
    }

    private void addClientToGame(final Game game, final SocketIOClient client, final UUID playerId) {
        ClientHandler clientHandler = game.getClientHandler();
        ClientWrapper clientWrapper = new ClientWrapper(client);
        clientHandler.addClient(playerId, clientWrapper);
    }

    private void setupGameOptionEvent(final UUID gameId) {
        handleServerEvent("option" + gameId.toString(), (responseClient, data, ackRequest) -> {
            try {
                Option.OptionType optionType = Option.OptionType.valueOf((String) data.get("type"));
                int amount = Integer.parseInt(data.get("amount").toString());
                String eventId = (String) data.get("eventId");
                Option option = new Option(optionType, amount);
                gameMap.get(gameId).getClientHandler().resolveOptionSelection(eventId, option);
            } catch (ClassCastException | IllegalArgumentException e) {
                System.out.println("Invalid option type: " + data.get("type"));
            }
        });
    }

    private void handleServerEvent(final String eventName, final DataListener<Map> dataListener) {
        server.addEventListener(eventName, Map.class, (client, data, ackSender) -> {
            try {
                logEvent(eventName, client);
                dataListener.onData(client, data, ackSender);
            } catch (RuntimeException e) {
                logException(eventName + " failure", e);
            }
        });
    }

    private static String getUsername(final Map map) {
        return (String) map.get("username");
    }

    private static void logEvent(final String eventName, final SocketIOClient client) {
        System.out.println(eventName + ": " + client.getSessionId());
    }

    private static void logException(final String message, final Exception e) {
        System.out.println(message + ": " + e.getMessage() + ".\n" + Arrays.toString(e.getStackTrace()));
    }
}
