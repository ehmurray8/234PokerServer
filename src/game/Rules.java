/**
 * 
 */
package game;

/**
 * @author Emmet
 *
 */
public class Rules {

	public enum GameType {
		HOLDEM,
		PINEAPPLE,
		OMAHA,
		MIXED;
	}
	
	/**
	 * Amount the small blind starts at.
	 */
	private double smallBlind;
	
	/**
	 * Amount the big blind starts at.
	 */
	private double bigBlind;
	
	/**
	 * Amount the ante starts at.
	 */
	private double ante;
	
	/**
	 * Amount of time in seconds each player gets to act.
	 */
	private int timeLimitSecs;
	
	/**
	 * Maximum number of players allowed to sit at the table.
	 */
	private int maxCapacity;
	
	/**
	 * The prizes for finishing in a certain place, the first place prize
	 * is located at position 0. Prizes only pertain to tournaments.
	 */
	private double[] prizes;
	
	/**
	 * Whether or not the game is a tournament.
	 */
	private boolean isTourney;
	
	/**
	 * The length of the blind level in minutes.
	 */
	private int blindLevelTimeMins;
	
	/**
	 * The amount for the big blinds at each level, this array excludes the initial bigBlind.
	 */
	private double[] bigBlinds;
	
	/**
	 * The amount for the small blinds at each level, this array excludes the initial smallBlind.
	 */
	private double[] smallBlinds;
	
	/**
	 * The amount for the antes at each level, this array excludes the initial ante.
	 */
	private double[] antes;
	
	
	/**
	 * The type of game the players will be playing at the table.
	 */
	private GameType gameType;
	
	/**
	 * Tournament constructor.
	 * 
	 * @param smallBlind the amount the small blind begins at
	 * @param bigBlind the amount the big blind begins at
	 * @param ante the amount the ante begins at
	 * @param timeLimitSecs the amount each player has to act;
	 * @param maxCapacity the maximum number of players allowed to sit at the table
	 * @param prizes the prizes awarded to the highest finishers
	 * @param blindLevelTimeMins the amount of time each blind level lasts
	 * @param bigBlinds the amount of money the big blind is at each level
	 * @param smallBlinds the amount of money the small blind is at each level
	 * @param antes the amount of money the ante is at each level
	 * @param gameType the type of game the players will be playing at the table
	 */
	public Rules(double smallBlind, double bigBlind, double ante, int timeLimitSecs, int maxCapacity, double[] prizes,
				 int blindLevelTimeMins, double[] bigBlinds, double[] smallBlinds, double[] antes, GameType gameType) {
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.ante = ante;
		this.timeLimitSecs = timeLimitSecs;
		this.maxCapacity = maxCapacity;
		this.prizes = prizes;
		this.isTourney = true;
		this.blindLevelTimeMins = blindLevelTimeMins;
		this.bigBlinds = bigBlinds;
		this.smallBlinds = smallBlinds;
		this.antes = antes;
		this.gameType = gameType;
	}
	
    /**
	 * Cash game constructor.
	 * 
	 * @param smallBlind the amount of money the small blind will be
	 * @param bigBlind the amount of money the big blind will be
	 * @param ante the amount of money the ante will be
	 * @param timeLimitSecs the number of seconds each player has to act on their turn
	 * @param maxCapacity the maximum number of people allowed to sit at the table
	 * @param gameType the type of game the players will be playing at the table
	 */
	public Rules(double smallBlind, double bigBlind, double ante, int timeLimitSecs, int maxCapacity, GameType gameType) {
		this.smallBlind = smallBlind;
		this.bigBlind = bigBlind;
		this.ante = ante;
		this.timeLimitSecs = timeLimitSecs;
		this.maxCapacity = maxCapacity;
		this.prizes = new double[0];
		this.isTourney = false;
		this.blindLevelTimeMins = 0;
		this.bigBlinds = new double[0];
		this.smallBlinds = new double[0];
		this.antes = new double[0];
		this.gameType = gameType;
	}
	
	
    public double[] getAntes() {
		return antes;
	}

	public GameType getGameType() {
		return gameType;
	}

	
	public double[] getPrizes() {
		return prizes;
	}

	public boolean isTourney() {
		return isTourney;
	}

	public int getBlindLevelTimeMins() {
		return blindLevelTimeMins;
	}

	public double[] getBigBlinds() {
		return bigBlinds;
	}

	public double[] getSmallBlinds() {
		return smallBlinds;
	}

	public double getSmallBlind() {
		return this.smallBlind;
	}
	
	public void setSmallBlind(double smallBlind) {
		this.smallBlind = smallBlind;
	}

	public double getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(double bigBlind) {
		this.bigBlind = bigBlind;
	}

	public double getAnte() {
		return ante;
	}

	public void setAnte(double ante) {
		this.ante = ante;
	}

	public int getTimeLimitSecs() {
		return timeLimitSecs;
	}
	
	public int getMaxCapacity() {
		return this.maxCapacity;
	}
	
	/**
	 * @Override
	 */
	public String toString() {
		return "Big Blind: " + this.bigBlind + ", Small Blind: " + this.smallBlind + ", Ante: " + this.ante +
				", Time limit (s): " + this.timeLimitSecs;
	}
}
