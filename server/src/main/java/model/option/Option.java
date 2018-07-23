/**
 *
 */
package model.option;

/** Represents an option available to the client during the client's turn. */
public class Option {

    /**
     * The different kinds of options the player can perform on their turn.
     *
     * <p>
     * Only four kinds, because on a specific turn a player doesn't have the
     * option to check or call, and the player doesn't have the option to raise
     * or bet. The player only has four possible options on each turn.
     * </p>
     */
    public enum OptionKind {
        FOLD, CHECK_CALL, RAISE_BET, ALLIN
    }

    /** The unique types of options the player can perform in general. */
    public enum OptionType {
        FOLD("Fold", OptionKind.FOLD),
        CHECK("Check", OptionKind.CHECK_CALL),
        CALL("Call", OptionKind.CHECK_CALL),
        RAISE("Raise", OptionKind.RAISE_BET),
        BET("Bet", OptionKind.RAISE_BET),
        ALLIN("All-in", OptionKind.ALLIN);

        /** Formatted string representation of this. */
        private String stringRep;

        /** The corresponding {@code OptionKind}. */
        private OptionKind kind;

        /**
         * Describes the option type.
         *
         * @param stringRep string representation
         * @param kind {@code OptionKind}
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

    /** The {@code OptionType} of this. */
    private OptionType type;

    /** The amount this option costs to perform. */
    private double amount;

    /**
     * Creates a Option instance.
     *
     * @param type {@code OptionType} of the instance
     * @param amount amount corresponding to the option described by the instance
     */
    public Option(OptionType type, double amount) {
        this.type = type;
        this.amount = amount;
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
     * The amount getter method.
     *
     * @return the amount this option costs to perform
     */
    public double getAmount() {
        return this.amount;
    }

    @Override
    public String toString() {
        return this.type.toString() + ": $" + this.amount;
    }
}
