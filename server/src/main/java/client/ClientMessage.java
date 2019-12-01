package client;

import model.card.Card;
import model.hand.representation.Hand;
import model.option.Option;
import model.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Date;

public class ClientMessage {

    private List<ClientPlayer> players;
    private List<Card> communityCards;
    private int mainPotAmount;
    private List<Card> userCards;
    private int userStackSize;
    private int numberOfCards;
    private String username;
    private List<Option> options;
    private int lastUserAmount;
    private List<Integer> lastActionAmounts;
    private List<Boolean> raiseCommunityCards;
    private List<Boolean> raiseUserCards;
    private boolean userHasFolded;
    private long decisionEndTime;
    private int decisionTimeMaxSeconds;
    private String eventId;
    private int betStepSize;

    private static ClientPlayer createPlayer(Player player, @Nullable List<Card> winningCards, boolean showCards) {
        String name = player.getName();
        int balance = player.getBalance();
        List<Card> cards = null;
        if (!player.hasFolded() && showCards) {
            cards = player.getHand();
        } else if (!player.hasFolded()) {
            cards = new ArrayList<>();
        }
        List<Boolean> raiseCards = new ArrayList<>();
        if (winningCards != null) {
            raiseCards.addAll(player.getHand().stream().map(winningCards::contains).collect(Collectors.toList()));
        }
        return new ClientPlayer(name, balance, cards, raiseCards);
    }

    static ClientMessage createClientMessage(Player player, @Nullable List<Option> options,
                                             @Nullable List<Card> winningCards, List<Player> players, Hand hand,
                                             int decisionTimeMaxSeconds, String eventId) {

        int mainPlayerIndex = players.stream().filter(Objects::nonNull).map(Player::getPlayerId)
                .collect(Collectors.toList()).indexOf(player.getPlayerId());

        ClientMessage message = new ClientMessage();
        message.setUserInfo(player, winningCards, options);
        message.setPlayerInfo(players, winningCards, mainPlayerIndex, hand);
        message.setHandInfo(hand, winningCards);
        message.setDecisionEndTimeFrom(decisionTimeMaxSeconds);
        message.setDecisionTimeMaxSeconds(decisionTimeMaxSeconds);
        message.setEventId(eventId);
        message.setBetStepSize(hand.getBetStepSize());

        if (hand.getWinningPlayers().contains(player)) {
            message.setLastUserAmount(hand.getWinnings());
        }
        return message;
    }

    private void setUserInfo(Player player, @Nullable List<Card> winningCards, @Nullable List<model.option.Option> options) {
        userCards = player.getHand();
        userStackSize = player.getBalance();
        lastUserAmount = 0;
        if (winningCards == null) {
            lastUserAmount = player.getAmountThisTurn();
        }
        username = player.getName();
        if (winningCards != null) {
            raiseUserCards = player.getHand().stream().map(winningCards::contains).collect(Collectors.toList());
        }
        userHasFolded = player.hasFolded();
        if (options != null) {
            this.options.addAll(options);
        }
    }

    private void setPlayerInfo(List<Player> players, @Nullable List<Card> winningCards,
                               int mainPlayerIndex, Hand hand) {
        this.players = new ArrayList<>();
        this.lastActionAmounts = new ArrayList<>();
        for (int i = mainPlayerIndex + 1, count = 0; count < players.size(); i = (i + 1) % players.size(), count++) {
            Player player = players.get(i);
            if (player != null && mainPlayerIndex != i) {
                boolean showCards = winningCards != null;
                if (player.hasFolded() || hand.onlyOnePlayerLeftInHand()) {
                    showCards = false;
                }
                this.players.add(createPlayer(player, winningCards, showCards));
                if (winningCards == null && !player.hasFolded()) {
                    lastActionAmounts.add(player.getAmountThisTurn());
                } else if (winningCards != null && !player.hasFolded() && hand.getWinningPlayers().contains(player)) {
                    lastActionAmounts.add(hand.getWinnings());
                } else {
                    lastActionAmounts.add(0);
                }
            }
        }
    }

    private void setHandInfo(Hand hand, @Nullable List<Card> winningCards) {
        communityCards.clear();
        communityCards.addAll(hand.getCommunityCards());
        mainPotAmount = hand.getTotalAmountInPots();
        numberOfCards = hand.getNumberOfCards();
        if (winningCards != null) {
            this.raiseCommunityCards = hand.getCommunityCards().stream().map(winningCards::contains).collect(Collectors.toList());
        }
    }

    private void setDecisionEndTimeFrom(int decisionTimeMaxSeconds) {
        long currentMilliseconds = new Date().getTime();
        decisionEndTime = currentMilliseconds + (decisionTimeMaxSeconds * 1000);
    }

    private ClientMessage() {
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.mainPotAmount = 0;
        this.userCards = new ArrayList<>();
        this.userStackSize = 0;
        this.username = "";
        this.options = new ArrayList<>();
        this.lastUserAmount = 0;
        this.lastActionAmounts = new ArrayList<>();
        this.raiseCommunityCards = new ArrayList<>();
        this.raiseUserCards = new ArrayList<>();
        this.userHasFolded = false;
        this.eventId = "";
        this.numberOfCards = 0;
    }

    public List<ClientPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<ClientPlayer> players) {
        this.players = players;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public int getMainPotAmount() {
        return mainPotAmount;
    }

    public List<Card> getUserCards() {
        return userCards;
    }

    public int getUserStackSize() {
        return userStackSize;
    }

    public List<Option> getOptions() {
        return options;
    }

    public int getLastUserAmount() {
        return lastUserAmount;
    }

    private void setLastUserAmount(int lastUserAmount) {
        this.lastUserAmount = lastUserAmount;
    }

    public List<Integer> getLastActionAmounts() {
        return lastActionAmounts;
    }

    public List<Boolean> getRaiseCommunityCards() {
        return raiseCommunityCards;
    }

    public List<Boolean> getRaiseUserCards() {
        return raiseUserCards;
    }

    public boolean isUserHasFolded() {
        return userHasFolded;
    }

    public String getUsername() {
        return username;
    }

    public String getEventId() {
        return eventId;
    }

    private void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public long getDecisionEndTime() {
        return decisionEndTime;
    }

    public int getDecisionTimeMaxSeconds() {
        return decisionTimeMaxSeconds;
    }

    private void setDecisionTimeMaxSeconds(int decisionTimeMaxSeconds) {
        this.decisionTimeMaxSeconds = decisionTimeMaxSeconds;
    }

    public double getBetStepSize() {
        return betStepSize;
    }

    private void setBetStepSize(int betStepSize) {
        this.betStepSize = betStepSize;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    static class ClientPlayer {
        private String name;
        private double balance;
        private List<Card> cards;
        private List<Boolean> raiseCards;

        ClientPlayer(String name, double balance, List<Card> cards, List<Boolean> raiseCards) {
            this.name = name;
            this.balance = balance;
            this.cards = cards;
            this.raiseCards = raiseCards;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public List<Card> getCards() {
            return cards;
        }

        public void setCards(List<Card> cards) {
            this.cards = cards;
        }

        public List<Boolean> getRaiseCards() {
            return raiseCards;
        }
    }
}
