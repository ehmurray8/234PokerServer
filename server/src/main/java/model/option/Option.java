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

    public static Option.OptionType stringToOptionType(Object optionObject) {
        String optionString;
        try {
            optionString = (String) optionObject;
        } catch (ClassCastException e) {
            return null;
        }

        if (optionString.equalsIgnoreCase("check")) {
            return Option.OptionType.CHECK;
        } else if (optionString.equalsIgnoreCase("fold")) {
            return Option.OptionType.FOLD;
        } else if (optionString.equalsIgnoreCase("bet")) {
            return Option.OptionType.BET;
        } else if (optionString.equalsIgnoreCase("raise")) {
            return Option.OptionType.RAISE;
        } else if (optionString.equalsIgnoreCase("call")) {
            return Option.OptionType.CALL;
        } else if (optionString.equalsIgnoreCase("allin")) {
            return Option.OptionType.ALLIN;
        }
        return null;
    }

    public String typeToString() {
        switch (type) {
            case BET: return "bet";
            case CHECK: return "check";
            case FOLD: return "fold";
            case RAISE: return "raise";
            case CALL: return "call";
            case ALLIN: return "allin";
        }
        return null;
    }

    @Override
    public String toString() {
        return type.toString() + ": $" + amount;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Option) {
            var option = (Option) obj;
            return amount == option.amount && type == option.type;
        }
        return false;
    }
}
