package client;

import model.hand.representation.Hand;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Date;

public class ClientMessage {

    private List<Player> players;
    private List<Card> communityCards;
    private int mainPotAmount;
    private List<Card> userCards;
    private int userStackSize;
    private String username;
    private List<Option> options;
    private int lastUserAmount;
    private List<Integer> lastActionAmounts;
    private List<Boolean> raiseCommunityCards;
    private List<Boolean> raiseUserCards;
    private boolean userHasFolded;
    private long decisionEndTime;
    private int decisionTimeMaxSeconds;
    private int eventId;
    private int betStepSize;


    private static Player createPlayer(model.player.Player player,
                                       @Nullable List<model.card.Card> winningCards, boolean showCards) {
        var name = player.getName();
        var balance = player.getBalance();
        List<Card> cards = null;
        if (!player.hasFolded() && showCards) {
            cards = player.getHand().stream().map(ClientMessage::createCard).collect(Collectors.toList());
        } else if (!player.hasFolded()) {
            cards = new ArrayList<>();
        }
        var raiseCards = new ArrayList<Boolean>();
        if (winningCards != null) {
            raiseCards.addAll(player.getHand().stream().map(winningCards::contains).collect(Collectors.toList()));
        }
        return new Player(name, balance, cards, raiseCards);
    }

    private static Card createCard(model.card.Card card) {
        var suit = card.getSuit().name().toLowerCase();
        var rank = card.getRank().getClientName();
        return new Card(rank, suit);
    }

    private static Option createOption(model.option.Option option) {
        var type = option.typeToString();
        var amount = option.getAmount();
        return new Option(type, amount);
    }

    static ClientMessage createClientMessage(model.player.Player player,
                                             @Nullable List<model.option.Option> options,
                                             @Nullable List<model.card.Card> winningCards,
                                             List<model.player.Player> players, Hand hand,
                                             int decisionTimeMaxSeconds, int eventId) {
        var mainPlayerIndex = players.stream().filter(Objects::nonNull).map(model.player.Player::getPlayerId)
                .collect(Collectors.toList()).indexOf(player.getPlayerId());

        var message = new ClientMessage();
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

    private void setUserInfo(model.player.Player player, @Nullable List<model.card.Card> winningCards,
                             @Nullable List<model.option.Option> options) {
        userCards = player.getHand().stream().map(ClientMessage::createCard).collect(Collectors.toList());
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
            this.options.addAll(options.stream().map(ClientMessage::createOption).collect(Collectors.toList()));
        }
    }

    private void setPlayerInfo(List<model.player.Player> players, @Nullable List<model.card.Card> winningCards,
                               int mainPlayerIndex, Hand hand) {
        this.players = new ArrayList<>();
        this.lastActionAmounts = new ArrayList<>();
        for (int i = mainPlayerIndex + 1, count = 0; count < players.size(); i = (i + 1) % players.size(), count++) {
            var player = players.get(i);
            if (player != null && mainPlayerIndex != i) {
                var showCards = winningCards != null;
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

    private void setHandInfo(Hand hand, @Nullable List<model.card.Card> winningCards) {
        communityCards.clear();
        communityCards.addAll(hand.getCommunityCards().stream().map(ClientMessage::createCard).collect(Collectors.toList()));
        mainPotAmount = hand.getTotalAmountInPots();
        if (winningCards != null) {
            this.raiseCommunityCards = hand.getCommunityCards().stream().map(winningCards::contains).collect(Collectors.toList());
        }
    }

    private void setDecisionEndTimeFrom(int decisionTimeMaxSeconds) {
        var currentMilliseconds = new Date().getTime();
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
        this.eventId = 0;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public int getMainPotAmount() {
        return mainPotAmount;
    }

    public void setMainPotAmount(int mainPotAmount) {
        this.mainPotAmount = mainPotAmount;
    }

    public List<Card> getUserCards() {
        return userCards;
    }

    public void setUserCards(List<Card> userCards) {
        this.userCards = userCards;
    }

    public int getUserStackSize() {
        return userStackSize;
    }

    public void setUserStackSize(int userStackSize) {
        this.userStackSize = userStackSize;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public int getLastUserAmount() {
        return lastUserAmount;
    }

    public void setLastUserAmount(int lastUserAmount) {
        this.lastUserAmount = lastUserAmount;
    }

    public List<Integer> getLastActionAmounts() {
        return lastActionAmounts;
    }

    public void setLastActionAmounts(List<Integer> lastActionAmounts) {
        this.lastActionAmounts = lastActionAmounts;
    }

    public List<Boolean> getRaiseCommunityCards() {
        return raiseCommunityCards;
    }

    public void setRaiseCommunityCards(List<Boolean> raiseCommunityCards) {
        this.raiseCommunityCards = raiseCommunityCards;
    }

    public List<Boolean> getRaiseUserCards() {
        return raiseUserCards;
    }

    public void setRaiseUserCards(List<Boolean> raiseUserCards) {
        this.raiseUserCards = raiseUserCards;
    }

    public boolean isUserHasFolded() {
        return userHasFolded;
    }

    public void setUserHasFolded(boolean userHasFolded) {
        this.userHasFolded = userHasFolded;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public long getDecisionEndTime() {
        return decisionEndTime;
    }

    public void setDecisionEndTime(long decisionEndTime) {
        this.decisionEndTime = decisionEndTime;
    }

    public int getDecisionTimeMaxSeconds() {
        return decisionTimeMaxSeconds;
    }

    public void setDecisionTimeMaxSeconds(int decisionTimeMaxSeconds) {
        this.decisionTimeMaxSeconds = decisionTimeMaxSeconds;
    }

    public double getBetStepSize() {
        return betStepSize;
    }

    public void setBetStepSize(int betStepSize) {
        this.betStepSize = betStepSize;
    }

    static class Player {
        private String name;
        private double balance;
        private List<Card> cards;
        private List<Boolean> raiseCards;

        Player(String name, double balance, List<Card> cards, List<Boolean> raiseCards) {
            this.name = name;
            this.balance = balance;
            this.cards = cards;
            this.raiseCards = raiseCards;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
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

        public void setRaiseCards(List<Boolean> raiseCards) {
            this.raiseCards = raiseCards;
        }
    }

    static class Card {
        private String rank;
        private String suit;

        Card(String rank, String suit) {
            this.rank = rank;
            this.suit = suit;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getSuit() {
            return suit;
        }

        public void setSuit(String suit) {
            this.suit = suit;
        }
    }

    static class Option {
        private String type;
        private double amount;

        Option(String type, double amount) {
            this.type = type;
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }
}
