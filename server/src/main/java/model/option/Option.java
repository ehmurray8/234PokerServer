package model.option;

public class Option {

    private final OptionType type;
    private final int amount;

    public enum OptionType {
        FOLD, CHECK, CALL, RAISE, BET, ALLIN
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
