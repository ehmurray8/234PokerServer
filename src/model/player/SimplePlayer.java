package model.player;

import java.io.Serializable;

import javafx.scene.image.Image;
import model.option.Option;
import model.option.Option.OptionKind;
import model.option.Option.OptionType;

/**
 * A SimplePlayer object is created to represent an instance of an already
 * created {@code Player} object more simply.
 *
 * <p>
 * The SimplePlayer object contains the {@code Player} object's name, balance, a
 * string representation of the last move they made, and an array of the
 * {@code Option} objects the player can perform. This object is used
 * specifically for representing a {@code Player} object in the GUI.
 * </p>
 *
 * @author Emmet Murray
 */
public class SimplePlayer implements SimplePlayerInterface, Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    /**
     * The name of the player, the variable is final once initialized in the
     * constructor.
     *
     * <p>
     * The player's name cannot be changed once initialized.
     * </p>
     */
    private final String name;

    /**
     * The amount of money the player has left.
     */
    private int balance;

    /**
     * String representation of the {@code Player}'s last move, created in the
     * inner {@code Move} class.
     */
    private Move lastMove;

    /**
     * Array of {@code Option}s that the player can execute on their turn.
     *
     * <p>
     * The options are only updated immediately before the {@code Player}s turn,
     * this is done to save time by not constantly updating this array.
     * </p>
     */
    private Option[] options;

    /**
     * Constructor for SimplePlayer to create the equivalent representation of
     * the {@code Player} the instance of this is based on.
     *
     * <p>
     * Sets the playerImg instance variable to the default user image.
     * </p>
     *
     * @param name
     *            the name of the player
     * @param balance
     *            the player's balance
     * @param lastMove
     *            {@code Move} instance used to create the string representation
     *            for this.lastMove
     */
    public SimplePlayer(String name, int balance, Move lastMove) {
        this.name = name;
        this.balance = balance;
        this.lastMove = lastMove;
        this.options = new Option[4];
        //this.playerImg = new Image(SimplePlayer.DEFAULT_USER_IMG_PATH);

        this.initOptionsArray();
    }

    /**
     * Constructor for SimplePlayer to create the equivalent representation of
     * the {@code Player} the instance of this is based on.
     *
     * @param name
     *            the name of the player
     * @param balance
     *            the player's balance
     * @param lastMove
     *            {@code Move} instance used to create the string representation
     *            for this.lastMove
     * @param img
     *            the Player's user image
     */
    public SimplePlayer(String name, int balance, Move lastMove, Image img) {
        this.name = name;
        this.balance = balance;
        this.lastMove = lastMove;
        this.options = new Option[4];
        //this.playerImg = img;

        this.initOptionsArray();
    }

    /**
     * Initializes this.options array to contain generic values, this is called
     * immediately to replace null values with {@code Option} instances.
     */
    private void initOptionsArray() {
        this.options[0] = new Option(OptionType.FOLD, 0);
        this.options[1] = new Option(OptionType.CHECK, 0);
        this.options[2] = new Option(OptionType.BET, 0);
        this.options[3] = new Option(OptionType.ALLIN, this.balance);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getBalanceString() {
        return "$" + this.balance;
    }

    @Override
    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String getLastMoveString() {
        return this.lastMove.toString();
    }

    @Override
    public Move getLastMove() {
        return this.lastMove;
    }

    @Override
    public void setLastMove(Move lastMove) {
        this.lastMove = lastMove;
    }

    @Override
    public void setOption(Option option) {
        switch (option.getKind()) {
            case FOLD: {
                this.options[OptionKind.FOLD.ordinal()] = option;
                break;
            }
            case CHECK_CALL: {
                this.options[OptionKind.CHECK_CALL.ordinal()] = option;
                break;
            }
            case RAISE_BET: {
                this.options[OptionKind.RAISE_BET.ordinal()] = option;
                break;
            }
            case ALLIN: {
                this.options[OptionKind.ALLIN.ordinal()] = option;
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public Option[] getOptions() {
        return this.options;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.balance + ", "
                + this.lastMove.toString();
    }

    /**
     * Describes a move, in this context a move is a player reacting to the
     * current game state by either folding, checking, calling, betting,
     * raising, or going all-in.
     *
     * <p>
     * This class is used to create the string representation stored in the
     * {@code SimplePlayer} class. A move contains an amount and an
     * {@code OptionType}.
     * </p>
     *
     * @author Emmet Murray
     */
    public static final class Move implements Serializable {

        private static final long serialVersionUID = 5950169519310163575L;

        /**
         * String representation of this move.
         */
        private String move;

        /**
         * Amount this move cost.
         */
        private int amount;

        /**
         * Type of this move.
         */
        private OptionType type;

        /**
         * Constructor for the {@code OptionType}'s CALL, RAISE, BET.
         *
         * <p>
         * This constructor is only used for {@code OptionType}'s that cost
         * money, and leave the player with a remaining balance.
         * </p>
         *
         * @param type
         *            the {@code OptionType} of the move
         * @param amount
         *            the amount the {@code Player} object spent on the move
         * @custom.requires {type} = CALL | RAISE | BET
         * @custom.ensures (During this move) balance &lt; #balance &amp; balance &gt; 0
         */
        public Move(Option.OptionType type, int amount) {
            this.move = type.toString() + ": $" + amount;
            this.amount = amount;
            this.type = type;
        }

        /**
         * Constructs an empty object, storing the empty string in the move
         * instance variable to prevent a null pointer exception.
         */
        public Move() {
            this.move = "";
        }

        /**
         * Constructor for the {@code OptionType}'s FOLD, CHECK, ALLIN.
         *
         * @param type
         *            the {@code OptionType} of the move
         * @custom.requires {type} = FOLD | CHECK | ALLIN
         * @custom.ensures (During this move) balance = #balance | balance = 0
         */
        public Move(Option.OptionType type) {
            this.move = type.toString() + ".";
        }

        @Override
        public String toString() {
            return this.move;
        }

        /**
         * Amount of this move getter method.
         *
         * @return amount of the move
         */
        public int getAmount() {
            return this.amount;
        }

        /**
         * {@code OptionType} of this move getter method.
         *
         * @return {@code OptionType} of this
         */
        public OptionType getType() {
            return this.type;
        }
    }
}
