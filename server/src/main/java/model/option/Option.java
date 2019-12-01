package model.option;

public class Option {

    private final OptionType type;
    private final int amount;

    public enum OptionType {
        FOLD("Fold"),
        CHECK("Check"),
        CALL("Call"),
        RAISE("Raise"),
        BET("Bet"),
        ALLIN("All-in");

        private final String string;

        OptionType(String string) {
            this.string = string;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    public Option(OptionType type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public OptionType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public static Option.OptionType stringToOptionType(String optionString) {
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
