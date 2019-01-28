package game;
import client.ClientHandler;
import model.player.Player;

import java.util.*;

import game.Rules.GameType;
import model.hand.representation.*;
import model.option.Option;

public class Game {
    Hand currentHand;

    public static class TableFullException extends Exception { }

	private static int GAME_ENDED = -1;

	private Player[] players;
    private Rules rules;
	int dealerNum;
	private int currentAction;
	private ArrayList<Player> playersInHand;
	private ClientHandler clientHandler;

	public Game(List<Player> players, Rules rules, ClientHandler clientHandler) {
		this.players = new Player[rules.getMaxCapacity()];
		for(int i = 0; i < players.size(); i++) {
            this.players[i] = players.get(i);
		}
		this.rules = rules;
		dealerNum = initDealerNum();
		playersInHand = new ArrayList<>();
		if (players.size() > 0) {
            currentAction = smallBlindNum();
        }
		this.clientHandler = clientHandler;
	}

	public boolean hasPlayer(UUID playerId) {
	    return Arrays.stream(players).map(Player::getPlayerId).filter(pid -> pid == playerId).toArray().length > 0;
    }

	public int getDealerNum() {
		return dealerNum;
	}

	private int initDealerNum() {
		return 0;
	}
	
	void incrementDealerNum() {
		do {
            dealerNum = (dealerNum + 1) % players.length;
		} while (players[dealerNum] == null || players[dealerNum].isSittingOut());
	}
	
	private int findStartingLocation() {
		int startLoc = -1;
		int num = 1;
		do {
			Player player = players[(dealerNum + num) % players.length];
			if(player != null) {
                startLoc = playersInHand.indexOf(player);
			}
			num ++;
		} while (startLoc == -1);
		return startLoc;
	}
	
	public int smallBlindNum() {
		int num = 1;
		int smallBlindNum;
		do {
            smallBlindNum = (dealerNum + num) % players.length;
            num++;
		} while (players[smallBlindNum] == null || players[smallBlindNum].isSittingOut());
		return smallBlindNum;
	}
	
	public int bigBlindNum() {
		int num = 1;
		int bigBlindNum;
		do {
			bigBlindNum = (smallBlindNum() + num) % players.length;
			num++;
		} while(players[bigBlindNum] == null || players[dealerNum].isSittingOut());
		return bigBlindNum;
	}

	public int getNumPlayers() {
	    return (int) Arrays.stream(players).filter(Objects::nonNull).count();
    }
	
	private void incrementCurrentAction() {
        currentAction = (currentAction + 1) % playersInHand.size();
	}

	public void addPlayer(Player player) throws TableFullException {
	    if(tableFull()) { throw new TableFullException(); }

		for(int i = 0; i < players.length; i++) {
			if(players[i] == null) {
				players[i] = player;
				break;
			}
		}
	}

	public void removePlayer(Player player) {
	    for(int i = 0; i < players.length; i++) {
            if(players[i] == player) {
                players[i] = null;
            }
        }
        playersInHand.remove(player);
	}

	public boolean tableFull() {
		return getNumPlayers() == rules.getMaxCapacity();
	}
	
	private GameType askDealerForGameType() {
        return clientHandler.getDesiredGameType(players[dealerNum].getPlayerId());
	}
	
	private void removeBrokePlayers() {
        Arrays.stream(players).filter(player -> player != null && player.getBalance() == 0)
                .forEach(player -> player.setSittingOut(true));
	}
	
	void bettingRound(boolean resetBetting) {
        currentAction = findStartingLocation();
        if(resetBetting) {
            this.currentHand.setupBetRound();
        }
        List<Option> currOptions;
        while(this.currentHand.playersBetting()) {
            Player player = playersInHand.get(currentAction);
            currOptions = this.currentHand.generateOptions(player);
            Option option = askPlayerForOption(currOptions, player);
            this.currentHand.executeOption(player, option);
            incrementCurrentAction();
        }
	}
	
	private Option askPlayerForOption(List<Option> options, Player player) {
        return clientHandler.getDesiredOption(player, options, rules.getTimeLimitSecs(), this.currentHand,
                Arrays.asList(this.players));
	}
	
	public boolean tableEmpty() {
	    return Arrays.stream(players).allMatch(Objects::isNull);
	}
	
	private boolean stillBetting() {
		return playersInHand.stream().filter(player -> !player.hasFolded()).count() > 1;
	}
	
	public void runGame() {
	    var gameState = 0;
		while(gameState != GAME_ENDED){
		    gameState = gameIteration();
        }
	}

	int gameIteration() {
	    prepareHand();

        if(playersInHand.size() <= 1) {
            return GAME_ENDED;
        }

        this.currentHand = createHand();
        runHand();

        prepareForNextHand();
        if(tableEmpty()) {
            return GAME_ENDED;
        }
        return 0;
    }

	private void prepareHand() {
	    for(Player p : players) {
            if(p != null && !p.isSittingOut()) {
                playersInHand.add(p);
            }
        }
    }

	private Hand createHand() {
	    GameType currGameType = rules.getGameType();
        if(currGameType == GameType.MIXED) {
            currGameType = askDealerForGameType();
        }

        Hand currentHand;
        switch(currGameType){
        case HOLDEM:
            currentHand = new TexasHoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(),
                    playersInHand, rules.getMinimumChipAmount());
            break;
        case PINEAPPLE:
            currentHand = new PineappleHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand,
                    rules.getMinimumChipAmount());
            break;
        case OMAHA:
            currentHand = new OmahaHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand,
                    rules.getMinimumChipAmount());
            break;
        case TEST:
            currentHand = new TestHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(), playersInHand,
                    rules.getMinimumChipAmount());
            break;
        case SHORTDECK:
            currentHand = new TexasHoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(),
                    playersInHand, true, rules.getMinimumChipAmount());
            break;
        default:
            currentHand = new TexasHoldEmHand(rules.getSmallBlind(), rules.getBigBlind(), rules.getAnte(),
                    playersInHand, rules.getMinimumChipAmount());
        }
        return currentHand;
    }

    protected void runHand() {
	    this.currentHand.dealInitialHand();
	    handleAction();
    }

    void handleAction() {
        handlePreFlop();
        if(stillBetting()) {
            handleFlop();
        }
        if(stillBetting()) {
            handleTurn();
        }
        if(stillBetting()) {
            handleRiver();
        }
        currentHand.payWinners();
        var winningCards = new ArrayList<>(currentHand.getWinningCards());
        clientHandler.sendHandOverMessage(currentHand, Arrays.asList(players), winningCards);
    }

    private void handlePreFlop() {
        if(rules.getAnte() > 0) {
            currentHand.chargeAntes();
        }
        currentHand.chargeSmallBlind(smallBlindNum());
        currentHand.chargeBigBlind(bigBlindNum());
        currentAction = playersInHand.indexOf(players[bigBlindNum()]);
        bettingRound(false);
        var bigBlindPlayer = players[bigBlindNum()];
        if(currentAction == bigBlindNum() && bigBlindPlayer.getAmountThisTurn() == rules.getBigBlind()
                && !currentHand.onlyOnePlayerLeftInHand()) {
            currentHand.setupBetRound();
            List<Option> currOptions = currentHand.generateOptions(bigBlindPlayer);
            Option option = askPlayerForOption(currOptions, bigBlindPlayer);
            currentHand.executeOption(bigBlindPlayer, option);
            incrementCurrentAction();
            if(option.getType() != Option.OptionType.CHECK) {
                bettingRound(false);
            }
        }
    }

    void handleFlop() {
        this.currentHand.dealFlop();
        bettingRound(true);
    }

    void handleTurn() {
        this.currentHand.dealTurn();
        bettingRound(true);
    }

    void handleRiver() {
        this.currentHand.dealRiver();
        bettingRound(true);
    }


    private void prepareForNextHand() {
	    Arrays.stream(players).filter(Objects::nonNull).forEach(Player::resetStatus);
        removeBrokePlayers();
        incrementDealerNum();
        playersInHand.clear();
    }
}