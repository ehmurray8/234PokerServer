package game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.card.Card;
import model.option.Option;
import model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegularUpdate {
    final String messageType = "regularType";
    final String gameType;
    final List<String> mainPlayerHand = new ArrayList<>();
    final String mainPlayerUsername;
    final int mainPosition;
    final List<String> players = new ArrayList<>();
    final List<String> playersInHand = new ArrayList<>();
    final List<Double> playerStacks = new ArrayList<>();
    final List<String> communityCards = new ArrayList<>();
    final List<String> options = new ArrayList<>();
    final int actingPlayerPosition;
    final List<String> playersLastActions = new ArrayList<>();
    final int actionLimit;

    RegularUpdate(Rules.GameType gameType, Player mainPlayer, int mainPosition, Player[] players,
                  List<Player> playersInHand, List<Card> communityCards, int actingPlayerPosition, int actionLimit) {
        this.gameType = gameType.name();
        this.mainPlayerHand.addAll(mainPlayer.getHand().stream().map(Card::toClientString).collect(Collectors.toList()));
        mainPlayerUsername = mainPlayer.getName();
        this.mainPosition = mainPosition;

        var playersStream = Arrays.stream(players);
        this.players.addAll(playersStream.map(Player::getName).collect(Collectors.toList()));
        this.playersInHand.addAll(playersInHand.stream().map(Player::getName).collect(Collectors.toList()));
        this.playerStacks.addAll(playersStream.map(Player::getBalance).collect(Collectors.toList()));
        this.communityCards.addAll(communityCards.stream().map(Card::toClientString).collect(Collectors.toList()));
        this.actingPlayerPosition = actingPlayerPosition;
        this.actionLimit = actionLimit;
    }

    public void setOptions(List<Option> options) {
        this.options.addAll(options.stream().map(Option::toString).collect(Collectors.toList()));
    }

    public int getActionLimit() {
        return actionLimit;
    }

    public String toJson() {
        var mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
