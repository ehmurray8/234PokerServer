/**
 *
 */
package model.option;

import java.io.Serializable;

/**
 * Represents an option available to the client during the client's turn.
 *
 * @author Emmet Murray
 */
public class Option implements Serializable {

    private static final long serialVersionUID = 5950169519310163575L;

    /**
     * The different kinds of options the player can perform on their turn.
     *
     * <p>
     * Only four kinds, because on a specific turn a player doesn't have the
     * option to check or call, and the player doesn't have the option to raise
     * or bet. The player only has four possible options on each turn.
     * </p>
     *
     * @author Emmet Murray
     */
    public enum OptionKind {
        /**
         * Fold.
         */
        FOLD,
        /**
         * Check or Call, a player only has the option to do one or the other on
         * a given turn.
         */
        CHECK_CALL,
        /**
         * Raise or bet, a player only has the option to do one or the other on
         * a given turn.
         */
        RAISE_BET,
        /**
         * All-in.
         */
        ALLIN;
    }

    /**
     * The unique types of options the player can perform in general.
     *
     * @author Emmet Murray
     */
    public enum OptionType {
        /**
         * Fold, corresponds to the {@code OptionKind} FOLD.
         */
        FOLD("Fold", OptionKind.FOLD),
        /**
         * Check, corresponds to the {@code OptionKind} CHECK_CALL.
         */
        CHECK("Check", OptionKind.CHECK_CALL),
        /**
         * Call, corresponds to the {@code OptionKind} CHECK_CALL.
         */
        CALL("Call", OptionKind.CHECK_CALL),
        /**
         * Raise, corresponds to the {@code OptionKind} RAISE_BET.
         */
        RAISE("Raise", OptionKind.RAISE_BET),
        /**
         * Bet, corresponds to the {@code OptionKind} RAISE_BET.
         */
        BET("Bet", OptionKind.RAISE_BET),
        /**
         * All-in, corresponds to the {@code OptionKind} ALLIN.
         */
        ALLIN("All-in", OptionKind.ALLIN),

        /**
         * Small blind, doesn't correspond to an {@code OptionKind}, unable to
         * call getKind().
         */
        SMALL_BLIND("Small Blind", null),

        /**
         * Small blind, doesn't correspond to an {@code OptionKind}, unable to
         * call getKind().
         */
        BIG_BLIND("Big Blind", null);

        /**
         * Formatted string representation of this.
         */
        private String stringRep;
        /**
         * The corresponding {@code OptionKind}.
         */
        private OptionKind kind;

        /**
         * Describes the option type.
         *
         * @param stringRep
         *            the string representation
         * @param kind
         *            the {@code OptionKind}
         */
        OptionType(String stringRep, OptionKind kind) {
            this.stringRep = stringRep;
            this.kind = kind;
        }

        @Override
        public String toString() {
            return this.stringRep;
        }

        /**
         * Returns the {@code OptionKind} of the enum entry.
         *
         * @return {@code OptionKind} for the enum instance.
         */
        public OptionKind getKind() {
            return this.kind;
        }
    }

    /**
     * The {@code OptionKind} of this, the OptionKind of this option cannot
     * change once created.
     */
    private final OptionKind kind;

    /**
     * The {@code OptionType} of this.
     */
    private OptionType type;

    /**
     * The amount this option costs to perform.
     */
    private int amount;

    /**
     * Creates a Option instance.
     *
     * @param type
     *            the {@code OptionType} of the instance
     * @param amount
     *            the amount corresponding to the option described by the
     *            instance
     */
    public Option(OptionType type, int amount) {
        this.type = type;
        this.amount = amount;
        this.kind = this.type.getKind();
    }

    /**
     * {@code OptionKind} getter method.
     *
     * @return the {@code OptionKind} described by this.
     */
    public OptionKind getKind() {
        return this.kind;
    }

    /**
     * {@code OptionType} getter method.
     *
     * @return the {@code OptionType} described by this.
     */
    public OptionType getType() {
        return this.type;
    }

    /**
     * {@code OptionType} setter method.
     *
     * @param type
     *            the new {@code OptionType}
     * @custom.requires {type} is of the same {@code OptionKind} of #type
     */
    public void setType(OptionType type) {
        this.type = type;
    }

    /**
     * The amount getter method.
     *
     * @return the amount this option costs to perform
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * The amount setter method
     *
     * @param amount
     *            the new amount of the option
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return this.type.toString() + ": $" + this.amount;
    }
}
