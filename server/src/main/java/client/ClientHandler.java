package client;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import game.Rules;
import model.card.Card;
import model.hand.representation.Hand;
import model.option.Option;
import model.player.Player;

import java.util.*;

public class ClientHandler {

    private Map<UUID, SocketIOClient> clients;
    private SocketIOServer server = null;
    private Option optionSelection = null;
    private Map<UUID, ClientMessage> pendingMessages;

    private static Random RANDOM_GENERATOR = new Random();

    public ClientHandler(SocketIOServer server) {
        this();
        this.server = server;
        this.pendingMessages = new HashMap<>();
    }

    public ClientMessage getPendingMessage(UUID playerId) {
        return pendingMessages.get(playerId);
    }

    ClientHandler() {
        clients = new HashMap<>();
    }

    public void addClient(UUID playerId, SocketIOClient client) {
        clients.remove(playerId);
        clients.put(playerId, client);
    }

    public Rules.GameType getDesiredGameType(UUID playerId) {
        return Rules.GameType.HOLDEM;
    }

    public Option getDesiredOption(Player player, List<Option> options, int timeoutSeconds, Hand hand, List<Player> players) {
        var client = clients.get(player.getPlayerId());
        var eventId = RANDOM_GENERATOR.nextInt();
        if (client == null) {
            return options.get(0);
        } else {
            var actingPlayerData = ClientMessage.createClientMessage(player, options, null, players, hand, timeoutSeconds, eventId);
            client.sendEvent("gameUpdate", actingPlayerData);
            pendingMessages.put(player.getPlayerId(), actingPlayerData);
            optionSelection = options.get(0);

            if (this.server != null) {
                this.server.addEventListener("option" + eventId, Map.class, (responseClient, data, ackRequest) -> {
                    var optionType = Option.stringToOptionType(data.get("type"));
                    if (optionType != null) {
                        try {
                            String amountString = (String) data.get("amount");
                            var amount = Double.parseDouble(amountString);
                            optionSelection = new Option(optionType, amount);
                            notifyAll();
                        } catch (ClassCastException | NumberFormatException ignored) { } }
                });
            }

            for (var otherPlayer: players) {
                if (otherPlayer != null && otherPlayer.getPlayerId() != player.getPlayerId()) {
                    var otherPlayerData = ClientMessage.createClientMessage(otherPlayer, null, null,
                            players, hand, timeoutSeconds, eventId);
                    var otherPlayerId = otherPlayer.getPlayerId();
                    clients.get(otherPlayerId).sendEvent("gameUpdate", otherPlayerData);
                    pendingMessages.put(otherPlayerId, otherPlayerData);
                }
            }

            try {
                System.out.println("Waiting... " + eventId);
                wait(timeoutSeconds * 1_000 + 1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Selected: " + optionSelection.toString());
                this.server.removeAllListeners("option" + eventId);
                this.pendingMessages.clear();
                return options.get(0);
            }

            System.out.println("Selected: " + optionSelection.toString());
            this.pendingMessages.clear();
            this.server.removeAllListeners("option" + eventId);

            return optionSelection;
        }
    }

    public void sendHandOverMessage(Hand hand, List<Player> players, List<Card> winningCards) {
        for(var player: players) {
            if (player.getPlayerId() != null) {
                var message = ClientMessage.createClientMessage(player, null, winningCards, players,
                        hand, 0, -1);
                clients.get(player.getPlayerId()).sendEvent("gameUpdate", message);
                pendingMessages.put(player.getPlayerId(), message);
            }
        }
        try {
            wait(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pendingMessages.clear();
    }
}
