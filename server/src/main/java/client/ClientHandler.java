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

    private static Random RANDOM_GENERATOR = new Random();

    public ClientHandler(SocketIOServer server) {
        this();
        this.server = server;
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
            var actingPlayerData = ClientMessage.createClientMessage(player, options, null, players, hand, timeoutSeconds);
            client.sendEvent("gameUpdate", actingPlayerData);
            optionSelection = options.get(0);

            if (this.server != null) {
                this.server.addEventListener("option" + eventId, Map.class, (responseClient, data, ackRequest) -> {
                    var optionType = Option.stringToOptionType(data.get("type"));
                    if (optionType != null) {
                        try {
                            String amountString = (String) data.get("amount");
                            var amount = Double.parseDouble(amountString);
                            optionSelection = new Option(optionType, amount);
                            notify();
                        } catch (ClassCastException | NumberFormatException ignored) { } }
                });
            }

            for (var otherPlayer: players) {
                var otherPlayerData = ClientMessage.createClientMessage(otherPlayer, null, null,
                        players, hand, timeoutSeconds);
                clients.get(otherPlayer.getPlayerId()).sendEvent("gameUpdate", otherPlayerData);
            }

            try {
                wait(timeoutSeconds * 1000 + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                this.server.removeAllListeners("option" + eventId);
                return options.get(0);
            }

            this.server.removeAllListeners("option" + eventId);

            return optionSelection;
        }
    }

    public void sendHandOverMessage(Hand hand, List<Player> players, List<Card> winningCards) {
        for(var player: players) {
            var message = ClientMessage.createClientMessage(player, null, winningCards, players, hand, 0);
            clients.get(player.getPlayerId()).sendEvent("gameUpdate", message);
        }
    }
}
