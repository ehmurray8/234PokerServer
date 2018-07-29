/**
 *
 */
package model.option;

public class Option {

    private OptionType type;
    private double amount;

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

    public enum OptionType {
        FOLD("Fold", OptionKind.FOLD),
        CHECK("Check", OptionKind.CHECK_CALL),
        CALL("Call", OptionKind.CHECK_CALL),
        RAISE("Raise", OptionKind.RAISE_BET),
        BET("Bet", OptionKind.RAISE_BET),
        ALLIN("All-in", OptionKind.ALLIN);

        private String string;
        private OptionKind kind;


        OptionType(String string, OptionKind kind) {
            this.string = string;
            this.kind = kind;
        }

        @Override
        public String toString() {
            return string;
        }

        public OptionKind getKind() {
            return kind;
        }
    }

    public Option(OptionType type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public OptionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return type.toString() + ": $" + amount;
    }
}
