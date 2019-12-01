package game;

public class Rules {

	public enum GameType {
		HOLDEM, PINEAPPLE, OMAHA, MIXED
	}
	
	private final int smallBlind;
	private final int bigBlind;
	private final int ante;
	private final int timeLimitSecs;
	private final int maxCapacity;
	private final int[] prizes;
	private final GameType gameType;
	private final int minimumChipAmount;

	private Rules(RulesBuilder rulesBuilder) {
	    validateRulesBuilder(rulesBuilder);
		this.smallBlind = rulesBuilder.smallBlind;
		this.bigBlind = rulesBuilder.bigBlind;
		this.ante = rulesBuilder.ante;
		this.timeLimitSecs = rulesBuilder.timeLimitSecs;
		this.maxCapacity = rulesBuilder.maxCapacity;
		this.prizes = rulesBuilder.prizes;
		this.gameType = rulesBuilder.gameType;
		this.minimumChipAmount = rulesBuilder.minimumChipAmount;
	}

	public GameType getGameType() {
		return gameType;
	}

	public int[] getPrizes() {
		return prizes;
	}

	public int getSmallBlind() {
		return this.smallBlind;
	}

	public int getBigBlind() {
		return bigBlind;
	}

	public int getAnte() {
		return ante;
	}

	public int getTimeLimitSecs() {
		return timeLimitSecs;
	}
	
	public int getMaxCapacity() {
		return this.maxCapacity;
	}

	public int getMinimumChipAmount() {
		return minimumChipAmount;
	}

	public static RulesBuilder builder() {
		return new RulesBuilder();
	}

	public static class RulesBuilder {
		private GameType gameType;
		private int[] prizes;
		private int smallBlind;
		private int bigBlind;
		private int ante = 0;
		private int timeLimitSecs;
		private int maxCapacity;
		private int minimumChipAmount;

	    public RulesBuilder gameType(final GameType gameType) {
			this.gameType = gameType;
			return this;
		}

		public RulesBuilder prizes(final int[] prizes) {
	    	this.prizes = prizes;
	    	return this;
		}

		public RulesBuilder smallBlind(final int smallBlind) {
	    	this.smallBlind = smallBlind;
	    	return this;
		}

		public RulesBuilder bigBlind(final int bigBlind) {
	    	this.bigBlind = bigBlind;
	    	return this;
		}

		public RulesBuilder ante(final int ante) {
	    	this.ante = ante;
	    	return this;
		}

		public RulesBuilder timeLimitSecs(final int timeLimitSecs) {
	    	this.timeLimitSecs = timeLimitSecs;
	    	return this;
		}

		public RulesBuilder maxCapacity(final int maxCapacity) {
	    	this.maxCapacity = maxCapacity;
	    	return this;
		}

		public RulesBuilder minimumChipAmount(final int minimumChipAmount) {
	    	this.minimumChipAmount = minimumChipAmount;
	    	return this;
		}

		public Rules build() {
	    	return new Rules(this);
		}
	}

	private static void validateRulesBuilder(final RulesBuilder rulesBuilder) {
		if (rulesBuilder.minimumChipAmount == 0 || rulesBuilder.gameType == null || rulesBuilder.maxCapacity == 0
				|| rulesBuilder.smallBlind == 0 || rulesBuilder.bigBlind == 0) {
			throw new IllegalArgumentException("Failed to create a rules object: minimumChipAmount, gameType, "
					+ "maxCapacity, smallBlind, and bigBlind must all be set.");
		}
	}
}
