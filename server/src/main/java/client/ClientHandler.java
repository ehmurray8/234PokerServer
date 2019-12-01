package client;

import game.Rules;
import model.card.Card;
import model.hand.representation.Hand;
import model.option.Option;
import model.option.OptionMonitor;
import model.player.Player;

import java.util.*;

public class ClientHandler {

    private Map<UUID, ClientWrapper> clients;
    private Map<UUID, ClientMessage> pendingMessages;
    private Map<UUID, OptionMonitor> eventMonitors;

    public ClientHandler() {
        clients = new HashMap<>();
        this.pendingMessages = new HashMap<>();
        this.eventMonitors = new HashMap<>();
    }

    public ClientMessage getPendingMessage(UUID playerId) {
        return pendingMessages.get(playerId);
    }

    public void addClient(UUID playerId, ClientWrapper client) {
        clients.remove(playerId);
        clients.put(playerId, client);
    }

    public Rules.GameType getDesiredGameType(UUID playerId) {
        return Rules.GameType.HOLDEM;
    }

    public Option getDesiredOption(Player player, List<Option> options, int timeoutSeconds, Hand hand, List<Player> players) {
        var client = clients.get(player.getPlayerId());
        var eventId = UUID.randomUUID();
        eventMonitors.put(eventId, new OptionMonitor());

        Option optionSelection = options.get(0);
        if (client != null) {
            ClientMessage actingPlayerData = ClientMessage.createClientMessage(player, options, null,
                    players, hand, timeoutSeconds, eventId.toString());
            client.sendGameUpdateEvent(actingPlayerData);
            pendingMessages.put(player.getPlayerId(), actingPlayerData);

            for (var otherPlayer: players) {
                if (otherPlayer != null && otherPlayer.getPlayerId() != player.getPlayerId()) {
                    var otherPlayerData = ClientMessage.createClientMessage(otherPlayer, null, null,
                            players, hand, timeoutSeconds, eventId.toString());
                    var otherPlayerId = otherPlayer.getPlayerId();
                    clients.get(otherPlayerId).sendGameUpdateEvent(otherPlayerData);
                    pendingMessages.put(otherPlayerId, otherPlayerData);
                }
            }

            try {
                System.out.println("Waiting... " + eventId + " for - " + timeoutSeconds);
                synchronized (eventMonitors.get(eventId)) {
                    eventMonitors.get(eventId).wait(timeoutSeconds * 1_000 + 500);
                }
                optionSelection = eventMonitors.get(eventId).getOption();
                System.out.println("Selected (Properly): " + optionSelection.toString());
            } catch (InterruptedException | NullPointerException e) {
                e.printStackTrace();
                optionSelection = options.get(0);
                System.out.println("Selected (Exception): " + optionSelection.toString());
            }
        }
        return optionSelection;
    }

    public void resolveOptionSelection(final String eventString, final Option option) {
        UUID eventId = UUID.fromString(eventString);
        eventMonitors.get(eventId).setOption(option);
        synchronized (eventMonitors.get(eventId)) {
            eventMonitors.get(eventId).notify();
        }
    }

    public void sendHandOverMessage(Hand hand, List<Player> players, List<Card> winningCards) {
        for(var player: players) {
            if (player != null && player.getPlayerId() != null) {
                var message = ClientMessage.createClientMessage(player, null, winningCards, players,
                        hand, 0, "");
                clients.get(player.getPlayerId()).sendGameUpdateEvent(message);
                pendingMessages.put(player.getPlayerId(), message);
            }
        }
        try {
            Thread.sleep(7500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pendingMessages.clear();
    }
}
