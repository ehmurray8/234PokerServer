package model.option;

public class Option {

    private final OptionType type;
    private final double amount;

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

        private final String string;
        private final OptionKind kind;


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
