package model.option;

public class Option {

  private final OptionType type;
  private final double amount;

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

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Option) {
      var option = (Option) obj;
      return amount == option.amount && type == option.type;
    }
    return false;
  }
}
